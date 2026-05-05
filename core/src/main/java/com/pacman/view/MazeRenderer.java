package com.pacman.view;

import com.pacman.model.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MazeRenderer {

    private final ShapeRenderer sr;

    public MazeRenderer(ShapeRenderer sr) {
        this.sr = sr;
    }

    public void draw(GameData game) {
        Color outerColor, innerColor;
        if (game.rainbowMode) {
            outerColor = ColourHelper.getRainbowColor(game.rainbowTimer);
            innerColor = ColourHelper.getRainbowColor(game.rainbowTimer + 1.0f);
        } else {
            outerColor = ColourHelper.getMazeColor(game.level, true);
            innerColor = ColourHelper.getMazeColor(game.level, false);
        }

        int ts = Constants.TILE_SIZE;

        for (int y = 0; y < Constants.MAZE_HEIGHT; y++) {
            for (int x = 0; x < Constants.MAZE_WIDTH; x++) {
                int   tile = game.maze[y][x];
                float px   = x * ts;
                float py   = y * ts;
                switch (tile) {
                    case 1: drawWall(px, py, ts, outerColor, innerColor); break;
                    case 2: drawDot(px, py, ts);                          break;
                    case 3: drawPowerPellet(px, py, ts, game.totalTime);  break;
                    default: break;
                }
            }
        }
    }

    private void drawWall(float px, float py, int ts, Color outer, Color inner) {
        drawRectLines(px,     py,     ts,     ts,     3, outer);
        drawRectLines(px + 1, py + 1, ts - 2, ts - 2, 2, inner);
    }

    private void drawDot(float px, float py, int ts) {
        float cx = px + ts / 2f, cy = py + ts / 2f;
        sr.setColor(1f, 1f, 1f, 0.39f);
        sr.circle(cx, cy, 3, 8);
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, 2, 8);
    }

    private void drawPowerPellet(float px, float py, int ts, float totalTime) {
        float pulse      = ((float) Math.sin(totalTime * 8.0f) + 1.0f) / 2.0f;
        float glowRadius = 8 + pulse * 4;
        float coreRadius = 4 + pulse * 2;
        float cx = px + ts / 2f, cy = py + ts / 2f;
        sr.setColor(1f, 1f, 0.784f, 0.235f);
        sr.circle(cx, cy, glowRadius, 12);
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, coreRadius, 12);
    }

    private void drawRectLines(float x, float y, float w, float h, float t, Color c) {
        sr.setColor(c);
        sr.rect(x,         y,         w, t);
        sr.rect(x,         y + h - t, w, t);
        sr.rect(x,         y,         t, h);
        sr.rect(x + w - t, y,         t, h);
    }
}
