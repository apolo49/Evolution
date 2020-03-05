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

/**
 * This class handles the creation and handling of the log file.
 * This includes:
 * <ul>
 * 		<li>The creation of the new latest log file to be written to.</li>
 * 		<li>The renaming of the old latest log to the current system time when it became a redundant log.</li>
 * 		<li>Creating the log directory.</li>
 * 		<li>Retrying to make the log again if log creation failed in a recursive manner.</li>
 * 		<li>Writing all errors to the log and any standard procedures in the process.</li>
 * 		<li>Handling any and all errors in the program and giving the right severity to the log.</li>
 * 		<li>Converting any exceptions to strings.</li>
 * </ul>
 * @author Joe
 *
 */

public class Logger {
	
	/**
	 * The path to the latest log
	 */
	
	static String logPath = System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt"; //A string representing the full path of the latest log.
	
	/**
	 * The array to that includes all current processes to be written to the log.
	 */
	
	static List<String> Log = new ArrayList<String>(); //Array list of type string which stores the log to be added to the file
	
	/**
	 * Method to create the files and directory used by the logs, including renaming the
	 * old latest log to the current date and time where it became an old log.
	 * 
	 * @param tried 
	 * 			-The amount of times the recursion has been attempted
	 * @return The log file to be used.
	 */
	
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
	
	/**
	 * This method creates the log to be used and sends it to the the {@code buildlog} method.
	 * If the rule is {@code int -1} then the {@code log(File, ArrayList<String>} method is called and the
	 * {@code ArrayList<String>} {@code Log} is cleared using the built in {ArrayList<>.clear()} method.
	 * 
	 * @param e 
	 * 			-The string to be added to the log
	 * @param rule
	 * 			-the rule to be used (-1 is write to the file straight away, 0 is add to the array and await writing).
	 * @param file
	 * 			-The file to be written to.
	 */
	
