package com.shooter3d.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.shooter3d.utils.ObjFixer;

public class ModelLoader {
    
    public Model loadModel(String path) {
        try {
            FileHandle originalFile = Gdx.files.internal(path);
            String content = originalFile.readString();
            
            // Исправляем запятые на точки
            String fixedContent = ObjFixer.fixCommas(content);
            
            // Создаём временный файл с исправленным содержимым
            FileHandle tempFile = Gdx.files.local("temp_fixed.obj");
            tempFile.writeString(fixedContent, false);
            
            // Загружаем исправленную модель
            ObjLoader loader = new ObjLoader();
            Model model = loader.loadModel(tempFile);
            
            // Удаляем временный файл
            tempFile.delete();
            
            return model;
            
        } catch (Exception e) {
            Gdx.app.log("ModelLoader", "Ошибка загрузки: " + e.getMessage());
            return null;
        }
    }
}
