package com.pacman.model;

import com.badlogic.gdx.graphics.Color;

public class GhostData {
    public Entity       entity          = new Entity();
    public GhostType    type;
    public GhostMode    mode            = GhostMode.MODE_SCATTER;
    public int          scatterX, scatterY;
    public float        modeTimer;
    public float        frightenedTimer;
    public Color        color           = new Color();
    public float        releaseTimer;
    public boolean      released;
    public boolean      inHouse;
    public float        homeX, homeY;
    public float        respawnTimer;
    public GhostBehavior behavior;
}
