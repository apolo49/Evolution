package shaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fileHandling.log.Logger;

/**
 * 
 * <p>
 * Allows for manipulation of light levels and reflectivity as well as how the world is fogged over and how culling is handled.
 * </p>
 * 
 * <p>
 * This class handles all GLSL code interfacing and is inherited to specialisation of shaders on general objects, terrain, and 
 * can be extended for other specialisations.
 * </p>
 * 
 * <p>
 * As this class handles OpenGL language code (GLSL) it is also the hub for all uniform variables to be handled.
 * Uniform variables definitions and docmentation can be found <a href="https://www.khronos.org/opengl/wiki/Uniform_(GLSL)">here</a>
 * </p>
 * 
 * @author Joe
 *
 */


public abstract class Shaders {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	static File file1 = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * <p>
	 * Grabs the IDs of the vertex shader and fragment shader ID.
	 * The method then initialises the program to bind both shaders together
	 * ({@code GL20.glCreateProgram()}) then proceeds to attach both shaders to
	 * the program. It then binds the attributes and links and validates the
	 * program.
	 * </p>
	 * 
	 * <p>It then finishes by grabbing all uniforms</p>
	 * 
	 * @param vertexFile
	 * @param fragmentFile
	 * 
	 * @see #loadShader
	 * @see #bindAttributes
	 * @see #getAllUniformLocations
	 */
	
	public Shaders(String vertexFile,String fragmentFile) {
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	/**
	 * Gets location of uniform in GLSL code.
	 * 
	 * @param uniformName
	 * @return {@code int} that represents location of uniform in GLSL code
	 */
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * Called whenever program is used and is required to boot the shader.
	 * <p>Just calls the method {@code glUseProgram} and uses the program ID
	 * created on construction in the constructor.</p>
	 * @see #Shaders
	 */
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Needs to be used to stop memory overload and over use. This stops all programs
	 * being used by using {@code glUseProgram(0)}.
	 */
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * Cleans up memory and removes and detaches the shader to stop overflow and
	 * overload.
	 * <ul>
	 * 		<li> First, calls the stop method to cancel the use of all shaders. </li>
	 * 		<li> Then, detaches both shaders from the program. </li>
	 * 		<li> Thirdly, deletes both shaders from memory. </li>
	 * 		<li> Finally, the method deletes the program </li>
	 * </ul>
	 */
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	/**
	 * Method used to bind attributes to the program. 
	 * 
	 * @param attribute
	 * 			-Number of the attribute list in the VAO to be bound.
	 * @param variableName
	 * 			-Variable name in the shader code to bind the attribute to 
	 * 			found in the shader GLSL code.
	 */
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Load a {@code float} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniform1f} to interface with the GLSL code.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param value
	 * 			-Value to load to the uniform.
	 */
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Load a {@code int} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniform1i} to interface with the GLSL code.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param value
	 * 			-Value to load to the uniform.
	 */
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Load a {@code Vector3f} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniform3f} to interface with the GLSL code and splitting 
	 * the vector into 3 floats and loading them in as 3 arguments.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param vector
	 * 			-{@code Vector3f} value to load to the uniform.
	 */
	
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Load a {@code Vector2f} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniform2f} to interface with the GLSL code and splitting
	 * the vector into 2 floats and loading them in as 2 arguments.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param vector
	 * 			-{@code Vector2f} to load to the uniform.
	 */
	
	protected void load2DVector(int location, Vector2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	/**
	 * Load a {@code boolean} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniform1f} to interface with the GLSL code and checking
	 * whether the passed in value is true and if it is assigning a 1 to be passed in else a 0.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param value
	 * 			-Value to load to the uniform.
	 */
	
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * Load a {@code Matrix4f} to a uniform at the location passed in from the GLSL code.
	 * 
	 * <p>
	 * This is achieved using {@code glUniformMatrix4} to interface with the GLSL code, putting the matrix
	 * into a matrix buffer and flipping it, then passing it through and putting transpose to false.
	 * </p>
	 * 
	 * @param location
	 * 			-Location of the uniform in the GLSL code.
	 * @param matrix
	 * 			-Matrix to load to the uniform.
	 */
	
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
	 * Loads in shader files (GLSL text files) and read in all lines
	 * and connects them together into one long string before creating
	 * a new vertex or fragment shader from the GLSL code. Then attaches
	 * a string of source code to it and compiles it.
	 * 
	 * 
	 * @param file
	 * 			-File to be loaded as a string of the path 
	 *			 to the file relative to the project root.
	 * @param type 
	 * 			-Type of shader to be loaded, vertex 
	 * 			 ({@code GL_VERTEX_SHADER}) or fragment ({@code GL_FRAGMENT_SHADER}) shader.
	 * @return shaderID
	 * 			-the ID of the newly compiled shader type.
	 */
	
	private static int loadShader(String file,int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e) {
			Logger.IOSevereErrorHandler(e, file1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE) {
			Logger.main("[SEVERE] Could not Compile Shader.", -1, file1);
			Logger.main("[SEVERE]"+GL20.glGetShaderInfoLog(shaderID, 500), -1, file1);
		}
		return shaderID;
	}
	
}