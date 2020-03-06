package RenderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

/**
 * <p>
 * Imagine a scenario wherein you are trying to render 200 identical objects
 * with identical textures but with different locations and rotations. You would
 * generally have to instantiate 200 different cubes then render and process 200 cubes every frame 
 * which would be horribly inefficient and resource intensive which is undesirable and pointless.
 * A solution to this problem would be to bind and texture 1 cube and render it across the screen 200 times.</p>
 * 
 * <p> that is what this class achieves a solution to this problem as well as properly handling culling,
 * shader management, entities and terrains.</p>
 * 
 * @author Joe
 *
 */

public class MasterRenderer {
	
	private static final float FOV = 70; //size of field of view
	private static final float NEAR_PLANE = 0.1f; //size of near plane
	private static final float FAR_PLANE = 1000; //size of far plane rhombus
	private static final float R = 0.5f;
	private static final float G = 0.5f;
	private static final float B = 0.5f;
	
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	/**
	 * Constructor for the renderer.
	 * This enables culling of the rear face of objects by calling the
	 * {@code enableCulling()} method and creates the projection matrix and initialises
	 * the entity renderer and terrain renderer using both the static shader and projection
	 * matrix.
	 * 
	 * @see #enableCulling()
	 * @see #createProjectionMatrix()
	 * @see EntityRenderer#EntityRenderer(StaticShader, Matrix4f)
	 * @see TerrainRenderer#TerrainRenderer(TerrainShader, Matrix4f)
	 */
	
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
	}
	
	/**
	 * Enables culling of the back face within the renderer.
	 * This means unnecessary faces are not rendered and are not
	 * taking up memory that is not needed as the player cannot see it.
	 * <p>
	 * <pre>
	 * {@code
	 * GL11.glEnable(GL11.GL_CULL_FACE);
	 * GL11.glCullFace(GL11.GL_BACK);
	 * }
	 * </pre>
	 * </p>
	 */
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**
	 * Disables culling across all 
	 * entities rendered by this renderer. Uses 
	 * {@code GL11.glDisable(GL11.GL_CULL_FACE)}
	 */
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * This method processes all shaders and 
	 * performs the rendering process
	 * on all entities and terrains.
	 * In the end it stops the shaders (terrain and static) and
	 * clears the terrain list and entity map. 
	 * 
	 * @param sun
	 * 			-The global light to be rendered.
	 * @param camera
	 * 			-The player camera.
	 * @see shaders.Shaders
	 * @see StaticShader
	 * @see TerrainShader
	 * @see TerrainRenderer
	 * @see EntityRenderer
	 */

	public void render(Light sun, Camera camera) {
		prepare();
		
		shader.start();
		shader.loadSkyColour(R, G, B);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColour(R, G, B);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		entities.clear();
	}
	
	/**
	 * Adds a terrain object to the list of terrains to be processed.
	 * 
	 * @param terrain
	 * 			-Terrain to be added to the list of terrains.
	 */
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**
	 * <p>Takes the inputted entity, checks whether the model is inside the 
	 * Entity map in the renderer. If it is, add another instance of the Entity
	 * to the list inside of the map.</p>
	 * 
	 * <p>If the model of the entity is not inside of the map then the model
	 * is added to the map and a list is created with the entity inside and both are
	 * added to the map to be processed by the renderer.</p>
	 * 
	 * @param entity
	 * 			-Entity to be added to be processed
	 */
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}
	
	/**
	 * Calls both the {@code shader} ({@code StaticShader.cleanUp()}) and {@code terrainShader}
	 * ({@code TerrainShader.cleanUp()}) clean up method so that they are no 
	 * longer being used and so that it is safe to close the program (Both methods are
	 * truly {@code Shaders.cleanUp}).
	 * 
	 * @see shaders.Shaders#cleanUp()
	 */
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	/**
	 * This method must be called each frame, before any rendering is carried
	 * out. It initially checks which triangles are ontop of which using {@code GL11.GL_DEPTH_TEST}
	 * clears the screen of everything that was rendered last frame 
	 * (using the {@code glClear()} method). The {@code glClearColor()} method determines
	 * the colour that it uses to clear the screen. In this example it makes the
	 * entire screen red at the start of each frame.
	 */
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(R, G, B, 1);
	}
	
	/**
	 * The projection matrix is the amount of visibility a person can have
	 * represented by the area of a frustum (the portion of a cone or pyramid which
	 * remains after its upper part has been cut off by a plane parallel to its base,
	 * or which is intercepted between two such planes). The aspect ratio, y-scale 
	 * and x-scale are all added to a 4x4 matrix of floats. The matrix in question comes
	 * to appear like:
	 * <pre>
	 * 
	 * 		[x                                                ][                                                 ][                                           ][                                               ]
	 * 		[                                                 ][y                                                ][                                           ][                                               ]
	 * 		[                                                 ][                                                 ][-((FAR_PLANE + NEAR_PLANE) / frustumLength)][-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength)]
	 * 		[                                                 ][                                                 ][-1                                         ][0                                              ]
	 * 
	 * </pre>
	 */
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projectionMatrix.m33 = 0;
	}
	
}
