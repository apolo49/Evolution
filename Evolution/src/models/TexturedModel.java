package models;

import textures.ModelTexture;

/**
 * A {@code rawModel} that has a {@code ModelTexture} 
 * applied to it. Can be used to create an {@code Entity}.
 * 
 * @author Joe
 *
 *@see entities.Entity
 *@see rawModel
 */

public class TexturedModel {
	
	private rawModel RawModel;
	private ModelTexture texture;
	
	/**
	 * <p>Creates the textured model to be used inside an entity.</p>
	 * <p>The constructor sets the {@code RawModel} attribute to the passed in {@code rawModel} parameter
	 * and the {@code texture} attribute to the texture passed in.</p>
	 * 
	 * @param model
	 * 			-The {@code rawModel} for the {@code ModelTexture} to be applied to.
	 * 
	 * @param texture
	 * 			-The {@code ModelTexture} to be applied to the {@code rawModel}.
	 */
	
	public TexturedModel(rawModel model, ModelTexture texture) {
		this.RawModel = model;
		this.texture = texture;
		
	}

	/**
	 * @return The {@code rawModel} used in the {@code TexturedModel}.
	 * @see models.rawModel
	 */
	
	public rawModel getRawModel() {
		return RawModel;
	}

	/**
	 * @return The {@code ModelTexture} used in the {@code TexturedModel}.
	 * @see textures.ModelTexture
	 */
	
	public ModelTexture getTexture() {
		return texture;
	}

}
