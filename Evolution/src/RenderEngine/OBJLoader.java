package RenderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fileHandling.log.Logger;
import models.rawModel;

/**
 * <p>Loads an object file to a raw model as all is put into a VAO.</p>
 * 
 * <p>As well as processes vertices to obtain normals to the vertex to put
 * a texture on it.</p>
 * 
 * @author Joe
 *
 */

public class OBJLoader {
	private static File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	
	/**
	 * <p>Loads the model from the object file processing vertex by vertex using a {@code BufferedReader} to
	 * read the file as a {@code String}. The vertices are be loaded into an {@code ArrayList} a 3D vector
	 * of {@code float} types; textures are loaded into an {@code ArrayList} of 2D {@code float} types;
	 * normals are be loaded into an {@code ArrayList} a 3D vector of {@code float} types; indices are loaded
	 * into an {@code ArrayList} of {@code Integer} types.</p>
	 * 
	 * <p>Then loops through the object file and parses each {@code String} to a {@code float}/{@code Integer} 
	 * and adds it to the vector list. Then all the vertices are processed to create triangles to build the model.
	 * Promptly the reader is closed and the processing is almost done.</p>
	 * 
	 * <p> Two new arrays are created representing vertices ({@code float[]} and indices {@code int[]}. The vertex array is defined as 3x the size
	 * of the amount of vertices, whereas the indices array is defined as being the same size as the amount of indices.
	 * All 3 dimensions of the vertices are added to the array in adjacent indexes. Then all indices are added to the indices
	 * list.</p>
	 * 
	 * @param fileName
	 * 			-The fileName of the {@code .obj} file.
	 * 
	 * @param loader
	 * 			-The loader to use to load the object file as a {@code rawModel} in the prorgam.
	 * 
	 * @return The {@code rawModel} to be returned as a result from the object file
	 */
	
	public static rawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/objects/"+fileName+".obj"));
		}catch (FileNotFoundException e) {
			Logger.FileNotFoundUnhealthyErrorHandler(e, file);
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray = null;
		try {
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")) {
					texturesArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			while(line!=null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1,indices,textures,normals,texturesArray,normalsArray);
				processVertex(vertex2,indices,textures,normals,texturesArray,normalsArray);
				processVertex(vertex3,indices,textures,normals,texturesArray,normalsArray);
				line = reader.readLine();
			}
			reader.close();
			
		}catch(Exception e) {
			Logger.UnhealthyException(e,file);
		}
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		for(int i=0;i<indices.size();i++) {
			indicesArray[i] = indices.get(i);
			
		}
		return loader.loadtoVAO(verticesArray, texturesArray, normalsArray, indicesArray);
	}
	
	/**
	 * <p>Processes each vertex of the shape.</p>
	 * 
	 * <p>For each vertex which tells us which normal and which texture
	 * is associated with which vertex position we want to put these in
	 * the correct position in the texture and normal array. </p>
	 * 
	 * <p>The first bit of data to be processed will be the first vertex pointer which will
	 * be the index for the vertex so it is added to the indices list. As the obj files 
	 * start at 1 and our vertices start at 0, 1 needs to be negated from the current 
	 * position of the pointer to get it to point at the right position in a list/array.</p>
	 * 
	 * <p>Now we wish to grab the texture that corresponds to this vertex which is stored in index
	 * 1 of the vertex data {@code String} {@code Array} (the second element), however we need to negate 1
	 * from the grabbed integer as the ".obj" file starts at position 1. We add this element to the texture
	 * array at position {@code currentVertexPointer*2} for the x value and {@code currentVertexPointer*2+1}
	 * for the y value texture which is negated from 1 (this is because OpenGL starts at the top left of 
	 * the texture, as opposed to the bottom left like normal Cartesian geometry). The reason the pointer
	 * position is multiplied by 2 is due to each texture having 2 floats, not 1 (an x float and a y float).</p>
	 * 
	 * <p>The normal vector associated with the current vertex to be processed is obtained (the data for this is
	 * stored in index 2 of the vertex (element 3)) and again 1 is negated from the obtained parsed integer due to
	 * obj file lists beginning at 1 instead of 0. Then, due to Normal vectors being 3 dimensional, the x value of
	 * the normal is added to the normal array at position {@code currentVertexPointer*3}; the y value of 
	 * the normal is added to the normal array at position {@code currentVertexPointer*3+1}; finally, the z value of
	 * the normal is added to the normal array at position {@code currentVertexPointer*3+2}}.</p>
	 * 
	 * <p> That leaves the vertex processed properly so the loader, renderer and shader can handle the object.</p>
	 * 
	 * @param vertexData
	 * 			-The data associated with the current vertex.
	 * @param indices
	 * 			-The list of indices for each vertex.
	 * @param textures
	 * 			-List of texture coordinates for each vertex.
	 * @param normals
	 * 			-The list of normal vectors to the vertices.
	 * @param textureArray
	 * 			-The float array for all processed textures to be added to in respect to their vertices.
	 * @param normalsArray
	 * 			-The float array of normals vectors for all processed normal vectors to be added to in respect to their vertices. 
	 */
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
	}

}
