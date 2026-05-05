package com.pacman.view;

import com.pacman.model.SoundPlayer;

public class LibGdxSoundPlayer implements SoundPlayer {

    private final ResourceManager rm;
    private boolean chompToggle = false;

    public LibGdxSoundPlayer(ResourceManager rm) {
        this.rm = rm;
    }

    @Override public void playChomp() {
        if (chompToggle) {
            rm.soundChomp.stop();  rm.soundChomp.play();
        } else {
            rm.soundChomp2.stop(); rm.soundChomp2.play();
        }
        chompToggle = !chompToggle;
    }
    @Override public void playDeath()         { rm.soundDeath.play(); }
    @Override public void playEatFruit()      { rm.soundEatFruit.play(); }
    @Override public void playEatGhost()      { rm.soundEatGhost.play(); }
    @Override public void playGhostTurnBlue() { rm.soundGhostTurnBlue.play(); }
    @Override public void playHighScore()     { rm.soundHighScore.play(); }
    @Override public void playIntro()         { rm.soundIntro.play(); }
    @Override public void stopIntro()         { rm.soundIntro.stop(); }
    @Override public boolean isIntroPlaying() { return rm.soundIntro.isPlaying(); }
    @Override public void startPowerSiren()   { rm.soundPowerSiren.play(); }
    @Override public void stopPowerSiren()    { rm.soundPowerSiren.stop(); }
    @Override public boolean isPowerSirenPlaying() { return rm.soundPowerSiren.isPlaying(); }
}
