package com.nickm.rpg.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.input.Input;
import com.nickm.rpg.manager.Assets;
import com.nickm.rpg.manager.GameStateManager;
import com.nickm.rpg.state.GameState;

public class StartMenu extends GameState {
	
	Sprite bg;
	
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
		
		bg = new Sprite(new Texture(Gdx.files.internal("images/grassbg1.gif")));
		bg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
	    table = new Table();
	    //table.setWidth(stage.getWidth());
	    table.align(Align.center);
	    
	    //generate our fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skins/custom/fonts/Arimo/Arimo-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetY = 1;
        BitmapFont titleFont = generator.generateFont(parameter);
        parameter.size = 22;
        BitmapFont buttonFont = generator.generateFont(parameter);
        generator.dispose();
        
	    labelStyle = new LabelStyle(titleFont, Color.WHITE);
		title = new Label("RPG Game", labelStyle);
		
		TextureAtlas blueUi = MainGame.assets.get(Assets.blueUi, TextureAtlas.class);
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
	    bg.draw(sb);
	    sb.end();
	    
	    stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		table.remove();
	}

}
