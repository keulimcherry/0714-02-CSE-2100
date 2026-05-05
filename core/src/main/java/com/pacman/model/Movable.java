package com.pacman.model;

public interface Movable {
    float getX();
    float getY();
    void  setX(float x);
    void  setY(float y);
    int   getTileX();
    int   getTileY();
    void  setTileX(int tx);
    void  setTileY(int ty);
    Direction getDirection();
    void      setDirection(Direction d);
    float     getSpeed();
}
