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

public class GameOverScreen implements Screen {
    final Drop game;
    int points;
    private Label.LabelStyle titleStyle;
    private Skin skin;

    //Leaderboard variabelen
    private Stage leaderboardStage;
    private Label score;
    private Label naam;
    private int cijfer = 1;
    private int hoogte = 300;

    //Score variabelen
    private Stage scoreStage;

    private TextButton restart;
    private TextButton tbScoreLeaderboard;
    private TextButton mainMenu;

    //Initialiseren schermen met een int
    int LEADERBOARD_SCREEN = 0;
    int SCORE_SCREEN = 1;

    int SET_SCREEN = 1;

    public GameOverScreen(final Drop game, int points) {
        this.game = game;
        this.points = points;

        titleStyle = new Label.LabelStyle();
        titleStyle.font = new BitmapFont(Gdx.files.internal("font/font-title-export.fnt"));
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        ArrayList<ArrayList<String>> playerid = QueryRepository.selectidfromname(game.screen.username);

        try {
            QueryRepository.inserthighscore(points, playerid.get(0).get(0));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e);
        }

        //Inladen verschillende stages (default = scoreStage)
        leaderboardStage = new Stage();
        scoreStage = new Stage();
        drawLeaderboard();
        drawScoreboard();
    }

    public void render(float delta) {
        ScreenUtils.clear(60/255f, 30/255f, 30/255f, 1);
        switch (SET_SCREEN) {
            case 0:
                leaderboardStage.act(delta);
                leaderboardStage.draw();
                Gdx.input.setInputProcessor(leaderboardStage);
                break;
            case 1:
                scoreStage.act(delta);
                scoreStage.draw();
                Gdx.input.setInputProcessor(scoreStage);
                break;
        }
    }

    public void drawLeaderboard() {
        ArrayList<ArrayList<String>> query = QueryRepository.getTopTen();

        Label gameover = new Label("Gameover",titleStyle);
        Label nummer = new Label("RANK",skin);
        Label gebruikersnaam = new Label("NAME",skin);
        Label punten = new Label("SCORE",skin);
        restart = new TextButton("Restart",skin);
        tbScoreLeaderboard = new TextButton("Score", skin);
        mainMenu = new TextButton("Main menu", skin);

        restart.setWidth(240);
        tbScoreLeaderboard.setWidth(240);
        mainMenu.setWidth(240);

        gameover.setPosition(leaderboardStage.getWidth() / 2 - gameover.getWidth()/2,370);
        nummer.setPosition(190,330);
        gebruikersnaam.setPosition(355,330);
        punten.setPosition(530,330);
        restart.setPosition(20,10);
        tbScoreLeaderboard.setPosition(leaderboardStage.getWidth() / 2 - tbScoreLeaderboard.getWidth()/2, 10);
        mainMenu.setPosition(leaderboardStage.getWidth() - mainMenu.getWidth() - 20, 10);

        leaderboardStage.addActor(gameover);
        leaderboardStage.addActor(nummer);
        leaderboardStage.addActor(gebruikersnaam);
        leaderboardStage.addActor(punten);
        leaderboardStage.addActor(restart);
        leaderboardStage.addActor(tbScoreLeaderboard);
        leaderboardStage.addActor(mainMenu);

        //Loopen door de top 10 lijst
        for (ArrayList<String> s:query) {
            String stringscore = String.valueOf(s.get(0));
            String id = String.valueOf(s.get(1));
            ArrayList<ArrayList<String>> query2 = QueryRepository.getUsername(Integer.parseInt(id));

            naam = new Label(String.valueOf(query2.get(0).get(0)),skin);
            Label label = new Label(String.valueOf(cijfer+"."),skin);
            label.setPosition(200,hoogte);
            cijfer++;
            naam.setPosition(leaderboardStage.getWidth() / 2 - naam.getWidth()/2,hoogte);
            score = new Label(String.valueOf(stringscore),skin);

            score.setPosition(550,hoogte);
            leaderboardStage.addActor(score);
            leaderboardStage.addActor(naam);
            leaderboardStage.addActor(label);

            hoogte=hoogte-25;
        }

        tbScoreLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SET_SCREEN = SCORE_SCREEN;
                game.screen.menuClick.play();
            }
        });

        restart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbRestartClicked();
            }
        });

        mainMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbMainMenuClicked();
            }
        });
    }

    public void drawScoreboard() {
        ArrayList<ArrayList<String>> playerid = QueryRepository.selectidfromname(game.screen.username);
        ArrayList<ArrayList<String>> getPersonalBestScore = QueryRepository.getPeronalBest(Integer.parseInt(playerid.get(0).get(0)));

        Label gameover = new Label("Gameover",titleStyle);
        Label gebruikersnaam = new Label("Your score",skin);
        score = new Label(String.valueOf(points), skin);
        Label personalBest = new Label("Personal best", skin);
        Label personalBestScore = new Label(getPersonalBestScore.get(0).get(0), skin);
        restart = new TextButton("Restart",skin);
        tbScoreLeaderboard = new TextButton("Leaderboard", skin);
        mainMenu = new TextButton("Main menu", skin);

        restart.setWidth(240);
        tbScoreLeaderboard.setWidth(240);
        mainMenu.setWidth(240);

        gameover.setPosition(scoreStage.getWidth() / 2 - gameover.getWidth()/2,370);
        gebruikersnaam.setPosition(scoreStage.getWidth() / 2 - gebruikersnaam.getWidth()/2,300);
        score.setPosition(scoreStage.getWidth() / 2 - score.getWidth()/2,270);
        personalBest.setPosition(scoreStage.getWidth() / 2 - personalBest.getWidth()/2,200);
        personalBestScore.setPosition(scoreStage.getWidth() / 2 - personalBestScore.getWidth()/2,170);
        restart.setPosition(20,10);
        tbScoreLeaderboard.setPosition(scoreStage.getWidth() / 2 - tbScoreLeaderboard.getWidth()/2, 10);
        mainMenu.setPosition(scoreStage.getWidth() - mainMenu.getWidth() - 20, 10);

        scoreStage.addActor(gameover);
        scoreStage.addActor(gebruikersnaam);
        scoreStage.addActor(score);
        scoreStage.addActor(personalBest);
        scoreStage.addActor(personalBestScore);
        scoreStage.addActor(restart);
        scoreStage.addActor(tbScoreLeaderboard);
        scoreStage.addActor(mainMenu);

        tbScoreLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SET_SCREEN = LEADERBOARD_SCREEN;
                game.screen.menuClick.play();
            }
        });
        restart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbRestartClicked();
            }
        });

        mainMenu.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbMainMenuClicked();
            }
        });
    }

    public void tbRestartClicked() {
        dispose();
        game.setScreen(new GameScreen(game));
        game.screen.menuClick.play();
    }

    public void tbMainMenuClicked() {
        dispose();
        game.setScreen(new MainMenuScreen(game, game.name));
        game.screen.menuClick.play();
    }

    @Override
    public void dispose() {
        leaderboardStage.dispose();
        scoreStage.dispose();
        skin.dispose();
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
}
