package RenderEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import fileHandling.log.Logger;
import models.rawModel;

/**
 * Handles the loading of geometry data into VAOs. It also keeps track of all
 * the created VAOs and VBOs so that they can all be deleted when the game
 * closes.
 * 
 * @author Joe
 *
 **/

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");

	/**
	 * <p>Creates a VAO and stores:
	 * <ul>			
	 * <li>The position data of the vertices into attribute
	 * 0 of the VAO.</li>
	 * 			
	 * <li>The texture coordinate data into attribute 1 of 
	 * the VAO.</li>
	 * 			
	 * <li>The normal of the vertices of the model into 
	 * attribute 3 of the VAO.</li>
	 * 			
	 * <li>The list of indices on the model (.obj format).</li>
	 * </ul>
	 * @param positions
	 *            - The 3D positions of each vertex in the geometry (in this
	 *            example a quad).
	 * @param textureCoords
	 * 			  -The coordinates of the textures in relation to the origin
	 * @param normals
	 * 			  -The normal to all vertices on the model
	 * @param indices
	 * 			  -The list of the indices of the model
	 * 
	 * @return The loaded model, with the VAO ID and the length of the index list.
	 **/
	
	public rawModel loadtoVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		vaos.add(vaoID);
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0,3,positions);
		storeDataInAttributeList(1,2,textureCoords);
		storeDataInAttributeList(2,3,normals);
		unbindVAO();
		return new rawModel(vaoID,indices.length);
	}
	
	/**
	 * Creates a VAO and stores the position data of the vertices into attribute
	 * 0 of the VAO.
	 * 
	 * @param positions
	 *            - The 3D positions of each vertex in the geometry (in this
	 *            example a quad).
	 * @return The loaded model.
	 */
	
	public rawModel loadtoVAO(float[] positions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, 2, positions);
		unbindVAO();
		return new rawModel(vaoID,positions.length/2);
	}
	
	/**
	 * Loads a texture into the OpenGL loader to be used by the program to be
	 * rendered properly by the shader.
	 * 
	 * <p>This method uses the slick-util texture loader to grab a texture in
	 * the "png" format and uses the file name to create a {@code FileInputStream}. It also
	 * generates the mipmap for the 2D texture and texture parameters f and i are used for the
	 * mipmap.</p>
	 * 
	 * @param fileName
	 * 			-File name of the texture to be used 
	 * @return textureID
	 */
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res//textures//"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
		} catch (FileNotFoundException e) {
			Logger.FileNotFoundUnhealthyErrorHandler(e, file);
		} catch (IOException e) {
			Logger.IOUnhealthyErrorHandler(e, file);
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Used to load the PNG of the map that can be used as a height
	 * map or terrain map. The default is generated using Perlin
	 * Noise generation in Python through the {@code PyExecuter.class}.
	 * This method uses {@code glGenerateMipmap}, {@code glTexParameteri}, {@code glTexParameterf} methods
	 * given to us by OpenGL to load it with the 2D attributes and mipmap and 
	 * minimal filter settings as well as a load bias.
	 * The method then adds the texture ID to the list of loaded textures.
	 * 
	 * @param mapfile
	 * 				-file stream of the map to be used
	 * @return ID of the texture of the map file
	 */
	
	public int reloadMap(FileInputStream mapfile) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", mapfile);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
		} catch (FileNotFoundException e) {
			Logger.FileNotFoundUnhealthyErrorHandler(e, file);
		} catch (IOException e) {
			Logger.IOUnhealthyErrorHandler(e, file);
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Deletes all the VAOs, textures and VBOs when the game is closed. VAOs, textures and VBOs are
	 * located in video memory.
	 */
	
	public void cleanUp() {
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	/**
	 * Creates a new VAO and returns its ID. A VAO holds geometry data that we
	 * can render and is physically stored in memory on the GPU, so that it can
	 * be accessed very quickly during rendering.
	 * 
	 * Like most objects in OpenGL, the new VAO is created using a "gen" method
	 * which returns the ID of the new VAO. In order to use the VAO it needs to
	 * be made the active VAO. Only one VAO can be active at a time. To make
	 * this VAO the active VAO (so that we can store stuff in it) we have to
	 * bind it.
	 * 
	 * @return The ID of the newly created VAO.
	 */
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * <p>Stores the position data of the vertices into attribute 0 of the VAO. To
	 * do this the positions must first be stored in a VBO. You can simply think
	 * of a VBO as an array of data that is stored in memory on the GPU for easy
	 * access during rendering.
	 * </p>
	 * <p>Just like with the VAO, we create a new VBO using the {@code GL15.glGenBuffers()} method, and
	 * make it the active VBO (so that we manipulate it) by binding it.
	 * </p>
	 * <p>We then store the positions data in the active VBO by using the
	 * {@code glBufferData} method. We also indicate using {@code GL_STATIC_DRAW} that this data
	 * won't need to be changed. If we wanted to edit the positions every frame
	 * (perhaps to animate the quad) then we would use {@code GL_DYNAMIC_DRAW} instead.
	 * </p>
	 * <p>We the connect the VBO to the VAO using the {@code glVertexAttribPointer()}
	 * method. This needs to know the attribute number of the VAO where we want
	 * to put the data, the number of floats used for each vertex (3 floats in
	 * this case, because each vertex has a 3D position, an x, y, and z value),
	 * the type of data (in this case we used floats) and then some other more
	 * complicated stuff for storing the data in more fancy ways.
	 * </p>
	 * 
	 * @param attributeNumber
	 *            - The number of the attribute of the VAO where the data is to
	 *            be stored.
	 * @param data
	 *            - The geometry data to be stored in the VAO, in this case the
	 *            positions of the vertices.
	 * @param coordinateSize
	 * 			 -The magnitude of the coordinates
	 */
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Unbinds the VAO after we're finished using it. If we want to edit or use
	 * the VAO we would have to bind it again first.
	 */
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	/**
	 * <p>Before we can store data in a VBO it needs to be in a certain format: in
	 * a buffer. In this case we will use a {@code IntBuffer} because the data we
	 * want to store is {@code IntBuffer}. If we were storing {@code float} data we would use an
	 * {@code FloatBuffer}.</p>
	 * 
	 * <p>First and empty buffer of the correct size is created. You can think of a
	 * buffer as basically an array with a pointer. After putting the necessary
	 * data into the buffer the pointer will have increased so that it points at
	 * the first empty element of the array. This is so that we could add more
	 * data to the buffer if we wanted and it wouldn't overwrite the data we've
	 * already put in. However, we're done with storing data and we want to make
	 * the buffer ready for reading. To do this we need to make the pointer
	 * point to the start of the data, so that OpenGL knows where in the buffer
	 * to start reading. The {@code flip()} method does just that, putting the pointer
	 * back to the start of the buffer.
	 * </p>
	 * 
	 * @see #storeDataInFloatBuffer(float[])
	 * 
	 * @param data
	 *            - The float data that is going to be stored in the buffer.
	 *            
	 * @return The FloatBuffer containing the data. This float buffer is ready
	 *         to be loaded into a VBO.
	 */
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * <p>Before we can store data in a VBO it needs to be in a certain format: in
	 * a buffer. In this case we will use a {@code FloatBuffer} because the data we
	 * want to store is {@code FloatBuffer}. If we were storing {@code int} data we would use an
	 * {@code IntBuffer}.</p>
	 * 
	 * <p>First and empty buffer of the correct size is created. You can think of a
	 * buffer as basically an array with a pointer. After putting the necessary
	 * data into the buffer the pointer will have increased so that it points at
	 * the first empty element of the array. This is so that we could add more
	 * data to the buffer if we wanted and it wouldn't overwrite the data we've
	 * already put in. However, we're done with storing data and we want to make
	 * the buffer ready for reading. To do this we need to make the pointer
	 * point to the start of the data, so that OpenGL knows where in the buffer
	 * to start reading. The {@code flip()} method does just that, putting the pointer
	 * back to the start of the buffer.
	 * </p>
	 * 
	 * @see #storeDataInIntBuffer(int[])
	 * 
	 * @param data
	 *            - The float data that is going to be stored in the buffer.
	 *            
	 * @return The FloatBuffer containing the data. This float buffer is ready
	 *         to be loaded into a VBO.
	 */
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}