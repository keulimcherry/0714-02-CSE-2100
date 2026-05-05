package com.pacman.model;

public abstract class GhostBehavior {
    public abstract int[] getChaseTarget(GhostData ghost, GameData game);

    public int[] getScatterTarget(GhostData ghost) {
        return new int[]{ ghost.scatterX, ghost.scatterY };
    }
}
