#include "pacman.h"
#include "map.h"
#include "ghost.h"
#include "render.h"
#include "resources.h"
#include "types.h"

int fruitPoints[8] = { 100, 300, 500, 700, 1000, 2000, 3000, 5000 };

bool CanPacmanMove(int x, int y, Direction dir) {
    int nextX = x;
    int nextY = y;

    if (dir == DIR_UP) nextY--;
    else if (dir == DIR_DOWN) nextY++;
    else if (dir == DIR_LEFT) nextX--;
    else if (dir == DIR_RIGHT) nextX++;
    else return false;

    if (nextY == 14 && (nextX < 0 || nextX >= MAZE_WIDTH)) return true;

    if (nextX < 0 || nextX >= MAZE_WIDTH || nextY < 0 || nextY >= MAZE_HEIGHT) return false;

    int tile = game.maze[nextY][nextX];
    return (tile != 1 && tile != 4);
}

void UpdatePacman(void) {
    if (IsKeyDown(KEY_UP)) game.pacman.nextDirection = DIR_UP;
    else if (IsKeyDown(KEY_DOWN)) game.pacman.nextDirection = DIR_DOWN;
    else if (IsKeyDown(KEY_LEFT)) game.pacman.nextDirection = DIR_LEFT;
    else if (IsKeyDown(KEY_RIGHT)) game.pacman.nextDirection = DIR_RIGHT;

    int centerX = (int)(game.pacman.x + TILE_SIZE / 2);
    int centerY = (int)(game.pacman.y + TILE_SIZE / 2);
    game.pacman.tileX = centerX / TILE_SIZE;
    game.pacman.tileY = centerY / TILE_SIZE;

    bool alignedX = ((int)game.pacman.x % TILE_SIZE) == 0;
    bool alignedY = ((int)game.pacman.y % TILE_SIZE) == 0;
    bool aligned = alignedX && alignedY;

    if (aligned && game.pacman.nextDirection != DIR_NONE) {
        if (CanPacmanMove(game.pacman.tileX, game.pacman.tileY, game.pacman.nextDirection)) {
            game.pacman.direction = game.pacman.nextDirection;
        }
    }

    if (!aligned || CanPacmanMove(game.pacman.tileX, game.pacman.tileY, game.pacman.direction)) {
        if (game.pacman.direction == DIR_UP) game.pacman.y -= game.pacman.speed;
        else if (game.pacman.direction == DIR_DOWN) game.pacman.y += game.pacman.speed;
        else if (game.pacman.direction == DIR_LEFT) game.pacman.x -= game.pacman.speed;
        else if (game.pacman.direction == DIR_RIGHT) game.pacman.x += game.pacman.speed;
    }
    else {
        game.pacman.x = (float)(game.pacman.tileX * TILE_SIZE);
        game.pacman.y = (float)(game.pacman.tileY * TILE_SIZE);
        game.pacman.direction = DIR_NONE;
    }

    if (game.pacman.tileY == 14) {
        if (game.pacman.x < 0) {
            game.pacman.x = (float)((MAZE_WIDTH - 1) * TILE_SIZE);
            game.pacman.tileX = MAZE_WIDTH - 1;
        }
        else if (game.pacman.x >= MAZE_WIDTH * TILE_SIZE) {
            game.pacman.x = 0;
            game.pacman.tileX = 0;
        }
    }

    centerX = (int)(game.pacman.x + TILE_SIZE / 2);
    centerY = (int)(game.pacman.y + TILE_SIZE / 2);
    game.pacman.tileX = centerX / TILE_SIZE;
    game.pacman.tileY = centerY / TILE_SIZE;

    if (game.pacman.tileX < 0) game.pacman.tileX = 0;
    if (game.pacman.tileX >= MAZE_WIDTH) game.pacman.tileX = MAZE_WIDTH - 1;
    if (game.pacman.tileY < 0) game.pacman.tileY = 0;
    if (game.pacman.tileY >= MAZE_HEIGHT) game.pacman.tileY = MAZE_HEIGHT - 1;

    if (game.maze[game.pacman.tileY][game.pacman.tileX] == 2) {
        game.maze[game.pacman.tileY][game.pacman.tileX] = 0;
        game.score += 10;
        if (game.score > game.highScore && game.score - 10 <= game.highScore) {
            game.rainbowMode = true;
            game.rainbowTimer = 0.0f;
        }
        game.dotsEaten++;
        PlaySound(soundChomp);
    }
    else if (game.maze[game.pacman.tileY][game.pacman.tileX] == 3) {
        game.maze[game.pacman.tileY][game.pacman.tileX] = 0;
        game.score += 50;
        if (game.score > game.highScore && game.score - 50 <= game.highScore) {
            game.rainbowMode = true;
            game.rainbowTimer = 0.0f;
        }
        game.dotsEaten++;
        game.powerPelletActive = 1;
        game.powerPelletTimer = 6.0f;
        game.ghostsEaten = 0;

        PlaySound(soundGhostTurnBlue);
        StopSound(soundPowerSiren);

        for (int i = 0; i < GHOST_COUNT; i++) {
            if (game.ghosts[i].mode != MODE_EATEN &&
                game.ghosts[i].mode != MODE_RESPAWNING &&
                game.ghosts[i].released && !game.ghosts[i].inHouse) {
                game.ghosts[i].mode = MODE_FRIGHTENED;
                game.ghosts[i].frightenedTimer = 6.0f;
            }
        }
    }

    if (game.fruitSpawned && !game.fruitEaten) {
        if (game.pacman.tileX == (int)game.fruitX && game.pacman.tileY == (int)game.fruitY) {
            int fruitIndex = (game.level - 1) % 8;
            int points = fruitPoints[fruitIndex];
            game.score += points;
            if (game.score > game.highScore && game.score - points <= game.highScore) {
                game.rainbowMode = true;
                game.rainbowTimer = 0.0f;
            }
            game.fruitEaten = 1;
            PlaySound(soundEatFruit);
        }
    }

    for (int i = 0; i < GHOST_COUNT; i++) {
        float dist = sqrtf(
            powf(game.pacman.x - game.ghosts[i].entity.x, 2) +
            powf(game.pacman.y - game.ghosts[i].entity.y, 2)
        );

        if (dist < TILE_SIZE * 0.7f) {
            if (game.ghosts[i].mode == MODE_FRIGHTENED) {
                int points[] = { 200, 400, 800, 1600 };
                int earnedPoints = points[game.ghostsEaten];
                game.score += earnedPoints;
                if (game.score > game.highScore && game.score - earnedPoints <= game.highScore) {
                    game.rainbowMode = true;
                    game.rainbowTimer = 0.0f;
                }

                game.ghostScoreValue = earnedPoints;
                game.ghostScoreX = game.ghosts[i].entity.x;
                game.ghostScoreY = game.ghosts[i].entity.y;
                game.ghostScoreTimer = 1.0f;
                game.ghostsEaten++;
                game.ghosts[i].mode = MODE_EATEN;

                PlaySound(soundEatGhost);
            }
            else if (game.ghosts[i].mode != MODE_EATEN &&
                game.ghosts[i].mode != MODE_RESPAWNING) {
                game.state = STATE_DEATH;
                game.stateTimer = 0;
                PlaySound(soundDeath);
                StopSound(soundPowerSiren);
                return;
            }
        }
    }
}

