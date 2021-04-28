package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.drop.*;

import java.net.SocketException;

public class DesktopLauncher {
	public static void main (String[] arg) throws SocketException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		try {
			String name = arg[0];
			new LwjglApplication(new Drop(name), config);
		} catch (ArrayIndexOutOfBoundsException e) {
			new LwjglApplication(new Drop(), config);
		}
	}
}


