package com.pacman.controller;

import com.pacman.model.Collidable;

public class CollisionDetector {

    public boolean overlaps(Collidable a, Collidable b) {
        float dx   = a.getCollisionX() - b.getCollisionX();
        float dy   = a.getCollisionY() - b.getCollisionY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return dist < Math.min(a.getCollisionRadius(), b.getCollisionRadius());
    }
}
