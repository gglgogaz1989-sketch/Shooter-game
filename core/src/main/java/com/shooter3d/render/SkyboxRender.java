package com.shooter3d.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class SkyboxRenderer {
    private Model model;
    private ModelInstance instance;
    private Texture texture;
    
    public SkyboxRenderer(String texturePath) {
        // Загружаем одну текстуру
        texture = new Texture(Gdx.files.internal(texturePath));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        
        // Создаём большой куб
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(100f, 100f, 100f,
            new Material(TextureAttribute.createDiffuse(texture)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        
        instance = new ModelInstance(model);
    }
    
    public void render(ModelBatch batch, Environment environment, PerspectiveCamera camera) {
        // Отключаем запись в глубину, чтобы небо всегда было на заднем плане
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        
        // Сохраняем позицию камеры
        Vector3 originalPos = camera.position.cpy();
        
        // Перемещаем камеру в центр для рендера неба
        camera.position.set(0, 0, 0);
        camera.update();
        
        batch.render(instance, environment);
        
        // Восстанавливаем камеру
        camera.position.set(originalPos);
        camera.update();
        
        // Включаем обратно тест глубины
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }
    
    public void dispose() {
        texture.dispose();
        model.dispose();
    }
}
