package com.pacman.model;

public class ClydeBehavior extends GhostBehavior {
    private static final float PROXIMITY_THRESHOLD = 4.0f;

    @Override
    public int[] getChaseTarget(GhostData ghost, GameData game) {
        int   dx   = ghost.entity.tileX - game.pacman.tileX;
        int   dy   = ghost.entity.tileY - game.pacman.tileY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist > PROXIMITY_THRESHOLD) {
            return new int[]{ game.pacman.tileX, game.pacman.tileY };
        }
        return getScatterTarget(ghost);
    }
}
