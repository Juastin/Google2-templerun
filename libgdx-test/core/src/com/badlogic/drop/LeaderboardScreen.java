package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class LeaderboardScreen implements Screen {

    final Drop game;
    private Stage stage;
    private Label score;
    private int hoogte=300;
    private Label naam;
    private TextButton Done;
    private int cijfer=1;
    private Label.LabelStyle titleStyle;


    public LeaderboardScreen(final Drop game) {
        this.game = game;

        titleStyle = new Label.LabelStyle();
        titleStyle.font = new BitmapFont(Gdx.files.internal("font/font-title-export.fnt"));


        //weergeeft de scores
        ArrayList<ArrayList<String>> query = QueryRepository.getTopTen();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));


        Label nummer = new Label("RANK",skin);
        Label gebruikersnaam = new Label("NAME",skin);
        Label punten = new Label("SCORE",skin);

        nummer.setPosition(190,330);
        gebruikersnaam.setPosition(355,330);
        punten.setPosition(530,330);

        stage.addActor(nummer);
        stage.addActor(gebruikersnaam);
        stage.addActor(punten);

        for (ArrayList<String> s:query) {
            String stringscore = String.valueOf(s.get(0));
            String id = String.valueOf(s.get(1));
            ArrayList<ArrayList<String>> query2 = QueryRepository.getUsername(Integer.parseInt(id));


            Label gameover = new Label("Leaderboard",titleStyle);
            gameover.setPosition(stage.getWidth() / 2 - gameover.getWidth()/2,370);
            naam = new Label(String.valueOf(query2.get(0).get(0)),skin);
            Label label = new Label(String.valueOf(cijfer+"."),skin);
            label.setPosition(200,hoogte);
            cijfer++;
            naam.setPosition(stage.getWidth() / 2 - naam.getWidth()/2,hoogte);
            score = new Label(String.valueOf(stringscore),skin);
            score.setPosition(550,hoogte);
            stage.addActor(score);
            stage.addActor(naam);
            stage.addActor(label);
            stage.addActor(gameover);

            hoogte=hoogte-25;
        }
        Done = new TextButton("Done",skin);
        Done.setPosition(stage.getWidth() / 2 - Done.getWidth()/2,10);
        stage.addActor(Done);

        Done.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbStartClicked();
                game.screen.menuClick.play();
            }
        });
    }

    public void tbStartClicked() {
        game.setScreen(new MainMenuScreen(game, game.name));
    }

    public void render(float delta) {
        ScreenUtils.clear(60/255f, 30/255f, 30/255f, 1);
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
        stage.dispose();
    }
}
