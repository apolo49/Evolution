package EngineTest;

import java.io.File;
import java.math.BigInteger;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GUIRenderer;
import guis.GUITexture;
import fileHandling.log.Logger;
import terrains.Terrain;

public class Game {
	
	protected static File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	private static Loader loader = new Loader(); //Make a new loader (loads different objects to be interpreted in the program) (loader.java, line 25).
	private static MasterRenderer renderer = new MasterRenderer(); //Make a new MasterRenderer (Used to render GUIs, Textures and terrain) (MasterRenderer.java, line 40).
	private static GUIRenderer guiRenderer = new GUIRenderer(loader); //Make a new GUIRenderer using the loader (GUIRenderer.java, line 20).
	
	/**
	 * @return the master Renderer
	 */
	public static MasterRenderer getMasterRenderer() {
		return renderer;
	}
	
	/**
	 * @return the guiRenderer
	 */
	public static GUIRenderer getGuiRenderer() {
		return guiRenderer;
	}
	
	/**
	 * @return the loader
	 */
	public static Loader getLoader() {
		return loader;
	}
	
	public static void main(MasterRenderer renderer, Camera camera, List<Terrain> terrains,Player player,List<Entity> allEntities,Light light,GUIRenderer guiRenderer,List<GUITexture> guis, Loader loader ) {
		
		boolean gamePaused = false;
		
		while(!Display.isCloseRequested()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !gamePaused)
	            gamePaused = !gamePaused;
	        if(gamePaused) {
	        	Logger.main("[HEALTHY] Game Paused", 0, file);
	        	String Quit = Pause.main();
	            if (Quit.contains("0")) {
	            	gamePaused = false;
	            }else if (Quit.contains("1")) {
	            	List<String> WorldSettingsList = Menu.Open();
	            	try {
	    				if(WorldSettingsList.get(0).contains("0")) {
	    			 		CreateWorld.loadWorld(WorldSettingsList.get(1), new BigInteger(WorldSettingsList.get(2))); //If the NewWorldFlag string contains 0 then load a world with the given name and seed (CreateWorld.java, line 181).
	    		 		}else if(WorldSettingsList.get(0).contains("1")) {
	    		 			CreateWorld.createNewWorld(WorldSettingsList.get(1),new BigInteger(WorldSettingsList.get(2))); //If the NewWorldFlag string contains 1 then create a new world with the given name and seed (CreateWorld.java, line 34). 
	    		 		}else {
	    		 			Logger.main("[FATAL] Not 1 or 0 in NewWorldFlag \n[FATAL] Shutting down.", -1, file); //If the newWorldFlag string contains neither then catch it and write to the logger (Logger.java, line 58).
	    		 			System.exit(-1); //Close the program unhealthily.
	    		 		}
	    			}catch (NullPointerException e) {
	    				Logger.NullPointerSevereErrorHandler(e, file); //If this doesn't work and a nullPointer is thrown then catch it and send it to the logger. (Logger.java, line 93).
	    			}
	            }else{
	            	Logger.main("[SEVERE] Has not landed from pause menu correctly!", -1, file);
	            	Logger.main("[SEVERE] Expected 1 or 0 got "+Quit+" Instead", -1, file);
	            	Logger.main("[SEVERE] System Closing", -1, file);
	            	EndCleanup();
	            }
	        }
			camera.move();
			for(Terrain terrain1 : terrains) {
				if(terrain1.getX() <= player.getPosition().x) { 
					if(terrain1.getX() + Terrain.getSize() > player.getPosition().x) {
						if(terrain1.getZ() <= player.getPosition().z) {
							if(terrain1.getZ() + Terrain.getSize() > player.getPosition().z) {
								player.move(terrain1);
							}
						}
					}
				}
			}
			//game logic
			for(Terrain terrain1 : terrains) {
				renderer.processTerrain(terrain1);
			}
			for (Entity entity : allEntities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
			
		}
	}
	
	public static void EndCleanup() {
		guiRenderer.cleanUP(); //Clean up all rendered GUIs and remove them (GUIRenderer.java, line 47).
		Logger.main("[HEALTHY] Cleaned up GUI", 0, file); //Write to the logger that the GUI has been cleaned up (Logger.java, line 58).
		renderer.cleanUp(); //Clean up all rendered entities and remove them (MasterRenderer.java, line 92).
		Logger.main("[HEALTHY] Cleaned up Renderer", 0, file); //Write to the logger that the renderer has been cleared (Logger.java, line 58).
		loader.cleanUp(); //Clean up the loader and remove all VBOs, VAOs and textures (Loader.java, line 90).
		Logger.main("[HEALTHY] Cleaned up Loader", 0, file); //Write to the logger that the loader is clean (Logger.java, line 58).
		DisplayManager.closeDisplay(); //Close the display (DisplayManager.java, line 49).
		Logger.main("[HEALTHY] Game ended correctly", -1, file); //Write to the logger that the game has closed properly and write all stored lines to the logger (Logger.java, line 58).

	}
	
}
