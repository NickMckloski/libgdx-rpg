package com.nickm.rpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nickm.rpg.entity.impl.Player;
import com.nickm.rpg.manager.AnimationManager;

public class Entity {

	protected Body body;
	protected AnimationManager animationManager;
	protected float width;
	protected float height;

	public Entity(Body body) {
		this.body = body;
		animationManager = new AnimationManager();
	}

	public void setAnimation(TextureRegion[] reg, float delay) {
		animationManager.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}

	public void update(float dt, Player player) {
		animationManager.update(dt, player);
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.draw(animationManager.getFrame(), body.getPosition().x * EntityConstants.PPM - width / 2, body.getPosition().y * EntityConstants.PPM - height / 2.5f);
		sb.end();
	}

	public Body getBody() {
		return body;
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

}
