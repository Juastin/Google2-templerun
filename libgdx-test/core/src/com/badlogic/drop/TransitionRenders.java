package com.badlogic.drop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;


public class TransitionRenders {
    float time = 1;
    float speed;
    float addtime;

    public TransitionRenders() {
        speed = 0.02f;
    }

    public TransitionRenders(float speed) {
        this.speed = speed;
    }

    public void transitionOut(SpriteBatch batch, ShaderProgram shader, Texture transition) {
        addtime -= speed;
        batch.setShader(shader);
        batch.begin();
        time += addtime;
        shader.setUniformf("cutoff", time);
        batch.draw(transition, 0, 0, 800, 480);
        batch.end();
        batch.setShader(null);
    }

    public void transitionIn(SpriteBatch batch, ShaderProgram shader, Texture transition) {
        addtime += speed;
        batch.setShader(shader);
        batch.begin();

        time += addtime;
        System.out.println(time);
        shader.setUniformf("cutoff", time);
        batch.draw(transition, 0, 0, 800, 480);
        batch.end();
        batch.setShader(null);
    }

    public void reset() {
        if (time <= 0) {
            time = 1;
        } else {
            time = 0;
        }
    }
}
