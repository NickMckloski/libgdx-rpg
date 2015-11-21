package com.nickm.rpg.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.input.Input;
import com.nickm.rpg.manager.AssetsManager;
import com.nickm.rpg.manager.FontManager;
import com.nickm.rpg.manager.GameStateManager;
import com.nickm.rpg.state.GameState;

public class StartMenu extends GameState {
	
	Texture bg;
	
	Table table;
	Label title;
	LabelStyle labelStyle;
    
	
	public StartMenu(GameStateManager gsm) {
		super(gsm);
		//stage = new Stage();
		
		//add inputs to multiplexer
		Input.inputs.addProcessor(stage);
		//set inputprocessor to the multiplexer
		Gdx.input.setInputProcessor(Input.inputs);
		
		bg = MainGame.assets.get(AssetsManager.skyBackground, Texture.class);
		
	    table = new Table();
	    //table.setWidth(stage.getWidth());
	    table.align(Align.center);
	    
	    //generate our fonts
        BitmapFont titleFont = FontManager.generateFont("Arimo", "Regular", 32, true);
        BitmapFont buttonFont = FontManager.generateFont("Arimo", "Regular", 22, true);
        
	    labelStyle = new LabelStyle(titleFont, Color.WHITE);
		title = new Label("RPG Game", labelStyle);
		
		TextureAtlas blueUi = MainGame.assets.get(AssetsManager.blueUi, TextureAtlas.class);
		TextureRegionDrawable playUp = new TextureRegionDrawable(blueUi.findRegion("button_04"));
		TextureRegionDrawable playDown = new TextureRegionDrawable(blueUi.findRegion("button_02"));
		TextButton playButton = new TextButton("Play", new TextButtonStyle(playUp, playDown, playUp, buttonFont));
        
        playButton.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent even, float x, float y) {
        		MainGame.getGameStateManager().setState(GameStateManager.PLAY);
        	}
        });
        
		table.add(title);
		table.row();
		table.add(playButton).padTop(100);
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
		sb.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    sb.end();
	    
	    stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		table.remove();
	}

}
