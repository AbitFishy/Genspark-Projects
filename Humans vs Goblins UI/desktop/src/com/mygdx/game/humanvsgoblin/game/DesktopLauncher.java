package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.humanvsgoblin.game.Game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Humans vs Goblins");
		config.setWindowedMode(640,700);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new Game(), config);
	}
}
