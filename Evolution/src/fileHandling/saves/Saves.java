package fileHandling.saves;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import fileHandling.log.Logger;
import terrains.Terrain;

public class Saves {
	
	static File logfile = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	static String savesPath = System.getenv("APPDATA")+"\\Evolution\\saves\\";

	public static void CreateDirectory(){
			try {
				if (Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves")) == false) {
					Files.createDirectories(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves")); //Create the saves directory if it doesn't already exist.
				}
				if (Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\flags and misc")) == false) {
					Files.createDirectories(Paths.get(System.getenv("APPDATA")+"\\Evolution\\flags and misc")); //If the Flags and misc directory doesn't exist then create it.
				}
			} catch (IOException e) {
				Logger.IOSevereErrorHandler(e, logfile); //If any errors are raised then report it to the logger (Logger.java, line 85).
			}
	}
	
	/*
	 * This method is used to create a world in which takes in no name.
	 */
	
	public static void NewWorld(List<Terrain> terrains){
		String worldName = "New World"; //Set the world name automatically to New World.
		while (Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName)) == true) {
			worldName = worldName + "-"; //While there is already a world with this name add a dash to the end of it (to avoid directory conflicts).
		}
		try {
			Files.createDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName)); //Create a directory with the world name.
		} catch (IOException e) {
			Logger.IOSevereErrorHandler(e, logfile); //If any Input/Output errors then write to the logger (Logger.java, line 85).
		}
		main(worldName); //Set up the internal directory with the world name. 
	}
	
	/*
	 * This method is used to create a world in which takes in a name.
	 */
	
	public static void NewWorld(String worldName,List<Terrain> terrains) {
		while (Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName)) == true) {
			worldName = worldName + "-"; //While there is already a world with this name add a dash to the end of it (to avoid directory conflicts).
		}
		try {
			Files.createDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName)); //Create a directory with the title of the world name.
		} catch (IOException e) {
			Logger.IOSevereErrorHandler(e, logfile); //If any Input/Output exception then write it to the logger (Logger.java, line 85).
		}
		main(worldName); //Set up the internals of the directory.
	}
	
	public static void main(String worldName) {
		try {
			Files.createDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps")); //Create a new directory to store the map information inside the world file
			FileInputStream heightMap = new FileInputStream("res//textures//world//heightMap.png"); //Create a file input stream for the height map
			FileInputStream worldMap = new FileInputStream("res//textures//world//worldMap.png"); //Create a file input stream for the world map
			FileInputStream seed = new FileInputStream(System.getenv("APPDATA")+"\\Evolution\\flags and misc\\SeedInfoFlag.flg"); //grab the seed with the file input stream.
			Files.copy(heightMap,Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\heightMap.png"),StandardCopyOption.REPLACE_EXISTING); //Copy the height map to the map directory.
			Files.copy(worldMap,Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\worldMap.png"),StandardCopyOption.REPLACE_EXISTING); //Copy the world map to the map directory.
			Files.copy(seed,Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\maps\\seed.flg"),StandardCopyOption.REPLACE_EXISTING); //Copy the seed to the map directory.
			Files.createDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+worldName+"\\Entities")); //Write the new directory of entities to the directory.
			
		} catch (IOException e) {
			Logger.IOSevereErrorHandler(e, logfile); //If any Input/Output exception then write to the logger (Logger.java, line 85).
		}
	}

}
