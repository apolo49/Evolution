package fileHandling.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Logger {

	static String logPath = System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt"; //A string representing the full path of the latest log.
	static List<String> Log = new ArrayList<String>(); //Array list of type string which stores the log to be added to the file
	
	public static File create(int tried){
		try {
			if (Files.isDirectory(Paths.get(System.getenv("APPDATA")+"\\Evolution\\logs")) == false) {
				Files.createDirectories(Paths.get(System.getenv("APPDATA")+"\\Evolution\\logs")); //If the log directory doesn't exist create the directory 
			}
		} catch (IOException e) {
			e.printStackTrace(); //If there is an error print out the error
		}
		if (tried == 0) {
			//If there is an existing latest log then
			if (Files.exists(Paths.get(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt")) == true) {
				DateFormat dateFormat = new SimpleDateFormat("ss-mm-HH dd-MM-yyyy"); //Create a date format in the given format of seconds, minutes, hours, days, month, year
				String date = dateFormat.format(new Date()); //Format the date format as the current date
				File f1 = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt"); //Create a file object that is the path to the latest log
				File f2 = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Old Log "+date+".txt"); //Create a file object that is the path of the log directory and "Old log "+ the date format + ".txt"
				f1.renameTo(f2); //Rename the old latest log to the old log
				try {
					Files.createFile(Paths.get(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt")); //Create the new latest log file
				} catch (IOException e) {
					e.printStackTrace(); //If there is a problem making the file, print the stacktrace to the console
				}
				int has_tried = 1; //Set has tried to 1
				create(has_tried); //Recurse itself with the new argument of has tried to 1
			}else {
				try {
					Files.createFile(Paths.get(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt")); //Create a file object of the latest log
				} catch (IOException e) {
					e.printStackTrace(); //If there is an error catch it and print the stacktrace to the console
				}
			}
		}
		File file = new File(logPath); //Create a new file object with the path to the latest log
		return file; //Return the file
	}
	
	public static void main(String e /*The string to be added to the log */,Integer rule /*the rule to be used (-1 is write to the file straight away, 0 is add to the array and await writing).*/, File file /*The file to be written to*/) {
		try {
			if (rule == -1) {
				Buildlog(e); //Send the string to the BuildLog method (Logger.java, line 74)
				log(file, Log); //Send the file and the log array to the log method (Logger.java, line 78)
				Log.clear(); //Clear the log array to be used again
			}else if(rule == 0) {
				Buildlog(e); //adds the string to the array
			}else {
				throw new IOException("Expected 0 or -1 got "+rule.toString()+"\n" + Thread.currentThread().getStackTrace()); //If neither -1 or 0 were put into the logger throw the IOException
			}
		} catch (IOException e2) {
			e2.printStackTrace(); //If any IOException is thrown catch it and print the stacktrace to the console
			System.exit(-1); // Close the system unhealthily
		}
	}
	
	public static void Buildlog(String exception) {
		Log.add(exception+"\n"); //Add the string to the log and add a newline after
	}
	
	public static void log(File file, List<String> Log) throws IOException {
		Path path = FileSystems.getDefault().getPath(logPath); //Get a path object of the current log path
		for (String string:Log) {
			byte[] strToBytes = string.getBytes(); //create a byte array object and convert the strings to a bytelike
			Files.write(path, strToBytes, StandardOpenOption.APPEND); //write the bytes to the latest log file
		}
	}
	
	public static void IOSevereErrorHandler(IOException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	public static void NullPointerSevereErrorHandler(NullPointerException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	public static void FileNotFoundSevereErrorHandler(FileNotFoundException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}

	public static void IOUnhealthyErrorHandler(IOException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	public static void NullPointerUnhealthyErrorHandler(NullPointerException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	public static void FileNotFoundUnhealthyErrorHandler(FileNotFoundException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	public static void UnhealthyException(Exception e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
}
