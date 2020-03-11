package entities;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import EngineTest.Game;
import RenderEngine.DisplayManager;
import models.TexturedModel;
import terrains.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20; //units / sec
	private static final float TURN_SPEED = 160; //degs / sec
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 5;

	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upSpeed = 0;
	
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upSpeed, 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y<terrainHeight) {
			upSpeed=0;
			isInAir=false;
			super.getPosition().y=terrainHeight;
		}

	}
	
	private void jump() {
		if(!isInAir) {
		this.upSpeed = JUMP_POWER;
		isInAir=true;
		}
	}

	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		}
		else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		}
		else {
			this.currentTurnSpeed = 0;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_H)) {
			List<Entity> AllEntities = Game.getAllEntities();
			Human NewHuman = new Human(this.position, this.rotX, this.rotY, this.rotZ, 1);
			System.out.println(NewHuman.getStats());
			AllEntities.add(NewHuman);
			Game.setAllEntities(AllEntities);
		}
	}
	
}
