package com.shooter3d.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.shooter3d.GameMain;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Shooter3D - Snow Shelter");
        config.setWindowedMode(1280, 720);
        config.setForegroundFPS(120);
        config.useVsync(true);
        config.setResizable(true);
        new Lwjgl3Application(new GameMain(), config);
    }
}
