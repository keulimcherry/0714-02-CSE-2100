#include "render.h"
#include "map.h"
#include "types.h"
#include "resources.h"

Color GetMazeColor(int level, bool isOuter) {
    int colorIndex = (level - 1) % 6;

    Color outerColors[] = {
        {40, 60, 180, 180},
        {40, 160, 60, 180},
        {180, 40, 40, 180},
        {180, 100, 40, 180},
        {140, 40, 180, 180},
        {180, 160, 40, 180}
    };

    Color innerColors[] = {
        {80, 150, 255, 255},
        {80, 255, 120, 255},
        {255, 80, 80, 255},
        {255, 180, 80, 255},
        {220, 80, 255, 255},
        {255, 240, 80, 255}
    };

    return isOuter ? outerColors[colorIndex] : innerColors[colorIndex];
}

Color GetRainbowColor(float time) {
    float hue = fmodf(time * 2.0f, 6.0f);
    int section = (int)hue;
    float fraction = hue - section;

    switch (section) {
    case 0: return (Color) { 255, (int)(fraction * 255), 0, 255 };
    case 1: return (Color) { (int)((1 - fraction) * 255), 255, 0, 255 };
    case 2: return (Color) { 0, 255, (int)(fraction * 255), 255 };
    case 3: return (Color) { 0, (int)((1 - fraction) * 255), 255, 255 };
    case 4: return (Color) { (int)(fraction * 255), 0, 255, 255 };
    default: return (Color) { 255, 0, (int)((1 - fraction) * 255), 255 };
    }
}

