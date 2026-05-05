package com.pacman.controller;

import com.pacman.model.*;

public class GameInitializer {

    public static void initGame(GameData game, MapManager mapManager, SoundPlayer sound) {
        int savedHigh    = game.highScore;
        game.score       = 0;
        game.highScore   = savedHigh;
        game.lives       = Constants.MAX_LIVES;
        game.level       = 1;
        game.state       = GameState.STATE_INTRO;
        game.stateTimer  = 0;
        game.sirenTimer  = 0;
        game.rainbowMode = false;
        game.rainbowTimer= 0.0f;

        mapManager.initMaze(game);
        mapManager.resetLevel(game);
        sound.playIntro();
    }
}
