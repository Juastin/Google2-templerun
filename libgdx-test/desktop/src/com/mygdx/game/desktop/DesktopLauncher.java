package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.drop.Drop;
import com.mygdx.game.MyGdxGame;

import java.net.SocketException;

public class DesktopLauncher {
	public static void main (String[] arg) throws SocketException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new Drop(), config);
	}
}


