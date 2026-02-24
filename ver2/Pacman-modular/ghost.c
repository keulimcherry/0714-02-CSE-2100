#include "ghost.h"
#include "map.h"
#include "types.h"
#include "resources.h"

Direction GetBestDirection(int currentX, int currentY, int targetX, int targetY, Direction currentDir) {
    Direction dirs[4] = { DIR_UP, DIR_LEFT, DIR_DOWN, DIR_RIGHT };
    Direction opposite[5] = { DIR_NONE, DIR_DOWN, DIR_UP, DIR_RIGHT, DIR_LEFT };

    float bestDist = 999999.0f;
    Direction bestDir = currentDir;

    for (int i = 0; i < 4; i++) {
        if (dirs[i] == opposite[currentDir]) continue;
        if (CanMove(currentX, currentY, dirs[i])) {
            int nextX = currentX;
            int nextY = currentY;

            if (dirs[i] == DIR_UP) nextY--;
            else if (dirs[i] == DIR_DOWN) nextY++;
            else if (dirs[i] == DIR_LEFT) nextX--;
            else if (dirs[i] == DIR_RIGHT) nextX++;

            int dx = nextX - targetX;
            int dy = nextY - targetY;
            float dist = sqrtf((float)(dx * dx + dy * dy));

            if (dist < bestDist || (dist == bestDist && dirs[i] == DIR_UP)) {
                bestDist = dist;
                bestDir = dirs[i];
            }
        }
    }

    return bestDir;
}

