package com.shooter3d;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.shooter3d.render.ModelBatchManager;

public class GameMain extends Game {
    private ModelBatch modelBatch;
    
    @Override
    public void create() {
        modelBatch = ModelBatchManager.getInstance().getModelBatch();
        setScreen(new GameScreen(modelBatch));
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1f, true);
        super.render();
    }
    
    @Override
    public void dispose() {
        modelBatch.dispose();
        if (getScreen() != null) getScreen().dispose();
    }
}
