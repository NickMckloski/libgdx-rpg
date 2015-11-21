package com.nickm.rpg.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.components.BoundedCamera;
import com.nickm.rpg.manager.GameStateManager;

public abstract class GameState {

	protected GameStateManager gsm;
	protected MainGame game;

	protected SpriteBatch sb;
	protected Stage stage;
	protected BoundedCamera cam;
	protected OrthographicCamera hudCam;

	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		game = gsm.game();
		sb = game.getSpriteBatch();
		stage = game.GetStage();
		cam = game.getCamera();
		hudCam = game.getHUDCamera();
	}

	public abstract void handleInput();

	public abstract void update(float dt);

	public abstract void render();

	public abstract void dispose();

}
