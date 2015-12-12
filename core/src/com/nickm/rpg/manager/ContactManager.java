package com.nickm.rpg.manager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.nickm.rpg.entity.impl.Player;
import com.nickm.rpg.state.impl.Play;

public class ContactManager implements ContactListener {

	private int numFootContacts;
	private int numMobSwordContacts;
	private int numMobFootContacts;
	private Array<Body> bodiesToRemove;
	private Array<Body> deadMobs;
	private Player player;
	private Play state;
	public Fixture swordHit;
	public Fixture hitBy;
	public boolean hitByMob = false;
	public boolean touchingObject = false;
	public String doorData = "";

	public ContactManager(Play s, Player p) {
		super();
		state = s;
		player = p;
		bodiesToRemove = new Array<Body>();
		deadMobs = new Array<Body>();
	}

	public void beginContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa == null || fb == null) {
			return;
		}

		// foot hitting ground
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			if (!fb.getUserData().equals("bat"))
				numFootContacts++;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			if (!fa.getUserData().equals("bat"))
				numFootContacts++;
		}
		// player or foot hiting object
		if (fa.getUserData() != null && fa.getUserData().equals("coin")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("coin")) {
			bodiesToRemove.add(fb.getBody());

		}
		if (fa.getUserData() != null && fa.getUserData().equals("heart")) {
			bodiesToRemove.add(fa.getBody());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("heart")) {
			bodiesToRemove.add(fb.getBody());
		}
		if (fa.getUserData() != null && fa.getUserData().equals("spike")) {
			hitByMob = true;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("spike")) {
			hitByMob = true;
		}
		// player contacting mob
		if (fb.getUserData() != null && fa.getUserData().equals("player") && fb.getUserData().equals("bat")) {
			System.out.println("im hit!");
			hitByMob = true;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("player") && fa.getUserData().equals("bat")) {
			System.out.println("im hit!");
			hitByMob = true;
		}
		// foot contacting mob
		if (fa.getUserData() != null && fa.getUserData().equals("bat") && fb.getUserData().equals("foot")) {
			numMobFootContacts++;
			swordHit = fa;
			hitBy = fb;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bat") && fa.getUserData().equals("foot")) {
			numMobFootContacts++;
			swordHit = fb;
			hitBy = fa;
		}
		// sword hitting mob
		if (fa.getUserData() != null && fa.getUserData().equals("bat")) {
			numMobSwordContacts++;
			swordHit = fa;
			hitBy = fb;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bat")) {
			numMobSwordContacts++;
			swordHit = fb;
			hitBy = fa;
		}
		// player contacting door
		if (fb.getUserData() != null && fa.getUserData().equals("player") && fb.getUserData().toString().contains("door")) {
			touchingObject = true;
			doorData = fb.getUserData().toString().replace("door-", "");
			state.displayAction(true, fb.getUserData().toString());
		}
		if (fb.getUserData() != null && fb.getUserData().equals("player") && fa.getUserData().toString().contains("door")) {
			touchingObject = true;
			doorData = fa.getUserData().toString().replace("door-", "");
			state.displayAction(true, fa.getUserData().toString());
		}
	}

	public void endContact(Contact c) {

		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();

		if (fa == null || fb == null) {
			return;
		}

		if (fa.getUserData() != null && fa.getUserData().equals("bat") && fb.getUserData().equals("foot")) {
			if (numMobFootContacts > 0)
				numMobFootContacts--;
		}
		if (fa.getUserData() != null && fb.getUserData().equals("bat") && fa.getUserData().equals("foot")) {
			if (numMobFootContacts > 0)
				numMobFootContacts--;
		}
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			if (numFootContacts > 0 && !fb.getUserData().equals("bat"))
				numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			if (numFootContacts > 0 && !fa.getUserData().equals("bat"))
				numFootContacts--;
		}
		if (fa.getUserData() != null && fa.getUserData().equals("bat")) {
			if (numMobSwordContacts > 0)
				numMobSwordContacts--;
			if (fa.getUserData().equals("player"))
				hitByMob = false;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("bat")) {
			if (numMobSwordContacts > 0)
				numMobSwordContacts--;
			if (fa.getUserData().equals("player"))
				hitByMob = false;
		}
		if (fa.getUserData() != null && fa.getUserData().equals("bat")) {
			hitByMob = false;
		}
		if (fa.getUserData() != null && fb.getUserData().equals("bat")) {
			hitByMob = false;
		}

		if (fa.getUserData() != null && fa.getUserData().toString().contains("door")) {
			touchingObject = false;
			doorData = "";
			state.displayAction(false, fa.getUserData().toString());
		}
		if (fa.getUserData() != null && fb.getUserData().toString().contains("door")) {
			touchingObject = false;
			doorData = "";
			state.displayAction(false, fb.getUserData().toString());
		}
	}

	public void resetMobCounters() {
		// setSwordContacts(0);
		// setFootContacts(0);
	}

	public boolean onGround() {
		return numFootContacts > 0;
	}

	public boolean isMobHit() {
		return numMobSwordContacts > 0;
	}

	public boolean isMobFootHit() {
		return numMobFootContacts > 0;
	}

	public void setSwordContacts(int i) {
		numMobSwordContacts = i;
	}

	public void setFootContacts(int i) {
		numMobFootContacts = i;
	}

	public Array<Body> getBodiesToRemove() {
		return bodiesToRemove;
	}

	public Array<Body> getDeadMobs() {
		return deadMobs;
	}

	public void addToDeadMobs(Body body) {
		deadMobs.add(body);
	}

	public void preSolve(Contact c, Manifold m) {
	}

	public void postSolve(Contact c, ContactImpulse ci) {
	}

}