void UpdateGhost(Ghost* ghost) {
    if (ghost->mode == MODE_RESPAWNING) {
        ghost->respawnTimer -= GetFrameTime();
        if (ghost->respawnTimer <= 0) {
            ghost->mode = MODE_SCATTER;
            ghost->modeTimer = 7.0f;
            ghost->inHouse = false;
            ghost->released = true;
        }
        return;
    }

    if (ghost->mode == MODE_EATEN) {
        float dirX = ghost->homeX - ghost->entity.x;
        float dirY = ghost->homeY - ghost->entity.y;
        float distance = sqrtf(dirX * dirX + dirY * dirY);

        if (distance < 5.0f) {
            ghost->entity.x = ghost->homeX;
            ghost->entity.y = ghost->homeY;
            ghost->mode = MODE_RESPAWNING;
            ghost->respawnTimer = 3.0f;
            ghost->inHouse = true;
            return;
        }

        float speed = 4.0f;
        if (distance > 0) {
            dirX /= distance;
            dirY /= distance;
        }

        ghost->entity.x += dirX * speed;
        ghost->entity.y += dirY * speed;
        ghost->entity.tileX = (int)(ghost->entity.x / TILE_SIZE);
        ghost->entity.tileY = (int)(ghost->entity.y / TILE_SIZE);
        return;
    }

    if (ghost->mode == MODE_SCATTER || ghost->mode == MODE_CHASE) {
        ghost->modeTimer -= GetFrameTime();
        if (ghost->modeTimer <= 0) {
            ghost->mode = (ghost->mode == MODE_SCATTER) ? MODE_CHASE : MODE_SCATTER;
            ghost->modeTimer = (ghost->mode == MODE_SCATTER) ? 7.0f : 20.0f;
        }
    }

    if (ghost->mode == MODE_FRIGHTENED) {
        ghost->frightenedTimer -= GetFrameTime();
        if (ghost->frightenedTimer <= 0) {
            ghost->mode = MODE_SCATTER;
            ghost->modeTimer = 7.0f;
        }
    }

    if (ghost->entity.y < 0) ghost->entity.y = 0;
    if (ghost->entity.y >= (MAZE_HEIGHT - 1) * TILE_SIZE) {
        ghost->entity.y = (float)((MAZE_HEIGHT - 2) * TILE_SIZE);
    }

    if (ghost->entity.x < -TILE_SIZE && ghost->entity.tileY != 14) ghost->entity.x = 0;
    if (ghost->entity.x >= MAZE_WIDTH * TILE_SIZE && ghost->entity.tileY != 14) {
        ghost->entity.x = (float)((MAZE_WIDTH - 1) * TILE_SIZE);
    }

    ghost->entity.tileX = (int)(ghost->entity.x / TILE_SIZE);
    ghost->entity.tileY = (int)(ghost->entity.y / TILE_SIZE);

    if (ghost->entity.tileX < 0) ghost->entity.tileX = 0;
    if (ghost->entity.tileX >= MAZE_WIDTH) ghost->entity.tileX = MAZE_WIDTH - 1;
    if (ghost->entity.tileY < 0) ghost->entity.tileY = 0;
    if (ghost->entity.tileY >= MAZE_HEIGHT) ghost->entity.tileY = MAZE_HEIGHT - 1;

    bool alignedX = ((int)ghost->entity.x % TILE_SIZE) == 0;
    bool alignedY = ((int)ghost->entity.y % TILE_SIZE) == 0;

    if (alignedX && alignedY) {
        int targetX = 13;
        int targetY = 11;

        if (ghost->inHouse) {
            targetX = 13;
            targetY = 11;
            if (ghost->entity.tileX == 13 && ghost->entity.tileY == 11) {
                ghost->inHouse = false;
            }
        }
        else {
            int dx = ghost->entity.tileX - game.pacman.tileX;
            int dy = ghost->entity.tileY - game.pacman.tileY;
            float dist = sqrtf((float)(dx * dx + dy * dy));

            if (ghost->mode == MODE_FRIGHTENED) {
                targetX = game.pacman.tileX < MAZE_WIDTH / 2 ? MAZE_WIDTH - 1 : 0;
                targetY = game.pacman.tileY < MAZE_HEIGHT / 2 ? MAZE_HEIGHT - 1 : 0;
            }
            else if (dist < 10.0f) {
                if (ghost->type == GHOST_BLINKY) {
                    targetX = game.pacman.tileX;
                    targetY = game.pacman.tileY;
                }
                else if (ghost->type == GHOST_PINKY) {
                    targetX = game.pacman.tileX;
                    targetY = game.pacman.tileY;

                    if (game.pacman.direction == DIR_UP) { targetY -= 4; targetX -= 4; }
                    else if (game.pacman.direction == DIR_DOWN) targetY += 4;
                    else if (game.pacman.direction == DIR_LEFT) targetX -= 4;
                    else if (game.pacman.direction == DIR_RIGHT) targetX += 4;
                }
                else if (ghost->type == GHOST_INKY) {
                    int pivotX = game.pacman.tileX;
                    int pivotY = game.pacman.tileY;
                    if (game.pacman.direction == DIR_UP) { pivotY -= 2; pivotX -= 2; }
                    else if (game.pacman.direction == DIR_DOWN) pivotY += 2;
                    else if (game.pacman.direction == DIR_LEFT) pivotX -= 2;
                    else if (game.pacman.direction == DIR_RIGHT) pivotX += 2;

                    int blinkyX = game.ghosts[GHOST_BLINKY].entity.tileX;
                    int blinkyY = game.ghosts[GHOST_BLINKY].entity.tileY;
                    targetX = pivotX + (pivotX - blinkyX);
                    targetY = pivotY + (pivotY - blinkyY);
                }
                else if (ghost->type == GHOST_CLYDE) {
                    if (dist > 4.0f) {
                        targetX = game.pacman.tileX;
                        targetY = game.pacman.tileY;
                    }
                    else {
                        targetX = ghost->scatterX;
                        targetY = ghost->scatterY;
                    }
                }
            }
            else {
                targetX = ghost->scatterX;
                targetY = ghost->scatterY;
            }
        }

        if (targetX < 0) targetX = 0;
        if (targetX >= MAZE_WIDTH) targetX = MAZE_WIDTH - 1;
        if (targetY < 0) targetY = 0;
        if (targetY >= MAZE_HEIGHT) targetY = MAZE_HEIGHT - 1;

        ghost->entity.direction = GetBestDirection(
            ghost->entity.tileX, ghost->entity.tileY,
            targetX, targetY, ghost->entity.direction
        );
    }

    float speed = ghost->entity.speed;
    if (ghost->mode == MODE_FRIGHTENED) speed *= 0.5f;

    if (ghost->entity.direction == DIR_UP) ghost->entity.y -= speed;
    else if (ghost->entity.direction == DIR_DOWN) ghost->entity.y += speed;
    else if (ghost->entity.direction == DIR_LEFT) ghost->entity.x -= speed;
    else if (ghost->entity.direction == DIR_RIGHT) ghost->entity.x += speed;

    if (ghost->entity.tileY == 14) {
        if (ghost->entity.x < 0) {
            ghost->entity.x = (float)((MAZE_WIDTH - 1) * TILE_SIZE);
        }
        else if (ghost->entity.x >= MAZE_WIDTH * TILE_SIZE) {
            ghost->entity.x = TILE_SIZE;
        }
    }

    if (ghost->entity.y < 0) ghost->entity.y = 0;
    if (ghost->entity.y >= (MAZE_HEIGHT - 1) * TILE_SIZE) {
        ghost->entity.y = (float)((MAZE_HEIGHT - 2) * TILE_SIZE);
    }
}

void UpdateGhosts(void) {
    bool anyHunting = false;

    for (int i = 0; i < GHOST_COUNT; i++) {
        if (game.ghosts[i].released && !game.ghosts[i].inHouse &&
            game.ghosts[i].mode != MODE_FRIGHTENED &&
            game.ghosts[i].mode != MODE_EATEN &&
            game.ghosts[i].mode != MODE_RESPAWNING) {
            anyHunting = true;
            break;
        }
    }

    if (anyHunting) {
        game.sirenTimer += GetFrameTime();
        if (game.sirenTimer > 3.0f && !IsSoundPlaying(soundPowerSiren)) {
            PlaySound(soundPowerSiren);
            game.sirenTimer = 0;
        }
    }
    else {
        if (IsSoundPlaying(soundPowerSiren)) {
            StopSound(soundPowerSiren);
        }
        game.sirenTimer = 0;
    }

    for (int i = 0; i < GHOST_COUNT; i++) {
        if (!game.ghosts[i].released) {
            game.ghosts[i].releaseTimer -= GetFrameTime();
            if (game.ghosts[i].releaseTimer <= 0) {
                game.ghosts[i].released = true;
                game.ghosts[i].inHouse = true;
            }
            else {
                continue;
            }
        }
        UpdateGhost(&game.ghosts[i]);
    }
}
