package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light in the game. This could hold colour 
 * or be white or black light and be at any position.
 * 
 * @author Joe
 *
 */

public class Light {
	
	/**
	 * This is the light's position in the world.
	 */
	
	private Vector3f position;
	
	/**
	 * This is the light's RGBA value/Intensity.
	 */
	
	private Vector3f colour;
	
	/**
	 * The constructor is used to set the position and colour/intensity
	 * of the light. 
	 * 
	 * @param position
	 * 		- the light's position in the world.
	 * @param colour
	 * 		- the light's RGBA value/Intensity.
	 */
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
	}
	
	/**
	 * @return the current position of the light.
	 */
	
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Set the new position of the light.
	 * @param position - the position you wish the light to be in.
	 */
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/**
	 * @return the colour and intensity of the light
	 */
	
	public Vector3f getColour() {
		return colour;
	}
	
	/**
	 * Set the colour and intensity of the light.
	 * @param colour
	 * 		-The colour and intensity of the light.
	 */
	
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	
	
}
