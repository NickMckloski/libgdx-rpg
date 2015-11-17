package com.nickm.rpg.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class AssetsManager {

	private AssetManager manager;
	
	//ui assets
	public static String blueUi = "skins/colorskins/ui-blue.atlas";
	public static String redUi = "skins/colorskins/ui-red.atlas";
	public static String heartEmpty = "skins/colorskins/ui-red.atlas";
	public static String heartFull = "skins/colorskins/ui-red.atlas";
	
	//entity assets
	public static String playerSheet = "images/playersheet.png";//playersheet1 currently not in use
	public static String playerSheet2 = "images/playertest.png";
	public static String bat = "images/bat.png";
	public static String crystal = "images/crystal.png";
	public static String coin = "images/coin.png";
	
	/**
	 * Construct our assets class
	 */
	public AssetsManager() {
		//initiate asset manager
		this.manager = new AssetManager();
		
		/*load assets*/
		//ui assets
		manager.load(blueUi, TextureAtlas.class);
		manager.load(redUi, TextureAtlas.class);
		manager.load(heartEmpty, Texture.class);
		manager.load(heartFull, Texture.class);
		
		//entity assets
		manager.load(playerSheet, Texture.class);
		manager.load(playerSheet2, Texture.class);
		manager.load(bat, Texture.class);
		manager.load(crystal, Texture.class);
		manager.load(coin, Texture.class);
		
		//finalize
		//XXX: add loading game state and display a loading bar
		while(!manager.update())
			System.out.println("Loading assets " + manager.getProgress() * 100 +"%");
		//manager.finishLoading();
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
