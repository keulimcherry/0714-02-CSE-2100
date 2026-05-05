package com.pacman.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class ResourceManager {

    private static ResourceManager instance;

    public Texture spritesheet;

    public Sound soundChomp;
    public Sound soundChomp2;
    public Sound soundDeath;
    public Sound soundEatFruit;
    public Sound soundEatGhost;
    public Sound soundGhostRunning;
    public Sound soundGhostTurnBlue;
    public Sound soundHighScore;

    public Music soundIntro;
    public Music soundPowerSiren;

    public static ResourceManager getInstance() {
        if (instance == null) instance = new ResourceManager();
        return instance;
    }

    public void load() {
        spritesheet       = new Texture(Gdx.files.internal("spritesheet.png"));
        soundChomp        = Gdx.audio.newSound(Gdx.files.internal("chomp1.wav"));
        soundChomp2       = Gdx.audio.newSound(Gdx.files.internal("chomp2.wav"));
        soundDeath        = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
        soundEatFruit     = Gdx.audio.newSound(Gdx.files.internal("eat_fruit.wav"));
        soundEatGhost     = Gdx.audio.newSound(Gdx.files.internal("eat_ghost.wav"));
        soundGhostRunning = Gdx.audio.newSound(Gdx.files.internal("ghost_running_away.wav"));
        soundGhostTurnBlue= Gdx.audio.newSound(Gdx.files.internal("ghost_turn_blue.wav"));
        soundHighScore    = Gdx.audio.newSound(Gdx.files.internal("high_score.wav"));
        soundIntro        = Gdx.audio.newMusic(Gdx.files.internal("Intro.wav"));
        soundPowerSiren   = Gdx.audio.newMusic(Gdx.files.internal("power_siren.wav"));
    }

    public void unload() {
        if (spritesheet        != null) spritesheet.dispose();
        if (soundChomp         != null) soundChomp.dispose();
        if (soundChomp2        != null) soundChomp2.dispose();
        if (soundDeath         != null) soundDeath.dispose();
        if (soundEatFruit      != null) soundEatFruit.dispose();
        if (soundEatGhost      != null) soundEatGhost.dispose();
        if (soundGhostRunning  != null) soundGhostRunning.dispose();
        if (soundGhostTurnBlue != null) soundGhostTurnBlue.dispose();
        if (soundHighScore     != null) soundHighScore.dispose();
        if (soundIntro         != null) soundIntro.dispose();
        if (soundPowerSiren    != null) soundPowerSiren.dispose();
    }
}
