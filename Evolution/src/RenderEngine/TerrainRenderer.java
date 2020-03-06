package RenderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import models.rawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.TerrainTexturePack;
import toolbox.Maths;

/**
 * Renders terrain on the world in respect to the player so that
 * entities can interact with it.
 * 
 * @author Joe
 * 
 * @see EntityRenderer
 * @see MasterRenderer
 * @see TerrainShader
 *
 */

public class TerrainRenderer {
	
	/**
	 * Shader to be used across the whole terrain renderer in a single OpenGL Context.
	 */
	
	private TerrainShader shader;
	
	/**
	 * Constructs the renderer so that the projection matrix and shader can 
	 * be started and textures can be connected to the terrain.
	 * 
	 * @param shader
	 * 			-The terrain shader to be used when rendering the terrain, should be used for the same OpenGL Context.
	 * @param projectionMatrix
	 * 			-Projection matrix to be used for the player.
	 */
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	/**
	 * Loops through list of all terrains and prepares, loads the model matrix 
	 * and draws the element on the screen using {@code GL11.GL_TRIANGLES}, the vertex count
	 * (128), {@code GL11.GL_UNSIGNED_INT} and indices buffer 0. Afterwards the textured model is unbound.
	 * 
	 * @param terrains
	 * 			-List of all terrains to be rendered
	 */
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}
	
	/**
	 * This renders the terrain and applies the correct attributes and factors to the model.
	 * <p>The attributes and factors includes:
	 * <ul>
	 * 		<li>Binding the vertex array (VAO)</li>
	 * 		<li>Enabling all 3 attributes in the attribute array</li>
	 * 		<li>Applies and binds the texture to the model</li>
	 * 		<li>Then loading the shine variable where the dampening factor is 1 
	 * 			and the reflectivity factor is 0<li>
	 * </ul>
	 * </p>
	 * 
	 * @param model
	 */
	
	private void prepareTerrain(Terrain terrain) {
		rawModel RawModel = terrain.getModel();
		GL30.glBindVertexArray(RawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}
	
	/**
	 * Binds each texture to the correct colour in the blend map within the texture pack.
	 * This is done by using {@code GL13.glActiveTexture} and binding it to {GL13.GL_TEXTUREX} 
	 * where X is the number attribute to be bound to by the texture. It is bound by using the 
	 * {@code GL11.glBindTexture} method where the arguments are {@code GL11.GL_TEXTURE_2D} and
	 * the respective texture ID to be bound to.
	 *  
	 * @param terrain
	 */
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	/**
	 * Unbinds the model so that it is no longer in use or memory.
	 * This is done by enabling culling from the renderer 
	 * and disabling all attributes on the VAO. It then binds the vertex array to nothing, 
	 * thus unbinding the textured model.
	 */
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	/**
	 * A transformation matrix is made consisting of a 3D vector grabbing the x value of the terrain, a y level of 0 and the terrain x coordinate.
	 * All rotation possibilities are set to 0 and the scale factor is set to 1. The transformation matrix is then loaded.
	 * 
	 * @param terrain
	 */
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
