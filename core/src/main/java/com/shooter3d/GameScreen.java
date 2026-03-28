package com.shooter3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.shooter3d.camera.CameraController;
import com.shooter3d.entities.Player;
import com.shooter3d.maps.World;
import com.shooter3d.render.SkyboxRenderer;

public class GameScreen implements Screen {
    private PerspectiveCamera camera;
    private CameraController cameraController;
    private ModelBatch modelBatch;
    private Environment environment;
    private World world;
    private Player player;
    private SkyboxRenderer skybox;
    
    public GameScreen(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }
    
    @Override
    public void show() {
        // Камера
        camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.1f;
        camera.far = 100f;
        
        // Игрок
        player = new Player();
        
        // Контроллер камеры
        cameraController = new CameraController(camera, player);
        
        // Окружение (свет)
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.35f, 0.35f, 0.4f, 1f));
        
        // Основной свет (солнце)
        DirectionalLight sunLight = new DirectionalLight();
        sunLight.set(0.9f, 0.85f, 0.8f, -0.8f, -1.2f, -0.5f);
        environment.add(sunLight);
        
        // Заполняющий свет снизу
        DirectionalLight fillLight = new DirectionalLight();
        fillLight.set(0.25f, 0.3f, 0.35f, 0f, -1f, 0f);
        environment.add(fillLight);
        
        // Контровой свет
        DirectionalLight backLight = new DirectionalLight();
        backLight.set(0.4f, 0.45f, 0.5f, 0.5f, 0.2f, -1f);
        environment.add(backLight);
        
        // Мир
        world = new World();
        
        // Небо (одна текстура на все стороны)
        skybox = new SkyboxRenderer("textures/skybox.png");
    }
    
    @Override
    public void render(float delta) {
        // Обновляем игрока
        player.update(delta);
        
        // Обновляем камеру
        cameraController.update(delta);
        
        // Рендерим небо (первым, чтобы было на заднем плане)
        skybox.render(modelBatch, environment, camera);
        
        // Рендерим мир и игрока
        modelBatch.begin(camera);
        
        world.render(modelBatch, environment);
        player.render(modelBatch, environment);
        
        modelBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        world.dispose();
        player.dispose();
        skybox.dispose();
    }
}
