package com.softdev.nightlite;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;

public class KolorLinker extends ApplicationAdapter {

	//The amount of squares on one side. Total amount of squares = amount
	private int squaresPerSide = 8;
	//amount of pixels per square
	int tilePixels;
	//The color of a tile (String because java/libgdx mismatch problems).
	String tileColor;
	//Map that contains the color data needed to render the game board
	private Board board;
	//font for the text
	private BitmapFont font;
	//SpriteBatch used to draw the BitmapFont
	private SpriteBatch batch;
	//ShapeRenderer used to draw the tiles. See libgdx doc.
	ShapeRenderer sr;
	//Camera view. See libgdx doc.
	OrthographicCamera camera;
	//Splashscreen
	Texture splash, endscreen;
	//scaling
	public static float SCALE_RATIO;
	//my InputListener (methods when input action happens)
	private InputListener il;
	//game states, small FSM implementation while rendering
	boolean begin = false;
	boolean end = false;
	//Time drawable + scoreboard
	private float deltaTime;
	private String timer;
	//score

	public KolorLinker() {
		board = new Board(squaresPerSide, squaresPerSide);
		board.generateNewMap();
	}

	/*
	* Create is called before rendering. This will serve to initialize all the textures, fonts, etc...
	*
	* */
	@Override
	public void create() {
		begin = false;

		font = new BitmapFont();
		font.getData().setScale(3, 3);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		this.tilePixels = Gdx.graphics.getWidth() / squaresPerSide;
		//set the inputprocessor AFTER tilePixels is instatiated
		Gdx.input.setInputProcessor(new GestureDetector(new InputListener(board, tilePixels)));

		splash = new Texture("splash.jpg");
		endscreen = new Texture("endscreen.jpg");

	}

	/*
	* Render is the main loop. Below is a FSM implementation. State 1 being the splash (begin)
	* screen. State 2 being the game itself and State 3 being the endscreen where the final score
	* is shown.
	* */

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//splashscreen + endscreen
		if(Gdx.input.isTouched()) {
			begin = true;
			if(end){
				board.generateNewMap();
				board.resetTaps();
				end = false;
				begin = true;
				deltaTime = 0;
			}
		}

		if (!begin && !end) {
			batch.begin();
			batch.draw(splash,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();
		}

		else if(end){
			begin = false;
			batch.begin();
			batch.draw(endscreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			this.drawInfo(batch);
			batch.end();
		}

		else {
			sr.begin(ShapeRenderer.ShapeType.Filled);
			for (int y = 0; y < board.getMapHeight(); y++) {
				for (int x = 0; x < board.getMapWidth(); x++) {
					//Math to get the first start at the bottom left corner (not the bottom left)
					sr.rect(x * tilePixels, (Gdx.graphics.getHeight() - ((y + 1) * tilePixels)), tilePixels, tilePixels);
					//Get the color of the tile
					tileColor = board.getColor(x, y);

					//Translate color (misbehaves sometimes @ startup --> try/catch)
					try {
						Color cl = Colors.get(tileColor);
						sr.setColor(cl);
					}
					catch (Exception e){

					}
					//Realign the shape renderer
					sr.rect(x * tilePixels, (Gdx.graphics.getHeight() - ((y + 1) * tilePixels)), tilePixels, tilePixels);
				}
			}

			this.end = board.gameEnd();
			sr.end();

			batch.begin();
			this.drawInfo(batch);
			batch.end();
		}
	}
	/*
	* FSM Controlled timer + displays amount of taps.
	* TODO: improve relative placement of the taps/time
	* */

	public void drawInfo(SpriteBatch batch) {
		if(!end) {
			deltaTime += Gdx.graphics.getDeltaTime();
			timer = Float.toString(deltaTime);
		}
		font.draw(batch, "Time: " + timer, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4);
		font.draw(batch, "Taps: " + board.getTaps(), Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4-100);
	}
}
