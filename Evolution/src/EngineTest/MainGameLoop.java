package EngineTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

import toolbox.Random;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import guis.GUIRenderer;
import guis.GUITexture;
import fileHandling.log.Logger;
import fileHandling.saves.Saves;
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

public class MainGameLoop {

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
			Logger.IOSevereErrorHandler(e1, file); //If any Input / Output exceptions are raised catch them and write them to the logger (Logger.java, line 85).
		}
		
		Logger.main("[HEALTHY] Menu Opened", 0, file); //Write to the logger that the main menu has opened (Logger.java, line 58).
		PyExecuter.main(null, "Menu.py"); //Use the PyExecuter to run the main menu for the game (PyExecuter.java, Menu.py).
		BufferedReader World; //Make a new buffered reader called world to read which world has been called for.
		BufferedReader Continue; //Make a new buffered reader called continue to read whether the user has quit the game or not. (quit game is 1 and continue is 0).
		BufferedReader Seed; //Make a new buffered reader called seed to read which seed will be used in generation.
		BufferedReader NewWorld; //Make a new buffered reader called new world to read whether the user is generating a new world or an existing world (new world is 1 and old world is 0).
		try {
			Continue = new BufferedReader(new FileReader(System.getenv("APPDATA")+"\\Evolution\\flags and misc\\QuitFlag.flg")); //Read the file with the buffered reader.
			String End; //Create a new string to store whether the user has quit or not.
			End = Continue.readLine(); //Store the contents of the file to the string.
			Continue.close(); //Close the Buffered Reader.
			if(!End.contains("0")) {
				Logger.main("[UNKNOWN] Error or Player chose to end the game", -1, file); //Write to the logger that the user has ended the game or an unknown error has occurred (Logger.java, line 58).
				System.exit(0); //Close the system healthily.
			}
			
			World = new BufferedReader(new FileReader(System.getenv("APPDATA")+"\\Evolution\\flags and misc\\chosenWorldFlag.flg")); //Read the contents of the file (the file with the name of the world) with the buffered reader.
			String CurrentWorld; //Create a new string to store the results of the file to.
			CurrentWorld = World.readLine(); //Store the current selected world inside the buffered reader to the string.
			World.close(); //Close the Buffered reader.

			Seed = new BufferedReader(new FileReader(System.getenv("APPDATA")+"\\Evolution\\flags and misc\\SeedInfoFlag.flg")); //Read the contents of the seed file with the buffered reader.
			String CurrentSeed; //Make a string to store the results of the file.
			CurrentSeed = Seed.readLine(); //Write the buffered reader to the string.
			Seed.close(); //Close the buffered reader.
			Random random = new Random(); //Make a new RNG for the seed (Random.java, line 48).
			BigInteger seed = random.nextBigInteger(); //Generate a new random BigInteger (Random.java, line 80).
			if (CurrentSeed == "" | CurrentSeed.contains("null")) {
				seed = random.nextBigInteger(); //If the seed string is empty or contains null make a new random seed of the type BigInteger.
			}else {
				try {
					seed = new BigInteger(CurrentSeed); //Otherwise try and turn the seed into a BigInteger object type.
				}catch (NumberFormatException e) {
					//If a number format exception is thrown, catch it.
					StringWriter sw = new StringWriter(); //Make a new StringWriter.
					e.printStackTrace(new PrintWriter(sw)); //Set the stack trace to a PrintWriter with a StringWriter inside so it stores the exception.
					String exceptionAsString = sw.toString(); //Make the string writer write the results to a string.
					Logger.main(exceptionAsString,-1,file); //Send the result to the logger to be written (Logger.java, line 58).
					System.exit(-1); //Close the program unhealthily.
				}
			}
			
			NewWorld = new BufferedReader(new FileReader(System.getenv("APPDATA")+"\\Evolution\\flags and misc\\NewWorldFlag.flg")); //Read the NewWorld file with the buffered reader.
			String NewWorldFlag; //Create a new String to store the contents of the file in.
			NewWorldFlag = NewWorld.readLine(); //Write the contents of the file to the string.
			NewWorld.close(); //Close the new World file
			
			DisplayManager.createDisplay(); //Create the display (DisplayManager.java, line 21).
			Logger.main("[HEALTHY] Display created", -1, file); //Write to the logger that the display has been created (Logger.java, line 58).
			
			Loader loader = new Loader(); //Make a new loader (loads different objects to be interpreted in the program) (loader.java, line 25).
			
			MasterRenderer renderer = new MasterRenderer(); //Make a new MasterRenderer (Used to render GUIs, Textures and terrain) (MasterRenderer.java, line 40).
			List<GUITexture> guis = new ArrayList<GUITexture>(); //Make a new Array that only takes in GUITexture objects.
			GUITexture gui = new GUITexture(loader.loadTexture("Evolution"),new Vector2f(0f,0.8f),new Vector2f(0.25f,0.5f)); //Make a GUITexture of the logo with new 2-Dimensional, floating point vectors (Loader.java, line 50; GUITexture.java, line 11).
			guis.add(gui); //Add the GUI to the array.
			GUIRenderer guiRenderer = new GUIRenderer(loader); //Make a new GUIRenderer using the loader (GUIRenderer.java, line 20).
			
			try {
				if(NewWorldFlag.contains("0")) {
			 		CreateWorld.loadWorld(CurrentWorld,seed); //If the NewWorldFlag string contains 0 then load a world with the given name and seed (CreateWorld.java, line 181).
		 		}else if(NewWorldFlag.contains("1")) {
		 			CreateWorld.createNewWorld(CurrentWorld,seed); //If the NewWorldFlag string contains 1 then create a new world with the given name and seed (CreateWorld.java, line 34). 
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
			
		} catch (IOException e1) {
			Logger.IOSevereErrorHandler(e1, file); //If any unhandled Input/Output Exceptions happened they are caught and handled with severe level (Logger.java, line 85).
		}
		
		
	}

}
/*
 * Okay with the Buddha.
 */