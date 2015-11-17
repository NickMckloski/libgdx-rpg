package com.nickm.rpg.entity.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.entity.Entity;
import com.nickm.rpg.manager.Assets;

public class Player extends Entity {
	
	public int currentAnim = 0;
	public int lastAnim = 0;
	//XXX: redo how animations are store and retrieved
	//Animationlist
	// 0 for idle, 1 back idle, 2 for walk, 3 back walk, 4 for jump, 5 back jump, 6 attack for, 7 attack back, 8 jump atk for, 9 jump atk back
	// 10 recoil, 11 recoil back
	private int face = 0; 
	//0 = forward, 1 = backward
	
	Texture tex = MainGame.assets.get(Assets.playerSheet2, Texture.class);
	
	public int coins = 0;
	
	public boolean attacking = false;
	public boolean stuck = false;
	public int hearts = 5;
	public int[][] health;;
	
	public Player(Body body) {
		super(body);
		health = new int[hearts][1];
		for(int i = 0; i < hearts; i++) {
			health[i][0] = 1;
		}
		TextureRegion[] sprites = TextureRegion.split(tex, 64, 64)[currentAnim];
		setAnimation(sprites, 1/8f);
	}
	
	public boolean isBusy() {
		if (attacking || stuck)
			return true;
		return false;
	}
	
	public void startAnimation(int anim) {
		if(currentAnim == anim)return;
		lastAnim = currentAnim;
		currentAnim = anim;
		setFace();
		TextureRegion[] sprites = TextureRegion.split(tex, 64, 64)[anim];
		setAnimation(sprites, anim == 8 || anim == 9 ? 1/8f : 1/8f);
	}

	private void setFace() {
		if(currentAnim == 0 || currentAnim == 2 || currentAnim == 4 || currentAnim == 6) {
			face = 0;
		} else if(currentAnim == 1 || currentAnim == 3 || currentAnim == 5 || currentAnim == 7) {
			face = 1;
		}
	}

	public int getFace() { return face; }

	public void collectCoin() {
		coins++;
	}
}
