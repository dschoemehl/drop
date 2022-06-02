package com.mygdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class Drop extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket;
	private Vector3 touchPos;
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	static final Rectangle screenSize = new Rectangle(0,0,800,400);
	static final int moveSpeed = 200;
	static final Rectangle imageSize = new Rectangle(0,0,64,64);

	@Override
	public void create () {
		//Load images for droplet and bucket
		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		//Load music and sound
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		//Start playback of rain music
		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenSize.width, screenSize.height);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = screenSize.width / 2 - imageSize.width / 2;
		bucket.y = 20;
		bucket.width = imageSize.width;
		bucket.height = imageSize.height;

		touchPos = new Vector3();

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render () {

		if(Gdx.input.isTouched()) {
			//Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - imageSize.width / 2;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucket.x -= moveSpeed * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucket.x += moveSpeed * Gdx.graphics.getDeltaTime();
		}
		if(bucket.x < 0) {
			bucket.x = 0;
		}
		if(bucket.x > screenSize.width - imageSize.width) {
			bucket.x = screenSize.width - imageSize.width;
		}

		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			spawnRaindrop();
		}

		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();){
			Rectangle raindrop = iter.next();
			raindrop.y -= moveSpeed * Gdx.graphics.getDeltaTime();
			if(raindrop.y + imageSize.width < 0){
				iter.remove();
			}
			else if (raindrop.overlaps(bucket)){
				dropSound.play();
				iter.remove();
			}
		}

		ScreenUtils.clear(0, 0, .2f, 1);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y );
		for(Rectangle raindrop: raindrops){
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		bucketImage.dispose();
		dropImage.dispose();
	}

	private void spawnRaindrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, screenSize.width- imageSize.width);
		raindrop.y = screenSize.height;
		raindrop.width = imageSize.width;
		raindrop.height = imageSize.height;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
}
