#include "map.h"
#include "render.h"
#include "types.h"

Game game;

int mazeTemplate[MAZE_HEIGHT][MAZE_WIDTH] = {
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

void InitMaze(void) {
    game.totalDots = 0;
    for (int y = 0; y < MAZE_HEIGHT; y++) {
        for (int x = 0; x < MAZE_WIDTH; x++) {
            game.maze[y][x] = mazeTemplate[y][x];
            if (game.maze[y][x] == 2 || game.maze[y][x] == 3) {
                game.totalDots++;
            }
        }
    }
}

void ResetPositions(void) {
    game.pacman.tileX = 13;
    game.pacman.tileY = 23;
    game.pacman.x = (float)(game.pacman.tileX * TILE_SIZE);
    game.pacman.y = (float)(game.pacman.tileY * TILE_SIZE);
    game.pacman.direction = DIR_NONE;
    game.pacman.nextDirection = DIR_NONE;
    game.pacman.speed = 2.0f;

    int ghostStartTiles[4][2] = { {13, 11}, {13, 14}, {11, 14}, {15, 14} };
    Color ghostColors[4] = { RED, PINK, SKYBLUE, ORANGE };
    int scatterTargets[4][2] = { {24, 1}, {3, 1}, {24, 29}, {3, 29} };
    float releaseTimes[4] = { 0.0f, 2.0f, 4.0f, 6.0f };

    for (int i = 0; i < GHOST_COUNT; i++) {
        game.ghosts[i].entity.tileX = ghostStartTiles[i][0];
        game.ghosts[i].entity.tileY = ghostStartTiles[i][1];
        game.ghosts[i].entity.x = (float)(game.ghosts[i].entity.tileX * TILE_SIZE);
        game.ghosts[i].entity.y = (float)(game.ghosts[i].entity.tileY * TILE_SIZE);
        game.ghosts[i].entity.direction = DIR_LEFT;
        game.ghosts[i].entity.speed = 1.5f;
        game.ghosts[i].type = (GhostType)i;
        game.ghosts[i].mode = MODE_SCATTER;
        game.ghosts[i].modeTimer = 7.0f;
        game.ghosts[i].scatterX = scatterTargets[i][0];
        game.ghosts[i].scatterY = scatterTargets[i][1];
        game.ghosts[i].color = ghostColors[i];
        game.ghosts[i].releaseTimer = releaseTimes[i];
        game.ghosts[i].released = (i == 0);
        game.ghosts[i].inHouse = (i != 0);
        game.ghosts[i].homeX = 13.0f * TILE_SIZE;
        game.ghosts[i].homeY = 14.0f * TILE_SIZE;
        game.ghosts[i].respawnTimer = 0.0f;
    }
}

void ResetLevel(void) {
    game.dotsEaten = 0;
    game.powerPelletActive = 0;
    game.ghostsEaten = 0;
    game.fruitSpawned = 0;
    game.fruitEaten = 0;
    game.cruiseElroy = false;

    ResetPositions();
}

bool IsWall(int x, int y) {
    if (x < 0 || x >= MAZE_WIDTH || y < 0 || y >= MAZE_HEIGHT) return false;
    return (game.maze[y][x] == 1);
}

bool CanMove(int x, int y, Direction dir) {
    int nextX = x;
    int nextY = y;

    if (dir == DIR_UP) nextY--;
    else if (dir == DIR_DOWN) nextY++;
    else if (dir == DIR_LEFT) nextX--;
    else if (dir == DIR_RIGHT) nextX++;
    else return false;

    if (nextY == 14 && (nextX < 0 || nextX >= MAZE_WIDTH)) return true;

    if (nextX < 0 || nextX >= MAZE_WIDTH || nextY < 0 || nextY >= MAZE_HEIGHT) return false;

    return !IsWall(nextX, nextY);
}

void DrawMaze(void) {
    Color outerColor, innerColor;

    if (game.rainbowMode) {
        outerColor = GetRainbowColor(game.rainbowTimer);
        innerColor = GetRainbowColor(game.rainbowTimer + 1.0f);
    }
    else {
        outerColor = GetMazeColor(game.level, true);
        innerColor = GetMazeColor(game.level, false);
    }

    for (int y = 0; y < MAZE_HEIGHT; y++) {
        for (int x = 0; x < MAZE_WIDTH; x++) {
            int tile = game.maze[y][x];

            if (tile == 1) {
                int posX = x * TILE_SIZE;
                int posY = y * TILE_SIZE;
                DrawRectangleLinesEx((Rectangle) { posX, posY, TILE_SIZE, TILE_SIZE }, 3, outerColor);
                DrawRectangleLinesEx((Rectangle) { posX + 1, posY + 1, TILE_SIZE - 2, TILE_SIZE - 2 }, 2, innerColor);
            }
            else if (tile == 2) {
                DrawCircle(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2, 3,
                    (Color) {
                    255, 255, 255, 100
                });
                DrawCircle(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2, 2, WHITE);
            }
            else if (tile == 3) {
                float pulse = (sinf((float)GetTime() * 8.0f) + 1.0f) / 2.0f;
                int glowRadius = 8 + (int)(pulse * 4);
                int coreRadius = 4 + (int)(pulse * 2);
                DrawCircle(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2,
                    glowRadius, (Color) { 255, 255, 200, 60 });
                DrawCircle(x * TILE_SIZE + TILE_SIZE / 2, y * TILE_SIZE + TILE_SIZE / 2,
                    coreRadius, WHITE);
            }
        }
    }
}
