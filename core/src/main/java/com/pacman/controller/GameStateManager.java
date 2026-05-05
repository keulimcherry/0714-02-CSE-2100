package com.pacman.controller;

import com.pacman.model.*;

public class GameStateManager {

    private static final float READY_DELAY          = 2.0f;
    private static final float DEATH_DELAY          = 2.0f;
    private static final float LEVEL_COMPLETE_DELAY = 2.0f;
    private static final float GAME_OVER_DELAY      = 3.0f;

    private final SoundPlayer         sound;
    private final InputProvider       input;
    private final MovementController  movement;
    private final DotConsumer         dotConsumer;
    private final FruitManager        fruitManager;
    private final GhostCollisionHandler collisionHandler;
    private final MapManager          mapManager;

    public GameStateManager(SoundPlayer sound,
                            InputProvider input,
                            MovementController movement,
                            DotConsumer dotConsumer,
                            FruitManager fruitManager,
                            GhostCollisionHandler collisionHandler,
                            MapManager mapManager) {
        this.sound            = sound;
        this.input            = input;
        this.movement         = movement;
        this.dotConsumer      = dotConsumer;
        this.fruitManager     = fruitManager;
        this.collisionHandler = collisionHandler;
        this.mapManager       = mapManager;
    }

    public void update(GameData game) {
        game.stateTimer += game.deltaTime;

        switch (game.state) {
            case STATE_INTRO:         handleIntro(game);         break;
            case STATE_READY:         handleReady(game);         break;
            case STATE_PLAYING:       handlePlaying(game);       break;
            case STATE_DEATH:         handleDeath(game);         break;
            case STATE_LEVEL_COMPLETE:handleLevelComplete(game); break;
            case STATE_GAME_OVER:     handleGameOver(game);      break;
        }
    }

    private void handleIntro(GameData game) {
        if (!sound.isIntroPlaying() || input.isStartPressed()) {
            sound.stopIntro();
            transitionTo(game, GameState.STATE_READY);
        }
    }

    private void handleReady(GameData game) {
        if (game.stateTimer > READY_DELAY) transitionTo(game, GameState.STATE_PLAYING);
    }

    private void handlePlaying(GameData game) {
        movement.update(game);
        dotConsumer.update(game);
        fruitManager.update(game);

        boolean died = collisionHandler.update(game);
        if (died) return;

        GhostController.updateGhosts(game, sound);

        if (game.rainbowMode) {
            game.rainbowTimer += game.deltaTime;
            if (game.rainbowTimer > 5.0f) game.rainbowMode = false;
        }

        if (game.powerPelletActive != 0) {
            game.powerPelletTimer -= game.deltaTime;
            if (game.powerPelletTimer <= 0) {
                game.powerPelletActive = 0;
                game.ghostsEaten       = 0;
            }
        }

        if (game.ghostScoreTimer > 0) game.ghostScoreTimer -= game.deltaTime;

        if (game.dotsEaten >= game.totalDots) {
            sound.stopPowerSiren();
            transitionTo(game, GameState.STATE_LEVEL_COMPLETE);
        }
    }

    private void handleDeath(GameData game) {
        if (game.stateTimer > DEATH_DELAY) {
            game.lives--;
            if (game.lives <= 0) {
                if (game.score > game.highScore) {
                    game.highScore = game.score;
                    sound.playHighScore();
                }
                transitionTo(game, GameState.STATE_GAME_OVER);
            } else {
                mapManager.resetPositions(game);
                transitionTo(game, GameState.STATE_READY);
            }
        }
    }

    private void handleLevelComplete(GameData game) {
        if (game.stateTimer > LEVEL_COMPLETE_DELAY) {
            game.level++;
            mapManager.initMaze(game);
            mapManager.resetLevel(game);
            transitionTo(game, GameState.STATE_READY);
        }
    }

    private void handleGameOver(GameData game) {
        if (game.stateTimer > GAME_OVER_DELAY && input.isStartPressed()) {
            GameInitializer.initGame(game, mapManager, sound);
        }
    }

    private void transitionTo(GameData game, GameState next) {
        game.state      = next;
        game.stateTimer = 0;
    }
}