void DrawGame(void) {
    DrawMaze();

    if (game.fruitSpawned && !game.fruitEaten) {
        DrawCircle(
            (int)(game.fruitX * TILE_SIZE + TILE_SIZE / 2),
            (int)(game.fruitY * TILE_SIZE + TILE_SIZE / 2),
            (float)(TILE_SIZE / 3),
            YELLOW
        );
    }

    for (int i = 0; i < GHOST_COUNT; i++) {
        if (game.ghosts[i].mode == MODE_EATEN) {
            int cx = (int)(game.ghosts[i].entity.x + TILE_SIZE / 2);
            int cy = (int)(game.ghosts[i].entity.y + TILE_SIZE / 2);

            DrawCircle(cx - 6, cy - 4, 5, WHITE);
            DrawCircle(cx + 6, cy - 4, 5, WHITE);
            DrawCircle(cx - 6, cy - 4, 3, BLUE);
            DrawCircle(cx + 6, cy - 4, 3, BLUE);
            continue;
        }

        if (game.ghosts[i].mode == MODE_RESPAWNING) {
            continue;
        }

        Color color = game.ghosts[i].color;
        if (game.ghosts[i].mode == MODE_FRIGHTENED) {
            if (game.ghosts[i].frightenedTimer < 2.0f &&
                ((int)(game.ghosts[i].frightenedTimer * 8) % 2 == 0)) {
                color = WHITE;
            }
            else {
                color = DARKBLUE;
            }
        }

        int cx = (int)(game.ghosts[i].entity.x + TILE_SIZE / 2);
        int cy = (int)(game.ghosts[i].entity.y + TILE_SIZE / 2);

        DrawCircle(cx, cy, (float)(TILE_SIZE / 2), color);
        DrawRectangle(
            (int)game.ghosts[i].entity.x,
            (int)(game.ghosts[i].entity.y + TILE_SIZE / 2),
            TILE_SIZE,
            TILE_SIZE / 2,
            color
        );

        for (int j = 0; j < 3; j++) {
            DrawCircle(
                (int)(game.ghosts[i].entity.x + (j + 0.5f) * TILE_SIZE / 3),
                (int)(game.ghosts[i].entity.y + TILE_SIZE - 2),
                TILE_SIZE / 6,
                color
            );
        }

        DrawCircle(
            (int)(game.ghosts[i].entity.x + TILE_SIZE / 3),
            (int)(game.ghosts[i].entity.y + TILE_SIZE / 3),
            3,
            WHITE
        );
        DrawCircle(
            (int)(game.ghosts[i].entity.x + 2 * TILE_SIZE / 3),
            (int)(game.ghosts[i].entity.y + TILE_SIZE / 3),
            3,
            WHITE
        );

        if (game.ghosts[i].mode == MODE_FRIGHTENED) {
            DrawCircle(
                (int)(game.ghosts[i].entity.x + TILE_SIZE / 3),
                (int)(game.ghosts[i].entity.y + TILE_SIZE / 3 + 2),
                1,
                PINK
            );
            DrawCircle(
                (int)(game.ghosts[i].entity.x + 2 * TILE_SIZE / 3),
                (int)(game.ghosts[i].entity.y + TILE_SIZE / 3 + 2),
                1,
                PINK
            );

            int mouthY = (int)(game.ghosts[i].entity.y + 2 * TILE_SIZE / 3);
            int mouthCenterX = (int)(game.ghosts[i].entity.x + TILE_SIZE / 2);

            for (int j = -4; j <= 4; j++) {
                int x = mouthCenterX + j * 2;
                int y = mouthY + abs(j) / 2;
                DrawPixel(x, y, WHITE);
            }
        }
        else {
            DrawCircle(
                (int)(game.ghosts[i].entity.x + TILE_SIZE / 3),
                (int)(game.ghosts[i].entity.y + TILE_SIZE / 3),
                2,
                BLUE
            );
            DrawCircle(
                (int)(game.ghosts[i].entity.x + 2 * TILE_SIZE / 3),
                (int)(game.ghosts[i].entity.y + TILE_SIZE / 3),
                2,
                BLUE
            );
        }
    }

    if (game.state != STATE_DEATH || ((int)(game.stateTimer * 10) % 2 == 0)) {
        float angle = 0;
        if (game.pacman.direction == DIR_RIGHT) angle = 0;
        else if (game.pacman.direction == DIR_DOWN) angle = 90;
        else if (game.pacman.direction == DIR_LEFT) angle = 180;
        else if (game.pacman.direction == DIR_UP) angle = 270;

        int animFrame = (int)(GetTime() * 10) % 4;
        float mouthAngle = (animFrame % 2 == 0) ? 30.0f : 0.0f;

        DrawCircleSector(
            (Vector2) {
            game.pacman.x + TILE_SIZE / 2.0f, game.pacman.y + TILE_SIZE / 2.0f
        },
            (float)(TILE_SIZE / 2),
            angle + mouthAngle,
            angle + 360 - mouthAngle,
            20,
            YELLOW
        );
    }

    if (game.ghostScoreTimer > 0) {
        char scoreText[10];
        sprintf_s(scoreText, sizeof(scoreText), "%d", game.ghostScoreValue);
        DrawText(scoreText, (int)game.ghostScoreX, (int)game.ghostScoreY, 20, YELLOW);
    }

    char scoreText[50];
    sprintf_s(scoreText, sizeof(scoreText), "SCORE: %d", game.score);
    DrawText(scoreText, 10, 10, 20, WHITE);

    sprintf_s(scoreText, sizeof(scoreText), "HIGH SCORE: %d", game.highScore);
    DrawText(scoreText, SCREEN_WIDTH - 250, 10, 20, WHITE);

    for (int i = 0; i < game.lives; i++) {
        DrawCircle(10 + i * 30, SCREEN_HEIGHT - 20, 10, YELLOW);
    }

    sprintf_s(scoreText, sizeof(scoreText), "LEVEL: %d", game.level);
    DrawText(scoreText, SCREEN_WIDTH / 2 - 50, 10, 20, WHITE);

    if (game.state == STATE_INTRO) {
        DrawText("PAC-MAN", SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2 - 50, 40, YELLOW);
        DrawText("PRESS SPACE TO START", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 20, 20, WHITE);
    }
    else if (game.state == STATE_READY) {
        DrawText("READY!", SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2, 30, YELLOW);
    }
    else if (game.state == STATE_DEATH) {
        DrawText("OOPS!", SCREEN_WIDTH / 2 - 40, SCREEN_HEIGHT / 2, 30, RED);
    }
    else if (game.state == STATE_LEVEL_COMPLETE) {
        DrawText("LEVEL COMPLETE!", SCREEN_WIDTH / 2 - 140, SCREEN_HEIGHT / 2, 30, GREEN);
    }
    else if (game.state == STATE_GAME_OVER) {
        DrawText("GAME OVER", SCREEN_WIDTH / 2 - 80, SCREEN_HEIGHT / 2, 30, RED);
        DrawText("PRESS SPACE TO RESTART", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 50, 20, WHITE);
    }
}
