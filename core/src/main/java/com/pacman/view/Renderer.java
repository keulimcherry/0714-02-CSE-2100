package com.pacman.view;

import com.pacman.model.GameData;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Gdx;

public class Renderer {

    private final ShapeRenderer  sr;
    private final SpriteBatch    batch;

    private final MazeRenderer   mazeRenderer;
    private final GhostRenderer  ghostRenderer;
    private final PacmanRenderer pacmanRenderer;
    private final HudRenderer    hudRenderer;

    public Renderer(ShapeRenderer sr, SpriteBatch batch, OrthographicCamera camera) {
        this.sr    = sr;
        this.batch = batch;

        mazeRenderer   = new MazeRenderer(sr);
        ghostRenderer  = new GhostRenderer(sr);
        pacmanRenderer = new PacmanRenderer(sr);
        hudRenderer    = new HudRenderer(batch, sr);
    }

    public void drawGame(GameData game) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeType.Filled);
        mazeRenderer.draw(game);
        ghostRenderer.draw(game);
        pacmanRenderer.draw(game);
        hudRenderer.drawShapes(game);
        sr.end();

        batch.begin();
        hudRenderer.drawText(game);
        batch.end();
    }

    public void dispose() {
        hudRenderer.dispose();
    }
}
