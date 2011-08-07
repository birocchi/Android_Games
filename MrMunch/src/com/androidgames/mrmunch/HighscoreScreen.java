package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class HighscoreScreen extends Screen {
	
	private final int BUTTON_PREV_X = 0;
	private final int BUTTON_PREV_Y = 416;
	
	private final int HIGHSCORE_IMAGE_X = 64;
	private final int HIGHSCORE_IMAGE_Y = 20;
	
	private final int HIGHSCORE_ITEM_SPACING = 50;
	private final int HIGHSCORE_ITEM_INIT_Y = 100;
	private final int HIGHSCORE_ITEM_X = 20;
	
    String lines[] = new String[5];

    public HighscoreScreen(Game game) {
        super(game);

        for (int i = 0; i < 5; i++) {
            lines[i] = "" + (i + 1) + ". " + Settings.highscores[i];
        }
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT) {
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

        g.clear(Color.BLACK);
        g.drawPixmap(Assets.mainMenu, HIGHSCORE_IMAGE_X, HIGHSCORE_IMAGE_Y, Assets.HIGHSCORE_SCRX, Assets.HIGHSCORE_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);

        int y = HIGHSCORE_ITEM_INIT_Y;
        for (int i = 0; i < 5; i++) {
            g.drawText(g, lines[i], HIGHSCORE_ITEM_X, y);
            y += HIGHSCORE_ITEM_SPACING;
        }

        g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_PREV_SCRX, Assets.BUTTON_PREV_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
