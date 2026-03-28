package com.shooter3d.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class TextureLoader {
    
    public static Texture loadTexture(String path) {
        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            return texture;
        } catch (Exception e) {
            Gdx.app.log("TextureLoader", "Не удалось загрузить текстуру: " + path);
            return null;
        }
    }
    
    public static Texture loadTexture(String path, boolean repeat) {
        Texture texture = loadTexture(path);
        if (texture != null && repeat) {
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }
        return texture;
    }
}
