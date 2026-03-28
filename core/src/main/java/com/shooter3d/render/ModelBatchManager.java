package com.shooter3d.render;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class ModelBatchManager {
    private static ModelBatchManager instance;
    private ModelBatch modelBatch;
    
    private ModelBatchManager() {
        modelBatch = new ModelBatch();
    }
    
    public static ModelBatchManager getInstance() {
        if (instance == null) {
            instance = new ModelBatchManager();
        }
        return instance;
    }
    
    public ModelBatch getModelBatch() {
        return modelBatch;
    }
}
