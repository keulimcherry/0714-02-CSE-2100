package com.pacman.controller;

import com.pacman.model.*;

public class MovementController {

    private final InputProvider input;

    public MovementController(InputProvider input) {
        this.input = input;
    }

    public void update(GameData game) {
        game.pacman.syncToEntity();

        Direction requested = input.getRequestedDirection();
        if (requested != Direction.DIR_NONE) {
            game.pacman.nextDirection = requested;
        }

        recalcTile(game);

        boolean alignedX = ((int) game.pacman.x % Constants.TILE_SIZE) == 0;
        boolean alignedY = ((int) game.pacman.y % Constants.TILE_SIZE) == 0;
        boolean aligned  = alignedX && alignedY;

        if (aligned && game.pacman.nextDirection != Direction.DIR_NONE) {
            if (canMove(game, game.pacman.tileX, game.pacman.tileY, game.pacman.nextDirection)) {
                game.pacman.direction = game.pacman.nextDirection;
            }
        }

        if (!aligned || canMove(game, game.pacman.tileX, game.pacman.tileY, game.pacman.direction)) {
            applyMovement(game);
        } else {
            snapToTile(game);
        }

        if (game.pacman.tileY == 14) {
            if (game.pacman.x < 0) {
                game.pacman.x     = (float) ((Constants.MAZE_WIDTH - 1) * Constants.TILE_SIZE);
                game.pacman.tileX = Constants.MAZE_WIDTH - 1;
            } else if (game.pacman.x >= Constants.MAZE_WIDTH * Constants.TILE_SIZE) {
                game.pacman.x     = 0;
                game.pacman.tileX = 0;
            }
        }

        recalcTile(game);
        clampTile(game);
        game.pacman.syncToEntity();
    }

    private boolean canMove(GameData game, int x, int y, Direction dir) {
        return MapManager.canMove(x, y, dir, game);
    }

    private void applyMovement(GameData game) {
        switch (game.pacman.direction) {
            case DIR_UP:    game.pacman.y -= game.pacman.speed; break;
            case DIR_DOWN:  game.pacman.y += game.pacman.speed; break;
            case DIR_LEFT:  game.pacman.x -= game.pacman.speed; break;
            case DIR_RIGHT: game.pacman.x += game.pacman.speed; break;
            default: break;
        }
    }

    private void snapToTile(GameData game) {
        game.pacman.x         = game.pacman.tileX * Constants.TILE_SIZE;
        game.pacman.y         = game.pacman.tileY * Constants.TILE_SIZE;
        game.pacman.direction = Direction.DIR_NONE;
    }

    private void recalcTile(GameData game) {
        int centerX = (int) (game.pacman.x + Constants.TILE_SIZE / 2);
        int centerY = (int) (game.pacman.y + Constants.TILE_SIZE / 2);
        game.pacman.tileX = centerX / Constants.TILE_SIZE;
        game.pacman.tileY = centerY / Constants.TILE_SIZE;
    }

    private void clampTile(GameData game) {
        game.pacman.tileX = Math.max(0, Math.min(game.pacman.tileX, Constants.MAZE_WIDTH  - 1));
        game.pacman.tileY = Math.max(0, Math.min(game.pacman.tileY, Constants.MAZE_HEIGHT - 1));
    }
}
