package com.pacman.controller;

import com.pacman.model.*;

public class FruitManager {

    private static final float FRUIT_LIFETIME = 10.0f;
    private static final float FRUIT_TILE_X   = 13.0f;
    private static final float FRUIT_TILE_Y   = 17.0f;

    private final SoundPlayer  sound;
    private final ScoreManager scorer;

    public FruitManager(SoundPlayer sound, ScoreManager scorer) {
        this.sound  = sound;
        this.scorer = scorer;
    }

    public void update(GameData game) {
        trySpawn(game);
        tickTimer(game);
        checkEaten(game);
    }

    private void trySpawn(GameData game) {
        if (game.fruitSpawned == 0 && game.dotsEaten >= game.totalDots / 2) {
            game.fruitSpawned = 1;
            game.fruitX       = FRUIT_TILE_X;
            game.fruitY       = FRUIT_TILE_Y;
            game.fruitTimer   = FRUIT_LIFETIME;
        }
    }

    private void tickTimer(GameData game) {
        if (game.fruitSpawned != 0 && game.fruitEaten == 0) {
            game.fruitTimer -= game.deltaTime;
            if (game.fruitTimer <= 0) game.fruitSpawned = 0;
        }
    }

    private void checkEaten(GameData game) {
        if (game.fruitSpawned == 0 || game.fruitEaten != 0) return;
        if (game.pacman.tileX == (int) game.fruitX &&
            game.pacman.tileY == (int) game.fruitY) {
            scorer.awardFruit();
            game.fruitEaten = 1;
            sound.playEatFruit();
        }
    }
}
