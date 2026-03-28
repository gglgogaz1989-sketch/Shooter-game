package com.shooter3d.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Player {
    private Model model;
    private ModelInstance instance;
    private Vector3 position;
    private float yaw;
    private float speed = 5f;
    
    public Player() {
        position = new Vector3(0, 0.5f, 0);
        yaw = 0;
        
        // Простая модель (куб)
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(0.6f, 1.2f, 0.6f,
            new Material(ColorAttribute.createDiffuse(Color.CYAN)),
            VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        
        instance = new ModelInstance(model);
        updateTransform();
    }
    
    public void update(float delta) {
        handleMovement(delta);
        updateTransform();
    }
    
    private void handleMovement(float delta) {
        float moveSpeed = speed * delta;
        float radYaw = (float) Math.toRadians(yaw);
        
        float forwardX = (float) Math.sin(radYaw);
        float forwardZ = (float) Math.cos(radYaw);
        float rightX = (float) Math.cos(radYaw);
        float rightZ = -(float) Math.sin(radYaw);
        
        Vector3 newPos = position.cpy();
        
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newPos.x += forwardX * moveSpeed;
            newPos.z += forwardZ * moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newPos.x -= forwardX * moveSpeed;
            newPos.z -= forwardZ * moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newPos.x += rightX * moveSpeed;
            newPos.z += rightZ * moveSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newPos.x -= rightX * moveSpeed;
            newPos.z -= rightZ * moveSpeed;
        }
        
        // Простая граница
        if (Math.abs(newPos.x) < 9f && Math.abs(newPos.z) < 9f) {
            position.set(newPos);
        }
    }
    
    private void updateTransform() {
        instance.transform.setToTranslation(position);
        instance.transform.rotate(Vector3.Y, yaw);
    }
    
    public void render(ModelBatch batch, Environment environment) {
        batch.render(instance, environment);
    }
    
    public Vector3 getPosition() {
        return position;
    }
    
    public float getYaw() {
        return yaw;
    }
    
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    public void dispose() {
        model.dispose();
    }
}
