#include "resources.h"
#include "raylib.h"

Texture2D spritesheet;

Sound soundChomp;
Sound soundDeath;
Sound soundEatFruit;
Sound soundEatGhost;
Sound soundGhostRunning;
Sound soundIntro;
Sound soundPowerSiren;
Sound soundGhostTurnBlue;
Sound soundHighScore;

void LoadResources(void) {
    spritesheet = LoadTexture("assets/spritesheet.png");
    soundChomp = LoadSound("assets/chomp.wav");
    soundDeath = LoadSound("assets/death.wav");
    soundEatFruit = LoadSound("assets/eat_fruit.wav");
    soundEatGhost = LoadSound("assets/eat_ghost.wav");
    soundGhostRunning = LoadSound("assets/ghost_running_away.wav");
    soundIntro = LoadSound("assets/Intro.wav");
    soundPowerSiren = LoadSound("assets/power_siren.wav");
    soundGhostTurnBlue = LoadSound("assets/ghost_turn_blue.wav");
    soundHighScore = LoadSound("assets/high_score.wav");

    SetMasterVolume(1.0f);
}

void UnloadResources(void) {
    UnloadTexture(spritesheet);
    UnloadSound(soundChomp);
    UnloadSound(soundDeath);
    UnloadSound(soundEatFruit);
    UnloadSound(soundEatGhost);
    UnloadSound(soundGhostRunning);
    UnloadSound(soundIntro);
    UnloadSound(soundPowerSiren);
    UnloadSound(soundGhostTurnBlue);
    UnloadSound(soundHighScore);
}
