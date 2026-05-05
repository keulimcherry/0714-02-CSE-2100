package com.pacman.model;

public class GameData {

    private static GameData instance;

    public PacmanEntity pacman = new PacmanEntity();
    public GhostData[]  ghosts = new GhostData[Constants.GHOST_COUNT];
    public int[][]      maze   = new int[Constants.MAZE_HEIGHT][Constants.MAZE_WIDTH];

    public int   score;
    public int   highScore;
    public int   lives;
    public int   level;
    public int   dotsEaten;
    public int   totalDots;

    public int   powerPelletActive;
    public float powerPelletTimer;

    public int   ghostsEaten;
    public float ghostScoreTimer;
    public int   ghostScoreValue;
    public float ghostScoreX, ghostScoreY;

    public GameState state;
    public float     stateTimer;

    public int   fruitSpawned;
    public int   fruitEaten;
    public float fruitX, fruitY;
    public float fruitTimer;

    public boolean cruiseElroy;
    public float   sirenTimer;
    public boolean rainbowMode;
    public float   rainbowTimer;

    public float deltaTime;
    public float totalTime;
    public float chompCooldown;

    private GameData() {
        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            ghosts[i] = new GhostData();
        }
    }

    public static GameData getInstance() {
        if (instance == null) instance = new GameData();
        return instance;
    }

    public static void reset() { instance = null; }

    public static class PacmanEntity {
        public final Entity entity = new Entity();

        public float     x, y;
        public Direction direction     = Direction.DIR_NONE;
        public Direction nextDirection = Direction.DIR_NONE;
        public float     speed;
        public int       tileX, tileY;

        public void syncToEntity() {
            entity.x         = x;
            entity.y         = y;
            entity.direction = direction;
            entity.speed     = speed;
            entity.tileX     = tileX;
            entity.tileY     = tileY;
        }

        public void syncFromEntity() {
            x         = entity.x;
            y         = entity.y;
            direction = entity.direction;
            speed     = entity.speed;
            tileX     = entity.tileX;
            tileY     = entity.tileY;
        }
    }
}
