package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class StaticShader extends Shaders {

	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_transfomationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColour;
	private int location_numberOfRows;
	private int location_offset;
	
	/**
	 * Uses the constructor in the parent class and uses the vertex
	 * and fragment shader files in a generalised implementation.
	 * 
	 * <p> Works similarly to {@link guis.GUIShader#GUIShader()} and 
	 * {@link shaders.TerrainShader#TerrainShader()}</p>
	 * 
	 * @see Shaders#Shaders(String, String)
	 */
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Uses the parent method ({@code Shaders.bindAttribute}) to bind
	 * the... 
	 * <ul>
	 * 		<li>Position attribute to index 0</li>
	 * 		<li>The texture coordinates to index 1</li>
	 * 		<li>The normals of the vertices to index 2</li>
	 * </ul> ...to the VAO.
	 * 
	 * @see Shaders#bindAttribute(int, String)
	 */
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/**
	 * Grabs location of every uniform variable in the Fragment shader GLSL code.
	 * This is done using the {@code getUniformLocation} in the {@code Shaders} class.
	 * 
	 * @see Shaders#getUniformLocation(String)
	 */
	
	@Override
	protected void getAllUniformLocations() {
		location_transfomationMatrix = super.getUniformLocation("transfomationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_skyColour = super.getUniformLocation("skyColour");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
	}
	
	public void loadNumberOfRows(float numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x,y));
	}
	
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r,g,b));
	}
	
	public void loadFakeLightingVariables(boolean useFake) {
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	/**
	 * Loads up the transformation matrix using the {@code loadMatrix} method in the
	 * {@code Shaders} class. This is so that objects can move around and be rendered
	 * at different angles and places and be scaled.
	 * 
	 * @param matrix
	 * 			-The transformation matrix to be loaded in.
	 * @see Shaders#loadMatrix(int, Matrix4f)
	 */
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transfomationMatrix, matrix);
	}
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
}
