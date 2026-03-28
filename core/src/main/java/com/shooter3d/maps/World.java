package com.shooter3d.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.shooter3d.loaders.ModelLoader;

public class World {
    private Model model;
    private ModelInstance instance;
    
    public World() {
        // Пытаемся загрузить OBJ модель
        ModelLoader loader = new ModelLoader();
        model = loader.loadModel("models/box_map_test.obj");
        
        if (model == null) {
            // Если не загрузилось, создаём пол
            Gdx.app.log("World", "Загрузка OBJ не удалась, создаю пол");
            ModelBuilder builder = new ModelBuilder();
            model = builder.createBox(20f, 0.1f, 20f,
                new Material(ColorAttribute.createDiffuse(Color.GRAY)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        }
        
        instance = new ModelInstance(model);
        
        // Настраиваем материалы для теней
        for (Material material : model.materials) {
            material.set(ColorAttribute.createSpecular(Color.WHITE));
        }
    }
    
    public void render(ModelBatch batch, Environment environment) {
        batch.render(instance, environment);
    }
    
    public void dispose() {
        if (model != null) model.dispose();
    }
}
