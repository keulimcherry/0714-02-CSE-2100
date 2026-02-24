#ifndef MAP_H
#define MAP_H

#include "types.h"

void InitMaze(void);
void ResetLevel(void);
void ResetPositions(void);

bool IsWall(int x, int y);
bool CanMove(int x, int y, Direction dir);

void DrawMaze(void);

#endif