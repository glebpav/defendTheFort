package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch; // ссылка на объект, отвечающий за вывод изображений
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;

	Texture[] imgMosq = new Texture[11]; // ссылка на текстуры (картинки)
	Texture imgBackGround; // фон

	Sound[] sndMosq = new Sound[4];
	Music sndMusic;

	public static int scrWidth = 1280;
	public static int scrHeight = 720;

	// создание массива ссылок на объекты
	Mosquito[] mosq = new Mosquito[15];
	int kills = 0;
	long timeStart, timeCurrent;

	boolean gameOver = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch(); // создаём объект, отвечающий за вывод изображений
		camera = new OrthographicCamera();
		camera.setToOrtho(false, scrWidth, scrHeight);
		touch = new Vector3();

		createFont();

		// создаём объекты изображений
		for(int i=0; i<imgMosq.length; i++){
			imgMosq[i] = new Texture("mosq"+i+".png");
		}
		imgBackGround = new Texture("moscowcity.jpg");

		// создаём объекты звуков
		for(int i=0; i<sndMosq.length; i++) {
			sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("mos"+i+".mp3"));
		}
		sndMusic = Gdx.audio.newMusic(Gdx.files.internal("smeshariki.mp3"));
		sndMusic.setLooping(true);
		//sndMusic.play();

		// создание объектов комаров
		for(int i=0; i<mosq.length; i++) {
			mosq[i] = new Mosquito();
		}

		timeStart = TimeUtils.millis();
	}

	void createFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("mr_countryhouse.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 50;
		parameter.color = Color.CORAL;
		parameter.borderWidth = 1;
		parameter.borderColor = Color.BLACK;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	String timeToString(long time){
		String min = "" + time/1000/60/10 + time/1000/60%10;
		String sec = "" + time/1000%60/10 + time/1000%60%10;
		return min+":"+sec;
	}

	@Override
	public void render () { // повторяется с частотой 60 fps
		// касания экрана/клики мышью
		if(Gdx.input.justTouched()) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			for (int i = mosq.length-1; i >= 0; i--) {
				if(mosq[i].isAlive) {
					if(mosq[i].hit(touch.x, touch.y)) {
						kills++;
						sndMosq[MathUtils.random(0, 3)].play();
						if(kills == mosq.length) {
							gameOver = true;
						}
						break;
					}
				}
			}
		}

		// события движение москитов
		for (int i = 0; i < mosq.length; i++) {
			mosq[i].fly();
		}
		if(!gameOver) {
			timeCurrent = TimeUtils.millis() - timeStart;
		}

		// отрисовка всего
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBackGround, 0, 0, scrWidth, scrHeight);
		for(int i=0; i<mosq.length; i++) {
			batch.draw(imgMosq[mosq[i].faza], mosq[i].x, mosq[i].y, mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
		}
		font.draw(batch, "MOSQUITOS KILLED: "+kills, 10, scrHeight-10);
		font.draw(batch, "TIME: "+timeToString(timeCurrent), scrWidth-300, scrHeight-10);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (int i = 0; i < imgMosq.length; i++) {
			imgMosq[i].dispose();
		}
		imgBackGround.dispose();
		sndMusic.dispose();
	}
}
