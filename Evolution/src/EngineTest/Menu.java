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

import fileHandling.log.Logger;
import python.PyExecuter;
import toolbox.Random;

public class Menu {

	private static File file = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	private static List<String> WorldSettingsList = new ArrayList<String>();
	
	/**
	 * A function that opens the menu and creates all the buffered readers
	 * for any and all files to be read that are relevant in creating worlds
	 * and/or ending/continuing the program from this point.
	 * This includes grabbing the seed and world name.
	 * 
	 * @return List of World settings (Base state is a null list) ({@code WorldSettingsList})
	 */
	
	public static List<String> Open(){
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
			
			Random random = new Random(); //Make a new RNG for the seed (Random.java, line 68).
			BigInteger seed = random.nextBigInteger(); //Generate a new random BigInteger (Random.java, line 190).
			if (CurrentSeed == "" | CurrentSeed.contains("null")) {
				seed = random.nextBigInteger(); //If the seed string is empty or contains null make a new random seed of the type BigInteger (random.java, line 190).
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

			WorldSettingsList.add(NewWorldFlag); //Add the new world flag to the list to passed back to get handled
			WorldSettingsList.add(CurrentWorld); //Add the current world name to the list to be passed back to get handled
			WorldSettingsList.add(seed.toString()); //Add the seed as a string to the list to be passed back to get handled
			return WorldSettingsList; //return the list
			
		}catch (IOException e1) {
			Logger.IOSevereErrorHandler(e1, file); //If any Input / Output exceptions are raised catch them and write them to the logger (Logger.java, line 96).
		}
		return WorldSettingsList; //return the empty list (base state)
	}
}
