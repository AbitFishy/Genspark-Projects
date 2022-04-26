package com.mygdx.game.humanvsgoblin.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.jetbrains.annotations.NotNull;

//import java.util.HashSet;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Dungeon dg;
	OrthographicCamera camera;
	//Viewport viewPort;
	//HashSet<Entity> dead = new HashSet<>();
	DecideMove dm;
	BitmapFont font;
	String stateStr = "Starting";
	InfoBox infoBox;

	enum State {DECIDE_PATH, CALCULATE_MOVES, PICK_TARGET, ANIMATE_MOVEMENT, ANIMATE_FIGHT, SHOW_ACTION, MENU, PICK_UP_ITEM}

	protected State currentState;
	
	@Override
	public void create () {


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 700);
		//viewPort = new FitViewport(640,640, camera);
		batch = new SpriteBatch();
		dg = new Dungeon();
		//dg.makeConfigFile();
		dg.configGame();
		dm = new DecideMove(dg, dg.player);
		currentState = State.DECIDE_PATH;
		dm.newMove(dg.player.getCoords());
		font = new BitmapFont();
		infoBox = new InfoBox(dg.spriteManager, batch);
		dg.infoBox = infoBox;
		//Gdx.input.setInputProcessor(new GameInputProcessor());
	}


	@Override
	public void render () {
		camera.update();

		//batch.setProjectionMatrix(infoBox.stage.getCamera().combined);



		batch.setProjectionMatrix(camera.combined);

		ScreenUtils.clear(0, 0, 0, 1);

		batch.begin();
		dg.world.displayGrid(batch);
		dg.world.displayDead(batch);
		dm.displayPath(batch);
		dg.displayPending(batch);
		font.draw(batch, stateStr, 10,630);
		//infoBox.setText(stateStr);
		infoBox.draw(batch);
		batch.end();

		Dungeon.cardinalDirections dir;

		switch (currentState) {
			case DECIDE_PATH:
				stateStr = "Decide Path";
				dir = getCardinalDirection();
				dm.continueMove(dir);
				if (hasKeyJustBeenPressed(Input.Keys.ENTER)) {

					currentState = State.PICK_TARGET;
				}
				break;
			case CALCULATE_MOVES:
				stateStr = "Calculate Moves";
				dg.actionStr = new StringBuilder();
				var path = dm.getPath();
				Coords lastCoord = dg.player.getCoords();
				for (var c : path){
					dg.world.moveEntity(dg.player, lastCoord, c, World.MoveOrAttack.MOVE);
					lastCoord = c;
				}
				if (dm.targetPosition != Coords.nullCoords) {
					dg.world.moveEntity(dg.player, lastCoord, dm.targetPosition, World.MoveOrAttack.ATTACK);
				}
				//TODO figure out enemy moves
				dg.world.move();
				currentState = State.ANIMATE_MOVEMENT;
				break;

			case PICK_TARGET:
				stateStr = "Pick Target";
				dir = getCardinalDirection();
				dm.highlightTarget(dg.player,dm.getCurrentProspectivePosition(), dir);
				if (hasKeyJustBeenPressed(Input.Keys.ENTER)) {
					currentState = State.CALCULATE_MOVES;
				}
				else if (hasKeyJustBeenPressed(Input.Keys.BACKSPACE) ){
					currentState = State.DECIDE_PATH;
				}
				break;
			case ANIMATE_MOVEMENT:
				stateStr =  "Animate Movement";
				//TODO animate movements before fights ???
				boolean pickUpItem = false;
				if (dg.hasNextAction()) {
					pickUpItem = dg.showNextAction();
				}
				else {
					dg.showNothingThisRound();
				}
				currentState = pickUpItem ? State.PICK_UP_ITEM : State.ANIMATE_FIGHT;
				//fall through
			case ANIMATE_FIGHT:
				stateStr = "Animate Fight";
				if (hasKeyJustBeenPressed(Input.Keys.ENTER)){
					if (dg.hasNextAction()) {
						if (dg.showNextAction()) {
							currentState = State.PICK_UP_ITEM;
						}
					}
					else {
						dg.resetActions();
						currentState = State.DECIDE_PATH;
						dg.world.clearDead(); //dead are also removed in showAction
						dm.newMove(dg.player.getCoords());
					}
				}
				break;
			case PICK_UP_ITEM:
				stateStr = "Pick Up Item";
				if (hasKeyJustBeenPressed(Input.Keys.Y)){
					dg.acceptItem();
					currentState = State.ANIMATE_FIGHT;
				}
				else if (hasKeyJustBeenPressed(Input.Keys.N)) {
					dg.declineItem();
					currentState = State.ANIMATE_FIGHT;
				}

				break;
			case MENU:
				stateStr = "Menu";
				break;
			case SHOW_ACTION:

			default:
				stateStr = "Default Case";
		}

		//dg.loop();
	}

	@NotNull
	private Dungeon.cardinalDirections getCardinalDirection() {
		Dungeon.cardinalDirections dir = Dungeon.cardinalDirections.NONE;
		if (hasKeyJustBeenPressed(Input.Keys.UP)) {
			dir = Dungeon.cardinalDirections.NORTH;
		} else if (hasKeyJustBeenPressed(Input.Keys.DOWN)) {
			dir = Dungeon.cardinalDirections.SOUTH;
		} else if (hasKeyJustBeenPressed(Input.Keys.RIGHT)) {
			dir = Dungeon.cardinalDirections.EAST;
		} else if (hasKeyJustBeenPressed(Input.Keys.LEFT)) {
			dir = Dungeon.cardinalDirections.WEST;
		}
		return dir;
	}

	protected boolean hasKeyJustBeenPressed(int key){
		return Gdx.input.isKeyJustPressed(key);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
