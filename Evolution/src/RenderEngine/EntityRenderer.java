package RenderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.TexturedModel;
import models.rawModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {

	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Prepares each instance of the model and
	 * renders each model and draws it onto the display,
	 * then unbinds the model.
	 * 
	 * 
	 * @param entities
	 * 			-List of all entities to be rendered to the display.
	 * 
	 * @see #prepareTexturedModel(TexturedModel)
	 * @see #prepareInstance(Entity)
	 */
	
	public void render(Map<TexturedModel,List<Entity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	/**
	 * This renders the model and applies the correct attributes and factors to the model.
	 * <p>The attributes and factors includes:
	 * <ul>
	 * 		<li>Binding the vertex array (VAO)</li>
	 * 		<li>Enabling all 3 attributes in the attribute array</li>
	 * 		<li>Grabbing the texture on the model to be applied</li>
	 * 		<li>Disables culling on the model if the model is already transparent</li>
	 * 		<li>Loads fake lighting properties depending on whether the object is a false light</li>
	 * 		<li>Loads the shine variables using the shine dampener and reflectivity factors</li>
	 * 		<li>Applies and binds the texture to the model</li>
	 * 
	 * @param model
	 */
	
	private void prepareTexturedModel(TexturedModel model) {
		rawModel RawModel = model.getRawModel();
		GL30.glBindVertexArray(RawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariables(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
	
}
