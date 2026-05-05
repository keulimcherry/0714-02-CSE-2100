package com.pacman.model;

public class PinkyBehavior extends GhostBehavior {
    @Override
    public int[] getChaseTarget(GhostData ghost, GameData game) {
        int tx = game.pacman.tileX;
        int ty = game.pacman.tileY;
        switch (game.pacman.direction) {
            case DIR_UP:    ty -= 4; tx -= 4; break;
            case DIR_DOWN:  ty += 4;          break;
            case DIR_LEFT:  tx -= 4;          break;
            case DIR_RIGHT: tx += 4;          break;
            default: break;
        }
        return new int[]{ tx, ty };
    }
}
