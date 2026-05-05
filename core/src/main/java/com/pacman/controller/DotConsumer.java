package com.pacman.controller;

import com.pacman.model.*;
import com.pacman.view.ResourceManager;

public class DotConsumer {

    private static final float FRIGHTENED_DURATION = 6.0f;

    private final SoundPlayer  sound;
    private final ScoreManager scorer;
    private boolean            chompToggle = false;

    public DotConsumer(SoundPlayer sound, ScoreManager scorer) {
        this.sound  = sound;
        this.scorer = scorer;
    }

    public void update(GameData game) {
        int tx   = game.pacman.tileX;
        int ty   = game.pacman.tileY;
        int tile = game.maze[ty][tx];

        if (tile == 2)      eatDot(game, tx, ty);
        else if (tile == 3) eatPowerPellet(game, tx, ty);
    }

    private void eatDot(GameData game, int tx, int ty) {
        game.maze[ty][tx] = 0;
        game.dotsEaten++;
        scorer.awardDot();
        playAlternatingChomp();
    }

    private void playAlternatingChomp() {
        ResourceManager rm = ResourceManager.getInstance();
        if (chompToggle) {
            rm.soundChomp.stop();  rm.soundChomp.play();
        } else {
            rm.soundChomp2.stop(); rm.soundChomp2.play();
        }
        chompToggle = !chompToggle;
    }

    private void eatPowerPellet(GameData game, int tx, int ty) {
        game.maze[ty][tx]      = 0;
        game.dotsEaten++;
        scorer.awardPowerPellet();
        game.powerPelletActive = 1;
        game.powerPelletTimer  = FRIGHTENED_DURATION;
        game.ghostsEaten       = 0;

        sound.playGhostTurnBlue();
        sound.stopPowerSiren();

        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g = game.ghosts[i];
            if (g.mode != GhostMode.MODE_EATEN &&
                g.mode != GhostMode.MODE_RESPAWNING &&
                g.released && !g.inHouse) {
                g.mode            = GhostMode.MODE_FRIGHTENED;
                g.frightenedTimer = FRIGHTENED_DURATION;
            }
        }
    }
}
