package com.pacman.controller;

import com.pacman.model.*;
import com.pacman.view.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private OrthographicCamera camera;
    private ShapeRenderer      shapeRenderer;
    private SpriteBatch        batch;
    private Renderer          renderer;
    private Viewport           viewport;
    private GameStateManager   stateManager;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        viewport      = new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch         = new SpriteBatch();

        ResourceManager rm = ResourceManager.getInstance();
        rm.load();

        SoundPlayer   sound    = new LibGdxSoundPlayer(rm);
        InputProvider input    = new LibGdxInputProvider();
        MapManager    map      = new MapManager();
        GameData      game     = GameData.getInstance();

        ScoreManager          scorer    = new ScoreManager(game, sound);
        MovementController    movement  = new MovementController(input);
        DotConsumer           dots      = new DotConsumer(sound, scorer);
        FruitManager          fruit     = new FruitManager(sound, scorer);
        GhostCollisionHandler collision = new GhostCollisionHandler(sound, scorer);

        stateManager = new GameStateManager(sound, input, movement, dots, fruit, collision, map);
        renderer     = new Renderer(shapeRenderer, batch, camera);

        GameInitializer.initGame(game, map, sound);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
        camera.setToOrtho(true, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    @Override
    public void render() {
        GameData game = GameData.getInstance();
        game.deltaTime = Gdx.graphics.getDeltaTime();
        game.totalTime += game.deltaTime;

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        stateManager.update(game);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.drawGame(game);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        shapeRenderer.dispose();
        batch.dispose();
        ResourceManager.getInstance().unload();
    }
}
