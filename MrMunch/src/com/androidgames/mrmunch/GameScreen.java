package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Pixmap;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class GameScreen extends Screen {
	
	private final int BUTTON_PAUSE_X = 0;
	private final int BUTTON_PAUSE_Y = 0;
	
	private final int BUTTON_LEFT_X = 0;
	private final int BUTTON_LEFT_Y = 416;
	
	private final int BUTTON_RIGHT_X = 256;
	private final int BUTTON_RIGHT_Y = 416;
	
	private final int BUTTON_SPEEDUP_X = 256;
	private final int BUTTON_SPEEDUP_Y = 350;
	
	private final int BUTTON_CANCEL_X = 128;
	private final int BUTTON_CANCEL_Y = 200;
	
	private final int READY_IMAGE_X = 47;
	private final int READY_IMAGE_Y = 100;
	
	private final int PAUSE_IMAGE_X = 80;
	private final int PAUSE_IMAGE_Y = 100;
	
	private final int GAME_OVER_IMAGE_X = 62;
	private final int GAME_OVER_IMAGE_Y = 100;
	
	private final int SCALING_FACTOR = 32;
	
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
    boolean speedingUp = false;
    
    public GameScreen(Game game) {
        super(game);
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        if(state == GameState.Ready)
            updateReady(touchEvents);
        if(state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if(state == GameState.Paused)
            updatePaused(touchEvents);
        if(state == GameState.GameOver)
            updateGameOver(touchEvents);        
    }
    
    private void updateReady(List<TouchEvent> touchEvents) {
        if(touchEvents.size() > 0){
            state = GameState.Running;
            World.tick = World.TICK_INITIAL - 1.5f * Settings.gameSpeed * World.TICK_DECREMENT;
        }
    }
    
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
    	
    	Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < Assets.BUTTON_WIDTH && event.y < Assets.BUTTON_HEIGHT) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    state = GameState.Paused;
                    return;
                }
            }
            if(event.type == TouchEvent.TOUCH_DOWN) {
                if(event.x < Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT) {
                	if(!world.snake.already_turned)
                		world.snake.turnLeft();
                }
                if(event.x > BUTTON_RIGHT_X && event.y > g.getHeight() - Assets.BUTTON_HEIGHT) {
                	if(!world.snake.already_turned)
                		world.snake.turnRight();
                }
                if(event.x > BUTTON_SPEEDUP_X && event.y > BUTTON_SPEEDUP_Y && event.y < g.getHeight() - Assets.BUTTON_HEIGHT) {
                	speedingUp = true;
                	if(World.tick - World.TICK_DECREMENT > 0)
                		World.tick /= 2;
                }
            }
            if(((event.type == TouchEvent.TOUCH_UP && event.x > BUTTON_SPEEDUP_X && event.y > BUTTON_SPEEDUP_Y && event.y < g.getHeight() - Assets.BUTTON_HEIGHT) || 
                (event.type == TouchEvent.TOUCH_DRAGGED && !(event.x > BUTTON_SPEEDUP_X && event.y > BUTTON_SPEEDUP_Y && event.y < g.getHeight() - Assets.BUTTON_HEIGHT))) && speedingUp){
            	speedingUp = false;
        		World.tick *= 2;
            }
        }
        
        world.update(deltaTime);
        if(world.gameOver) {
            if(Settings.soundEnabled)
                Assets.bitten.play(1);
            state = GameState.GameOver;
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
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > PAUSE_IMAGE_X && event.x <= PAUSE_IMAGE_X + Assets.PAUSE_MENU_ITEM_WIDTH) {
                    if(event.y > PAUSE_IMAGE_Y && event.y <= PAUSE_IMAGE_Y + Assets.PAUSE_MENU_ITEM_HEIGHT) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        state = GameState.Running;
                        return;
                    }                    
                    if(event.y > PAUSE_IMAGE_Y + Assets.PAUSE_MENU_ITEM_HEIGHT && event.y < PAUSE_IMAGE_Y + 2*Assets.PAUSE_MENU_ITEM_HEIGHT) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        game.setScreen(new MainMenuScreen(game));                        
                        return;
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
        
        g.drawText(g, score, g.getWidth() / 2 - score.length()*Assets.NUMBER_WIDTH / 2, g.getHeight() - 42);
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
        x = head.x * SCALING_FACTOR + 16;
        y = head.y * SCALING_FACTOR + 16;
        g.drawPixmap(headPixmap, x - headPixmap.getWidth() / 2, y - headPixmap.getHeight() / 2);
    }
    
    private void drawReadyUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.ready, READY_IMAGE_X, READY_IMAGE_Y);
        g.drawRect(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT, g.getWidth(), 2*Assets.BUTTON_HEIGHT, Color.BLACK);
        g.drawLine(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, g.getWidth(), g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, Color.GREEN);
    }
    
    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.buttons, BUTTON_PAUSE_X, BUTTON_PAUSE_Y, Assets.BUTTON_PAUSE_SCRX, Assets.BUTTON_PAUSE_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawRect(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT, g.getWidth(), 2*Assets.BUTTON_HEIGHT, Color.BLACK);
        g.drawLine(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, g.getWidth(), g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, Color.GREEN);
        g.drawPixmap(Assets.buttons, BUTTON_LEFT_X, BUTTON_LEFT_Y, Assets.BUTTON_PREV_SCRX, Assets.BUTTON_PREV_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.buttons, BUTTON_RIGHT_X, BUTTON_RIGHT_Y, Assets.BUTTON_NEXT_SCRX, Assets.BUTTON_NEXT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.buttons, BUTTON_SPEEDUP_X, BUTTON_SPEEDUP_Y, Assets.BUTTON_SPEEDUP_SCRX, Assets.BUTTON_SPEEDUP_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    }
    
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.pause, PAUSE_IMAGE_X, PAUSE_IMAGE_Y);
        g.drawRect(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT, g.getWidth(), 2*Assets.BUTTON_HEIGHT, Color.BLACK);
        g.drawLine(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, g.getWidth(), g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, Color.GREEN);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.gameOver, GAME_OVER_IMAGE_X, GAME_OVER_IMAGE_Y);
        g.drawPixmap(Assets.buttons, BUTTON_CANCEL_X, BUTTON_CANCEL_Y,  Assets.BUTTON_CANCEL_SCRX,  Assets.BUTTON_CANCEL_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawRect(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT, g.getWidth(), 2*Assets.BUTTON_HEIGHT, Color.BLACK);
        g.drawLine(0, g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, g.getWidth(), g.getHeight() - 2*Assets.BUTTON_HEIGHT - 1, Color.GREEN);
    }
            
    @Override
    public void pause() {
        if(state == GameState.Running)
            state = GameState.Paused;
        
        if(world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
}