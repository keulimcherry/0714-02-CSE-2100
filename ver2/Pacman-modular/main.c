#include "raylib.h"
#include "types.h"
#include "map.h"
#include "pacman.h"
#include "render.h"
#include "resources.h"

Game game;

int main(void) {
    InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "PAC-MAN");
    InitAudioDevice();
    SetTargetFPS(60);

    LoadResources();
    InitGame();

    while (!WindowShouldClose()) {
        UpdateGame();

        BeginDrawing();
        ClearBackground(BLACK);
        DrawGame();
        EndDrawing();
    }

    UnloadResources();
    CloseAudioDevice();
    CloseWindow();

    return 0;
}
