package com.nickm.rpg.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetsManager {

	private AssetManager manager;

	// ui assets
	public static String blueUI = "skins/colorskins/ui-blue.atlas";
	public static String redUI = "skins/colorskins/ui-red.atlas";
	public static String orangeUI = "skins/colorskins/ui-orange.atlas";
	public static String heartFull = "images/heartfull.png";
	public static String heartEmpty = "images/heartempty.png";

	// images
	public static String skyBackground = "images/grassbg1.gif";

	// entity assets
	public static String playerSheet = "images/playersheet.png";// playersheet1
																// currently not
																// in use
	public static String playerSheet2 = "images/playertest.png";
	public static String bat = "images/bat.png";
	public static String crystal = "images/crystal.png";
	public static String coin = "images/coin.png";

	/**
	 * Construct our assets class
	 */
	public AssetsManager() {
		// initiate asset manager
		manager = new AssetManager();

		/* load assets */
		// ui assets
		manager.load(blueUI, TextureAtlas.class);
		manager.load(redUI, TextureAtlas.class);
		manager.load(orangeUI, TextureAtlas.class);
		manager.load(heartFull, Texture.class);
		manager.load(heartEmpty, Texture.class);

		// images
		manager.load(skyBackground, Texture.class);

		// entity assets
		manager.load(playerSheet, Texture.class);
		manager.load(playerSheet2, Texture.class);
		manager.load(bat, Texture.class);
		manager.load(crystal, Texture.class);
		manager.load(coin, Texture.class);

	}

	public <T> T get(String fileName, Class<T> type) {
		return manager.get(fileName, type);
	}

	public AssetManager getManager() {
		return manager;
	}

	public void dispose() {
		manager.dispose();
	}

}
