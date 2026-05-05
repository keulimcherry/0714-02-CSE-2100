package com.pacman.model;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    public static final int SCREEN_WIDTH  = 672;
    public static final int SCREEN_HEIGHT = 864;
    public static final int TILE_SIZE     = 24;
    public static final int MAZE_WIDTH    = 28;
    public static final int MAZE_HEIGHT   = 31;
    public static final int GHOST_COUNT   = 4;
    public static final int MAX_LIVES     = 3;

    public static final int[] FRUIT_POINTS = { 100, 300, 500, 700, 1000, 2000, 3000, 5000 };

    public static final Color RL_RED      = new Color(0.902f, 0.161f, 0.216f, 1f);
    public static final Color RL_PINK     = new Color(1f,     0.427f, 0.761f, 1f);
    public static final Color RL_SKYBLUE  = new Color(0.4f,   0.749f, 1f,     1f);
    public static final Color RL_ORANGE   = new Color(1f,     0.631f, 0f,     1f);
    public static final Color RL_DARKBLUE = new Color(0f,     0.322f, 0.675f, 1f);
    public static final Color RL_WHITE    = Color.WHITE;
    public static final Color RL_YELLOW   = Color.YELLOW;
    public static final Color RL_BLACK    = Color.BLACK;
    public static final Color RL_BLUE     = Color.BLUE;
    public static final Color RL_GREEN    = Color.GREEN;
    public static final Color RL_RED_FULL = new Color(1f, 0f, 0f, 1f);
}