void InitGame(void) {
    int savedHighScore = game.highScore;
    game.score = 0;
    game.highScore = savedHighScore;
    game.lives = MAX_LIVES;
    game.level = 1;
    game.state = STATE_INTRO;
    game.stateTimer = 0;
    game.sirenTimer = 0;
    game.rainbowMode = false;
    game.rainbowTimer = 0.0f;

    InitMaze();
    ResetLevel();
    PlaySound(soundIntro);
}

void UpdateGame(void) {
    game.stateTimer += GetFrameTime();

    if (game.state == STATE_INTRO) {
        if (!IsSoundPlaying(soundIntro) || IsKeyPressed(KEY_SPACE)) {
            game.state = STATE_READY;
            game.stateTimer = 0;
            StopSound(soundIntro);
        }
    }
    else if (game.state == STATE_READY) {
        if (game.stateTimer > 2.0f) {
            game.state = STATE_PLAYING;
            game.stateTimer = 0;
        }
    }
    else if (game.state == STATE_PLAYING) {
        UpdatePacman();
        UpdateGhosts();

        if (game.rainbowMode) {
            game.rainbowTimer += GetFrameTime();
            if (game.rainbowTimer > 5.0f) {
                game.rainbowMode = false;
            }
        }

        if (game.dotsEaten >= game.totalDots) {
            game.state = STATE_LEVEL_COMPLETE;
            game.stateTimer = 0;
            StopSound(soundPowerSiren);
        }

        if (!game.fruitSpawned && game.dotsEaten >= game.totalDots / 2) {
            game.fruitSpawned = 1;
            game.fruitX = 13.0f;
            game.fruitY = 17.0f;
            game.fruitTimer = 10.0f;
        }

        if (game.fruitSpawned && !game.fruitEaten) {
            game.fruitTimer -= GetFrameTime();
            if (game.fruitTimer <= 0) game.fruitSpawned = 0;
        }

        if (game.powerPelletActive) {
            game.powerPelletTimer -= GetFrameTime();
            if (game.powerPelletTimer <= 0) {
                game.powerPelletActive = 0;
                game.ghostsEaten = 0;
            }
        }

        if (game.ghostScoreTimer > 0) {
            game.ghostScoreTimer -= GetFrameTime();
        }
    }
    else if (game.state == STATE_DEATH) {
        if (game.stateTimer > 2.0f) {
            game.lives--;
            if (game.lives <= 0) {
                game.state = STATE_GAME_OVER;
                if (game.score > game.highScore) {
                    game.highScore = game.score;
                    PlaySound(soundHighScore);
                }
            }
            else {
                ResetPositions();
                game.state = STATE_READY;
            }
            game.stateTimer = 0;
        }
    }
    else if (game.state == STATE_LEVEL_COMPLETE) {
        if (game.stateTimer > 2.0f) {
            game.level++;
            InitMaze();
            ResetLevel();
            game.state = STATE_READY;
            game.stateTimer = 0;
        }
    }
    else if (game.state == STATE_GAME_OVER) {
        if (game.stateTimer > 3.0f && IsKeyPressed(KEY_SPACE)) {
            InitGame();
        }
    }
}
