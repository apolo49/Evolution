package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.Shaders;

/**
 * 
 * Shader specialisation used in giving GUIs depth
 * and render against the display properly allowing alpha
 * transparency and size and space manipulation and positioning.
 * 
 * @author Joe
 *
 * @see Shaders
 * @see shaders.StaticShader
 * @see shaders.TerrainShader
 */

public class GUIShader extends Shaders {

   private static final String VERTEX_FILE = "src/guis/guiVertexShader.txt";
   private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.txt";
    
   private int location_transformationMatrix;

   /**
    * Uses the constructor in the parent class and uses the vertex
    * and fragment shader files specialised for the GUI implementation.
    * 
    * <p> Works similarly to {@link shaders.StaticShader#StaticShader()} and 
    * {@link shaders.TerrainShader#TerrainShader()}</p>
    * 
    * @see Shaders#Shaders(String, String)
    */
   
   public GUIShader() {
       super(VERTEX_FILE, FRAGMENT_FILE);
   }
   
   /**
    * Uses the load matrix method in the parent class to load the transformation
    * matrix in for the GUI quads to see the true location of the GUI on the screen.
    * 
    * @param matrix
    * 		-Transformation matrix.
    * 
    * @see Shaders#loadMatrix
    */
   
   public void loadTransformation(Matrix4f matrix){
       super.loadMatrix(location_transformationMatrix, matrix);
   }

   /**
    * The only uniform used in GUI rendering is the transformation matrix.
    * This is grabbed using the {@code Shaders.getUniformLocation} method.
    * 
    * @see Shaders#getUniformLocation
    */
   
   @Override
   protected void getAllUniformLocations() {
       location_transformationMatrix = super.getUniformLocation("transformationMatrix");
   }

   /**
    * Binds the position attribute to location 0 in the VAO.
    * 
    * @see Shaders#bindAttribute
    */
   @Override
   protected void bindAttributes() {
       super.bindAttribute(0, "position");
   }
    
    
    

	
}
