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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch; // ссылка на объект, отвечающий за вывод изображений
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font, fontLarge;

	Texture[] imgMosq = new Texture[11]; // ссылка на текстуры (картинки)
	Texture imgBackGround; // фон

	Sound[] sndMosq = new Sound[4];
	Music sndMusic;

	public static int scrWidth = 1280;
	public static int scrHeight = 720;

	// создание массива ссылок на объекты
	Mosquito[] mosq = new Mosquito[5];
	int kills;
	long timeStart, timeCurrent;

	boolean gameOver;
	Player[] players = new Player[5];

	TextButton btnRestart, btnExit;
	
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
		imgBackGround = new Texture("boloto.jpg");

		// создаём объекты звуков
		for(int i=0; i<sndMosq.length; i++) {
			sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("mos"+i+".mp3"));
		}
		sndMusic = Gdx.audio.newMusic(Gdx.files.internal("smeshariki.mp3"));
		sndMusic.setLooping(true);
		//sndMusic.play();

		// создаём объекты игроков для таблицы рекордов
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("None", 0);
		}

		// создаём кнопки
		btnRestart = new TextButton(font, "RESTART", 450, 200);
		btnExit = new TextButton(font, "EXIT", 750, 200);

		gameStart();
	}

	void createFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("mr_countryhouse.ttf"));
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 50;
		parameter.color = Color.CHARTREUSE;
		parameter.borderWidth = 2;
		parameter.borderColor = Color.BLACK;
		font = generator.generateFont(parameter);

		parameter.size = 70;
		fontLarge = generator.generateFont(parameter);

		generator.dispose();
	}

	String timeToString(long time){
		String min = "" + time/1000/60/10 + time/1000/60%10;
		String sec = "" + time/1000%60/10 + time/1000%60%10;
		return min+":"+sec;
	}

	void gameStart(){
		// создание объектов комаров
		for(int i=0; i<mosq.length; i++) {
			mosq[i] = new Mosquito();
		}
		kills = 0;
		gameOver = false;
		timeStart = TimeUtils.millis();
	}

	void gameOver(){
		gameOver = true;
		players[players.length-1].name = "Hunter";
		players[players.length-1].time = timeCurrent;
		sortTableOfRecords();
	}

	void sortTableOfRecords(){
		for (int i = 0; i < players.length; i++) {
			if(players[i].time == 0) players[i].time = 1000000;
		}
		for (int j = 0; j < players.length-1; j++) {
			for (int i = 0; i < players.length-1; i++) {
				if(players[i].time>players[i+1].time){
					long c = players[i].time;
					players[i].time = players[i+1].time;
					players[i+1].time = c;
					String s = players[i].name;
					players[i].name = players[i+1].name;
					players[i+1].name = s;
					/* Player c = players[i];
					players[i] = players[i+1];
					players[i+1] = c; */
				}
			}
		}
		for (int i = 0; i < players.length; i++) {
			if(players[i].time == 1000000) players[i].time = 0;
		}
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
							gameOver();
						}
						break;
					}
				}
			}
			if(gameOver){
				if(btnExit.hit(touch.x, touch.y)) {
					Gdx.app.exit();
				}
				if(btnRestart.hit(touch.x, touch.y)) {
					gameStart();
				}
			}
		}

		// события игры
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
		if(gameOver) {
			fontLarge.draw(batch,"Game Over", 0, 600, scrWidth, Align.center, true);
			for (int i = 0; i < players.length; i++) {
				String s = players[i].name + "......." + timeToString(players[i].time);
				font.draw(batch, s, 0, 500-i*50, scrWidth, Align.center, true);
			}
			font.draw(batch, btnRestart.text, btnRestart.x, btnRestart.y);
			font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
		}
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
