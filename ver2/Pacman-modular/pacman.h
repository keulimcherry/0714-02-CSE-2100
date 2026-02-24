#ifndef PACMAN_H
#define PACMAN_H

#include "types.h"

bool CanPacmanMove(int x, int y, Direction dir);
void UpdatePacman(void);
void InitGame(void);
void UpdateGame(void);

#endif
