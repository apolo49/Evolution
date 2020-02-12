package python;

import java.io.*;

import fileHandling.log.Logger;


public class PyExecuter {
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
	
	public static String output(String s) {
		return s;
	}
}
