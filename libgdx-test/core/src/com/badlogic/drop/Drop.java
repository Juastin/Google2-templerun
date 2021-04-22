package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Drop extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public MainMenuScreen screen;
    private String name;

    public Drop () {
        this.name = null;
    }
    public Drop (String name) {
        this.name = name;
    }

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(
                Gdx.files.internal( "font/font-export.fnt" ),
                Gdx.files.internal( "font/font-export.png" ),
                false ); // use libGDX's default Arial font
        screen = new MainMenuScreen(this, name);
        this.setScreen(screen);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}