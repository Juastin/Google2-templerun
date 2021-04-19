package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class MainMenuScreen implements Screen {
    final Drop game;

    private TextField tfUsername;
    private Label lUsername;
    private TextButton tbStart;
    private Stage stage;

    private String username;

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

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        lUsername = new Label("Enter username:", skin);
        tfUsername = new TextField("", skin);
        tbStart = new TextButton("Play", skin);

        lUsername.setPosition(stage.getWidth() / 2 - lUsername.getWidth()/2, stage.getHeight() / 2 - lUsername.getHeight()/2+50);
        tfUsername.setSize(500, 60);
        tfUsername.setPosition(stage.getWidth() / 2 - tfUsername.getWidth()/2, stage.getHeight() / 2 - tfUsername.getHeight()/2);
        tbStart.setSize(300, 60);
        tbStart.setPosition(stage.getWidth() / 2 - tbStart.getWidth()/2, stage.getHeight() / 2 - tbStart.getHeight()/2-70);

        tbStart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                tbStartClicked();
            }
        });

        stage.addActor(tfUsername);
        stage.addActor(lUsername);
        stage.addActor(tbStart);
    }

    public void tbStartClicked() {
        String username = tfUsername.getText();
        game.setScreen(new GameScreen(game));
        dispose();
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        ScreenUtils.clear(94/255f, 51/255f, 30/255f, 1);

//        camera.update();
//        game.batch.setProjectionMatrix(camera.combined);

//        game.batch.begin();
//        game.font.draw(game.batch, "Welcome to Drop!!! ", 100, 150);
//        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
//        game.batch.end();

        stage.act(delta);
        stage.draw();

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
    //...Rest of class omitted for succinctness.

}
