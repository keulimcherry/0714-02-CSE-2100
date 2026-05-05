package com.pacman.view;

import com.badlogic.gdx.graphics.Color;

public class ColourHelper {

    private ColourHelper() {}

    public static Color getMazeColor(int level, boolean isOuter) {
        int idx = (level - 1) % 6;
        float[][] outer = {
            {40/255f,  60/255f,  180/255f, 180/255f},
            {40/255f,  160/255f, 60/255f,  180/255f},
            {180/255f, 40/255f,  40/255f,  180/255f},
            {180/255f, 100/255f, 40/255f,  180/255f},
            {140/255f, 40/255f,  180/255f, 180/255f},
            {180/255f, 160/255f, 40/255f,  180/255f}
        };
        float[][] inner = {
            {80/255f,  150/255f, 255/255f, 1f},
            {80/255f,  255/255f, 120/255f, 1f},
            {255/255f, 80/255f,  80/255f,  1f},
            {255/255f, 180/255f, 80/255f,  1f},
            {220/255f, 80/255f,  255/255f, 1f},
            {255/255f, 240/255f, 80/255f,  1f}
        };
        float[] c = isOuter ? outer[idx] : inner[idx];
        return new Color(c[0], c[1], c[2], c[3]);
    }

    public static Color getRainbowColor(float time) {
        float hue = time * 2.0f % 6.0f;
        if (hue < 0) hue += 6.0f;
        int   section = (int) hue;
        float frac    = hue - section;
        float r = 0, g = 0, b = 0;
        switch (section) {
            case 0: r = 1;      g = frac;   b = 0;      break;
            case 1: r = 1-frac; g = 1;      b = 0;      break;
            case 2: r = 0;      g = 1;      b = frac;   break;
            case 3: r = 0;      g = 1-frac; b = 1;      break;
            case 4: r = frac;   g = 0;      b = 1;      break;
            default:r = 1;      g = 0;      b = 1-frac; break;
        }
        return new Color(r, g, b, 1f);
    }
}
