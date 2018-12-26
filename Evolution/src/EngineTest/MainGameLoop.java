package EngineTest;

//
//					  _oo0oo_
//					 o8888888o
//					 88" . "88
//					 (| -_- |)
//					 0\  =  /0
//				   ___/`---'\___
//          	  .'\\|     |// '.
//			     /\\|||  :  |||// \
//			    /_||||| -:- |||||- \
//			   |  | \\\  -  /// |   |
//			   |\_|  ''\---/''  |_/ |
//			   \ .-\__  '-'  ___/-. /
//			___'. .'  /--.--\  `. .'___
//		 ."" '<  `.___\_<|>_/___.' >' "".
//		| | :  `- \`.;`\ _ /`;.`/ - ` : | |
//		\  \ `_.   \_ __\ /__ _/   .-` /  /
//	=====`-.____`.___ \_____/___.-`___.-'=====
//					  `=---='
//
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//	BUDDHA BLESS YOUR CODE TO BE BUG FREE
//


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.OBJLoader;
import RenderEngine.EntityRenderer;
import entities.Camera;
import entities.Entity;
import entities.Light;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import models.TexturedModel;
import models.rawModel;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		

		
		rawModel tree = OBJLoader.loadObjModel("Tree", loader);
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("plain"));
		treeTexture.setShineDamper(15);
		treeTexture.setReflectivity(10);
		TexturedModel texturedTree = new TexturedModel(tree,treeTexture);
		Entity TreeEntity = new Entity(texturedTree,new Vector3f(0,0,-30),0,0,0,1);
		
		rawModel grass = OBJLoader.loadObjModel("Grass_01", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("plain"));
		grassTexture.setShineDamper(15);
		grassTexture.setReflectivity(10);
		TexturedModel texturedGrass = new TexturedModel(grass,grassTexture);
		texturedGrass.getTexture().setHasTransparency(true);
		Entity grassEntity = new Entity(texturedGrass,new Vector3f(0,0,-30),0,0,0,1);
		
		rawModel grass1 = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture grassTexture1 = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture1.setShineDamper(15);
		grassTexture1.setReflectivity(10);
		TexturedModel texturedGrass1 = new TexturedModel(grass1,grassTexture1);
		texturedGrass1.getTexture().setHasTransparency(true);
		Entity grassEntity1 = new Entity(texturedGrass1,new Vector3f(0,0,-30),0,0,0,1);
		
		Light light = new Light(new Vector3f(20000,20000,20000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain3 = new Terrain(1,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain4 = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain5 = new Terrain(-1,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain6 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		List<Entity> allTrees = new ArrayList<Entity>();
		List<Entity> allGrasses = new ArrayList<Entity>();
		Random random = new Random();
		
		for (int i =0; i < 200; i++) {
			float x = random.nextFloat() * 800 -400;
			float y = 0;
			float z = random.nextFloat() * -600;
			allTrees.add(new Entity(texturedTree,new Vector3f(x,y,z),0,0,0,3));
		}
		for (int i =0; i < 200; i++) {
			float x = random.nextFloat() * 800 -400;
			float y = 0;
			float z = random.nextFloat() * -600;
			allGrasses.add(new Entity(texturedGrass,new Vector3f(x,y,z),0,0,0,3));
			float x1 = random.nextFloat() * 800 -400;
			float y1 = 0;
			float z1 = random.nextFloat() * -600;
			allGrasses.add(new Entity(texturedGrass1,new Vector3f(x1,y1,z1),0,0,0,3));
		}
		
		List<Entity> allEntities = new ArrayList<Entity>();
		for (Entity entity : allTrees) {
			allEntities.add(entity);
		}
		for (Entity entity : allGrasses) {
			allEntities.add(entity);
		}
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			TreeEntity.increaseRotation(0, 1, 0);
			camera.move();
			//game logic
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain3);
			renderer.processTerrain(terrain4);
			renderer.processTerrain(terrain5);
			renderer.processTerrain(terrain6);			
			for (Entity entity : allEntities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
			
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		
	}

}