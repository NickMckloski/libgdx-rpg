package com.nickm.rpg.manager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.nickm.rpg.entity.impl.Player;

public class AnimationManager {

	private TextureRegion[] frames;
	private float time;
	private float delay;
	private int currentFrame;
	private int timesPlayed;
	
	public AnimationManager() {}
	
	public AnimationManager(TextureRegion[] frames) {
		this(frames, 1/12f);
	}
	
	public AnimationManager(TextureRegion[] frames, float delay) {
		setFrames(frames, delay);
	}
	
	public void setDelay(float f) { delay = f; }
	public void setCurrentFrame(int i) { if(i < frames.length) currentFrame = i; }
	public void setFrames(TextureRegion[] frames) {
		setFrames(frames, 1 / 12f);
	}
	
	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}

	public void update(float dt, Player player) {
		if(delay <= 0)return;
		time += dt;
		while(time >= delay) {
			step(player);
		}
	}
	
	private void step(Player player) {
		time -= delay;
		currentFrame++;
		if(currentFrame == frames.length) {
			currentFrame = 0;
			timesPlayed++;
		}
	}
	
	public TextureRegion getFrame() { return frames[currentFrame]; }
	public int getTimesPlayed() { return timesPlayed; }
	
}
