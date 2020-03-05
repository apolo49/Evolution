package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * 
 * Class used to build GUIs by giving them a look.
 * Used to be used to render the logo on the screen before version numbers were implemented.
 * <p>Should be used to render any 2D textures to the screen that are not bound to entities.</p>
 * 
 * @author Joe
 *
 */
		

public class GUITexture {

		private int texture;
		private Vector2f position;
		private Vector2f size;
		
		/**
		 * Constructor that sets the attributes for the GUI.
		 * 
		 * @param texture
		 * 			-texture ID.
		 * @param position
		 * 			-Position of the GUI on the screen represented by a 2D-{@code float} vector between -1 and 1 where 0 is centre and -1 is far left and far bottom.
		 * @param size
		 * 			-Size of the GUI on the screen represented by a 2D-{@code float} vector between -1 and 1.
		 */
		
		public GUITexture(int texture, Vector2f position, Vector2f size) {
			this.texture = texture;
			this.position = position;
			this.size = size;
		}

		/**
		 * @return The texture ID
		 */
		
		public int getTexture() {
			return texture;
		}

		/**
		 * @return The position of the GUI on the screen as a 2D-{@code float} vector between -1 and 1.
		 * 
		 * @see #GUITexture(int, Vector2f, Vector2f)
		 */
		
		public Vector2f getPosition() {
			return position;
		}

		/**
		 * @return The size of the GUI on the screen as a 2D-{@code float} vector between -1 and 1.
		 * 
		 * @see #GUITexture(int, Vector2f, Vector2f) 
		 */
		
		public Vector2f getSize() {
			return size;
		}
	
}
