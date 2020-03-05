package textures;

/**
 * A collection of textures to be set to the terrain
 * 
 * Each texture represents a colour on a terrain map.
 * 
 * @see TerrainTexture
 * 
 * @author Joe
 *
 */

public class TerrainTexturePack {
	
	private TerrainTexture BackgroundTexture;
	private TerrainTexture RTexture;
	private TerrainTexture GTexture;
	private TerrainTexture BTexture;
	
	/**
	 * Construct the texture pack, consisting of 4 textures to be used
	 * on the terrain.
	 * 
	 * @param backgroundTexture
	 * 			-The background texture to be used, represented by the colour black
	 * 			on the terrain map.
	 * 
	 * @param rTexture
	 * 			-The red texture to be used, red on the terrain map.
	 * 
	 * @param gTexture
	 * 			-The green texture to be used, green on the terrain map.
	 * 
	 * @param bTexture
	 * 			-The blue texture to be used, blue on the terrain map.
	 */
	
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture) {
		BackgroundTexture = backgroundTexture;
		RTexture = rTexture;
		GTexture = gTexture;
		BTexture = bTexture;
	}

	/**
	 * @return the background texture, black on the terrain map.
	 */
	
	public TerrainTexture getBackgroundTexture() {
		return BackgroundTexture;
	}
	
	/**
	 * @return the red texture, red on the terrain map.
	 */
	
	public TerrainTexture getRTexture() {
		return RTexture;
	}

	/**
	 * @return the green texture, green on the terrain map.
	 */
	
	public TerrainTexture getGTexture() {
		return GTexture;
	}

	/**
	 * @return the blue texture, blue on the terrain map. 
	 */
	
	public TerrainTexture getBTexture() {
		return BTexture;
	}
	
	

}
