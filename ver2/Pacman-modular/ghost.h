#ifndef GHOST_H
#define GHOST_H

#include "types.h"

Direction GetBestDirection(int currentX, int currentY, int targetX, int targetY, Direction currentDir);
void UpdateGhost(Ghost* ghost);
void UpdateGhosts(void);

#endif
