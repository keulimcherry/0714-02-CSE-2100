#ifndef RENDER_H
#define RENDER_H

#include "types.h"

Color GetMazeColor(int level, bool isOuter);
Color GetRainbowColor(float time);

void DrawGame(void);

#endif
