package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	
	
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x,scale.y,1f), matrix, matrix);
		return matrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	/**
	 * Transforms the matrix by the given translation and allows 
	 * for a movement of the object and scaling of the object.
	 * 
	 * <p> We do this by creating an identity matrix and translating the matrix 
	 * by a 3D vector ({@code translation}). Then we rotate across each individual axis
	 * using each of the rotation parameters (converted to radians), afterwards the matrix
	 * is scaled using the scale factor. </p>
	 * 
	 * @param translation
	 * 			-The translation to move the object by in all 3 axis.
	 * 
	 * @param rx
	 * 			-The rotation around the x-axis (in degrees)
	 * 
	 * @param ry
	 * 			-The rotation around the y-axis (in degrees)
	 * 
	 * @param rz
	 * 			-The rotation around the z-axis (in degrees)
	 * 
	 * @param scale
	 * 			-The factor to scale the transformation by.
	 * 
	 * @return the resulting transformation matrix
	 */
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	/**
	 * Creates a new Identity matrix, rotating it by the camera pitch and a 3D vector with a {@code Integer} value of 1 in the x-axis.
	 * The method rotates it again by the camera yaw against a 3D vector with an {@code Integer} value of 1 in the y-axis.
	 * Then make the camera position negative (as we want the world to move against the movement of the camera) and translate the matrix
	 * against the negative camera position and return it.
	 * 
	 * @param camera
	 * 			-The camera of the player.
	 * @return The view matrix
	 */
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos,viewMatrix,viewMatrix);
		return viewMatrix;
	}

}
