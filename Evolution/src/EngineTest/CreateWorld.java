package EngineTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import toolbox.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.OBJLoader;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fileHandling.log.Logger;
import fileHandling.saves.Saves;
import guis.GUIRenderer;
import guis.GUITexture;
import models.TexturedModel;
import models.rawModel;
import python.PyExecuter;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class CreateWorld {
	public static void createNewWorld(String Name,BigInteger seed) {
		
		File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
		Loader loader = Game.getLoader();
		try {
			GLContext.useContext(DisplayManager.class);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("water"));
		
		TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		Logger.main("[HEALTHY] Created textures", 0, file);

		PyExecuter.main(null, "Mapper.py");
		PyExecuter.main(null, "heightMap.py");
		Logger.main("[HEALTHY] Map Generation Done!", 0, file);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("world\\worldMap"));

		rawModel tree = OBJLoader.loadObjModel("Tree", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("plain"));
		treeTexture.setShineDamper(15);
		treeTexture.setReflectivity(10);
		TexturedModel texturedTree = new TexturedModel(tree,treeTexture);
		
		rawModel grass = OBJLoader.loadObjModel("Grass_01", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("plain"));
		grassTexture.setShineDamper(15);
		grassTexture.setReflectivity(10);
		TexturedModel texturedGrass = new TexturedModel(grass,grassTexture);
		texturedGrass.getTexture().setHasTransparency(true);
		
		rawModel grass1 = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture grassTexture1 = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture1.setShineDamper(15);
		grassTexture1.setReflectivity(10);
		TexturedModel texturedGrass1 = new TexturedModel(grass1,grassTexture1);
		texturedGrass1.getTexture().setHasTransparency(true);
		
		rawModel fern = OBJLoader.loadObjModel("fern", loader);
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(4);
		fernTexture.setShineDamper(15);
		fernTexture.setReflectivity(10);
		TexturedModel texturedFern = new TexturedModel(fern,fernTexture);
		texturedFern.getTexture().setHasTransparency(true);
		
		Light light = new Light(new Vector3f(20000,20000,20000),new Vector3f(1,1,1));
		Logger.main("[HEALTHY] Created entities", 0, file);
		
		Terrain terrain = new Terrain(0,-1,loader,terrainTexturePack,blendMap,"heightMap.png");
		Terrain terrain2 = new Terrain(1,-1,loader,terrainTexturePack,blendMap,"heightMap.png");
		Terrain terrain3 = new Terrain(1,0,loader,terrainTexturePack,blendMap,"heightMap.png");
		Terrain terrain4 = new Terrain(0,0,loader,terrainTexturePack,blendMap,"heightMap.png");
		Terrain terrain5 = new Terrain(-1,0,loader,terrainTexturePack,blendMap,"heightMap.png");
		Terrain terrain6 = new Terrain(-1,-1,loader,terrainTexturePack,blendMap,"heightMap.png");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);
		terrains.add(terrain5);
		terrains.add(terrain6);
		Logger.main("[HEALTHY] Created terrains", 0, file);
		
		List<Entity> allTrees = new ArrayList<Entity>();
		List<Entity> allGrasses = new ArrayList<Entity>();
		Random random = new Random(seed);
		
		for(Terrain terrain1 : terrains) {
			for (int i = 0; i < 100; i++) {
				float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
				float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
				float y = terrain1.getHeightOfTerrain(x, z);
				allTrees.add(new Entity(texturedTree, new Vector3f(x, y, z),0, random.nextFloat() * 360, 0, 1));
			}
		}
		for(Terrain terrain1 :terrains){
			for (int j =0; j < 200; j++) {
				float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
				float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
				float y = terrain1.getHeightOfTerrain(x, z);
				allGrasses.add(new Entity(texturedGrass,new Vector3f(x,y,z),0,0,0,3));
			}
		}
		for (Terrain terrain1 :terrains) {
			for (int k=0; k<200; k++) {
			float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
			float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
			float y = terrain1.getHeightOfTerrain(x, z);
				allGrasses.add(new Entity(texturedGrass1,new Vector3f(x,y,z),0,0,0,3));
			}
		}
		
		for (Terrain terrain1 :terrains) {
			for (int k=0; k<200; k++) {
			float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
			float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
			float y = terrain1.getHeightOfTerrain(x, z);
				allGrasses.add(new Entity(texturedFern,random.nextInt(16),new Vector3f(x,y,z),0,0,0,3));
			}
		}
		
		
		List<Entity> allEntities = new ArrayList<Entity>();
		for (Entity entity : allTrees) {
			allEntities.add(entity);
		}
		for (Entity entity : allGrasses) {
			allEntities.add(entity);
		}
		Logger.main("[HEALTHY] Populated world", 0, file);
		
		MasterRenderer renderer = Game.getMasterRenderer();
		
		PyExecuter.main(null, "test.py");
		Logger.main("[HEALTHY] DNA Generation done!", 0, file);
		
		rawModel HumanModel = OBJLoader.loadObjModel("Human1", loader);
		ModelTexture HumanTexture = new ModelTexture(loader.loadTexture("plain"));
		HumanTexture.setShineDamper(15);
		HumanTexture.setReflectivity(10);
		TexturedModel Human = new TexturedModel(HumanModel,HumanTexture);
		
		Player player = new Player(Human, new Vector3f(100,0,-50), 0, 0, 0, 1);
		allEntities.add(player);
		Camera camera = new Camera(player);
		Logger.main("[HEALTHY] Created player, loading world", 0, file);
		
		List<GUITexture> guis = new ArrayList<GUITexture>();
		GUITexture gui = new GUITexture(loader.loadTexture("Evolution"),new Vector2f(0f,0.8f),new Vector2f(0.25f,0.5f));
		guis.add(gui);
		GUIRenderer guiRenderer = Game.getGuiRenderer();
		
		Logger.main("[HEALTHY] World loaded!", -1, file);
		if (!(Name == "New World")) {
			Saves.NewWorld(Name,terrains);
		}else {
			Saves.NewWorld(terrains);
		}
		
		Game.main(renderer, camera, terrains, player, allEntities, light, guiRenderer, guis,loader);
	}
	
	public static void loadWorld(String worldName,BigInteger seed) {
		File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
		Loader loader = Game.getLoader();
		if(Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName))) {

			TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
			TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
			TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("path"));
			TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("water"));

			TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
			Logger.main("[HEALTHY] Created textures", 0, file);

			Logger.main("Grabbing world map",0,file);
			TerrainTexture blendMap;
			try {
				blendMap = new TerrainTexture(loader.reloadMap(new FileInputStream(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\worldMap.png")));

				Logger.main("[HEALTHY] Map Getting Done!", 0, file);

				rawModel tree = OBJLoader.loadObjModel("Tree", loader);
				ModelTexture treeTexture = new ModelTexture(loader.loadTexture("plain"));
				treeTexture.setShineDamper(15);
				treeTexture.setReflectivity(10);
				TexturedModel texturedTree = new TexturedModel(tree,treeTexture);

				rawModel grass = OBJLoader.loadObjModel("Grass_01", loader);
				ModelTexture grassTexture = new ModelTexture(loader.loadTexture("plain"));
				grassTexture.setShineDamper(15);
				grassTexture.setReflectivity(10);
				TexturedModel texturedGrass = new TexturedModel(grass,grassTexture);
				texturedGrass.getTexture().setHasTransparency(true);

				rawModel grass1 = OBJLoader.loadObjModel("grassModel", loader);
				ModelTexture grassTexture1 = new ModelTexture(loader.loadTexture("grassTexture"));
				grassTexture1.setShineDamper(15);
				grassTexture1.setReflectivity(10);
				TexturedModel texturedGrass1 = new TexturedModel(grass1,grassTexture1);
				texturedGrass1.getTexture().setHasTransparency(true);

				rawModel fern = OBJLoader.loadObjModel("fern", loader);
				ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
				fernTexture.setNumberOfRows(4);
				fernTexture.setShineDamper(15);
				fernTexture.setReflectivity(10);
				TexturedModel texturedFern = new TexturedModel(fern,fernTexture);
				texturedFern.getTexture().setHasTransparency(true);

				Light light = new Light(new Vector3f(20000,20000,20000),new Vector3f(1,1,1));
				Logger.main("[HEALTHY] Created entities", 0, file);

				Terrain terrain = new Terrain(0,-1,loader,terrainTexturePack,blendMap, new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				Terrain terrain2 = new Terrain(1,-1,loader,terrainTexturePack,blendMap,new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				Terrain terrain3 = new Terrain(1,0,loader,terrainTexturePack,blendMap,new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				Terrain terrain4 = new Terrain(0,0,loader,terrainTexturePack,blendMap,new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				Terrain terrain5 = new Terrain(-1,0,loader,terrainTexturePack,blendMap,new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				Terrain terrain6 = new Terrain(-1,-1,loader,terrainTexturePack,blendMap,new File (System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"));
				List<Terrain> terrains = new ArrayList<Terrain>();
				terrains.add(terrain);
				terrains.add(terrain2);
				terrains.add(terrain3);
				terrains.add(terrain4);
				terrains.add(terrain5);
				terrains.add(terrain6);
				Logger.main("[HEALTHY] Created terrains", 0, file);

				List<Entity> allTrees = new ArrayList<Entity>();
				List<Entity> allGrasses = new ArrayList<Entity>();
				Random random = new Random(seed);

				for(Terrain terrain1 : terrains) {
					for (int i = 0; i < 100; i++) {
						float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
						float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
						float y = terrain1.getHeightOfTerrain(x, z);
						allTrees.add(new Entity(texturedTree, new Vector3f(x, y, z),0, random.nextFloat() * 360, 0, 1));
					}
				}
				for(Terrain terrain1 :terrains){
					for (int j =0; j < 200; j++) {
						float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
						float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
						float y = terrain1.getHeightOfTerrain(x, z);
						allGrasses.add(new Entity(texturedGrass,new Vector3f(x,y,z),0,0,0,3));
					}
				}
				for (Terrain terrain1 :terrains) {
					for (int k=0; k<200; k++) {
						float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
						float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
						float y = terrain1.getHeightOfTerrain(x, z);
						allGrasses.add(new Entity(texturedGrass1,new Vector3f(x,y,z),0,0,0,3));
					}
				}

				for (Terrain terrain1 :terrains) {
					for (int k=0; k<200; k++) {
						float x = random.nextInt((int) Terrain.getSize() ) + terrain1.getX();
						float z = random.nextInt((int) Terrain.getSize() ) + terrain1.getZ();
						float y = terrain1.getHeightOfTerrain(x, z);
						allGrasses.add(new Entity(texturedFern,random.nextInt(16),new Vector3f(x,y,z),0,0,0,3));
					}
				}


				List<Entity> allEntities = new ArrayList<Entity>();
				for (Entity entity : allTrees) {
					allEntities.add(entity);
				}
				for (Entity entity : allGrasses) {
					allEntities.add(entity);
				}
				Logger.main("[HEALTHY] Populated world", 0, file);

				MasterRenderer renderer = Game.getMasterRenderer();

				System.out.println("Generating DNA...");
				PyExecuter.main(null, "test.py");
				Logger.main("[HEALTHY] DNA Generation done!", 0, file);

				rawModel HumanModel = OBJLoader.loadObjModel("Human1", loader);
				ModelTexture HumanTexture = new ModelTexture(loader.loadTexture("plain"));
				HumanTexture.setShineDamper(15);
				HumanTexture.setReflectivity(10);
				TexturedModel Human = new TexturedModel(HumanModel,HumanTexture);

				Player player = new Player(Human, new Vector3f(100,0,-50), 0, 0, 0, 1);
				allEntities.add(player);
				Camera camera = new Camera(player);
				Logger.main("[HEALTHY] Created player, loading world", 0, file);

				List<GUITexture> guis = new ArrayList<GUITexture>();
				GUITexture gui = new GUITexture(loader.loadTexture("Evolution"),new Vector2f(0f,0.8f),new Vector2f(0.25f,0.5f));
				guis.add(gui);
				GUIRenderer guiRenderer = Game.getGuiRenderer();

				Logger.main("[HEALTHY] World loaded!", -1, file);
				Game.main(renderer, camera, terrains, player, allEntities, light, guiRenderer, guis,loader);
			} catch (FileNotFoundException e) {
				Logger.FileNotFoundSevereErrorHandler(e, file);
			}
		}else {
			Logger.main("[FATAL] World file "+worldName+" Not Found.",0,file);
			Logger.main("[FATAL] Closing down", -1, file);
			System.exit(-1);
		}
	}
}
