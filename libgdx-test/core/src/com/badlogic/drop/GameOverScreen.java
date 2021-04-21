package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class GameOverScreen implements Screen {

    final Drop game;
    int points;
    private Stage stage;
    private Label score;
    private int hoogte=430;
    private Label naam;
    private TextButton restart;

    public GameOverScreen(final Drop game, int points) {
        this.game = game;
        this.points = points;
        ArrayList<ArrayList<String>> playerid =Database.query(String.format("SELECT id FROM namen WHERE gebruikersnaam='%s'", game.screen.username));
        Database.query(String.format("INSERT INTO highscore VALUES (%s,%s)",points,playerid.get(0).get(0)));
        System.out.println(playerid.get(0).get(0));


        //weergeeft de scores
        ArrayList<ArrayList<String>> query = Database.query("SELECT score, playerID FROM highscore ORDER BY SCORE DESC LIMIT 10");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        for (ArrayList<String> s:query) {
            String stringscore = String.valueOf(s.get(0));
            String id = String.valueOf(s.get(1));
            ArrayList<ArrayList<String>> query2 =Database.query(String.format("SELECT gebruikersnaam FROM namen WHERE id=%s", id));
            naam = new Label(String.valueOf(query2),skin);
            naam.setPosition(300,hoogte);
            score = new Label(String.valueOf(stringscore),skin);
            score.setPosition(100,hoogte);
            stage.addActor(score);
            stage.addActor(naam);
            hoogte=hoogte-30;
        }
        restart = new TextButton("restart",skin);
        restart.setPosition(200,80);
        stage.addActor(restart);

        restart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbStartClicked();
            }
        });
    }

    public void tbStartClicked() {
        game.setScreen(new GameScreen(game));
    }

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
