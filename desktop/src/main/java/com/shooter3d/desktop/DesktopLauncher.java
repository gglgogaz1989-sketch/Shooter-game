package com.shooter3d.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.shooter3d.GameMain;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Shooter3D - Snow Shelter");
        config.setWindowedMode(800, 800);
        config.setResizable(false);
        config.setForegroundFPS(90);
        config.useVsync(true);
        
        // Одна иконка для десктопа
        config.setWindowIcon("icon.png");
        
        new Lwjgl3Application(new GameMain(), config);
    }
}