	public static void main(String e, Integer rule , File file) {
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
	
	/**
	 * This method adds the exception string is added to the {@code ArrayLog<String>}
	 * {@code log}. 
	 * 
	 * @param exception
	 * 			-The string to be added to the log.
	 */
	
	private static void Buildlog(String exception) {
		Log.add(exception+"\n"); //Add the string to the log and add a newline after
	}
	
	/**
	 * Gets the Path object linked to the {@code String} {@code logpath} variable.
	 * This then loops through the {@code ArrayList<String>} {@code Log} and converts each
	 * {@code String} to a byte array and write the bytes to the latest log file.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * @param Log
	 * 			-The Log Array.
	 * @throws IOException
	 * 			Failure to access the file to be written to, or failure to write to the file.
	 */
	
	public static void log(File file, List<String> Log) throws IOException {
		Path path = FileSystems.getDefault().getPath(logPath); //Get a path object of the current log path
		for (String string:Log) {
			byte[] strToBytes = string.getBytes(); //create a byte array object and convert the strings to a bytelike
			Files.write(path, strToBytes, StandardOpenOption.APPEND); //write the bytes to the latest log file
		}
	}
	
	/*
	 * The next several functions are essentially the same functions reporting events:
	 * 		of differing severity
	 * 		of different exception types
	 * This is because java does not treat each different type of exception as the same object and cannot be passed to a function that requires a different exception,
	 * each exception therefore has to be handled in a separate method and to reduce redundancy are all handled here in the logger.
	 */
	
	/**
	 * The method to handle IOException that are too severe for the program to continue with.
	 * <p>Takes in a raw {@code IOException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then closes the system.
	 * 
	 * @param e
	 * 			-The IOException to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #IOUnhealthyErrorHandler(IOException, File)
	 * @see #FileNotFoundSevereErrorHandler(FileNotFoundException, File)
	 * @see #NullPointerSevereErrorHandler(NullPointerException, File)
	 * @see #StackOverflowErrorHandler(StackOverflowError, File)
	 */
	
	public static void IOSevereErrorHandler(IOException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	/**
	 * The method to handle NullPointerExceptions that are too severe for the program to continue with.
	 * <p>Takes in a raw {@code NullPointerException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then closes the system.
	 * 
	 * @param e
	 * 			-The NullPointerSevereErrorHandler to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #NullPointerUnhealthyErrorHandler(NullPointerException, File)
	 * @see #IOSevereErrorHandler(IOException, File)
	 * @see #FileNotFoundSevereErrorHandler(FileNotFoundException, File)
	 * @see #StackOverflowErrorHandler(StackOverflowError, File)
	 */
	
	public static void NullPointerSevereErrorHandler(NullPointerException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	/**
	 * The method to handle FileNotFoundExceptions that are too severe for the program to continue with.
	 * <p>Takes in a raw {@code FileNotFoundException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then closes the system.
	 * 
	 * @param e
	 * 			-The FileNotFoundException to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #FileNotFoundUnhealthyErrorHandler(FileNotFoundException, File)
	 * @see #IOSevereErrorHandler(IOException, File)
	 * @see #NullPointerSevereErrorHandler(NullPointerException, File)
	 * @see #StackOverflowErrorHandler(StackOverflowError, File)
	 */
	
	public static void FileNotFoundSevereErrorHandler(FileNotFoundException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	/**
	 * The method to handle Stack Overflow Errors.
	 * <p>Takes in a raw {@code StackOverflowError} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then closes the system.
	 * 
	 * @param e
	 * 			-The StackOverflowError to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #IOSevereErrorHandler(IOException, File)
	 * @see #NullPointerSevereErrorHandler(NullPointerException, File)
	 * @see #FileNotFoundSevereErrorHandler(FileNotFoundException, File)
	 */
	
	public static void StackOverflowErrorHandler(StackOverflowError e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[SEVERE]"+exceptionAsString,-1,file); //Make it a severe error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}

	/**
	 * The method to handle IOException that are unhealthy but not too severe for the program to continue with.
	 * <p>Takes in a raw {@code IOException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then continues.
	 * 
	 * @param e
	 * 			-The IOException to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #IOSevereErrorHandler(IOException, File)
	 * @see #NullPointerUnhealthyErrorHandler(NullPointerException, File)
	 * @see #FileNotFoundUnhealthyErrorHandler(FileNotFoundException, File)
	 * @see #UnhealthyException(Exception, File)
	 */
	
	public static void IOUnhealthyErrorHandler(IOException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
	}
	
	/**
	 * The method to handle NullPointerException that are unhealthy but not too severe for the program to continue with.
	 * <p>Takes in a raw {@code NullPointerException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then continues.
	 * 
	 * @param e
	 * 			-The NullPointerException to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #NullPointerSevereErrorHandler(NullPointerException, File)
	 * @see #IOUnhealthyErrorHandler(IOException, File)
	 * @see #FileNotFoundUnhealthyErrorHandler(FileNotFoundException, File)
	 * @see #UnhealthyException(Exception, File)
	 */
	
	public static void NullPointerUnhealthyErrorHandler(NullPointerException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
	
	/**
	 * The method to handle FileNotFoundException that are unhealthy but not too severe for the program to continue with.
	 * <p>Takes in a raw {@code FileNotFoundException} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then continues.
	 * 
	 * @param e
	 * 			-The FileNotFoundException to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #FileNotFoundSevereErrorHandler(FileNotFoundException, File)
	 * @see #IOUnhealthyErrorHandler(IOException, File)
	 * @see #NullPointerUnhealthyErrorHandler(NullPointerException, File)
	 * @see #UnhealthyException(Exception, File)
	 */
	
	public static void FileNotFoundUnhealthyErrorHandler(FileNotFoundException e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
	}
	
	/**
	 * The method to handle an Exception that are unhealthy but not too severe for the program to continue with.
	 * <p>Takes in a raw {@code Exception} and uses a {@code PrintWriter} and {@code StringWriter} 
	 * to convert the exception to the string and uses the method {@code main} method and then continues.
	 * 
	 * @param e
	 * 			-The Exception to be added to the log.
	 * 
	 * @param file
	 * 			-The file to be written to.
	 * 
	 * @see #main
	 * @see #FileNotFoundSevereErrorHandler(FileNotFoundException, File)
	 * @see #IOUnhealthyErrorHandler(IOException, File)
	 * @see #NullPointerUnhealthyErrorHandler(NullPointerException, File)
	 * @see #FileNotFoundUnhealthyErrorHandler(FileNotFoundException, File)
	 */
	
	public static void UnhealthyException(Exception e, File file) {
		StringWriter sw = new StringWriter(); //Create a new string writer
		e.printStackTrace(new PrintWriter(sw)); //Get the stacktrace and put it to the string writer
		String exceptionAsString = sw.toString(); //Make the string writer write to a string
		main("[UNHEALTHY]"+exceptionAsString,-1,file); //Make it an unhealthy error and send to the main method (Logger.java, line 59)
		System.exit(-1); //Close the program unhealthily
	}
}
