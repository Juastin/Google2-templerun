package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class MainMenuScreen implements Screen {
    final Drop game;
    public String username;

    private Label.LabelStyle titleStyle;
    private Label lTitle;
    private TextField tfUsername;
    private Label lUsername;
    private TextButton tbStart;
    private TextButton tbLeaderboard;
    private Stage stage;
    private Skin skin;

    Sound menuClick;

    //Connectie voor Arduino
    private long diff, start = System.currentTimeMillis();
    public String input = "";
    final SerialPort comPort = SerialPort.getCommPort("COM3");

    //Connectie voor Raspberry Pi
    int MAX_LEN = 2048;
    int localPortNum = 22;
    DatagramSocket mySocket;
    byte[] buffer;
    DatagramPacket packet;
    int sleep = 0;
    String previousinput = "";

    OrthographicCamera camera;
    private SpriteBatch batch;
    ShaderProgram shader;
//    Texture transition;
//    boolean doTrans;
//    TransitionRenders test;
//    Screen goToScreen;

    public MainMenuScreen(final Drop game, String name) {
        this.game = game;

        //transition setup
        batch = new SpriteBatch();
//        shader = new ShaderProgram(batch.getShader().getVertexShaderSource(), Gdx.files.internal("Fragment.fsh").readString());
//        transition = new Texture(Gdx.files.internal("trans4.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        if (!game.connectAttempted) {
            //Socket connectie maken via ssh met Raspberry Pi
            try {
                mySocket = new DatagramSocket(localPortNum);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            buffer = new byte[MAX_LEN];
            packet = new DatagramPacket(buffer, MAX_LEN);
            comPort.openPort();
            comPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    else {
                    }
                    byte[] newData = new byte[comPort.bytesAvailable()];
                    int numRead = comPort.readBytes(newData, newData.length);
                    String result = new String(newData);
                    input = result;
                }
            });
            game.connectAttempted = true;
        }

        titleStyle = new Label.LabelStyle();
        titleStyle.font = new BitmapFont(Gdx.files.internal("font/font-title-export.fnt"));
        menuClick = Gdx.audio.newSound(Gdx.files.internal("minecraft_click.mp3"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        //Maken onderdelen voor scherm
        lTitle = new Label("Temple Run",titleStyle);
        lUsername = new Label("Enter username:", skin);
        tbStart = new TextButton("Play", skin);
        tbLeaderboard = new TextButton("Leaderboard", skin);

        if (name != null) {
            tfUsername = new TextField(name, skin);
            tfUsername.setTouchable(Touchable.disabled);
        } else {
            tfUsername = new TextField("", skin);
        }

        //Positie en size voor de onderdelen
        tfUsername.setSize(500, 60);
        tbStart.setSize(300, 60);
        tbLeaderboard.setSize(300, 60);

        lTitle.setPosition(stage.getWidth()/2 - lTitle.getWidth()/2,350);
        lUsername.setPosition(stage.getWidth() / 2 - lUsername.getWidth()/2, stage.getHeight() / 2 - lUsername.getHeight()/2+50);
        tfUsername.setPosition(stage.getWidth() / 2 - tfUsername.getWidth()/2, stage.getHeight() / 2 - tfUsername.getHeight()/2);
        tbStart.setPosition(stage.getWidth() / 2 - tbStart.getWidth()/2, stage.getHeight() / 2 - tbStart.getHeight()/2-70);
        tbLeaderboard.setPosition(stage.getWidth() / 2 - tbStart.getWidth()/2, stage.getHeight() / 2 - tbStart.getHeight()/2-140);

        //Toevoegen onderdelen aan scherm
        stage.addActor(lTitle);
        stage.addActor(tfUsername);
        stage.addActor(lUsername);
        stage.addActor(tbStart);
        stage.addActor(tbLeaderboard);

        //Luisteren naar knop
        tbStart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbStartClicked();
            }
        });

        tbLeaderboard.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                menuClick.play();
                game.setScreen(new LeaderboardScreen(game));
//                doTrans = true;
//                test = new TransitionRenders();
//                goToScreen = new LeaderboardScreen(game);
            }
        });
    }

    public void tbStartClicked() {
        menuClick.play();
        if (!tfUsername.getText().equals("")) {
            game.screen.username = tfUsername.getText();
        } else {
            game.screen.username = "Gastgebruiker";
        }
        QueryRepository.insertName(game.screen.username);
        System.out.println("player: " + game.screen.username);
        game.setScreen(new GameScreen(game));
//        doTrans = true;
//        test= new TransitionRenders();
//        goToScreen = new GameScreen(game);
    }

    public void render(float delta) {
        ScreenUtils.clear(60/255f, 30/255f, 30/255f, 1);
        stage.act(delta);
        stage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
//        if (doTrans) {
//            test.transitionOut(batch, shader, transition);
//            if (test.time <= -1) {
//                dispose();
//                game.setScreen(goToScreen);
//            }
//        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        shader.dispose();
        batch.dispose();
        skin.dispose();
//        transition.dispose();
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
