package com.nickm.rpg.entity.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.entity.Entity;
import com.nickm.rpg.manager.AssetsManager;

public class Bats extends Entity {

	public Bats(Body body) {
		super(body);
		Texture tex = MainGame.assets.get(AssetsManager.bat, Texture.class);
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[1];
		setAnimation(sprites, 1 / 6f);
	}
}
