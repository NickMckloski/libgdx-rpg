package com.nickm.rpg.components;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.entity.impl.Player;
import com.nickm.rpg.input.Input;
import com.nickm.rpg.manager.Assets;
import com.nickm.rpg.state.impl.Play;

public class HUD {

	private Player player;

    //atlas
    //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skins/colorskins/ui-blue.atlas"));
    
    TextureRegionDrawable settings;
    public Button settingsButton;
    Table settingsTable;
	public int[][] health;
	private Texture heartFull;
	private Texture heartEmpty;
	private TextureRegion[] numbers;

    TextureRegionDrawable jumpUp;
    TextureRegionDrawable jumpDown;
    public TextButton jumpButton;
    TextureRegionDrawable attackUp;
    TextureRegionDrawable attackDown;
    public TextButton attackButton;
    BitmapFont font;
    Table jumpTable;
    Table attackTable;
    
    Label coins;
    
    public Touchpad touchpad;
	private TouchpadStyle touchpadStyle;
	private Skin touchpadSkin;
	private Drawable touchBackground;
	private Drawable touchKnob;

	public Window settingsWindow;
	
    public HUD(Player player) {
		
		this.player = player;
		
		TextureAtlas blueUi = MainGame.assets.get(Assets.blueUi, TextureAtlas.class);
		TextureAtlas redUi = MainGame.assets.get(Assets.redUi, TextureAtlas.class);

		//font
		font = new BitmapFont(Gdx.files.internal("skins/custom/Arimo.fnt"), Gdx.files.internal("skins/custom/Arimo.png"), false);
		
		//load the heart/health sprites
		heartFull = new Texture(Gdx.files.internal("images/heartfull.png"));
		heartEmpty = new Texture(Gdx.files.internal("images/heartempty.png"));
		
		//create textures for settings button
		settings = new TextureRegionDrawable(blueUi.findRegion("icon_tools"));
		settingsButton = new Button(settings);
		settingsButton.setFillParent(true);
		//create table to place button into
		settingsTable = new Table();
		settingsTable.add(settingsButton);

        //add coin counter label and set it's position 
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		coins = new Label("Coins: "+Integer.toString(player.coins), labelStyle);
		if(MainGame.getRunType() == ApplicationType.Android) {
			settingsTable.setBounds(640, 460, 32, 32);
			coins.setBounds(20, 420, 100, 50);
		} else {
			settingsTable.setBounds(1200, 655, 45, 45);
			coins.setBounds(10, 665, 100, 50);
		}

		//create textures for jump button
		jumpUp = new TextureRegionDrawable(blueUi.findRegion("button_04"));
		jumpDown = new TextureRegionDrawable(blueUi.findRegion("button_02"));
        jumpButton = new TextButton("Jump", new TextButtonStyle(jumpUp, jumpDown, jumpUp, font));
        jumpButton.setFillParent(true);
		//create table to add jump button to
        jumpTable = new Table();
        jumpTable.setBounds(380, 10, 100, 64);
        jumpTable.add(jumpButton);
        

        //create textures and setup attack button
		attackUp = new TextureRegionDrawable(redUi.findRegion("button_04"));
        attackDown = new TextureRegionDrawable(redUi.findRegion("button_02"));
		attackButton = new TextButton("Attack", new TextButtonStyle(attackUp, attackDown, attackUp, font));
        attackButton.setFillParent(true);
		//create table to add the attack button to
        attackTable = new Table();
		attackTable.setBounds(500, 10, 100, 64);
		attackTable.add(attackButton);
		
        //Create a touchpad skin	
      	touchpadSkin = new Skin();
      	//Set background image
      	touchpadSkin.add("touchBackground", new Texture("buttons/touchBackground.png"));
      	//Set knob image
      	touchpadSkin.add("touchKnob", new Texture("buttons/touchKnob.png"));
      	//Create TouchPad Style
      	touchpadStyle = new TouchpadStyle();
      	//Create Drawable's from TouchPad skin
      	touchBackground = touchpadSkin.getDrawable("touchBackground");
      	touchKnob = touchpadSkin.getDrawable("touchKnob");
      	touchKnob.setMinWidth(40);
      	touchKnob.setMinHeight(40);
      	//Apply the Drawables to the TouchPad Style
      	touchpadStyle.background = touchBackground;
      	touchpadStyle.knob = touchKnob;
      	//Create new TouchPad with the created style
      	touchpad = new Touchpad(10, touchpadStyle);
      	//setBounds(x,y,width,height)
      	touchpad.setBounds(15, 15, 100, 100);
	}
	
	public void render(SpriteBatch sb, Stage stage) {
		if(MainGame.getRunType() == ApplicationType.Android) {
			if(!stage.getActors().contains(settingsWindow, true)) {
				stage.addActor(attackTable);
				stage.addActor(jumpTable);
				stage.addActor(touchpad);
			}
			sb.begin();
			drawHeartsAndroid(sb);
		} else {
			sb.begin();
			/*stage.addActor(attackTable);
			stage.addActor(jumpTable);
			stage.addActor(touchpad);*/
			drawHearts(sb);
		}

		sb.end();
		
		//coins.remove();
		coins.setText("Coins: "+Integer.toString(player.coins));
		stage.addActor(coins);
		
		if(!stage.getActors().contains(settingsWindow, true)) {
			stage.addActor(settingsTable);
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	public void setHealth(int status) {
		for(int i = 0; i < health.length; i++) {
			health[i][0] = status;
		}
	}
	
	private void drawHeartsAndroid(SpriteBatch sb) {//android method
		for(int i = 0; i < health.length; i++) {
			if(health[i][0] == 1) {
				sb.draw(heartFull, 560-(i*36), 416, 32, 32);
			} else {
				sb.draw(heartEmpty, 560-(i*36), 416, 32, 32);
			}
		}
	}
	private void drawHearts(SpriteBatch sb) {//desktop method
		for(int i = 0; i < health.length; i++) {
			if(health[i][0] == 1) {
				sb.draw(heartFull, 1160-(i*48), 660, 40, 40);
			} else {
				sb.draw(heartEmpty, 1160-(i*48), 660, 40, 40);
			}
		}
	}
	
}

