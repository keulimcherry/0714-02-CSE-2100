package com.pacman.model;

import com.badlogic.gdx.graphics.Color;

public class MapManager {

    public static final int[][] MAZE_TEMPLATE = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,3,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,3,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
        {1,2,1,1,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,1,1,1,0,1,1,0,1,1,1,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,4,4,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,4,4,4,4,4,4,1,0,1,1,2,1,1,1,1,1,1},
        {0,0,0,0,0,0,2,0,0,0,1,4,4,4,4,4,4,1,0,0,0,2,0,0,0,0,0,0},
        {1,1,1,1,1,1,2,1,1,0,1,4,4,4,4,4,4,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,0,0,0,0,0,0,0,0,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,3,2,2,1,1,2,2,2,2,2,2,2,0,0,2,2,2,2,2,2,2,1,1,2,2,3,1},
        {1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1},
        {1,1,1,2,1,1,2,1,1,2,1,1,1,1,1,1,1,1,2,1,1,2,1,1,2,1,1,1},
        {1,2,2,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,1,1,2,2,2,2,2,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,2,1,1,1,1,1,2,1,1,1,1,2,1},
        {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public void initMaze(GameData game) {
        game.totalDots = 0;
        for (int y = 0; y < Constants.MAZE_HEIGHT; y++) {
            for (int x = 0; x < Constants.MAZE_WIDTH; x++) {
                game.maze[y][x] = MAZE_TEMPLATE[y][x];
                if (game.maze[y][x] == 2 || game.maze[y][x] == 3) game.totalDots++;
            }
        }
    }

    public void resetLevel(GameData game) {
        game.dotsEaten         = 0;
        game.powerPelletActive = 0;
        game.ghostsEaten       = 0;
        game.fruitSpawned      = 0;
        game.fruitEaten        = 0;
        game.cruiseElroy       = false;
        resetPositions(game);
    }

    public void resetPositions(GameData game) {
        game.pacman.tileX         = 13;
        game.pacman.tileY         = 23;
        game.pacman.x             = game.pacman.tileX * Constants.TILE_SIZE;
        game.pacman.y             = game.pacman.tileY * Constants.TILE_SIZE;
        game.pacman.direction     = Direction.DIR_NONE;
        game.pacman.nextDirection = Direction.DIR_NONE;
        game.pacman.speed         = 1.0f;

        int[][] startTiles   = {{13,11},{13,14},{11,14},{15,14}};
        Color[] ghostColors  = {Constants.RL_RED, Constants.RL_PINK, Constants.RL_SKYBLUE, Constants.RL_ORANGE};
        int[][] scatter      = {{24,1},{3,1},{24,29},{3,29}};
        float[] releaseTimes = {0.0f, 2.0f, 4.0f, 6.0f};

        GhostBehavior[] behaviors = {
            new BlinkyBehavior(),
            new PinkyBehavior(),
            new InkyBehavior(),
            new ClydeBehavior()
        };

        GhostType[] types = GhostType.values();

        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g        = game.ghosts[i];
            g.entity.tileX     = startTiles[i][0];
            g.entity.tileY     = startTiles[i][1];
            g.entity.x         = g.entity.tileX * Constants.TILE_SIZE;
            g.entity.y         = g.entity.tileY * Constants.TILE_SIZE;
            g.entity.direction = Direction.DIR_LEFT;
            g.entity.speed     = 0.8f;
            g.type             = types[i];
            g.mode             = GhostMode.MODE_SCATTER;
            g.modeTimer        = 7.0f;
            g.scatterX         = scatter[i][0];
            g.scatterY         = scatter[i][1];
            g.color            = new Color(ghostColors[i]);
            g.releaseTimer     = releaseTimes[i];
            g.released         = (i == 0);
            g.inHouse          = (i != 0);
            g.homeX            = 13.0f * Constants.TILE_SIZE;
            g.homeY            = 14.0f * Constants.TILE_SIZE;
            g.respawnTimer     = 0.0f;
            g.behavior         = behaviors[i];
        }
    }

    public static boolean isWall(int x, int y, GameData game) {
        if (x < 0 || x >= Constants.MAZE_WIDTH || y < 0 || y >= Constants.MAZE_HEIGHT)
            return false;
        return (game.maze[y][x] == 1);
    }

    public static boolean canMove(int x, int y, Direction dir, GameData game) {
        int nextX = x, nextY = y;
        switch (dir) {
            case DIR_UP:    nextY--; break;
            case DIR_DOWN:  nextY++; break;
            case DIR_LEFT:  nextX--; break;
            case DIR_RIGHT: nextX++; break;
            default: return false;
        }
        if (nextY == 14 && (nextX < 0 || nextX >= Constants.MAZE_WIDTH)) return true;
        if (nextX < 0 || nextX >= Constants.MAZE_WIDTH  ||
            nextY < 0 || nextY >= Constants.MAZE_HEIGHT) return false;
        return !isWall(nextX, nextY, game);
    }
}
