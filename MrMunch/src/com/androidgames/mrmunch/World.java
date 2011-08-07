package com.androidgames.mrmunch;

import java.util.Random;

public class World {
    static final int WORLD_WIDTH = 10;
    static final int WORLD_HEIGHT = 13;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = 0.5f;
    static final float TICK_DECREMENT = 0.05f;

    public Snake snake;
    public Fruit fruit;
    public Fruit extraFruit = null;
    private int fruitsEaten = 0;
    public boolean gameOver = false;;
    public int score = 0;

    boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random random = new Random();
    float tickTime = 0;
    static float tick = TICK_INITIAL - 1.5f * Settings.gameSpeed * World.TICK_DECREMENT;
    int deltaTick = 0;

    public World() {
        snake = new Snake();
        placeFruit();
    }

    private void placeFruit() {
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int y = 0; y < WORLD_HEIGHT; y++) {
                fields[x][y] = false;
            }
        }

        int len = snake.parts.size();
        for (int i = 0; i < len; i++) {
            SnakePart part = snake.parts.get(i);
            fields[part.x][part.y] = true;
        }

        int fruitX = random.nextInt(WORLD_WIDTH);
        int fruitY = random.nextInt(WORLD_HEIGHT);
        while (true) {
            if (fields[fruitX][fruitY] == false)
                break;
            fruitX += 1;
            if (fruitX >= WORLD_WIDTH) {
                fruitX = 0;
                fruitY += 1;
                if (fruitY >= WORLD_HEIGHT) {
                    fruitY = 0;
                }
            }
        }
        fruit = new Fruit(fruitX, fruitY, random.nextInt(3));
        fields[fruitX][fruitY] = true;
        
        if(fruitsEaten >= 10 && extraFruit == null) {
        	fruitX = random.nextInt(WORLD_WIDTH);
        	fruitY = random.nextInt(WORLD_HEIGHT);
        	while (true) {
                if (fields[fruitX][fruitY] == false)
                    break;
                fruitX += 1;
                if (fruitX >= WORLD_WIDTH) {
                    fruitX = 0;
                    fruitY += 1;
                    if (fruitY >= WORLD_HEIGHT) {
                        fruitY = 0;
                    }
                }
            }
        	extraFruit = new Fruit(fruitX, fruitY, Fruit.TYPE_4);
        	fruitsEaten = 0;
        }
    }

    public void update(float deltaTime) {
        if (gameOver)
            return;

        tickTime += deltaTime;

        while (tickTime > tick) {
            tickTime -= tick;
            snake.advance();
            if (snake.checkBitten()) {
                gameOver = true;
                return;
            }

            SnakePart head = snake.parts.get(0);
            if (head.x == fruit.x && head.y == fruit.y) {
                score += SCORE_INCREMENT;
                fruitsEaten++;
                snake.eat();
                if (snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
                    gameOver = true;
                    return;
                } else {
                	if (fruitsEaten >= 10 && tick - TICK_DECREMENT > 0) {
                        tick -= TICK_DECREMENT;
                    }
                    placeFruit();
                }
            }
            if (extraFruit != null){
	            if (head.x == extraFruit.x && head.y == extraFruit.y) {
	            	score += 5 * SCORE_INCREMENT;
	            	snake.shrink();
	            	extraFruit = null;
	            	deltaTick = 0;
	            } else {
	                if (deltaTick >= 20){
	            	    extraFruit = null;
	            	    deltaTick = 0;
	                } else { 
	            	    deltaTick++;
	                }
	            }
            }
        }
    }
}
