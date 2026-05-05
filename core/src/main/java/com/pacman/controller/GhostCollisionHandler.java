package com.pacman.controller;

import com.pacman.model.*;

public class GhostCollisionHandler {

    private final SoundPlayer  sound;
    private final ScoreManager scorer;

    public GhostCollisionHandler(SoundPlayer sound, ScoreManager scorer) {
        this.sound  = sound;
        this.scorer = scorer;
    }

    public boolean update(GameData game) {
        for (int i = 0; i < Constants.GHOST_COUNT; i++) {
            GhostData g = game.ghosts[i];

            Collidable pacCollidable   = game.pacman.entity;
            Collidable ghostCollidable = g.entity;

            float dx   = pacCollidable.getCollisionX() - ghostCollidable.getCollisionX();
            float dy   = pacCollidable.getCollisionY() - ghostCollidable.getCollisionY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < ghostCollidable.getCollisionRadius()) {
                if (g.mode == GhostMode.MODE_FRIGHTENED) {
                    eatGhost(game, g);
                } else if (g.mode != GhostMode.MODE_EATEN &&
                           g.mode != GhostMode.MODE_RESPAWNING) {
                    killPacman(game);
                    return true;
                }
            }
        }
        return false;
    }

    private void eatGhost(GameData game, GhostData g) {
        scorer.awardGhostEaten();
        game.ghostScoreX     = g.entity.x;
        game.ghostScoreY     = g.entity.y;
        game.ghostScoreTimer = 1.0f;
        game.ghostsEaten++;
        g.mode = GhostMode.MODE_EATEN;
        sound.playEatGhost();
    }

    private void killPacman(GameData game) {
        game.state      = GameState.STATE_DEATH;
        game.stateTimer = 0;
        sound.playDeath();
        sound.stopPowerSiren();
    }
}
