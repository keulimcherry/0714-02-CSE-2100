#ifndef TYPES_H
#define TYPES_H

#include "raylib.h"
#include <math.h>
#include <stdbool.h>

#define SCREEN_WIDTH 672
#define SCREEN_HEIGHT 864
#define TILE_SIZE 24
#define MAZE_WIDTH 28
#define MAZE_HEIGHT 31
#define GHOST_COUNT 4
#define MAX_LIVES 3

typedef enum {
    STATE_INTRO,
    STATE_READY,
    STATE_PLAYING,
    STATE_DEATH,
    STATE_LEVEL_COMPLETE,
    STATE_GAME_OVER
} GameState;

typedef enum {
    GHOST_BLINKY = 0,
    GHOST_PINKY = 1,
    GHOST_INKY = 2,
    GHOST_CLYDE = 3
} GhostType;

typedef enum {
    MODE_SCATTER,
    MODE_CHASE,
    MODE_FRIGHTENED,
    MODE_EATEN,
    MODE_RESPAWNING
} GhostMode;

typedef enum {
    DIR_NONE = 0,
    DIR_UP = 1,
    DIR_DOWN = 2,
    DIR_LEFT = 3,
    DIR_RIGHT = 4
} Direction;

typedef struct {
    float x, y;
    Direction direction;
    Direction nextDirection;
    float speed;
    int tileX, tileY;
} Entity;

typedef struct {
    Entity entity;
    GhostType type;
    GhostMode mode;
    int scatterX, scatterY;
    float modeTimer;
    float frightenedTimer;
    Color color;
    float releaseTimer;
    bool released;
    bool inHouse;
    float homeX, homeY;
    float respawnTimer;
} Ghost;

typedef struct {
    Entity pacman;
    Ghost ghosts[GHOST_COUNT];
    int maze[MAZE_HEIGHT][MAZE_WIDTH];

    int score;
    int highScore;
    int lives;
    int level;
    int dotsEaten;
    int totalDots;

    int powerPelletActive;
    float powerPelletTimer;

    int ghostsEaten;
    float ghostScoreTimer;
    int ghostScoreValue;
    float ghostScoreX, ghostScoreY;

    GameState state;
    float stateTimer;

    int fruitSpawned;
    int fruitEaten;
    float fruitX, fruitY;
    float fruitTimer;

    bool cruiseElroy;
    float sirenTimer;

    bool rainbowMode;
    float rainbowTimer;
} Game;

extern Game game;

extern int mazeTemplate[MAZE_HEIGHT][MAZE_WIDTH];

extern int fruitPoints[8];

#endif
#pragma once
