package terrains;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import RenderEngine.Loader;
import fileHandling.log.Logger;
import models.rawModel;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {

	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = (float) Math.pow(256, 3);
	private static final File logFile = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	
	private float x;
	private float z;
	private rawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private float[][] heights;
	
	/**
	 * <p>The constructor for a terrain object using a {@code String} of the height map 
	 * name rather than the {@code File} of the height map. This sets the {@code texturePack} &
	 * {@code blendMap} attributes to the input variables of the same name.</p>
	 * 
	 * <p>The method also sets {@code x} and {@code z} values to the gridX and gridZ values 
	 * multiplied by the size of the terrain to calculate their coordinates in the world. The model
	 * is then generated using the {@code generateTerrain} method and 
	 * the loader and {@code String} {@code heightMap}.</p>
	 * 
	 * @param gridX
	 * 		-The location of the terrain in relation to other terrains on the x-axis.
	 * @param gridZ
	 * 		-The location of the terrain in relation to other terrains on the y-axis.
	 * @param loader
	 * 		-The loader in use by the current OpenGL context.
	 * @param texturePack
	 * 		-The {@code TerrainTexturePack} to be applied to the blend map on the terrain.
	 * @param blendMap
	 * 		-The blend map (or terrain map) to be used on the terrain.
	 * @param heightMap
	 * 		-The height map to be applied to give the terrain a shape as a string of the name.
	 * 
	 * @see #generateTerrain(Loader, String)
	 * @see RenderEngine.Loader
	 */
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader,heightMap);
	}
	
	/**
	 * <p>The constructor for a terrain object using a {@Code File} of the height map 
	 * rather than the {@code String} of the height map name. This sets the {@code texturePack} &
	 * {@code blendMap} attributes to the input variables of the same name.</p>
	 * 
	 * <p>The method also sets {@code x} and {@code z} values to the gridX and gridZ values 
	 * multiplied by the size of the terrain to calculate their coordinates in the world. The model
	 * is then generated using the {@code generateTerrain} method and 
	 * the loader and {@code File} {@code heightMap}.</p>
	 * 
	 * @param gridX
	 * 		-The location of the terrain in relation to other terrains on the x-axis.
	 * @param gridZ
	 * 		-The location of the terrain in relation to other terrains on the y-axis.
	 * @param loader
	 * 		-The loader in use by the current OpenGL context.
	 * @param texturePack
	 * 		-The {@code TerrainTexturePack} to be applied to the blend map on the terrain.
	 * @param blendMap
	 * 		-The blend map (or terrain map) to be used on the terrain.
	 * @param heightMap
	 * 		-The file object of height map to be applied to give the terrain a shape.
	 * 
	 * @see #generateTerrain(Loader, String)
	 * @see RenderEngine.Loader
	 * @see java.io.File
	 */
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, File heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader,heightMap);
	}
	
	/**
	 * @return The x-coordinate of the terrain in the world as a {@code float}.
	 */
	
	public float getX() {
		return x;
	}
	
	/**
	 * @return The z-coordinate of the terrain in the world as a {@code float}.
	 */
	
	public float getZ() {
		return z;
	}

	/**
	 * @return The size of the terrain in coordinate units as a {@code float}.
	 */
	
	public static float getSize() {
		return SIZE;
	}

	/**
	 * @return The terrain {@code rawModel}
	 * @see rawModel
	 */
	
	public rawModel getModel() {
		return model;
	}

	/**
	 * @return The {@code TerrainTexturePack} used and applied to terrain.
	 */
	
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * @return The blend map used to show where textures belong on the terrain.
	 */
	
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)heights.length-1);
		int gridX = (int) Math.floor(terrainX/gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX <0 || gridZ < 0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	private rawModel generateTerrain(Loader loader,String heightMap){
        BufferedImage image = null;
        File HeightMap = new File ("res\\textures\\world\\"+heightMap);
        try {
            image = ImageIO.read(HeightMap);
        } catch (IOException e) {
            Logger.IOSevereErrorHandler(e, logFile);
        }
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j,i,image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = getHeight(j,i,image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadtoVAO(vertices, textureCoords, normals, indices);
	}
	
	private rawModel generateTerrain(Loader loader,File HeightMap){
        BufferedImage image = null;
        try {
            image = ImageIO.read(HeightMap);
        } catch (IOException e) {
            Logger.IOSevereErrorHandler(e, logFile);
        }
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j,i,image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = getHeight(j,i,image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadtoVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x-1, z,image);
		float heightR = getHeight(x+1, z,image);
		float heightD = getHeight(x, z-1,image);
		float heightU = getHeight(x, z+1,image);
		Vector3f normal = new Vector3f(heightL-heightR,2f,heightD-heightU);
		normal.normalise();
		return normal;
	}
	
	private float getHeight(int x, int z, BufferedImage image) {
		if(x<=0 || x>=image.getHeight() || z<=0 || z>=image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height +=MAX_PIXEL_COLOUR/2f;
		height /=MAX_PIXEL_COLOUR/2f;
		height *=MAX_HEIGHT;
		return height;
	}
}
