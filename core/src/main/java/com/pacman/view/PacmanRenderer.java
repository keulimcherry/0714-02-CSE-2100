package com.pacman.view;

import com.pacman.model.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PacmanRenderer {

    private final ShapeRenderer sr;

    public PacmanRenderer(ShapeRenderer sr) {
        this.sr = sr;
    }

    public void draw(GameData game) {
        if (game.state == GameState.STATE_DEATH &&
            (int)(game.stateTimer * 10) % 2 != 0) return;

        float cx = game.pacman.x + Constants.TILE_SIZE / 2.0f;
        float cy = game.pacman.y + Constants.TILE_SIZE / 2.0f;
        float r  = Constants.TILE_SIZE / 2.0f;

        float angle      = directionToAngle(game.pacman.direction);
        float mouthAngle = computeMouthAngle(game.totalTime);

        sr.setColor(Color.YELLOW);
        sr.arc(cx, cy, r, angle + mouthAngle, 360 - 2 * mouthAngle, 20);
    }

    private float directionToAngle(Direction dir) {
        switch (dir) {
            case DIR_RIGHT: return 0;
            case DIR_DOWN:  return 90;
            case DIR_LEFT:  return 180;
            case DIR_UP:    return 270;
            default:        return 0;
        }
    }

    private float computeMouthAngle(float totalTime) {
        int animFrame = (int)(totalTime * 10) % 4;
        return (animFrame % 2 == 0) ? 30.0f : 0.0f;
    }
}
