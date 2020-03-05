package textures;

/**
 * <p>
 * This class is the constructor and object for all
 * textures that can be used and applied to 3D models
 * and objects.
 * </p>
 * 
 * <p>
 * It stores properties such as shine dampening factor
 * and whether the object is a light or has reflectivity.
 * </p>
 * @author Joe
 *
 */

public class ModelTexture {

	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	/**
	 * @return the number of rows in the texture
	 */
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * Set the number of rows in a compound texture and texture grid.
	 * 
	 * @param numberOfRows
	 */
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * @return {@code boolean} {@code true} if the texture is a light and uses false lighting.
	 */
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * Sets the {@code useFakeLighting} boolean to the passed through boolean.
	 * 
	 * @param useFakeLighting
	 * 			-Set to true if the object is to use false lighting.
	 */
	
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	/**
	 * @return a {@code boolean} representing whether the texture should be transparent.
	 */

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * 
	 * Set whether the object should be transparent or not using the passed through
	 * {@code boolean}.
	 * 
	 * @param hasTransparency
	 */
	
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * Sets the Texture ID to the ID passed into the constructor.
	 * 
	 * @param id
	 * 			-Texture ID
	 */
	
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	/**
	 * @return The texture ID given to the texture.
	 */
	
	public int getID() {
		return this.textureID;
	}

	/**
	 * @return The factor which the shine is dampened on the object 
	 * as a floating point number.
	 */
	
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * Set the value for the shine to be dampened on the object
	 * (make it less/more shiny). The larger the number the more
	 * the shine is dampened.
	 * 
	 * @param shineDamper
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return the factor of reflectivity on the object and texture from any lights.
	 */
	
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets the reflectivity factor of the object to the passed through value.
	 * 
	 * @param reflectivity
	 */
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
	
}
