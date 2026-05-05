package com.pacman.model;

public enum GhostType {
    GHOST_BLINKY(0),
    GHOST_PINKY(1),
    GHOST_INKY(2),
    GHOST_CLYDE(3);

    public final int index;
    GhostType(int index) { this.index = index; }
}
