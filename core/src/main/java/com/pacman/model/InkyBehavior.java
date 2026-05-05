package com.pacman.model;

public class InkyBehavior extends GhostBehavior {
    @Override
    public int[] getChaseTarget(GhostData ghost, GameData game) {
        int pivX = game.pacman.tileX;
        int pivY = game.pacman.tileY;
        switch (game.pacman.direction) {
            case DIR_UP:    pivY -= 2; pivX -= 2; break;
            case DIR_DOWN:  pivY += 2;             break;
            case DIR_LEFT:  pivX -= 2;             break;
            case DIR_RIGHT: pivX += 2;             break;
            default: break;
        }
        int bx = game.ghosts[GhostType.GHOST_BLINKY.index].entity.tileX;
        int by = game.ghosts[GhostType.GHOST_BLINKY.index].entity.tileY;
        return new int[]{ pivX + (pivX - bx), pivY + (pivY - by) };
    }
}
