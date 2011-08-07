package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class HelpScreen extends Screen {  
	
	private final int BUTTON_NEXT_X = 256;
	private final int BUTTON_NEXT_Y = 416;
	private final int BUTTON_CANCEL_X = 0;
	private final int BUTTON_CANCEL_Y = 416;
	private final int HELP_IMAGE_X = 64;
	private final int HELP_IMAGE_Y = 100;
	
    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > g.getWidth() - Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT ) {
                    game.setScreen(new HelpScreen2(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(event.x < Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT ) {
                    game.setScreen(new MainMenuScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.help1, HELP_IMAGE_X, HELP_IMAGE_Y);
        g.drawPixmap(Assets.buttons, BUTTON_NEXT_X, BUTTON_NEXT_Y, Assets.BUTTON_NEXT_SCRX, Assets.BUTTON_NEXT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.buttons, BUTTON_CANCEL_X, BUTTON_CANCEL_Y, Assets.BUTTON_CANCEL_SCRX, Assets.BUTTON_CANCEL_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
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