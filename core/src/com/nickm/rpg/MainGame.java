package com.nickm.rpg;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nickm.rpg.components.BoundedCamera;
import com.nickm.rpg.input.Input;
import com.nickm.rpg.manager.AssetsManager;
import com.nickm.rpg.manager.GameStateManager;
import com.nickm.rpg.manager.SaveManager;

public class MainGame extends ApplicationAdapter {

	public static final String TITLE = "Platformer";
	public static int V_WIDTH;
	public static int V_HEIGHT;
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;

	public static final float STEP = 1 / 60f;

	private SpriteBatch sb;
	private Stage stage;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;

	private static GameStateManager gameStateManager;
	public static AssetsManager assets;
	public SaveManager saveManager;

	//saves
	public static boolean fullscreen;
	public static String resolution;
	public static String level = "level1room1"; //level1room1 is default level

	public void create() {
		// Gdx.input.setInputProcessor(new InputManager());
		getDeviceResolution();
		if (isMobileRuntime()) {
			V_WIDTH = 320;
			V_HEIGHT = 240;
		} else {
			V_WIDTH = 640;
			V_HEIGHT = 480;
		}

		// initiate new spritebatch
		sb = new SpriteBatch();

		saveManager = new SaveManager();
		if (!saveManager.isEmpty())
			loadSave();

		// create input multiplexer
		Input.inputs = new InputMultiplexer();
		// create player cam
		cam = new BoundedCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		// create stage
		stage = new Stage();
		// stage.setDebugAll(true);
		// create hud cam
		hudCam = new OrthographicCamera();
		// set hudcam and stage views
		if (isMobileRuntime()) {
			hudCam.setToOrtho(false, V_WIDTH * 2, V_HEIGHT * 2);
			stage.setViewport(new StretchViewport(V_WIDTH * 2, V_HEIGHT * 2));
		} else {
			hudCam.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
			stage.setViewport(new StretchViewport(WINDOW_WIDTH, WINDOW_HEIGHT));
		}

		// apply saved settings
		applySave();

		setGameStateManager(new GameStateManager(this));
		getGameStateManager().pushState(GameStateManager.LOADING);

		// load assets
		MainGame.assets = new AssetsManager();
	}

	public static void getDeviceResolution() {
		WINDOW_WIDTH = Gdx.graphics.getWidth();
		WINDOW_HEIGHT = Gdx.graphics.getHeight();
	}

	public void render() {
		Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());

		getGameStateManager().update(Gdx.graphics.getDeltaTime());
		getGameStateManager().render();
		Input.update();
	}

	private void applySave() {
		if (resolution != null) {
			Gdx.graphics.setDisplayMode(Integer.parseInt(resolution.substring(0, 4)), Integer.parseInt(resolution.substring(5)), MainGame.fullscreen);
		}
	}

	private void loadSave() {
		resolution = SaveManager.loadDataValue("resolution", String.class);
		fullscreen = SaveManager.loadDataValue("fullscreen", Boolean.class) ? true : false;
		if(SaveManager.loadDataValue("level", String.class) != null)
			level = SaveManager.loadDataValue("level", String.class);

	}

	public void dispose() {
		SaveManager.saveDataValue("level", level);
		SaveManager.saveDataValue("resolution", Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
		SaveManager.saveDataValue("fullscreen", Gdx.graphics.isFullscreen() ? true : false);
	}

	public static boolean isMobileRuntime() {
		return Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;
	}

	public static GameStateManager getGameStateManager() {
		return gameStateManager;
	}

	private void setGameStateManager(GameStateManager gameStateManager) {
		MainGame.gameStateManager = gameStateManager;
	}

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public Stage GetStage() {
		return stage;
	}

	public BoundedCamera getCamera() {
		return cam;
	}

	public OrthographicCamera getHUDCamera() {
		return hudCam;
	}

	public void resize(int w, int h) {
		stage.getViewport().update(w, h);
	}

	public void pause() {
	}

	public void resume() {
	}

}
