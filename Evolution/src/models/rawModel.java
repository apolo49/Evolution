package models;

/**
 * Represents a loaded model. It contains the ID of the VAO that contains the
 * model's data, and holds the number of vertices in the model.
 * 
 * @author Joe
 *
 */

public class rawModel {

	private int vaoID;
	private int vertexCount;
	
	/**
	 * The constructor for the rawModel type. Takes in the VAO ID and Vertex Count to refer to the model in the loader.
	 * 
	 * @param VaoID
	 * 
	 * @param vertexCount
	 */
	
	public rawModel(int VaoID, int vertexCount) {
		this.vaoID = VaoID;
		this.vertexCount = vertexCount;			
	}

	/**
	 * @return The ID of the VAO which contains the data about all the geometry
	 *         of this model.
	 */
	
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return The number of vertices in the model.
	 */
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	
	
}
