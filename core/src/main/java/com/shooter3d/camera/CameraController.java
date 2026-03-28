package com.shooter3d.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.shooter3d.entities.Player;

public class CameraController {
    private PerspectiveCamera camera;
    private Player player;
    private float yaw = 0f;
    private float pitch = 20f;
    private float distance = 5f;
    private float sensitivity = 0.3f;
    
    private boolean rotating = false;
    private float lastX, lastY;
    
    public CameraController(PerspectiveCamera camera, Player player) {
        this.camera = camera;
        this.player = player;
        updateCameraPosition();
    }
    
    public void update(float delta) {
        // Поворот правой кнопкой мыши
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            if (!rotating) {
                rotating = true;
                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
            } else {
                float deltaX = Gdx.input.getX() - lastX;
                float deltaY = Gdx.input.getY() - lastY;
                
                yaw += deltaX * sensitivity;
                pitch += deltaY * sensitivity;
                pitch = MathUtils.clamp(pitch, -80f, 80f);
                
                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
                
                player.setYaw(yaw);
                updateCameraPosition();
            }
        } else {
            rotating = false;
        }
        
        // Приближение/отдаление
        float scroll = Gdx.input.getDeltaY() * 0.3f;
        if (scroll != 0) {
            distance += scroll;
            distance = MathUtils.clamp(distance, 2f, 15f);
            updateCameraPosition();
        }
    }
    
    private void updateCameraPosition() {
        float radYaw = (float) Math.toRadians(yaw);
        float radPitch = (float) Math.toRadians(pitch);
        
        Vector3 target = player.getPosition();
        target.y = 1f;
        
        float x = (float) (Math.cos(radPitch) * Math.cos(radYaw)) * distance;
        float y = (float) Math.sin(radPitch) * distance + target.y;
        float z = (float) (Math.cos(radPitch) * Math.sin(radYaw)) * distance;
        
        camera.position.set(target.x + x, y, target.z + z);
        camera.lookAt(target);
        camera.update();
    }
}
