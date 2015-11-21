package com.nickm.rpg.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.nickm.rpg.MainGame;
import com.nickm.rpg.components.HUD;
import com.nickm.rpg.entity.EntityConstants;
import com.nickm.rpg.entity.impl.Bats;
import com.nickm.rpg.entity.impl.Coins;
import com.nickm.rpg.entity.impl.Hearts;
import com.nickm.rpg.entity.impl.Player;
import com.nickm.rpg.input.Input;
import com.nickm.rpg.input.InputManager;
import com.nickm.rpg.manager.ContactManager;
import com.nickm.rpg.manager.GameStateManager;
import com.nickm.rpg.state.GameState;

public class Play extends GameState {

	private boolean debug = false;
	private World world;
	private Box2DDebugRenderer b2dr;

	private OrthographicCamera b2dCam;

	private ContactManager contactManager;

	public ContactManager getContactManager() {
		return contactManager;
	}

	private TiledMap tileMap;
	private int tileMapWidth;
	private int tileMapHeight;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;

	private Player player;
	private Array<Coins> coins;
	private Array<Hearts> hearts;
	private Array<Bats> bats;

	private Array<Body> deadMobs;

	private HUD hud;

	private Skin skin;

	/**
	 * Constructs the play gamestate
	 * 
	 * @param gsm
	 */
	public Play(GameStateManager gsm) {
		super(gsm);

		deadMobs = new Array<Body>();

		world = new World(new Vector2(0, -9.81f), true);
		b2dr = new Box2DDebugRenderer();

		createPlayer();
		world.setContactListener(contactManager = new ContactManager(player));
		createTiles();
		createObjects();
		createMobs();

		// setup b2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, MainGame.V_WIDTH / EntityConstants.PPM, MainGame.V_HEIGHT / EntityConstants.PPM);

		cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

		// set hud
		hud = new HUD(player);

		// set health
		hud.health = new int[player.health.length][1];
		for (int i = 0; i < player.health.length; i++) {
			hud.setHealth(player.health[i][0]);
		}

		// add inputs to multiplexer
		Input.inputs.addProcessor(stage);
		Input.inputs.addProcessor(new InputManager());
		// set inputmultiplexer to inputprocessor
		Gdx.input.setInputProcessor(Input.inputs);

		hud.settingsButton.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				if (!stage.getActors().contains(hud.settingsWindow, true))
					openSettings();
			}
		});
		if (MainGame.isMobileRuntime())
			handleListeners();
	}

	/**
	 * Opens and constructs the settings window
	 */
	protected void openSettings() {

		// MainGame.pause();

		// load the skin
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		// setup window
		hud.settingsWindow = new Window("Settings", skin, "dialog");

		// change size based on run type
		if (MainGame.isMobileRuntime()) {
			hud.settingsWindow.setBounds(75, 75, (MainGame.V_WIDTH * 2) - 150, (MainGame.V_HEIGHT * 2) - 150);
		} else {
			hud.settingsWindow.setBounds(75, 75, 1280 - 150, 720 - 150);
		}

		// create select box for resolution
		final SelectBox<String> select = new SelectBox<String>(skin, "default");
		Label size = new Label("Size/Resolution: ", skin);
		// add size options
		select.setItems("1280x720", "1920x1080");
		// set current item to MainGame.resolution
		select.setSelected(MainGame.resolution);
		hud.settingsWindow.add(size);
		hud.settingsWindow.add(select);
		select.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (select.getSelected().equals("1280x720")) {
					// 1280
					Gdx.graphics.setDisplayMode(1280, 720, MainGame.fullscreen);
					MainGame.resolution = select.getSelected();
					MainGame.getDeviceResolution();
				} else if (select.getSelected().equals("1920x1080")) {
					// 1920
					Gdx.graphics.setDisplayMode(1920, 1080, MainGame.fullscreen);
					MainGame.resolution = select.getSelected();
					MainGame.getDeviceResolution();
				}
			}
		});

		// break
		hud.settingsWindow.row();

		// add fullscreen checkbox
		Label fullscreen = new Label("Fullscreen: ", skin);
		final CheckBox box = new CheckBox("", skin, "default");
		// set box to checked/unchecked based on MainGame.fullscreen
		box.setChecked(MainGame.fullscreen);
		hud.settingsWindow.add(fullscreen);
		hud.settingsWindow.add(box);
		box.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (box.isChecked()) {
					// on
					MainGame.fullscreen = true;
					Gdx.graphics.setDisplayMode(MainGame.WINDOW_WIDTH, MainGame.WINDOW_HEIGHT, true);
				} else {
					// off
					MainGame.fullscreen = false;
					Gdx.graphics.setDisplayMode(MainGame.WINDOW_WIDTH, MainGame.WINDOW_HEIGHT, false);
				}
			}
		});

		// break
		hud.settingsWindow.row();

		// exit and save buttons
		TextButton exitGame = new TextButton("Exit Game", skin);
		TextButton close = new TextButton("Save Settings", skin);
		hud.settingsWindow.add(exitGame);
		hud.settingsWindow.add(close);
		exitGame.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				// exit
				Gdx.app.exit();
			}
		});
		close.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				// close settings
				hud.settingsWindow.remove();
			}
		});
		stage.addActor(hud.settingsWindow);
	}

	public void update(float dt) {

		world.step(dt, 1, 1);
		handleInput();

		// removing objects
		Array<Body> bodies = contactManager.getBodiesToRemove();
		for (int i = 0; i < bodies.size; i++) {
			if (world.getBodyCount() > 1) {
				Body b = bodies.get(i);
				if (b.getUserData().toString().contains("Coins")) {
					coins.removeValue((Coins) b.getUserData(), true);
					player.collectCoin();
				} else if (b.getUserData().toString().contains("Hearts")) {
					hearts.removeValue((Hearts) b.getUserData(), true);
					gainHealth(1);
				}
				world.destroyBody(b);
				bodies.removeIndex(i);
			}
		}
		bodies.clear();
		// removing mobs
		for (int i = 0; i < deadMobs.size; i++) {
			if (world.getBodyCount() > 1) {
				Body b = deadMobs.get(i);
				bats.removeValue((Bats) b.getUserData(), true);
				world.destroyBody(b);
				deadMobs.removeIndex(i);
			}
		}
		deadMobs.clear();

		// update player
		player.update(dt, player);

		// update objects
		if (coins != null)
			for (int i = 0; i < coins.size; i++) {
				coins.get(i).update(dt, player);
			}
		if (hearts != null)
			for (int i = 0; i < hearts.size; i++) {
				hearts.get(i).update(dt, player);
			}
		// update mobs
		if (bats != null)
			for (int i = 0; i < bats.size; i++) {
				bats.get(i).update(dt, player);
			}
	}

	public void gainHealth(int amount) {
		for (int i = 0; i < amount; i++) {
			for (int a = player.health.length - 1; a >= 0; a--) {
				if (player.health[a][0] == 0) {
					player.health[a][0] = 1;
					hud.health[a][0] = 1;
					return;
				} else {
					// full
				}
			}
		}
	}

	public void loseHealth(int amount) {
		for (int i = 0; i < amount; i++) {
			for (int a = 0; a < player.health.length; a++) {
				if (player.health[a][0] == 1) {
					player.health[a][0] = 0;
					hud.health[a][0] = 0;
					return;
				} else {
					// dead
				}
			}
		}
	}

	public void handleListeners() {
		hud.attackButton.addListener(new ClickListener() {

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				player.attacking = true;
				if (contactManager.onGround() && !player.stuck) {
					player.startAnimation(player.getFace() == 0 ? 6 : 7);
					if (contactManager.isMobHit()) {
						if (contactManager.hitBy.getUserData().equals("rsword") && player.getFace() == 0) {
							deadMobs.add(contactManager.swordHit.getBody());
							System.out.println("sword hit");
						} else if (contactManager.hitBy.getUserData().equals("lsword") && player.getFace() == 1) {
							deadMobs.add(contactManager.swordHit.getBody());
							System.out.println("sword hit");
						}
					}
				} else {
					if (!player.stuck) {
						player.startAnimation(player.getFace() == 0 ? 8 : 9);
						if (contactManager.isMobFootHit()) {
							if (contactManager.hitBy.getUserData() == "foot") {
								deadMobs.add(contactManager.swordHit.getBody());
							}
						}
					}
				}
				Timer.schedule(new Task() {

					@Override
					public void run() {
						player.attacking = false;
					}

				}, .7f);
			}
		});
		hud.jumpButton.addListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				if (contactManager.onGround() && !player.isBusy()) {
					player.startAnimation(player.getFace() == 0 ? 4 : 5);
					contactManager.setFootContacts(0);
					player.getBody().applyForceToCenter(0, 250, true);
				}
			}
		});
	}

	public void handleInput() {

		Vector2 vel = new Vector2();
		vel = player.getBody().getLinearVelocity();
		vel.x = 0;
		player.getBody().setLinearVelocity(vel);

		// android
		if (MainGame.isMobileRuntime()) {
			if (hud.touchpad.isTouched() && !player.isBusy()) {
				if (hud.touchpad.getKnobPercentX() < 0) { // left
					player.startAnimation(contactManager.onGround() && !player.isBusy() ? 3 : 5);
				} else if (hud.touchpad.getKnobPercentX() > 0) {// right
					player.startAnimation(contactManager.onGround() && !player.isBusy() ? 2 : 4);
				} else {
					if (contactManager.onGround() && !player.isBusy() && player.getBody().getLinearVelocity().isZero()) {
						player.startAnimation(player.getFace() == 0 ? 0 : 1); // idle
																				// when
																				// on
																				// the
																				// ground,
																				// not
																				// attacking,
																				// and
																				// not
																				// moving
					}
				}
				vel.x = hud.touchpad.getKnobPercentX() * 1.55f;
				player.getBody().setLinearVelocity(vel);
			} else {
				if (contactManager.onGround() && !player.isBusy() && player.getBody().getLinearVelocity().isZero()) {
					player.startAnimation(player.getFace() == 0 ? 0 : 1); // idle
																			// when
																			// on
																			// the
																			// ground,
																			// not
																			// attacking,
																			// and
																			// not
																			// moving
				}
			}
			if (contactManager.isMobFootHit() && player.attacking) { // handle
																		// air
																		// attacks
																		// for
																		// android
				if (contactManager.hitBy.getUserData() == "foot") {
					deadMobs.add(contactManager.swordHit.getBody());
				}
			}
			if (contactManager.hitByMob) { // handle being hit
				if (player.attacking) {
					deadMobs.add(contactManager.swordHit.getBody());
				} else {
					player.stuck = true;
					player.getBody().applyForceToCenter(player.getFace() == 0 ? -1000 : 1000, 0, true);
					player.startAnimation(player.getFace() == 0 ? 10 : 11);
					Timer.schedule(new Task() {

						@Override
						public void run() {
							player.stuck = false;
						}

					}, 1);
				}
				loseHealth(1);
				contactManager.hitByMob = false;
			}

		} else {
			// desktop
			if (Input.isPressed(Input.ESCAPE)) {
				if (!stage.getActors().contains(hud.settingsWindow, true))
					openSettings();
				else
					hud.settingsWindow.remove();
			}
			if (Input.isDown(Input.SPACE) && !player.stuck) {
				player.attacking = true;
				if (contactManager.onGround()) {
					player.startAnimation(player.getFace() == 0 ? 6 : 7);
					if (contactManager.isMobHit()) {
						if (contactManager.hitBy.getUserData().equals("rsword") && player.getFace() == 0) {
							deadMobs.add(contactManager.swordHit.getBody());
							System.out.println("sword hit");
						} else if (contactManager.hitBy.getUserData().equals("lsword") && player.getFace() == 1) {
							deadMobs.add(contactManager.swordHit.getBody());
							System.out.println("sword hit");
						}
					}
				} else {
					player.startAnimation(player.getFace() == 0 ? 8 : 9);
					if (contactManager.isMobFootHit()) {
						if (contactManager.hitBy.getUserData() == "foot") {
							deadMobs.add(contactManager.swordHit.getBody());
						}
					}
				}
			} else if (Input.isUp(Input.SPACE) && !player.stuck) {
				player.attacking = false;
				Input.setUpKey(Input.SPACE, false);
			} else if (Input.isDown(Input.A) && !player.stuck) {
				vel.x = -1.5f;
				player.getBody().setLinearVelocity(vel);
				player.startAnimation(contactManager.onGround() && !player.attacking ? 3 : 5);
			} else if (Input.isDown(Input.D) && !player.stuck) {
				vel.x = 1.5f;
				player.getBody().setLinearVelocity(vel);
				player.startAnimation(contactManager.onGround() && !player.attacking ? 2 : 4);
			} else {
				if (contactManager.onGround() && !player.isBusy() && player.getBody().getLinearVelocity().isZero()) {
					player.startAnimation(player.getFace() == 0 ? 0 : 1); // idle
																			// when
																			// on
																			// the
																			// ground,
																			// not
																			// attacking,
																			// and
																			// not
																			// moving
				}
			}
			if (Input.isPressed(Input.W) && !player.stuck) { // jumping
				if (contactManager.onGround() && !player.isBusy()) {
					player.startAnimation(player.getFace() == 0 ? 4 : 5);
					contactManager.setFootContacts(0);
					vel.x = 0;
					player.getBody().applyForceToCenter(0, 250, true);
				}
			}
			if (contactManager.hitByMob) { // handle being hit
				if (player.attacking) {
					deadMobs.add(contactManager.swordHit.getBody());
				} else {
					player.stuck = true;
					player.getBody().applyForceToCenter(player.getFace() == 0 ? -1000 : 1000, 0, true);
					player.startAnimation(player.getFace() == 0 ? 10 : 11);
					Timer.schedule(new Task() {

						@Override
						public void run() {
							player.stuck = false;
						}

					}, 1);
				}
				loseHealth(1);
				contactManager.hitByMob = false;
			}
		}

	}

	public void render() {
		// clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// set camera to follow player
		cam.setPosition(player.getPosition().x * EntityConstants.PPM + MainGame.V_WIDTH / 30, player.getPosition().y * EntityConstants.PPM + MainGame.V_HEIGHT / 20);
		cam.update();
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		// draw objects
		if (coins != null)
			for (int i = 0; i < coins.size; i++) {
				sb.setProjectionMatrix(cam.combined);
				coins.get(i).render(sb);
			}
		if (hearts != null)
			for (int i = 0; i < hearts.size; i++) {
				sb.setProjectionMatrix(cam.combined);
				hearts.get(i).render(sb);
			}
		// draw mobs
		if (bats != null)
			for (int i = 0; i < bats.size; i++) {
				sb.setProjectionMatrix(cam.combined);
				bats.get(i).render(sb);
			}
		// draw player
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		// draw hud
		sb.setProjectionMatrix(hudCam.combined);
		stage.getBatch().setProjectionMatrix(hudCam.combined);
		stage.getViewport().setCamera(hudCam);
		hud.render(sb, stage);
		// draw box2d world
		if (debug) {
			b2dr.render(world, b2dCam.combined);
		}
	}

	public void dispose() {

	}

	public void createPlayer() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		// create player
		bdef.position.set(50 / EntityConstants.PPM, 100 / EntityConstants.PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		shape.setAsBox(10 / EntityConstants.PPM, 20 / EntityConstants.PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = EntityConstants.BIT_PLAYER;
		fdef.filter.maskBits = EntityConstants.BIT_GROUND | EntityConstants.BIT_WALL | EntityConstants.BIT_OBJECT | EntityConstants.BIT_MOB;
		body.createFixture(fdef).setUserData("player");

		// create forward sword sensor
		shape.setAsBox(12 / EntityConstants.PPM, 16 / EntityConstants.PPM, new Vector2(20 / EntityConstants.PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = EntityConstants.BIT_PLAYER;
		fdef.filter.maskBits = EntityConstants.BIT_MOB;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("rsword");

		// create backward sword sensor
		shape.setAsBox(12 / EntityConstants.PPM, 16 / EntityConstants.PPM, new Vector2(-20 / EntityConstants.PPM, 0), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = EntityConstants.BIT_PLAYER;
		fdef.filter.maskBits = EntityConstants.BIT_MOB;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("lsword");

		// create foot sensor
		shape.setAsBox(8 / EntityConstants.PPM, 2 / EntityConstants.PPM, new Vector2(0, -20 / EntityConstants.PPM), 0);
		fdef.shape = shape;
		fdef.filter.categoryBits = EntityConstants.BIT_PLAYER;
		fdef.filter.maskBits = EntityConstants.BIT_GROUND | EntityConstants.BIT_WALL | EntityConstants.BIT_MOB;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		shape.dispose();

		player = new Player(body);

		body.setUserData(player);
	}

	public void createTiles() {
		// load tile map
		tileMap = new TmxMapLoader().load("maps/test2.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileMapWidth = (Integer) tileMap.getProperties().get("width");
		tileMapHeight = (Integer) tileMap.getProperties().get("height");
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");

		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		createLayer(layer, EntityConstants.BIT_GROUND, "ground");
	}

	public void createLayer(TiledMapTileLayer layer, short bits, Object userData) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		// go through cells in layer
		if (layer != null)
			for (int row = 0; row < layer.getHeight(); row++) {
				for (int col = 0; col < layer.getWidth(); col++) {
					// get cell
					Cell cell = layer.getCell(col, row);
					// check if cell exists
					if (cell == null)
						continue;
					if (cell.getTile() == null)
						continue;
					// create body and fixture for tile
					bdef.type = BodyType.StaticBody;
					bdef.position.set((col + 0.5f) * tileSize / EntityConstants.PPM, (row + 0.5f) * tileSize / EntityConstants.PPM);

					ChainShape cs = new ChainShape();
					Vector2[] v = new Vector2[5];
					v[0] = new Vector2(-tileSize / 2 / EntityConstants.PPM, -tileSize / 2 / EntityConstants.PPM);
					v[1] = new Vector2(-tileSize / 2 / EntityConstants.PPM, tileSize / 2 / EntityConstants.PPM);
					v[2] = new Vector2(tileSize / 2 / EntityConstants.PPM, tileSize / 2 / EntityConstants.PPM);
					v[3] = new Vector2(tileSize / 2 / EntityConstants.PPM, -tileSize / 2 / EntityConstants.PPM);
					v[4] = new Vector2(-tileSize / 2 / EntityConstants.PPM, -tileSize / 2 / EntityConstants.PPM);
					cs.createChain(v);
					fdef.friction = 0;
					fdef.shape = cs;
					fdef.filter.categoryBits = bits;
					fdef.filter.maskBits = EntityConstants.BIT_PLAYER;
					fdef.isSensor = false;
					world.createBody(bdef).createFixture(fdef).setUserData(userData);
					cs.dispose();
				}
			}
	}

	public void createObjects() {
		coins = new Array<Coins>();

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		MapLayer coinsLayer = tileMap.getLayers().get("coins");
		if (coinsLayer == null)
			return;
		for (MapObject mo : coinsLayer.getObjects()) {
			bdef.type = BodyType.StaticBody;

			float x = (Float) mo.getProperties().get("x") / EntityConstants.PPM;
			float y = (Float) mo.getProperties().get("y") / EntityConstants.PPM;

			bdef.position.set(x, y);

			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / EntityConstants.PPM);
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = EntityConstants.BIT_OBJECT;
			fdef.filter.maskBits = EntityConstants.BIT_PLAYER;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("coin");
			Coins o = new Coins(body);
			coins.add(o);
			body.setUserData(o);
			cshape.dispose();
		}

		hearts = new Array<Hearts>();
		MapLayer hearstLayer = tileMap.getLayers().get("hearts");
		if (hearstLayer == null)
			return;
		for (MapObject mo : hearstLayer.getObjects()) {
			bdef.type = BodyType.StaticBody;

			float x = (Float) mo.getProperties().get("x") / EntityConstants.PPM;
			float y = (Float) mo.getProperties().get("y") / EntityConstants.PPM;

			bdef.position.set(x, y);

			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / EntityConstants.PPM);
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = EntityConstants.BIT_OBJECT;
			fdef.filter.maskBits = EntityConstants.BIT_PLAYER;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("heart");
			Hearts o = new Hearts(body);
			hearts.add(o);
			body.setUserData(o);
			cshape.dispose();
		}
	}

	public void createMobs() {
		bats = new Array<Bats>();

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();

		MapLayer layer = tileMap.getLayers().get("bats");
		if (layer == null)
			return;
		for (MapObject mo : layer.getObjects()) {
			bdef.type = BodyType.StaticBody;

			float x = (Float) mo.getProperties().get("x") / EntityConstants.PPM;
			float y = (Float) mo.getProperties().get("y") / EntityConstants.PPM;

			bdef.position.set(x, y);

			PolygonShape shape = new PolygonShape();
			shape.setAsBox(12 / EntityConstants.PPM, 12 / EntityConstants.PPM);
			fdef.shape = shape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = EntityConstants.BIT_MOB;
			fdef.filter.maskBits = EntityConstants.BIT_PLAYER;

			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("bat");
			Bats m = new Bats(body);
			bats.add(m);
			body.setUserData(m);
			shape.dispose();
		}
	}

}
