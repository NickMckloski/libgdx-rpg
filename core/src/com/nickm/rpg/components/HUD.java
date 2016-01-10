package com.nickm.rpg.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.entity.impl.Player;
import com.nickm.rpg.manager.AssetsManager;
import com.nickm.rpg.manager.FontManager;

public class HUD {

	private Player player;

	//status info
	Label coins;
	public int[][] health;
	private Texture heartFull;
	private Texture heartEmpty;
	public Label actionText;

	//settings
	public Window settingsWindow;
	public Button settingsButton;
	private Table settingsTable;

	//controls
	public TextButton actionButton;
	public Table actionTable;
	public TextButton jumpButton;
	private Table jumpTable;
	public TextButton attackButton;
	private Table attackTable;
	public Touchpad touchpad;

	//deathscreen
	public Label deathText;
	
	public HUD(Player player) {

		this.player = player;

		// grab the ui textures
		TextureAtlas blueUi = MainGame.assets.get(AssetsManager.blueUI, TextureAtlas.class);
		TextureAtlas redUi = MainGame.assets.get(AssetsManager.redUI, TextureAtlas.class);
		TextureAtlas orangeUi = MainGame.assets.get(AssetsManager.orangeUI, TextureAtlas.class);

		// generate the font
		BitmapFont font = FontManager.generateFont("Arimo", "Regular", 20, true);

		/*setup button controls for mobile*/
		// create textures for settings button
		TextureRegionDrawable settings = new TextureRegionDrawable(blueUi.findRegion("icon_tools"));
		settingsButton = new Button(settings);
		settingsButton.setFillParent(true);
		// create table to place button into
		settingsTable = new Table();
		settingsTable.add(settingsButton);

		/*action button*/
		// create textures for jump button
		TextureRegionDrawable actionUp = new TextureRegionDrawable(orangeUi.findRegion("button_04"));
		TextureRegionDrawable actionDown = new TextureRegionDrawable(orangeUi.findRegion("button_02"));
		actionButton = new TextButton("Action", new TextButtonStyle(actionUp, actionDown, actionUp, font));
		actionButton.setFillParent(true);
		// create table to add jump button to
		actionTable = new Table();
		actionTable.setBounds(380, 100, 100, 64);
		actionTable.add(actionButton);

		/*jump button*/
		// create textures for jump button
		TextureRegionDrawable jumpUp = new TextureRegionDrawable(blueUi.findRegion("button_04"));
		TextureRegionDrawable jumpDown = new TextureRegionDrawable(blueUi.findRegion("button_02"));
		jumpButton = new TextButton("Jump", new TextButtonStyle(jumpUp, jumpDown, jumpUp, font));
		jumpButton.setFillParent(true);
		// create table to add jump button to
		jumpTable = new Table();
		jumpTable.setBounds(380, 10, 100, 64);
		jumpTable.add(jumpButton);
		
		/*attack button*/
		// create textures and setup attack button
		TextureRegionDrawable attackUp = new TextureRegionDrawable(redUi.findRegion("button_04"));
		TextureRegionDrawable attackDown = new TextureRegionDrawable(redUi.findRegion("button_02"));
		attackButton = new TextButton("Attack", new TextButtonStyle(attackUp, attackDown, attackUp, font));
		attackButton.setFillParent(true);
		// create table to add the attack button to
		attackTable = new Table();
		attackTable.setBounds(500, 10, 100, 64);
		attackTable.add(attackButton);

		
		// load the heart/health sprites
		heartFull = MainGame.assets.get(AssetsManager.heartFull, Texture.class);
		heartEmpty = MainGame.assets.get(AssetsManager.heartEmpty, Texture.class);

		// add coin counter label and set it's position
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		coins = new Label("Coins: " + Integer.toString(player.coins), labelStyle);
		if (MainGame.isMobileRuntime()) {
			settingsTable.setBounds(640, 460, 32, 32);
			coins.setBounds(20, 420, 100, 50);
		} else {
			settingsTable.setBounds(1200, 655, 45, 45);
			coins.setBounds(10, 665, 100, 50);
		}
		
		/*create touchpad control*/
		// Create a touchpad skin
		Skin touchpadSkin = new Skin();
		// Set background image
		touchpadSkin.add("touchBackground", new Texture("buttons/touchBackground.png"));
		// Set knob image
		touchpadSkin.add("touchKnob", new Texture("buttons/touchKnob.png"));
		// Create TouchPad Style
		TouchpadStyle touchpadStyle = new TouchpadStyle();
		// Create Drawable's from TouchPad skin
		Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
		Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchKnob.setMinWidth(40);
		touchKnob.setMinHeight(40);
		// Apply the Drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		// Create new TouchPad with the created style
		touchpad = new Touchpad(10, touchpadStyle);
		// setBounds(x,y,width,height)
		touchpad.setBounds(15, 15, 100, 100);
		
		
		/*create deathscreen*/
		// generate death font
		BitmapFont deathFont = FontManager.generateFont("Arimo", "Regular", 40, true);
		LabelStyle deathLabelStyle = new LabelStyle(deathFont, Color.WHITE);
		deathText = new Label("You are dead!", deathLabelStyle);
		deathText.setX(MainGame.WINDOW_WIDTH / 2 - (deathText.getWidth()/2));
		deathText.setY(MainGame.WINDOW_HEIGHT / 2 - (deathText.getHeight()/2));
		
		actionText = new Label("Press 'E' to enter.", deathLabelStyle);
		
	}

	public void render(SpriteBatch sb, Stage stage) {
		if (MainGame.isMobileRuntime()) {
			//draw controls if settings window or death screen are not up
			if (!stage.getActors().contains(settingsWindow, true) && !stage.getActors().contains(deathText, true)) {
				stage.addActor(attackTable);
				stage.addActor(jumpTable);
				stage.addActor(touchpad);
			}
			sb.begin();
			drawHeartsAndroid(sb);
		} else {
			sb.begin();
			drawHearts(sb);
		}

		sb.end();

		// coins.remove();
		coins.setText("Coins: " + Integer.toString(player.coins));
		stage.addActor(coins);

		if (!stage.getActors().contains(settingsWindow, true)) {
			stage.addActor(settingsTable);
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	public void setHealth(int status) {
		for (int i = 0; i < health.length; i++) {
			health[i][0] = status;
		}
	}

	private void drawHeartsAndroid(SpriteBatch sb) {// android method
		for (int i = 0; i < health.length; i++) {
			if (health[i][0] == 1) {
				sb.draw(heartFull, 560 - (i * 36), 416, 32, 32);
			} else {
				sb.draw(heartEmpty, 560 - (i * 36), 416, 32, 32);
			}
		}
	}

	private void drawHearts(SpriteBatch sb) {// desktop method
		for (int i = 0; i < health.length; i++) {
			if (health[i][0] == 1) {
				sb.draw(heartFull, 1160 - (i * 48), 660, 40, 40);
			} else {
				sb.draw(heartEmpty, 1160 - (i * 48), 660, 40, 40);
			}
		}
	}

}
