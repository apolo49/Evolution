package python;

import java.io.*;

import fileHandling.log.Logger;

/**
 * <p>
 * Class to execute Python files, the extension needs to be passed 
 * when passing the name to the method.
 * </p>
 * <p>
 * This uses the current working directory and the Python code located in the directory
 * to run the process in the command line.
 * </p>
 * @author Joe
 *
 */

public class PyExecuter {
	
	/**
	 * The method grabs the path to the file relative to the current path of the execution of the program.
	 * Then creates a new runtime environment and executes the Python file on the command-line executing the python.exe
	 * This is a bottleneck for the system as the system requires the Python version to be Python 3.7.3.2.
	 * <p>
	 * The method reads any inputs in the stream and errors in the stream and outputs them to the output console and
	 * reports any errors to the logger.
	 * </p>
	 * <p>The method sends any output to the output method</p>
	 * 
	 * 
	 * @param args
	 * 			-Arguments to pass to the command-line
	 * @param file
	 * 			-The file name and type extension to be run, relative to src\\python\\ directory.
	 * 
	 * @see #output(String)
	 * 
	 * @see Process java.lang.Runtime#exec(String command)
	 */
	
	public static void main(String args[], String file) {
		File file1 = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
        String s = null;
        String path = System.getProperty("user.dir").concat("\\src\\python\\").concat(file); //locates desired file to run
        String in = System.getenv("LOCALAPPDATA")+"\\Programs\\Python\\Python37-32\\python.exe ".concat(path); //locates python.exe and builds command to run file (reason only windows enabled)

        try {
            Process p = Runtime.getRuntime().exec(in); //executes the command in windows directly
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream())); //get input stream and save to buffer
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream())); //get error stream and save to buffer
            
            while ((s = stdInput.readLine()) != null) { //while loop for while there is an input stream
                System.out.println(s); //prints input stream
                output(s);
            }
            
            while ((s = stdError.readLine()) != null) { //while loop for while there is an error stream
    			Logger.main("[SEVERE] "+s,-1,file1);
            }
            
        }
        catch (IOException e) { //catches exception and stops the program to prevent breaks.
        	Logger.IOSevereErrorHandler(e, file1);
        }
    }
	
	/**
	 * @param s - the string that was output in the runtime of the file.
	 * @return the string that was output in the runtime of the file.
	 * 
	 * @deprecated since version 0.1.1A
	 */
	
	public static String output(String s) {
		return s;
	}
}
