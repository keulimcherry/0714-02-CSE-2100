package com.pacman.model;

public class ScoreManager {

    private final GameData    game;
    private final SoundPlayer sound;

    public ScoreManager(GameData game, SoundPlayer sound) {
        this.game  = game;
        this.sound = sound;
    }

    public void awardDot() {
        addPoints(10);
    }

    public void awardPowerPellet() {
        addPoints(50);
    }

    public void awardGhostEaten() {
        int[] pts    = {200, 400, 800, 1600};
        int   earned = pts[Math.min(game.ghostsEaten, 3)];
        addPoints(earned);
        game.ghostScoreValue = earned;
    }

    public void awardFruit() {
        int points = Constants.FRUIT_POINTS[(game.level - 1) % 8];
        addPoints(points);
    }

    private void addPoints(int amount) {
        int before = game.score;
        game.score += amount;
        checkNewHighScore(before);
    }

    private void checkNewHighScore(int scoreBefore) {
        if (game.score > game.highScore && scoreBefore <= game.highScore) {
            game.rainbowMode  = true;
            game.rainbowTimer = 0.0f;
            sound.playHighScore();
        }
    }
}
