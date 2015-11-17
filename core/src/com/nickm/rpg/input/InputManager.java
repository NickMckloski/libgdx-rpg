package com.nickm.rpg.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
	
	//android
	@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
        	Input.touches.get(pointer).touchX = screenX;
        	Input.touches.get(pointer).touchY = screenY;
        	Input.touches.get(pointer).touched = true;
        	Input.touches.get(pointer).button = button;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
        	Input.touches.get(pointer).touchX = 0;
        	Input.touches.get(pointer).touchY = 0;
        	Input.touches.get(pointer).touched = false;
        	Input.touches.get(pointer).button = button;
        }
        return true;
    }
	
	//desktop
	public boolean keyDown(int k) {
		if(k == Keys.W) {
			Input.setKey(Input.W, true);
		}
		if(k == Keys.D) {
			Input.setKey(Input.D, true);
		}
		if(k == Keys.A) {
			Input.setKey(Input.A, true);
		}
		if(k == Keys.SPACE) {
			Input.setKey(Input.SPACE, true);
		}
		if(k == Keys.ESCAPE) {
			Input.setKey(Input.ESCAPE, true);
		}
		return true;
	}
	
	public boolean keyUp(int k) {
		if(k == Keys.W) {
			Input.setKey(Input.W, false);
		}
		if(k == Keys.D) {
			Input.setKey(Input.D, false);
		}
		if(k == Keys.A) {
			Input.setKey(Input.A, false);
		}
		if(k == Keys.SPACE) {
			Input.setKey(Input.SPACE, false);
		}
		if(k == Keys.ESCAPE) {
			Input.setKey(Input.ESCAPE, false);
		}
		return true;
	}
	
}
