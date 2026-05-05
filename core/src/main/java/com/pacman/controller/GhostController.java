package com.pacman.controller;

import com.pacman.model.*;

public class GhostController {

    public static void updateGhosts(GameData game, SoundPlayer sound) {
        tickSiren(game, sound);

        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g = game.ghosts[i];
            if (!g.released) {
                g.releaseTimer -= game.deltaTime;
                if (g.releaseTimer <= 0) {
                    g.released = true;
                    g.inHouse  = false;
                } else {
                    continue;
                }
            }
            updateGhost(g, game);
        }
    }

    private static void updateGhost(GhostData ghost, GameData game) {
        float dt = game.deltaTime;

        if (handleRespawning(ghost, dt))  return;
        if (handleEaten(ghost))           return;

        tickModeTimer(ghost, dt);
        tickFrightenedTimer(ghost, dt);
        clampBounds(ghost);
        updateTile(ghost);

        boolean alignedX = ((int) ghost.entity.x % Constants.TILE_SIZE) == 0;
        boolean alignedY = ((int) ghost.entity.y % Constants.TILE_SIZE) == 0;

        if (alignedX && alignedY) {
            int[] target = resolveTarget(ghost, game);
            target[0] = Math.max(0, Math.min(target[0], Constants.MAZE_WIDTH  - 1));
            target[1] = Math.max(0, Math.min(target[1], Constants.MAZE_HEIGHT - 1));

            ghost.entity.direction = getBestDirection(
                ghost.entity.tileX, ghost.entity.tileY,
                target[0], target[1],
                ghost.entity.direction,
                game
            );
        }

        move(ghost);
        tunnelWrap(ghost);
        clampBounds(ghost);
    }

    private static int[] resolveTarget(GhostData ghost, GameData game) {
        if (ghost.inHouse) {
            if (ghost.entity.tileX == 13 && ghost.entity.tileY == 11)
                ghost.inHouse = false;
            return new int[]{ 13, 11 };
        }

        int   dx   = ghost.entity.tileX - game.pacman.tileX;
        int   dy   = ghost.entity.tileY - game.pacman.tileY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (ghost.mode == GhostMode.MODE_FRIGHTENED) {
            int tx = game.pacman.tileX < Constants.MAZE_WIDTH  / 2 ? Constants.MAZE_WIDTH  - 1 : 0;
            int ty = game.pacman.tileY < Constants.MAZE_HEIGHT / 2 ? Constants.MAZE_HEIGHT - 1 : 0;
            return new int[]{ tx, ty };
        }

        if (dist < 10.0f && ghost.behavior != null) {
            return ghost.behavior.getChaseTarget(ghost, game);
        }

        return ghost.behavior != null
            ? ghost.behavior.getScatterTarget(ghost)
            : new int[]{ ghost.scatterX, ghost.scatterY };
    }

    private static boolean handleRespawning(GhostData ghost, float dt) {
        if (ghost.mode != GhostMode.MODE_RESPAWNING) return false;
        ghost.respawnTimer -= dt;
        if (ghost.respawnTimer <= 0) {
            ghost.mode      = GhostMode.MODE_SCATTER;
            ghost.modeTimer = 7.0f;
            ghost.inHouse   = false;
            ghost.released  = true;
        }
        return true;
    }

    private static boolean handleEaten(GhostData ghost) {
        if (ghost.mode != GhostMode.MODE_EATEN) return false;

        float dirX = ghost.homeX - ghost.entity.x;
        float dirY = ghost.homeY - ghost.entity.y;
        float dist = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (dist < 5.0f) {
            ghost.entity.x     = ghost.homeX;
            ghost.entity.y     = ghost.homeY;
            ghost.mode         = GhostMode.MODE_RESPAWNING;
            ghost.respawnTimer = 3.0f;
            ghost.inHouse      = true;
            return true;
        }

        float speed = 4.0f;
        if (dist > 0) { dirX /= dist; dirY /= dist; }
        ghost.entity.x     += dirX * speed;
        ghost.entity.y     += dirY * speed;
        ghost.entity.tileX  = (int) (ghost.entity.x / Constants.TILE_SIZE);
        ghost.entity.tileY  = (int) (ghost.entity.y / Constants.TILE_SIZE);
        return true;
    }

    private static void tickModeTimer(GhostData ghost, float dt) {
        if (ghost.mode == GhostMode.MODE_SCATTER || ghost.mode == GhostMode.MODE_CHASE) {
            ghost.modeTimer -= dt;
            if (ghost.modeTimer <= 0) {
                ghost.mode      = (ghost.mode == GhostMode.MODE_SCATTER)
                                  ? GhostMode.MODE_CHASE : GhostMode.MODE_SCATTER;
                ghost.modeTimer = (ghost.mode == GhostMode.MODE_SCATTER) ? 7.0f : 20.0f;
            }
        }
    }

    private static void tickFrightenedTimer(GhostData ghost, float dt) {
        if (ghost.mode == GhostMode.MODE_FRIGHTENED) {
            ghost.frightenedTimer -= dt;
            if (ghost.frightenedTimer <= 0) {
                ghost.mode      = GhostMode.MODE_SCATTER;
                ghost.modeTimer = 7.0f;
            }
        }
    }

    private static void clampBounds(GhostData ghost) {
        if (ghost.entity.y < 0)
            ghost.entity.y = 0;
        if (ghost.entity.y >= (Constants.MAZE_HEIGHT - 1) * Constants.TILE_SIZE)
            ghost.entity.y = (float) ((Constants.MAZE_HEIGHT - 2) * Constants.TILE_SIZE);
        if (ghost.entity.x < -Constants.TILE_SIZE && ghost.entity.tileY != 14)
            ghost.entity.x = 0;
        if (ghost.entity.x >= Constants.MAZE_WIDTH * Constants.TILE_SIZE && ghost.entity.tileY != 14)
            ghost.entity.x = (float) ((Constants.MAZE_WIDTH - 1) * Constants.TILE_SIZE);
    }

    private static void updateTile(GhostData ghost) {
        ghost.entity.tileX = (int) (ghost.entity.x / Constants.TILE_SIZE);
        ghost.entity.tileY = (int) (ghost.entity.y / Constants.TILE_SIZE);
        ghost.entity.tileX = Math.max(0, Math.min(ghost.entity.tileX, Constants.MAZE_WIDTH  - 1));
        ghost.entity.tileY = Math.max(0, Math.min(ghost.entity.tileY, Constants.MAZE_HEIGHT - 1));
    }

    private static void move(GhostData ghost) {
        float speed = ghost.entity.speed;
        if (ghost.mode == GhostMode.MODE_FRIGHTENED) speed *= 0.5f;
        switch (ghost.entity.direction) {
            case DIR_UP:    ghost.entity.y -= speed; break;
            case DIR_DOWN:  ghost.entity.y += speed; break;
            case DIR_LEFT:  ghost.entity.x -= speed; break;
            case DIR_RIGHT: ghost.entity.x += speed; break;
            default: break;
        }
    }

    private static void tunnelWrap(GhostData ghost) {
        if (ghost.entity.tileY == 14) {
            if (ghost.entity.x < 0)
                ghost.entity.x = (float) ((Constants.MAZE_WIDTH - 1) * Constants.TILE_SIZE);
            else if (ghost.entity.x >= Constants.MAZE_WIDTH * Constants.TILE_SIZE)
                ghost.entity.x = Constants.TILE_SIZE;
        }
    }

    public static Direction getBestDirection(int currentX, int currentY,
                                              int targetX,  int targetY,
                                              Direction currentDir,
                                              GameData game) {
        Direction[] dirs = { Direction.DIR_UP, Direction.DIR_LEFT,
                             Direction.DIR_DOWN, Direction.DIR_RIGHT };
        float     bestDist = 999999.0f;
        Direction bestDir  = currentDir;

        for (Direction d : dirs) {
            if (d == getOpposite(currentDir)) continue;
            if (MapManager.canMove(currentX, currentY, d, game)) {
                int nx = currentX, ny = currentY;
                switch (d) {
                    case DIR_UP:    ny--; break;
                    case DIR_DOWN:  ny++; break;
                    case DIR_LEFT:  nx--; break;
                    case DIR_RIGHT: nx++; break;
                    default: break;
                }
                int dx = nx - targetX, dy = ny - targetY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist < bestDist || (dist == bestDist && d == Direction.DIR_UP)) {
                    bestDist = dist;
                    bestDir  = d;
                }
            }
        }
        return bestDir;
    }

    private static Direction getOpposite(Direction dir) {
        switch (dir) {
            case DIR_UP:    return Direction.DIR_DOWN;
            case DIR_DOWN:  return Direction.DIR_UP;
            case DIR_LEFT:  return Direction.DIR_RIGHT;
            case DIR_RIGHT: return Direction.DIR_LEFT;
            default:        return Direction.DIR_NONE;
        }
    }

    private static void tickSiren(GameData game, SoundPlayer sound) {
        boolean anyHunting = false;
        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g = game.ghosts[i];
            if (g.released && !g.inHouse
                    && g.mode != GhostMode.MODE_FRIGHTENED
                    && g.mode != GhostMode.MODE_EATEN
                    && g.mode != GhostMode.MODE_RESPAWNING) {
                anyHunting = true;
                break;
            }
        }

        if (anyHunting) {
            game.sirenTimer += game.deltaTime;
            if (game.sirenTimer > 3.0f && !sound.isPowerSirenPlaying()) {
                sound.startPowerSiren();
                game.sirenTimer = 0;
            }
        } else {
            if (sound.isPowerSirenPlaying()) sound.stopPowerSiren();
            game.sirenTimer = 0;
        }
    }
}
