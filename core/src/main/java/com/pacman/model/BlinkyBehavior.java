package com.pacman.model;

public class BlinkyBehavior extends GhostBehavior {
    @Override
    public int[] getChaseTarget(GhostData ghost, GameData game) {
        return new int[]{ game.pacman.tileX, game.pacman.tileY };
    }
}
