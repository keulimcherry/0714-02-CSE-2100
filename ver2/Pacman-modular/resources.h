#ifndef RESOURCES_H
#define RESOURCES_H

#include "types.h"

extern Texture2D spritesheet;

extern Sound soundChomp;
extern Sound soundDeath;
extern Sound soundEatFruit;
extern Sound soundEatGhost;
extern Sound soundGhostRunning;
extern Sound soundIntro;
extern Sound soundPowerSiren;
extern Sound soundGhostTurnBlue;
extern Sound soundHighScore;

void LoadResources(void);
void UnloadResources(void);

#endif
