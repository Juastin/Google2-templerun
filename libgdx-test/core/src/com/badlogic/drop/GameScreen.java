package com.badlogic.drop;

import java.io.IOException;
import java.util.Iterator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.graalvm.compiler.debug.CSVUtil;

public class GameScreen implements Screen {
    final Drop game;

    Texture dropImage;
    Texture bucketImage;
    Sound dropSound;
    Music rainMusic;
    Music stormTheme;
    Music setMusic;
    OrthographicCamera camera;
    Rectangle bucket;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    private boolean pauze=false;
    private Stage stage;
    private TextButton con;
    private Slider rwaarde;
    private Slider gwaarde;
    private Slider bwaarde;
    private Slider difficulty;
    private Label moeilijkheid;
    private Label rood;
    private Label groen;
    private Label blauw;


    public GameScreen(final Drop game) {
        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("drop.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        stormTheme = Gdx.audio.newMusic(Gdx.files.internal("Google2_-_Temple_Run_Original_Theme.mp3"));
        setMusic = stormTheme;
        setMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        spawnRaindrop();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

        con = new TextButton("Continue",skin);
        moeilijkheid = new Label("Difficulty",skin);
        moeilijkheid.setTouchable(Touchable.disabled);

        difficulty = new Slider(100,600,1,false,skin);
        rood = new Label("R",skin);
        groen = new Label("G",skin);
        blauw = new Label("B",skin);
        rood.setTouchable(Touchable.disabled);
        groen.setTouchable(Touchable.disabled);
        blauw.setTouchable(Touchable.disabled);

        difficulty.setWidth(moeilijkheid.getWidth()+50);
        rwaarde = new Slider(0,255,1,false,skin);
        gwaarde = new Slider(0,255,1,false,skin);
        bwaarde = new Slider(0,255,1,false,skin);



        con.setPosition(200,80);
        difficulty.setPosition(400,400);
        rwaarde.setPosition(200,400);
        gwaarde.setPosition(200,300);
        bwaarde.setPosition(200,200);

        moeilijkheid.setPosition((difficulty.getX()+difficulty.getWidth()/2)-moeilijkheid.getWidth()/2,415);
        rood.setPosition((rwaarde.getX()+rwaarde.getWidth()/2)-rood.getWidth()/2,410);
        groen.setPosition((gwaarde.getX()+gwaarde.getWidth()/2)-groen.getWidth()/2,310);
        blauw.setPosition((bwaarde.getX()+bwaarde.getWidth()/2)-blauw.getWidth()/2,210);

        difficulty.setValue(200);
        rwaarde.setValue(0);
        gwaarde.setValue(0);
        bwaarde.setValue(51);


        stage.addActor(difficulty);
        stage.addActor(moeilijkheid);
        stage.addActor(con);
        stage.addActor(rwaarde);
        stage.addActor(gwaarde);
        stage.addActor(bwaarde);
        stage.addActor(rood);
        stage.addActor(groen);
        stage.addActor(blauw);

        con.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                change();
            }
        });

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }


    @Override
    public void render(float delta) {
        //Ophalen button data van Raspberry Pi

        if (game.screen.sleep ==3) {

            try {
                game.screen.mySocket.setSoTimeout(1);
                game.screen.mySocket.receive(game.screen.packet);
                String message = new String(game.screen.buffer);
                game.screen.input = message;
            } catch (IOException ignored) {
            }
            game.screen.sleep = 0;
        } else {
            game.screen.input = game.screen.previousinput;
        }

        game.screen.sleep++;

        if(game.screen.input.contains("left")||game.screen.input.contains("right")){pauze=false;}


        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        ScreenUtils.clear(rwaarde.getValue()/255f, gwaarde.getValue()/255f, bwaarde.getValue()/255f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 10, 470);
        game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.batch.end();



        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }

        if(game.screen.input.contains("left") || game.screen.input.contains("A")) bucket.x -= 800 * Gdx.graphics.getDeltaTime();
        if(game.screen.input.contains("right") || game.screen.input.contains("D")) bucket.x += 800 * Gdx.graphics.getDeltaTime();
        if(game.screen.input.contains("middle") || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){pauze=true;}
        game.screen.previousinput = game.screen.input;

        game.screen.input = "";

        if(pauze){
            setMusic.pause();
            con.setVisible(true);
            rwaarde.setVisible(true);
            gwaarde.setVisible(true);
            bwaarde.setVisible(true);
            difficulty.setVisible(true);
            moeilijkheid.setVisible(true);
            rood.setVisible(true);
            groen.setVisible(true);
            blauw.setVisible(true);
            stage.act(delta);
            stage.draw();
        }

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if(!pauze) {
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){pauze=false;}
            setMusic.play();
            if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
                spawnRaindrop();

            // move the raindrops, remove any that are beneath the bottom edge of
            // the screen or that hit the bucket. In the later case we increase the
            // value our drops counter and add a sound effect.
            Iterator<Rectangle> iter = raindrops.iterator();
            while (iter.hasNext()) {
                Rectangle raindrop = iter.next();
                if (Gdx.graphics.getDeltaTime() > 0.3) {
                    System.out.println("niets");
                } else {
                    raindrop.y -= difficulty.getValue() * Gdx.graphics.getDeltaTime();
                }

                if (raindrop.y < 0) {
                    game.setScreen(new GameOverScreen(game, dropsGathered));
                    dispose();
                }
                if (raindrop.y + 64 < 0)
                    iter.remove();
                if (raindrop.overlaps(bucket)) {
                    dropsGathered++;
                    dropSound.play();
                    iter.remove();
                }
            }
        }
    }

    public void change(){
        pauze=false;
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        setMusic.play();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void hide() {
    }


    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        stormTheme.dispose();
        setMusic.dispose();
    }
}