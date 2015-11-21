package com.nickm.rpg.manager;

import java.util.Stack;

import com.nickm.rpg.MainGame;
import com.nickm.rpg.state.GameState;
import com.nickm.rpg.state.impl.Loading;
import com.nickm.rpg.state.impl.Play;
import com.nickm.rpg.state.impl.StartMenu;

public class GameStateManager {

	private MainGame game;

	private Stack<GameState> gameStates;

	public static final int PLAY = 1;
	public static final int STARTMENU = 2;
	public static final int LOADING = 3;

	public GameStateManager(MainGame game) {
		this.game = game;
		gameStates = new Stack<GameState>();
	}

	public MainGame game() {
		return game;
	};

	public void update(float dt) {
		gameStates.peek().update(dt);
	}

	public void render() {
		gameStates.peek().render();
	}

	private GameState getState(int state) {
		if (state == PLAY)
			return new Play(this);
		if (state == STARTMENU)
			return new StartMenu(this);
		if (state == LOADING)
			return new Loading(this);
		return null;
	}

	public void setState(int state) {
		popState();
		pushState(state);
	}

	public void pushState(int state) {
		gameStates.push(getState(state));
	}

	public void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}

}
