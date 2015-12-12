package com.nickm.rpg.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.InputMultiplexer;

public class Input {

	public static InputMultiplexer inputs;

	// android
	public static class touchInfo {

		public float touchX = 0;
		public float touchY = 0;
		public boolean touched = false;
		public int button = 0;
	}
	public static Map<Integer, touchInfo> touches = new HashMap<Integer, touchInfo>();

	// desktop
	public static boolean[] keys;
	public static boolean[] upkeys;
	public static boolean[] pkeys;

	public static final int NUM_KEYS = 6;//total number of keys
	public static final int A = 0;
	public static final int D = 1;
	public static final int W = 2;
	public static final int SPACE = 3;
	public static final int ESCAPE = 4;
	public static final int E = 5;

	static {
		for (int i = 0; i < 5; i++) {
			touches.put(i, new touchInfo());
		}
		keys = new boolean[NUM_KEYS];
		upkeys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}

	public static void update() {
		for (int i = 0; i < NUM_KEYS; i++) {
			pkeys[i] = keys[i];
			if (i == 3 && keys[i])
				setUpKey(i, true);
		}
	}

	public static void setKey(int i, boolean b) {
		keys[i] = b;
	}

	public static void setUpKey(int i, boolean b) {
		upkeys[i] = b;
	}

	public static boolean isDown(int i) {
		return keys[i];
	}

	public static boolean isUp(int i) {
		return upkeys[i];
	}

	public static boolean isPressed(int i) {
		return keys[i] && !pkeys[i];
	}
}
