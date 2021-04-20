package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    private Label.LabelStyle titleStyle;

    private Label lTitle;
    private TextField tfUsername;
    private Label lUsername;
    private TextButton tbStart;
    private Stage stage;
    public Skin skin;
    Sound menuClick;

    public String username;

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

    public MainMenuScreen(final Drop game) {
        this.game = game;

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
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return; else {
                }
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String result = new String(newData);
                input = result;
            }
        });

        menuClick = Gdx.audio.newSound(Gdx.files.internal("minecraft_click.mp3"));

        titleStyle = new Label.LabelStyle();
        titleStyle.font = new BitmapFont(Gdx.files.internal("font/font-title-export.fnt"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        //Maken onderdelen voor scherm
        lTitle = new Label("Temple Run",titleStyle);
        lTitle.setPosition(stage.getWidth()/2 - lTitle.getWidth()/2,350);
        lUsername = new Label("Enter username:", skin);
        lUsername.setPosition(stage.getWidth() / 2 - lUsername.getWidth()/2, stage.getHeight() / 2 - lUsername.getHeight()/2+50);
        tfUsername = new TextField("", skin);
        tfUsername.setSize(500, 60);
        tfUsername.setPosition(stage.getWidth() / 2 - tfUsername.getWidth()/2, stage.getHeight() / 2 - tfUsername.getHeight()/2);
        tbStart = new TextButton("Play", skin);
        tbStart.setSize(300, 60);
        tbStart.setPosition(stage.getWidth() / 2 - tbStart.getWidth()/2, stage.getHeight() / 2 - tbStart.getHeight()/2-70);

        //Toevoegen onderdelen aan scherm
        stage.addActor(lTitle);
        stage.addActor(tfUsername);
        stage.addActor(lUsername);
        stage.addActor(tbStart);

        //Luisteren naar knop
        tbStart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbStartClicked();
            }
        });
    }

    public void tbStartClicked() {
        menuClick.play();
        if (!tfUsername.getText().equals("")) {
            username = tfUsername.getText();
        } else {
            username = "Gastgebruiker";
        }
        Database.query(String.format("insert into namen (gebruikersnaam) Select '%s' Where not exists(select * from namen where gebruikersnaam='%s')", username, username));
        System.out.println("player: " + username);
        game.setScreen(new GameScreen(game));
        dispose();
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
