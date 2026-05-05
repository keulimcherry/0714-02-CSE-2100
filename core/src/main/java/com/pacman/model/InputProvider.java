package com.pacman.model;

public interface InputProvider {
    Direction getRequestedDirection();
    boolean   isStartPressed();
}
