package com.androidgames.mrmunch;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.Toast;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input;
import com.androidgames.framework.Pixmap;
import com.androidgames.framework.Position;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.impl.AndroidGame;


public class GameScreen extends Screen {

	private final int BUTTON_PAUSE_X = 0;
	private final int BUTTON_PAUSE_Y = 0;

	private final int BUTTON_CANCEL_X = 128;
	private final int BUTTON_CANCEL_Y = 200;

	private final int READY_IMAGE_X = 47;
	private final int READY_IMAGE_Y = 100;

	private final int PAUSE_IMAGE_X = 80;
	private final int PAUSE_IMAGE_Y = 100;

	private final int GAME_OVER_IMAGE_X = 62;
	private final int GAME_OVER_IMAGE_Y = 100;

	private final int SCALING_FACTOR = 20;

	private final int CLICK_NO_EVENT = -1;

	private SparseArray<Bounds> mBoundsRunning;

	private final int CLICK_PAUSE = 0;
	private final int CLICK_TURN_RIGHT = 1;
	private final int CLICK_TURN_LEFT = 2;
	private final int CLICK_TURN_UP = 3;
	private final int CLICK_TURN_DOWN = 4;

	private SparseArray<Bounds> mBoundsPaused;

	private final int CLICK_RESUME = 0;
	private final int CLICK_QUIT = 1;

	enum GameState {
		Ready,
		Running,
		Paused,
		GameOver
	}

	GameState state = GameState.Ready;
	World world;
	int oldScore = 0;
	String score = "0";
	String playerName;
	private int bufferTurns=-1;

