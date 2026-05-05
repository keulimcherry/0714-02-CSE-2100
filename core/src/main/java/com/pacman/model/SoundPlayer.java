package com.pacman.model;

public interface SoundPlayer {
    void playChomp();
    void playDeath();
    void playEatFruit();
    void playEatGhost();
    void playGhostTurnBlue();
    void playHighScore();
    void playIntro();
    void stopIntro();
    boolean isIntroPlaying();
    void startPowerSiren();
    void stopPowerSiren();
    boolean isPowerSirenPlaying();
}
