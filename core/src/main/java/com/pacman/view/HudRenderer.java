package com.pacman.view;

import com.pacman.model.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;

public class HudRenderer {

    private final SpriteBatch   batch;
    private final ShapeRenderer sr;
    private final BitmapFont    font20, font30, font40;

    public HudRenderer(SpriteBatch batch, ShapeRenderer sr) {
        this.batch = batch;
        this.sr    = sr;

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("PressStart2P-Regular.ttf"));
        font20 = buildFont(gen, 14);
        font30 = buildFont(gen, 20);
        font40 = buildFont(gen, 28);
        gen.dispose();
    }

    public void drawShapes(GameData game) {
        drawLives(game);
        drawFruit(game);
    }

    public void drawText(GameData game) {
        float barY        = Constants.MAZE_HEIGHT * Constants.TILE_SIZE;
        float mazeCenterY = barY / 2f;

        drawString("SCORE: " + game.score,
            10, barY + 18, 20, Color.WHITE);
        drawString("LEVEL: " + game.level,
            Constants.SCREEN_WIDTH / 2f - 30, barY + 18, 20, Color.WHITE);
        drawString("HIGH SCORE: " + game.highScore,
            Constants.SCREEN_WIDTH - 200, barY + 18, 20, Color.WHITE);
        drawString("LIVES:", 10, barY + 58, 20, Color.WHITE);

        if (game.ghostScoreTimer > 0) {
            drawString(String.valueOf(game.ghostScoreValue),
                game.ghostScoreX, game.ghostScoreY, 20, Color.YELLOW);
        }

        drawStateOverlay(game, mazeCenterY);
    }

    public void dispose() {
        font20.dispose();
        font30.dispose();
        font40.dispose();
    }

    private void drawLives(GameData game) {
        float barY    = Constants.MAZE_HEIGHT * Constants.TILE_SIZE;
        float cy      = barY + 49f;
        float startX  = 105f;
        float spacing = 28f;
        float radius  = 10f;
        sr.setColor(Color.YELLOW);
        for (int i = 0; i < game.lives; i++) {
            sr.arc(startX + i * spacing, cy, radius, 30f, 300f, 16);
        }
    }

    private void drawFruit(GameData game) {
        if (game.fruitSpawned == 0 || game.fruitEaten != 0) return;

        float cx = game.fruitX * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
        float cy = game.fruitY * Constants.TILE_SIZE + Constants.TILE_SIZE / 2f;
        float r  = Constants.TILE_SIZE / 2.8f;

        sr.setColor(0.85f, 0.1f, 0.1f, 1f);
        sr.circle(cx, cy, r, 20);
        sr.circle(cx - r * 0.35f, cy - r * 0.75f, r * 0.35f, 12);
        sr.circle(cx + r * 0.35f, cy - r * 0.75f, r * 0.35f, 12);

        sr.setColor(0.1f, 0.75f, 0.1f, 1f);
        sr.triangle(cx, cy - r * 1.1f, cx + r * 0.6f, cy - r * 0.6f, cx, cy - r * 0.5f);

        sr.setColor(0.35f, 0.18f, 0.05f, 1f);
        sr.rectLine(cx, cy - r, cx + r * 0.2f, cy - r * 1.4f, 2f);

        sr.setColor(1f, 1f, 1f, 0.5f);
        sr.ellipse(cx - r * 0.3f, cy - r * 0.5f, r * 0.3f, r * 0.2f);
    }

    private void drawStateOverlay(GameData game, float centerY) {
        switch (game.state) {
            case STATE_INTRO:
                drawString("PAC-MAN",
                    Constants.SCREEN_WIDTH / 2f - 100, centerY - 50, 40, Color.YELLOW);
                drawString("PRESS SPACE TO START",
                    Constants.SCREEN_WIDTH / 2f - 150, centerY + 20, 20, Color.WHITE);
                break;
            case STATE_READY:
                drawString("READY!",
                    Constants.SCREEN_WIDTH / 2f - 50, centerY, 30, Color.YELLOW);
                break;
            case STATE_DEATH:
                drawString("OOPS!",
                    Constants.SCREEN_WIDTH / 2f - 40, centerY, 30, Color.RED);
                break;
            case STATE_LEVEL_COMPLETE:
                drawString("LEVEL COMPLETE!",
                    Constants.SCREEN_WIDTH / 2f - 140, centerY, 30, Color.GREEN);
                break;
            case STATE_GAME_OVER:
                drawString("GAME OVER",
                    Constants.SCREEN_WIDTH / 2f - 110, centerY - 60, 30, Color.RED);
                drawString("SCORE: " + game.score,
                    Constants.SCREEN_WIDTH / 2f - 90,  centerY - 10, 20, Color.YELLOW);
                if (game.score > 0 && game.score >= game.highScore) {
                    drawString("NEW HIGH SCORE!",
                        Constants.SCREEN_WIDTH / 2f - 130, centerY + 30, 20,
                        new Color(1f, 0.84f, 0f, 1f));
                }
                drawString("PRESS SPACE TO RESTART",
                    Constants.SCREEN_WIDTH / 2f - 160, centerY + 75, 20, Color.WHITE);
                break;
            default: break;
        }
    }

    private void drawString(String text, float x, float y, int size, Color color) {
        BitmapFont f = (size >= 35) ? font40 : (size >= 25) ? font30 : font20;
        f.setColor(color);
        f.draw(batch, text, x, y - f.getCapHeight());
    }

    private BitmapFont buildFont(FreeTypeFontGenerator gen, int size) {
        FreeTypeFontParameter p = new FreeTypeFontParameter();
        p.size      = size;
        p.minFilter = Texture.TextureFilter.Linear;
        p.magFilter = Texture.TextureFilter.Linear;
        p.flip      = true;
        return gen.generateFont(p);
    }
}
