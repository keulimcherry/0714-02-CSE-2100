package com.pacman.model;

public class Entity implements Movable, Renderable, Collidable {

    public float     x, y;
    public Direction direction     = Direction.DIR_NONE;
    public Direction nextDirection = Direction.DIR_NONE;
    public float     speed;
    public int       tileX, tileY;

    @Override public float     getX()                    { return x; }
    @Override public float     getY()                    { return y; }
    @Override public void      setX(float x)             { this.x = x; }
    @Override public void      setY(float y)             { this.y = y; }
    @Override public int       getTileX()                { return tileX; }
    @Override public int       getTileY()                { return tileY; }
    @Override public void      setTileX(int tx)          { this.tileX = tx; }
    @Override public void      setTileY(int ty)          { this.tileY = ty; }
    @Override public Direction getDirection()            { return direction; }
    @Override public void      setDirection(Direction d) { this.direction = d; }
    @Override public float     getSpeed()                { return speed; }

    @Override public float getRenderX() { return x; }
    @Override public float getRenderY() { return y; }

    @Override public float getCollisionX()      { return x + Constants.TILE_SIZE / 2f; }
    @Override public float getCollisionY()      { return y + Constants.TILE_SIZE / 2f; }
    @Override public float getCollisionRadius() { return Constants.TILE_SIZE * 0.7f; }
}
