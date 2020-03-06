package entities;

import org.lwjgl.util.vector.Vector3f;
import models.TexturedModel;

/**
 * A textured model with the ability to move, position and scale itself,
 * as well as be rendered effectively on the screen, have multiple textures
 * and iterate through the textures to be rendered on the model as well as 
 * change model if desired.
 * 
 * <p>Can be extended if wanted through external classes to be given an AI.</p>
 * 
 * @author Joe
 * 
 * @see TexturedModel
 * @see Player
 * @see Camera
 * @see Light
 *
 */

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	
	private int textureIndex = 0;
	
	/**
	 * Constructor for an entity with only one frame of texture.
	 * 
	 * @param model
	 * 		-The textured model for the entity.
	 * @param position
	 * 		-The position of the entity.
	 * @param rotX
	 * 		-The rotation on the x-axis of the entity.
	 * @param rotY
	 * 		-The rotation on the y-axis of the entity.
	 * @param rotZ
	 * 		-The rotation on the z-axis of the entity.
	 * @param scale
	 * 		-The scale of the model.
	 */
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	/**
	 * Constructor of an entity that has a texture map and multiple frames of textures to be used.
	 * 
	 * @param model
	 * 		-The textured model for the entity.
	 * @param textureIndex
	 * 		-The index of the texture to be used on the texture map
	 * @param position
	 * 		-The position of the entity.
	 * @param rotX
	 * 		-The rotation on the x-axis of the entity.
	 * @param rotY
	 * 		-The rotation on the y-axis of the entity.
	 * @param rotZ
	 * 		-The rotation on the z-axis of the entity.
	 * @param scale
	 * 		-The scale of the model.
	 */
	
	public Entity(TexturedModel model,int textureIndex, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.textureIndex = textureIndex;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return(float)column/model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return(float)row/model.getTexture().getNumberOfRows();
	}
	
	/**
	 * Method to move the position of the entity, to move it backwards enter a negative float.
	 * 
	 * @param dx
	 * 		-The change of position of the entity on the x-axis
	 * @param dy
	 * 		-The change of position of the entity on the y-axis
	 * @param dz
	 * 		-The change of position of the entity on the z-axis
	 */
	
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}
	
	/**
	 * Method to rotate the position of the entity, to rotate it anti-clockwise on an axis
	 * enter a negative float.
	 * 
	 * @param dx
	 * 		-The change of rotation of the entity on the x-axis
	 * @param dy
	 * 		-The change of rotation of the entity on the y-axis
	 * @param dz
	 * 		-The change of rotation of the entity on the z-axis
	 */
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
	}

	/**
	 * @return The textured model of the entity
	 */
	
	public TexturedModel getModel() {
		return model;
	}
	
	/**
	 * @param model - changes the textured model to the input model.
	 */

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	/**
	 * @return The position of the entity as a 3D float vector.
	 */
	
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Instantly set the position of the entity using a 3D float vector
	 * of coordinates on the menu.
	 * 
	 * @param position
	 */
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/**
	 * @return The rotation of the Entity on the x-axis.
	 */

	public float getRotX() {
		return rotX;
	}
	
	/**
	 * Sets the rotation of the entity on the x-axis.
	 * 
	 * @param rotX
	 */

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	/**
	 * @return The rotation of the Entity on the y-axis.
	 */
	
	public float getRotY() {
		return rotY;
	}

	/**
	 * Sets the rotation of the entity on the y-axis.
	 * 
	 * @param rotX
	 */
	
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	/**
	 * @return The rotation of the Entity on the z-axis.
	 */
	
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * Sets the rotation of the entity on the y-axis.
	 * 
	 * @param rotX
	 */
	
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	/**
	 * @return The scale factor on the Entity.
	 */
	
	
	public float getScale() {
		return scale;
	}

	/**
	 * Change the scale factor acting on the entity.
	 * 
	 * @param scale
	 */
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	
}
