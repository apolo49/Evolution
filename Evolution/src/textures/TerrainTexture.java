package textures;

/**
 * A single texture that is used on the terrain,
 * not to be confused with the terrain texture pack
 * which is a collection of all terrain textures to be used
 * on the terrain
 * 
 * @see TerrainTexturePack
 * 
 * @see shaders.TerrainShader
 * 
 * @see terrains.Terrain
 * 
 * @author Joe
 *
 */
public class TerrainTexture {
	private int textureID;
	
	/**
	 * Sets the texture ID attribute to the passed through texture ID.
	 * 
	 * @param textureID
	 * 			-ID of the texture to be used as the terrain texture.
	 */
	
	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}

	/**
	 * @return the ID of the texture.
	 */
	
	public int getTextureID() {
		return textureID;
	}
	
}
