package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class MainMenuScreen extends Screen {
	
	private final int LOGO_IMAGE_X = 19;
	private final int LOGO_IMAGE_Y = 13;
	
	private final int MENU_IMAGE_X = 64;
	private final int MENU_IMAGE_Y = 220;
	
	private final int BUTTON_SOUND_X = 0;
	private final int BUTTON_SOUND_Y = 416;
	
    public MainMenuScreen(Game game) {
        super(game);               
    }   

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, BUTTON_SOUND_X, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                }
                if(inBounds(event, MENU_IMAGE_X, MENU_IMAGE_Y, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT) ) {
                    game.setScreen(new GameScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, MENU_IMAGE_X, MENU_IMAGE_Y + Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT) ) {
                    game.setScreen(new HighscoreScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, MENU_IMAGE_X, MENU_IMAGE_Y + 2 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT) ) {
                    game.setScreen(new SettingsScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, MENU_IMAGE_X, MENU_IMAGE_Y + 3 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT) ) {
                    game.setScreen(new HelpScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, MENU_IMAGE_X, MENU_IMAGE_Y + 4 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT) ) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    ((MrMunchGame)game).finish();
                }
            }
        }
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.logo, LOGO_IMAGE_X, LOGO_IMAGE_Y);
        g.drawPixmap(Assets.mainMenu, MENU_IMAGE_X, MENU_IMAGE_Y);
        if(Settings.soundEnabled)
            g.drawPixmap(Assets.buttons, BUTTON_SOUND_X, BUTTON_SOUND_Y, Assets.BUTTON_SOUND_ON_SCRX, Assets.BUTTON_SOUND_ON_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        else
            g.drawPixmap(Assets.buttons, BUTTON_SOUND_X, BUTTON_SOUND_Y, Assets.BUTTON_SOUND_OFF_SCRX, Assets.BUTTON_SOUND_OFF_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    }

    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