	public GameScreen(Game game) {
		super(game);
		world = new World();
		Graphics g = game.getGraphics();

		//Clicks Bounds of the Running state
		mBoundsRunning = new SparseArray<Bounds>();
		mBoundsRunning.append(0,new Bounds(CLICK_PAUSE, 0, 0, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
		mBoundsRunning.append(1,new Bounds(CLICK_TURN_LEFT, g.getWidth() - 3*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
		mBoundsRunning.append(2,new Bounds(CLICK_TURN_RIGHT, g.getWidth() - Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
		mBoundsRunning.append(3,new Bounds(CLICK_TURN_UP, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - 2*Assets.BUTTON_HEIGHT -32, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
		mBoundsRunning.append(4,new Bounds(CLICK_TURN_DOWN, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));

		//Clicks Bounds of the Paused state
		mBoundsPaused = new SparseArray<Bounds>();
		mBoundsPaused.append(0,new Bounds(CLICK_RESUME, PAUSE_IMAGE_X, PAUSE_IMAGE_Y, Assets.PAUSE_MENU_ITEM_WIDTH, Assets.PAUSE_MENU_ITEM_HEIGHT));
		mBoundsPaused.append(1,new Bounds(CLICK_QUIT, PAUSE_IMAGE_X, PAUSE_IMAGE_Y + Assets.PAUSE_MENU_ITEM_HEIGHT, Assets.PAUSE_MENU_ITEM_WIDTH, Assets.PAUSE_MENU_ITEM_HEIGHT));

	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();

		int len = keyEvents.size();
		for(int i=0; i<len; i++){
			KeyEvent kevent = keyEvents.get(i);
			if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == KeyEvent.KEY_UP){
				if(state == GameState.Running)
					state = GameState.Paused;
				else
					game.setScreen(new MainMenuScreen(game));

			}
		}

		if(state == GameState.Ready)
			updateReady(touchEvents);
		if(state == GameState.Running)
			updateRunning(touchEvents, keyEvents, deltaTime);
		if(state == GameState.Paused)
			updatePaused(touchEvents);
		if(state == GameState.GameOver)
			updateGameOver(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents) {
		if(touchEvents.size() > 0){
			state = GameState.Running;
		}
	}

	private void updateRunning(List<TouchEvent> touchEvents, List<Input.KeyEvent> keyEvents, float deltaTime) {
		int clickEvent;

		//Deal with Touch events
		int len = touchEvents.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			clickEvent = eventInBounds(mBoundsRunning, event);
			if(event.type == TouchEvent.TOUCH_UP) {
				if(clickEvent == CLICK_PAUSE) {
					if(Settings.soundEnabled)
						Assets.click.play(1);
					state = GameState.Paused;
					return;
				}
			}
			if(bufferTurns == -1) {
				switch(clickEvent){
				case CLICK_TURN_LEFT:
					if(bufferTurns != Snake.LEFT && world.snake.direction != Snake.LEFT)
						bufferTurns=Snake.LEFT;
					break;

				case CLICK_TURN_RIGHT:
					if(bufferTurns != Snake.RIGHT && world.snake.direction != Snake.RIGHT)
						bufferTurns=Snake.RIGHT;
					break;

				case CLICK_TURN_UP:
					if(bufferTurns != Snake.UP && world.snake.direction != Snake.UP)
						bufferTurns=Snake.UP;
					break;

				case CLICK_TURN_DOWN:
					if(bufferTurns != Snake.DOWN && world.snake.direction != Snake.DOWN)
						bufferTurns = Snake.DOWN;
					break;
				}
			}
		}

		//Deal with Key events
		len = keyEvents.size();
		for(int i = 0; i < len; i++) {
			KeyEvent kevent = keyEvents.get(i);

			if(bufferTurns==-1){
				switch(kevent.keyCode){
				case android.view.KeyEvent.KEYCODE_DPAD_LEFT:
					bufferTurns=Snake.LEFT;
					break;

				case android.view.KeyEvent.KEYCODE_DPAD_RIGHT:
					bufferTurns=Snake.RIGHT;
					break;

				case android.view.KeyEvent.KEYCODE_DPAD_UP:
					bufferTurns=Snake.UP;
					break;

				case android.view.KeyEvent.KEYCODE_DPAD_DOWN:
					bufferTurns=Snake.DOWN;
					break;
				}
			}
		}

		//Turn the snake based on the buffer
		if(world.snake.already_turned!=true && bufferTurns!=-1){
			world.snake.turn(bufferTurns);
			bufferTurns=-1;
		}


		world.update(deltaTime);
		if(world.gameOver) {
			if(Settings.soundEnabled)
				Assets.bitten.play(1);
			state = GameState.GameOver;
			//If it was a Highscore, get the player name
			if (world.score > Settings.highscores[4]){
				getPlayerName();
			}
			//Check for possible completed achievements
			checkCompletedAchievements();
		}
		if(oldScore != world.score) {
			oldScore = world.score;
			score = "" + oldScore;
			if(Settings.soundEnabled)
				Assets.eat.play(1);
		}
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();

		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			int clickEvent = eventInBounds(mBoundsPaused, event);

			if(event.type == TouchEvent.TOUCH_UP) {
				//play sound if clicked a item and sound is enabled
				if(clickEvent != CLICK_NO_EVENT && Settings.soundEnabled)
					Assets.click.play(1);

				if(event.type == TouchEvent.TOUCH_UP) {
					switch(clickEvent){
					case CLICK_RESUME:
						state = GameState.Running;
						break;
					case CLICK_QUIT:
						game.setScreen(new MainMenuScreen(game));
						break;
					}
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {  	
		int len = touchEvents.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP) {
				if(event.x >= BUTTON_CANCEL_X && event.x <= BUTTON_CANCEL_X + Assets.BUTTON_WIDTH &&
						event.y >= BUTTON_CANCEL_Y && event.y <= BUTTON_CANCEL_Y + Assets.BUTTON_HEIGHT) {
					if(Settings.soundEnabled)
						Assets.click.play(1);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}


	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.background, 0, 0);
		drawWorld(world);
		if(state == GameState.Ready) 
			drawReadyUI();
		if(state == GameState.Running)
			drawRunningUI();
		if(state == GameState.Paused)
			drawPausedUI();
		if(state == GameState.GameOver)
			drawGameOverUI();
		g.drawText(g, Assets.characters, "Score", g.getWidth() / 4 - "Score".length()*Assets.CHARACTER_WIDTH / 2, g.getHeight() - 2*Assets.BUTTON_HEIGHT- 28);
		g.drawText(g, Assets.characters, score, g.getWidth() / 4 - score.length()*Assets.CHARACTER_WIDTH / 2, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.CHARACTER_HEIGHT/2 -32/2);
	}

	private void drawWorld(World world) {
		Graphics g = game.getGraphics();
		Snake snake = world.snake;
		SnakePart head = snake.parts.get(0);
		Fruit fruit = world.fruit;
		Fruit extraFruit = world.extraFruit;


		Pixmap fruitPixmap = null;
		Pixmap extraFruitPixmap = null;
		if(fruit.type == Fruit.TYPE_1)
			fruitPixmap = Assets.fruit1;
		if(fruit.type == Fruit.TYPE_2)
			fruitPixmap = Assets.fruit2;
		if(fruit.type == Fruit.TYPE_3)
			fruitPixmap = Assets.fruit3;
		int x = fruit.x * SCALING_FACTOR;
		int y = fruit.y * SCALING_FACTOR;      
		g.drawPixmap(fruitPixmap, x, y);   

		if(extraFruit != null) {
			extraFruitPixmap = Assets.candy;
			x = extraFruit.x * SCALING_FACTOR;
			y = extraFruit.y * SCALING_FACTOR;
			g.drawPixmap(extraFruitPixmap, x, y);
		}

		int len = snake.parts.size();
		for(int i = 1; i < len; i++) {
			SnakePart part = snake.parts.get(i);
			x = part.x * SCALING_FACTOR;
			y = part.y * SCALING_FACTOR;
			g.drawPixmap(Assets.tail, x, y);
		}

		Pixmap headPixmap = null;
		if(snake.direction == Snake.UP) 
			headPixmap = Assets.headUp;
		if(snake.direction == Snake.LEFT) 
			headPixmap = Assets.headLeft;
		if(snake.direction == Snake.DOWN) 
			headPixmap = Assets.headDown;
		if(snake.direction == Snake.RIGHT) 
			headPixmap = Assets.headRight;        
		x = head.x * SCALING_FACTOR + 10;
		y = head.y * SCALING_FACTOR + 10;
		g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.ready, READY_IMAGE_X, READY_IMAGE_Y);
		g.drawPixmap(Assets.rectangle, 0 ,g.getHeight() - 2*Assets.BUTTON_HEIGHT -32);
		g.drawPixmap(Assets.buttons, g.getWidth() - 3*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_RIGHT_SCRX, Assets.BUTTON_RIGHT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - 2*Assets.BUTTON_HEIGHT -32, Assets.BUTTON_UP_SCRX, Assets.BUTTON_UP_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_DOWN_SCRX, Assets.BUTTON_DOWN_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.buttons, BUTTON_PAUSE_X, BUTTON_PAUSE_Y, Assets.BUTTON_PAUSE_SCRX, Assets.BUTTON_PAUSE_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.rectangle, 0 ,g.getHeight() - 2*Assets.BUTTON_HEIGHT -32);
		g.drawPixmap(Assets.buttons, g.getWidth() - 3*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_RIGHT_SCRX, Assets.BUTTON_RIGHT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - 2*Assets.BUTTON_HEIGHT -32, Assets.BUTTON_UP_SCRX, Assets.BUTTON_UP_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_DOWN_SCRX, Assets.BUTTON_DOWN_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);

		if(DEBUG_BOUNDS == true){
			drawDebugBounds(g, mBoundsRunning);
		}
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.pause, PAUSE_IMAGE_X, PAUSE_IMAGE_Y);
		g.drawPixmap(Assets.rectangle, 0 ,g.getHeight() - 2*Assets.BUTTON_HEIGHT -32);
		g.drawPixmap(Assets.buttons, g.getWidth() - 3*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT - Assets.BUTTON_HEIGHT/2 -32/2, Assets.BUTTON_RIGHT_SCRX, Assets.BUTTON_RIGHT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - 2*Assets.BUTTON_HEIGHT -32, Assets.BUTTON_UP_SCRX, Assets.BUTTON_UP_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.buttons, g.getWidth() - 2*Assets.BUTTON_WIDTH, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_DOWN_SCRX, Assets.BUTTON_DOWN_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);

		if(DEBUG_BOUNDS == true){
			drawDebugBounds(g, mBoundsPaused);
		}
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.gameOver, GAME_OVER_IMAGE_X, GAME_OVER_IMAGE_Y);
		g.drawPixmap(Assets.buttons, BUTTON_CANCEL_X, BUTTON_CANCEL_Y,  Assets.BUTTON_CANCEL_SCRX,  Assets.BUTTON_CANCEL_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		g.drawPixmap(Assets.rectangle, 0 ,g.getHeight() - 2*Assets.BUTTON_HEIGHT -32);
	}

	@Override
	public void pause() {
		if(state == GameState.Running)
			state = GameState.Paused;

		if(world.gameOver) {

			//Asks for the player name if his score is higher than the last one
			if(world.score > Settings.highscores[4]){
				Settings.addScore(world.score, playerName);
				world.score = 0;
				playerName = null;
			}

			Settings.save(game.getFileIO());
		}
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	private void getPlayerName() {
		((AndroidGame)game).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder alert = new AlertDialog.Builder((MrMunchGame)game);
				final EditText input = new EditText((MrMunchGame)game);
				//Filtering the input to accept only 5 characters
				int maxLength = 5;
				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(maxLength);
				input.setFilters(FilterArray);
				//-----
				alert.setView(input);
				alert.setTitle("Top 5 Score!");
				alert.setMessage("Enter your nick (5 characters Max):");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						playerName = input.getText().toString().trim();
						playerName = playerName.substring(0, playerName.length());
						if(playerName == null || playerName.length() == 0)
							playerName = ".....";
					}
				});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						playerName = ".....";
					}
				});

				alert.show();
			}

		});
	}

	private void showAchievementCompletedToast(String message) {

		final String CompletionMessage = "Achievement \"" + message + "\" Completed!";

		((AndroidGame)game).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast t = Toast.makeText((MrMunchGame)game, CompletionMessage, Toast.LENGTH_LONG);
				t.show();
			}
		});

	}

	private void checkCompletedAchievements(){

		//Achievement 1
		if(world.score >= 100){
			Achievement achievement = Settings.achievementsList.get(1);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 2
		if(world.score >= 500){
			Achievement achievement = Settings.achievementsList.get(2);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 3
		if(world.score >= 800){
			Achievement achievement = Settings.achievementsList.get(3);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 4
		if(world.score >= 1000){
			Achievement achievement = Settings.achievementsList.get(4);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 5 - 9
		if(Settings.gameSpeed > 1)
			checkTetrisPieceAchievements();

		//Achievement 10
		//TODO: implement
	}

	private void checkTetrisPieceAchievements() {

		Position snakePart = new Position();
		Position PositionsSum = new Position();
		Position centerPosition = new Position();
		int snakeSize = world.snake.parts.size();
		int cicleSize = 0;
		SnakePart head = world.snake.parts.get(0);

		//Calculate where is the center of the body cycle
		for(int i=1; i < snakeSize ; i++){
			snakePart.x = world.snake.parts.get(i).x;
			snakePart.y = world.snake.parts.get(i).y;

			PositionsSum.x += snakePart.x;
			PositionsSum.y += snakePart.y;

			if(snakePart.x == head.x && snakePart.y == head.y){
				//Trims the rest of the snake to calculate the center position
				cicleSize = i;
				break;
			}
		}
		centerPosition.x = (int)Math.round((float)PositionsSum.x/(float)cicleSize);
		centerPosition.y = (int)Math.round((float)PositionsSum.y/(float)cicleSize);
		
		//Checks if the center collides with the body
		for(int i = 0; i < snakeSize; i++){
			if( centerPosition.equals(world.snake.parts.get(i)) ) return;    		
		}

		//If the center does not collide, check if the area around it is equal to 4 squares
		ArrayList<Position> expandedNodes = new ArrayList<Position>();
		if(expandAreaAroundPoint(centerPosition, cicleSize, expandedNodes) == false) return;
		
		//Achievement 5
		if(TetrisPiece.match(TetrisPiece.SQUARE,expandedNodes)){
			Achievement achievement = Settings.achievementsList.get(5);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}
		
		//Achievement 6
		if(TetrisPiece.match(TetrisPiece.LINE,expandedNodes)){
			Achievement achievement = Settings.achievementsList.get(6);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 7
		if(TetrisPiece.match(TetrisPiece.L,expandedNodes)){
			Achievement achievement = Settings.achievementsList.get(7);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 8
		if(TetrisPiece.match(TetrisPiece.S,expandedNodes)){
			Achievement achievement = Settings.achievementsList.get(8);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

		//Achievement 9
		if(TetrisPiece.match(TetrisPiece.T,expandedNodes)){
			Achievement achievement = Settings.achievementsList.get(9);
			if(!achievement.isCompleted){
				achievement.isCompleted = true;
				showAchievementCompletedToast(achievement.title);
			}
		}

	}

	//If the expanded area is greater or less than 4 nodes, return false
	private boolean expandAreaAroundPoint(Position point, int cicleSize, ArrayList<Position> nodes) {
		
		if(nodes.size() > 4)
			return false;
		else{
			//checks if the point collides with the body
			for(int i = 0; i < cicleSize; i++)
				if(point.equals(world.snake.parts.get(i))) return false;    		

			//checks if the node was already expanded
			for(int i = 0; i < nodes.size(); i++)
				if( point.equals(nodes.get(i)) ) return false;
			
			nodes.add(point);
			//Expand UP
			expandAreaAroundPoint( new Position(point.x,point.y-1), cicleSize, nodes);
			//Expand RIGHT
			expandAreaAroundPoint( new Position(point.x+1,point.y), cicleSize, nodes);
			//Expand DOWN
			expandAreaAroundPoint( new Position(point.x,point.y+1), cicleSize, nodes);
			//Expand LEFT
			expandAreaAroundPoint( new Position(point.x-1,point.y), cicleSize, nodes);

			if(nodes.size() != 4)
				return false;
			else
				return true;
		}
		
	}

}
