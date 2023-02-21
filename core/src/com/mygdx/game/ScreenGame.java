package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
	MyGdxGame mgg;

	Texture[] imgMosq = new Texture[11]; // ссылка на текстуры (картинки)
	Texture imgBackGround; // фон
	Texture imgBtnMenu;

	Sound[] sndMosq = new Sound[4];
	Music sndMusic;

	// создание массива ссылок на объекты
	Mosquito[] mosq;
	int kills;
	long timeStart, timeCurrent;

	// состояние игры
	public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
	int gameState;

	Player[] players = new Player[5];

	MosquitoButton btnRestart, btnExit;
	MosquitoButton btnMenu;

	public ScreenGame (MyGdxGame myGdxGame) {
		mgg = myGdxGame;

		// создаём объекты изображений
		for(int i=0; i<imgMosq.length; i++){
			imgMosq[i] = new Texture("mosq"+i+".png");
		}
		imgBackGround = new Texture("backgrounds/bg_boloto.jpg");
		imgBtnMenu = new Texture("menu.png");

		// создаём объекты звуков
		for(int i=0; i<sndMosq.length; i++) {
			sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("sound/mos"+i+".mp3"));
		}
		sndMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/soundcrazymosquitos.mp3"));
		sndMusic.setLooping(true);
		sndMusic.setVolume(0.2f);
		if(mgg.musicOn) sndMusic.play();

		// создаём объекты игроков для таблицы рекордов
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("Noname", 0);
		}
		loadTableOfRecords();

		// создаём кнопки
		btnRestart = new MosquitoButton(mgg.font, "RESTART", 450, 200);
		btnExit = new MosquitoButton(mgg.font, "EXIT", 750, 200);
		btnMenu = new MosquitoButton(SCR_WIDTH-60, SCR_HEIGHT-60, 50, 50);
	}

	@Override
	public void show() {
		gameStart();
	}

	@Override
	public void render(float delta) {// повторяется с частотой 60 fps
		// касания экрана/клики мышью
		if(Gdx.input.justTouched()) {
			mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			mgg.camera.unproject(mgg.touch);
			if(gameState == PLAY_GAME) {
				for (int i = mosq.length - 1; i >= 0; i--) {
					if (mosq[i].isAlive) {
						if (mosq[i].hit(mgg.touch.x, mgg.touch.y)) {
							kills++;
							if(mgg.soundOn) sndMosq[MathUtils.random(0, 3)].play();
							if (kills == mosq.length) {
								gameState = ENTER_NAME;
							}
							break;
						}
					}
				}
			}
			if(gameState == SHOW_TABLE){
				if(btnExit.hit(mgg.touch.x, mgg.touch.y)) {
					mgg.setScreen(mgg.screenIntro);
				}
				if(btnRestart.hit(mgg.touch.x, mgg.touch.y)) {
					gameStart();
				}
			}
			if(gameState == ENTER_NAME){
				if(mgg.keyboard.endOfEdit(mgg.touch.x, mgg.touch.y)){
					gameOver();
				}
			}
			if(btnMenu.hit(mgg.touch.x, mgg.touch.y)){
				mgg.setScreen(mgg.screenIntro);
			}
		}

		// события игры
		for (int i = 0; i < mosq.length; i++) {
			mosq[i].fly();
		}
		if(gameState == PLAY_GAME) {
			timeCurrent = TimeUtils.millis() - timeStart;
		}

		// отрисовка всего
		mgg.camera.update();
		mgg.batch.setProjectionMatrix(mgg.camera.combined);
		mgg.batch.begin();
		mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for(int i=0; i<mosq.length; i++) {
			mgg.batch.draw(imgMosq[mosq[i].faza], mosq[i].x, mosq[i].y, mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
		}
		mgg.font.draw(mgg.batch, "MOSQUITOS KILLED: "+kills, 10, SCR_HEIGHT-10);
		mgg.font.draw(mgg.batch, "TIME: "+timeToString(timeCurrent), SCR_WIDTH-500, SCR_HEIGHT-10);
		if(gameState == SHOW_TABLE) {
			mgg.fontLarge.draw(mgg.batch,"Game Over", 0, 600, SCR_WIDTH, Align.center, true);
			for (int i = 0; i < players.length; i++) {
				String s = players[i].name + "......." + timeToString(players[i].time);
				mgg.font.draw(mgg.batch, s, 0, 500-i*50, SCR_WIDTH, Align.center, true);
			}
			mgg.font.draw(mgg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
			mgg.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
		}
		if(gameState == ENTER_NAME){
			mgg.keyboard.draw(mgg.batch);
		}
		mgg.batch.draw(imgBtnMenu, btnMenu.x, btnMenu.y, btnMenu.width, btnMenu.height);
		mgg.batch.end();
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
	public void dispose () {
		for (int i = 0; i < imgMosq.length; i++) {
			imgMosq[i].dispose();
		}
		for (int i = 0; i < sndMosq.length; i++) {
			sndMosq[i].dispose();
		}
		sndMusic.dispose();
		imgBackGround.dispose();
	}

	String timeToString(long time){
		String min = "" + time/1000/60/10 + time/1000/60%10;
		String sec = "" + time/1000%60/10 + time/1000%60%10;
		return min+":"+sec;
	}

	void gameStart(){
		// создание объектов комаров
		mosq = new Mosquito[mgg.numMosquitos];
		for(int i=0; i<mosq.length; i++) {
			mosq[i] = new Mosquito(mgg);
		}
		kills = 0;
		gameState = PLAY_GAME;
		timeStart = TimeUtils.millis();
	}

	void gameOver(){
		gameState = SHOW_TABLE;
		players[players.length-1].name = mgg.keyboard.getText();
		players[players.length-1].time = timeCurrent;
		sortTableOfRecords();
		saveTableOfRecords();
	}

	void saveTableOfRecords(){
		Preferences prefs = Gdx.app.getPreferences("Table Of Records");
		for (int i = 0; i < players.length; i++) {
			prefs.putString("name"+i, players[i].name);
			prefs.putLong("time"+i, players[i].time);
		}
		prefs.flush();
	}

	void loadTableOfRecords(){
		Preferences prefs = Gdx.app.getPreferences("Table Of Records");
		for (int i = 0; i < players.length; i++) {
			if(prefs.contains("name"+i)) players[i].name = prefs.getString("name"+i);
			if(prefs.contains("time"+i)) players[i].time = prefs.getLong("time"+i);
		}
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

	void clearTableOfRecords(){
		for (int i = 0; i < players.length; i++) {
			players[i].name = "Noname";
			players[i].time = 0;
		}
	}
}
