package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * The camera is not an actual camera and it does not actually move, 
 * the centre of the screen is always 0,0 to the computer and OpenGL.
 * 
 * <p>
 * The camera is always joined to the player and can be rotated using the right
 * mouse button and dragging the mouse (a lock button might be added in the future to
 * disable this feature. (as of version 0.1.5A))
 * </p>
 * 
 * @author Joe
 *
 */

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(100,30,-50);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private Player player;
	
	/**
	 * Constructs the camera and sets the {@code Player} for the camera attach onto.
	 * 
	 * @param player
	 * 		-The {@code Player} {@code Entity} for the {@code Camera} to latch onto.
	 */
	
	public Camera(Player player) {
		this.player = player;
	}
	
	/**
	 * 
	 */
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calcAngleAroundPlayer();
		float horizontalDistance = calculateHorizontal();
		float verticalDistance = calculateVertical();
		calculateCameraPos(horizontalDistance, verticalDistance);
		this.yaw = 180-(player.getRotY()+angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPos(float horizDis, float vertDis) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDis * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDis * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vertDis+ 6;
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.01f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY()*0.1f;
			pitch -=pitchChange;
		}
	}
	
	private void calcAngleAroundPlayer() {
		if(Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX()*0.3f;
			angleAroundPlayer -=angleChange;
		}
	}
	
	private float calculateHorizontal() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVertical() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

}
