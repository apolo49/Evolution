package RenderEngine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import fileHandling.log.Logger;

/**
 * Handles all operations to do with the created window in OpenGL and LWJGL.
 * All attributes and properties of the window are handled in this one file and will always be manipulated from within here
 * this is obeying the rules of OpenGL contexts.
 * 
 * @author Joe
 *
 */

public class DisplayManager {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta; //time taken to render previous frame from the current frame
	
	/**
	 * Creates display using openGL's display handler.
	 * Using OpenGL 3.2 with forward compatibility, and using a profile core.
	 * It gives the height and width of the display its dimensions using 1280*720 and an fps cap of 120.
	 * Also sets the title of the display.
	 * This also sets the last frame time to {@code getCurrentTime}.
	 * 
	 * Any LWJGLExceptions are handled and reported to the logger and the game is forced to crash.
	 * 
	 * @see #getCurrentTime()
	 */
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(),attribs);
			Display.setTitle("Evolution");
		} catch (LWJGLException e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Logger.main("[SEVERE]"+exceptionAsString,-1,new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt"));
			System.exit(-1);
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	/**
	 * This is used to update all objects on the display 
	 * and keep the display running.
	 * First it attempts to sync the display with the {@code FPS_CAP}
	 * (120 fps), then updates the display using {@code org.lwjgl.opengl.Display.update()}.
	 * It then sets current frame time to the current time
	 * as calculated by {@code getCurrentTime()}.
	 * The method proceeds by calculating the delta t of 
	 * the frame time and sets {@code delta} to this value
	 * It then sets {@code lastFrameTime} to {@code currentFrameTime}.
	 * 
	 * @see #getCurrentTime()
	 */
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	/**
	 * @return The time taken to render the current frame in seconds.
	 */
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * Destroys the display using org.lwgjl.opengl.Display.detroy()
	 */
	
	public static void closeDisplay() {
		
		Display.destroy();
		
	}
	/**
	 * grabs the current system time, multiplies it by 1000
	 * and then divides it by the resolution used by the system.
	 * <p>The method reads is:
	 * <pre>{@code
	 * sys.getTime()*1000/Sys.getTimerResolution}
	 * </pre>
	 * 
	 * @return The current system time in milliseconds
	 */
	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();  //time in milliseconds
	}
	
}
