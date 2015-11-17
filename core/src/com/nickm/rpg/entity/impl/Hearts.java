package com.nickm.rpg.entity.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.entity.Entity;
import com.nickm.rpg.manager.Assets;

public class Hearts extends Entity {

	public Hearts(Body body) {
		super(body);
		Texture tex = MainGame.assets.get(Assets.crystal, Texture.class);
		TextureRegion[] sprites = TextureRegion.split(tex, 16, 16)[0];
		setAnimation(sprites, 1/8f);
	}
		
}
