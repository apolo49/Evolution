package guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import RenderEngine.Loader;
import models.rawModel;
import toolbox.Maths;

/**
 * This is the renderer for GUIs and other 2D models on the display.
 * <p>The 2D attributes on the display are rendered on a single screen-sized
 * quad and are applied using the GUIShader in this class.</p> 
 * 
 * @author Joe
 *
 * @see RenderEngine.MasterRenderer
 * @see RenderEngine.EntityRenderer
 * @see RenderEngine.TerrainRenderer
 * 
 * 
 */

public class GUIRenderer {

	private final rawModel quad;
	private GUIShader shader;
	
	/**
	 * <p>
	 * Creates a renderer for any and all GUIs as all are 
	 * represented as quadrilaterals on the plane.
	 * </p>
	 * 
	 * <p> 
	 * As all GUIs are represented as quadrilaterals on
	 * the plane all are represented by the positions [-1,1,-1,-1,1,1,1,-1].
	 * This is a quad two triangles making a single quad with vectors between adjacent pairs.
	 * </p>
	 * 
	 * <p>
	 * The constructor loads the quad into the loader through the {@code loadtoVAO(float[] positions)} method.
	 * Then creates a new {@code GUIShader()}.
	 * </p>
	 * 
	 * @param loader
	 * 		-The current loader in use for the current OpenGL context.
	 * @see Loader
	 * @see GUIShader
	 * 
	 */
	
	public GUIRenderer(Loader loader) {	
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadtoVAO(positions);
		shader = new GUIShader();
	}
	
	/**
	 * 
	 * This is called every frame and allows the {@code GUIShader} to update
	 * and adapt to changes on the screen of the GUI and all other entities.
	 * 
	 * <p>A quick run-through of the method shows:
	 * <ul>
	 * 
	 * <li>First, the quad is bound to allow all GUIs to be rendered onto the same quad ({@code glBindVertexArray}).</li>
	 * 
	 * <li>Secondly, Enabling attribute 0 of the VAO as that is where the position is stored in the Attribute Array 
	 * ({@code glEnableVertexAttribArray}).</li>
	 * 
	 * <li>Enables the blend function</li>
	 * 
	 * <li>Enables the blending of the alpha function with the background</li>
	 * 
	 * <li>Disables the depth test ({@code glDisable(GL_DEPTH_TEST)})
	 * 
	 * <li>Then comes the actual rendering which includes:
	 * 		<ul>
	 * 			<li>Looping through the entire list. So each and every GUI can be rendered.</li>
	 * 			
	 * 			<li>Texture Unit 0 is activated through OpenGL ({@code glActiveTexture(GL_TEXTURE0)}).</li>
	 * 			
	 * 			<li>Binds the texture to be used to unit 0 with type 2D ({@code GL_TEXTURE_2D}) and the 
	 * 				texture ID ({@code GUITexture.getTexture()}). This is done using the method {@code glBindTexture}.</li>
	 * 
	 * 			<li>Creates a transformation matrix for each GUI in relation to position and size 
	 * 				{@code Maths.createTransformationMatrix}.</li> 
	 * 
	 * 			<li>Loads the transformation matrix into the {@code GUIShader}.</li>
	 * 
	 * 			<li>{@code glDrawArrays} is called so the entire rendered quad can be drawn to the screen using the
	 * 				{@code GL_TRIANGLE_STRIP} method and the vertex count ({@code rawModel.getVertexCount}).</li>
	 * 		</ul>
	 * <li>Penultimately, we enable the depth test to allow GUIs to be rendered over each-other.</li>
	 * 
	 * <li>Finally, we disable attribute 0 and bind the array then halt the shader 
	 * ({@code glDisableVertexAttribArray}, {@code glBindVertexArray}, {@code GUIShader.stop()}).</li>
	 * 
	 * </ul>
	 * 
	 * @param guis
	 * 			-All {@code GUITexture}s to be rendered on the screen.
	 * 
	 * @see GUIShader
	 * @see RenderEngine.MasterRenderer#render
	 * @see RenderEngine.EntityRenderer#render
	 */
	
	public void render(List<GUITexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GUITexture gui: guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getSize());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	/**
	 * Cleans up the shader by calling the shader cleanup method: {@code GUIShader.cleanUp}
	 * 
	 * @see GUIShader
	 */
	public void cleanUP() {
		shader.cleanUp();
	}
	
}
