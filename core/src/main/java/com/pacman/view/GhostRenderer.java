package com.pacman.view;

import com.pacman.model.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GhostRenderer {

    private final ShapeRenderer sr;

    public GhostRenderer(ShapeRenderer sr) {
        this.sr = sr;
    }

    public void draw(GameData game) {
        int ts = Constants.TILE_SIZE;

        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g  = game.ghosts[i];
            float     gx = g.entity.x;
            float     gy = g.entity.y;
            float     cx = gx + ts / 2f;
            float     cy = gy + ts / 2f;

            if (g.mode == GhostMode.MODE_RESPAWNING) continue;

            if (g.mode == GhostMode.MODE_EATEN) {
                drawEyesOnly(cx, cy);
                continue;
            }

            Color color = resolveColor(g);
            drawBody(gx, gy, cx, cy, ts, color);
            drawEyes(gx, gy, ts, g.mode == GhostMode.MODE_FRIGHTENED);

            if (g.mode == GhostMode.MODE_FRIGHTENED) {
                drawFrightenedMouth(gx, gy, ts);
            }
        }
    }

    private Color resolveColor(GhostData g) {
        if (g.mode == GhostMode.MODE_FRIGHTENED) {
            boolean flash = g.frightenedTimer < 2.0f && (int)(g.frightenedTimer * 8) % 2 == 0;
            return flash ? Color.WHITE : Constants.RL_DARKBLUE;
        }
        return g.color;
    }

    private void drawBody(float gx, float gy, float cx, float cy, int ts, Color color) {
        sr.setColor(color);
        sr.circle(cx, cy, ts / 2f, 16);
        sr.rect(gx, gy + ts / 2f, ts, ts / 2f);
        for (int j = 0; j < 3; j++) {
            sr.circle(gx + (j + 0.5f) * ts / 3f, gy + ts - 2, ts / 6f, 8);
        }
    }

    private void drawEyes(float gx, float gy, int ts, boolean frightened) {
        sr.setColor(Color.WHITE);
        sr.circle(gx + ts / 3f,     gy + ts / 3f, 3, 8);
        sr.circle(gx + 2 * ts / 3f, gy + ts / 3f, 3, 8);

        Color pupilColor = frightened ? Constants.RL_PINK : Color.BLUE;
        sr.setColor(pupilColor);
        sr.circle(gx + ts / 3f,     gy + ts / 3f + (frightened ? 2 : 0), frightened ? 1 : 2, 6);
        sr.circle(gx + 2 * ts / 3f, gy + ts / 3f + (frightened ? 2 : 0), frightened ? 1 : 2, 6);
    }

    private void drawFrightenedMouth(float gx, float gy, int ts) {
        sr.setColor(Constants.RL_PINK);
        float mouthY = gy + 2 * ts / 3f;
        for (int k = 0; k < 4; k++) {
            float mx = gx + k * ts / 4f;
            sr.triangle(mx, mouthY, mx + ts / 8f, mouthY - 3, mx + ts / 4f, mouthY);
        }
    }

    private void drawEyesOnly(float cx, float cy) {
        sr.setColor(Color.WHITE);
        sr.circle(cx - 6, cy - 4, 5, 8);
        sr.circle(cx + 6, cy - 4, 5, 8);
        sr.setColor(Color.BLUE);
        sr.circle(cx - 6, cy - 4, 3, 6);
        sr.circle(cx + 6, cy - 4, 3, 6);
    }
}
