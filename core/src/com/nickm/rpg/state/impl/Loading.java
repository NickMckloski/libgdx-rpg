package com.nickm.rpg.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.manager.FontManager;
import com.nickm.rpg.manager.GameStateManager;
import com.nickm.rpg.state.GameState;

public class Loading extends GameState {

	Sprite bg;
	Label text;
	Table table;
	ProgressBar loadingBar;

	public Loading(GameStateManager gsm) {
		super(gsm);

		// setup background
		bg = new Sprite(new Texture(Gdx.files.internal("images/grassbg1.gif")));
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// setup text
		BitmapFont font = FontManager.generateFont("Arimo", "Regular", 26, true);
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		text = new Label("Loading: 0%", labelStyle);

		// create skin for our textures
		Skin barSkin = new Skin();
		// Set background image
		barSkin.add("barBg", new Texture("images/ui/loadingBack.png"));
		// Set knob fill
		barSkin.add("knobFill", new Texture("images/ui/loadingKnobFill.png"));
		ProgressBarStyle style = new ProgressBarStyle(barSkin.getDrawable("barBg"), barSkin.getDrawable("knobFill"));
		style.knobBefore = style.knob;
		loadingBar = new ProgressBar(0, 1, .001f, false, style);

		// setup table
		table = new Table();
		table.add(text);
		table.row();
		table.add(loadingBar).size(350, loadingBar.getPrefHeight());
		stage.addActor(table);

	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update(float dt) {

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		bg.draw(sb);
		sb.end();

		// update the loading bar
		if (!MainGame.assets.getManager().update()) {
			text.setText("Loading: " + (int) (loadingBar.getPercent() * 100) + "%");
			loadingBar.setValue(MainGame.assets.getManager().getProgress());
		} else if (!text.getText().toString().contains("100%")) {
			text.setText("Loading: 100%");
			loadingBar.setValue(1);
		} else {
			MainGame.getGameStateManager().setState(GameStateManager.STARTMENU);
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		table.remove();
	}

}
