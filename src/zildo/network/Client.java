package zildo.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import zildo.Zildo;
import zildo.fwk.engine.EngineZildo;
import zildo.fwk.opengl.OpenGLZildo;

/**
 * Client job:
 * -----------
 * -Read user movement
 * -Display the scene
 * 
 * @author tchegito
 *
 */
public class Client {

	EngineZildo engineZildo;
	OpenGLZildo glGestion;
	boolean awt;
	boolean done=false;
	
	InetAddress address;
	
	/**
	 * Client and server are on two differents PC.
	 */
	public Client(boolean p_awt) {
		// On cr�e les objets OpenGL
		glGestion=new OpenGLZildo(Zildo.fullScreen);
		engineZildo=new EngineZildo(glGestion);
		glGestion.setEngineZildo(engineZildo);
		
		awt=p_awt;
		try {
			address=InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new RuntimeException("Can't determine local host address");
		}
	}
	
	/**
	 * Same PC for client and server.
	 * @param p_engine
	 */
	public Client(EngineZildo p_engine, boolean p_awt) {
		awt=p_awt;
		if (p_awt) {
			glGestion=new OpenGLZildo();
		} else {
			glGestion=new OpenGLZildo(Zildo.fullScreen);
		}
		engineZildo=p_engine;
		glGestion.setEngineZildo(p_engine);
		p_engine.setOpenGLGestion(glGestion);
		if (!p_awt) {
			engineZildo.initializeClient(false);
		}
	}
	
	public void readKeyboard() {
		Keyboard.poll();
	}
	
	public void initGL() {
		try {
			glGestion.init();
		} catch (LWJGLException e) {
			throw new RuntimeException("Problem initializing ZildoRenderer !");
		}
		engineZildo.initializeClient(true);
	}
	
	public void render()
	{
		if (!awt) {
	        // Read keyboard
	        Keyboard.poll();
	
	        done=glGestion.mainloop();
		}
		
        // Display scene
        glGestion.render();
	}
	
	public void updateEngine() {
		
	}
	
	public void run() {
		
        try {
            while (!done) {
            	render();
            	Thread.sleep(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        cleanUp();
	}

	public void cleanUp() {
		glGestion.cleanUp();				
	}

	public InetAddress getAddress() {
		return address;
	}
}
