package com.pacman.view;

import com.pacman.model.Direction;
import com.pacman.model.InputProvider;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class LibGdxInputProvider implements InputProvider {

    @Override
    public Direction getRequestedDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    return Direction.DIR_UP;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  return Direction.DIR_DOWN;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  return Direction.DIR_LEFT;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return Direction.DIR_RIGHT;
        return Direction.DIR_NONE;
    }

    @Override
    public boolean isStartPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
    }
}
