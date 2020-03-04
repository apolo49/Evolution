package EngineTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import fileHandling.log.Logger;
import fileHandling.saves.Saves;
import guis.GUIRenderer;
import guis.GUITexture;
import python.PyExecuter;

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

public class Setup {

	private static String VersionNumber = "0.1.5A";
	
	public static String getVersionNumber() {
		return VersionNumber;
	}
	
	public static void main(String[] args) {
		File file = null; //Create a File type object and set equal to null.
		Saves.CreateDirectory(); //Generate the directories to store all flags, objects and saves (Saves.java, line 19).
		file = Logger.create(0); //Create the logger file and set the previous null file to this file (Logger.java, line 24).
		Logger.main("[INIT] Game started", 0, file); //write to the logger file that the game has started (Logger.java, line 58).
		PyExecuter.main(null, "hashtest.pyw"); //Start the Login Screen (PyExecuter.java, hashtest.pyw).
		BufferedReader LoggedIn; //Create a new buffered reader to read whether the user successfully logged in.
		try {
			LoggedIn = new BufferedReader(new FileReader("src//python//obj//LoggedIn.flg")); //Read what was stored in the logged in file.
			String Line; //String to store the results of the logged in file.
			Line = LoggedIn.readLine(); //write the results of the logged in file to the string.
			if (Line.contains("true")) {
				Logger.main("[HEALTHY] Logged in", -1, file); //Write to the logger that the user has logged in and continue with the program (Logger.java, line 58).
			}
			else {
				Logger.main("[HEALTHY // UNWANTED] Couldn't log in", -1, file); //Write to the logger that the user could not log in (Logger.java, line 58).
				System.exit(0); //Close the program healthily.
			}
		} catch (IOException e1) {
			Logger.IOSevereErrorHandler(e1, file); //If any Input / Output exceptions are raised catch them and write them to the logger (Logger.java, line 96).
		}
			List<String> WorldSettingsList = Menu.Open();
			
			DisplayManager.createDisplay(); //Create the display (DisplayManager.java, line 21).
			Logger.main("[HEALTHY] Display created", -1, file); //Write to the logger that the display has been created (Logger.java, line 58).
			
			Loader loader = new Loader(); //Make a new loader (loads different objects to be interpreted in the program) (loader.java, line 25).
			
			MasterRenderer renderer = new MasterRenderer(); //Make a new MasterRenderer (Used to render GUIs, Textures and terrain) (MasterRenderer.java, line 40).
			List<GUITexture> guis = new ArrayList<GUITexture>(); //Make a new Array that only takes in GUITexture objects.
			GUITexture gui = new GUITexture(loader.loadTexture("Evolution"),new Vector2f(0f,0.8f),new Vector2f(0.25f,0.5f)); //Make a GUITexture of the logo with new 2-Dimensional, floating point vectors (Loader.java, line 50; GUITexture.java, line 11).
			guis.add(gui); //Add the GUI to the array.
			GUIRenderer guiRenderer = new GUIRenderer(loader); //Make a new GUIRenderer using the loader (GUIRenderer.java, line 20)
			
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
/*
 * Okay with the Buddha.
 */