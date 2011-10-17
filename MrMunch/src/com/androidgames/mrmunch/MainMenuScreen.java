package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;
import android.util.SparseArray;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;

public class MainMenuScreen extends Screen {
	
	private final int LOGO_IMAGE_X = 19;
	private final int LOGO_IMAGE_Y = 13;
	
	private final int MENU_IMAGE_X = 64;
	private final int MENU_IMAGE_Y = 220;
	
	private final int BUTTON_SOUND_X = 0;
	private final int BUTTON_SOUND_Y = 416;
	
	private SparseArray<Bounds> mBounds;
	
	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_SOUND = 0;
	private final int CLICK_PLAY = 1;
	private final int CLICK_HIGHSCORES = 2;
	private final int CLICK_SETTINGS = 3;
	private final int CLICK_BADGES = 4;
	private final int CLICK_HELP = 5;
	private final int CLICK_QUIT = 6;
	
    public MainMenuScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        
        //Defining the BOUNDS where some CLICK_EVENT should happen
        mBounds = new SparseArray<Bounds>();
        mBounds.append(0,new Bounds(CLICK_SOUND, BUTTON_SOUND_X, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
        mBounds.append(1,new Bounds(CLICK_PLAY, MENU_IMAGE_X, MENU_IMAGE_Y, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));
        mBounds.append(2,new Bounds(CLICK_HIGHSCORES, MENU_IMAGE_X, MENU_IMAGE_Y + Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));
        mBounds.append(3,new Bounds(CLICK_SETTINGS, MENU_IMAGE_X, MENU_IMAGE_Y + 2 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));
        mBounds.append(4,new Bounds(CLICK_BADGES, MENU_IMAGE_X, MENU_IMAGE_Y + 3 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));        
        mBounds.append(5,new Bounds(CLICK_HELP, MENU_IMAGE_X, MENU_IMAGE_Y + 4 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));
        mBounds.append(6,new Bounds(CLICK_QUIT, MENU_IMAGE_X, MENU_IMAGE_Y + 5 * Assets.MENU_ITEM_HEIGHT, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT));
    }

    @Override
    public void update(float deltaTime) {
        int len, clickEvent;
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
        //Dealing with KEY EVENTS
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK  && kevent.type == KeyEvent.KEY_UP)
	    		game.finish();
        }
        
        //Dealing with TOUCH EVENTS
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	
            	clickEvent = eventInBounds(mBounds, event);
            	
            	//play sound if clicked a item and sound is enabled
            	if(clickEvent != CLICK_NO_EVENT && clickEvent != CLICK_SOUND && Settings.soundEnabled)
            		Assets.click.play(1);

            	switch(clickEvent){
            	case CLICK_SOUND:
            		Settings.soundEnabled = !Settings.soundEnabled;
            		break;
            	case CLICK_PLAY:
            		game.setScreen(new GameScreen(game));
            		break;
            	case CLICK_HIGHSCORES:
            		game.setScreen(new HighscoreScreen(game));
            		break;
            	case CLICK_SETTINGS:
            		game.setScreen(new SettingsScreen(game));
            		break;
            	case CLICK_BADGES:
            		game.setScreen(new AchievementsListScreen(game));
            		break;
            	case CLICK_HELP:
            		game.setScreen(new HelpScreens(game));
            		break;
            	case CLICK_QUIT:
            		((MrMunchGame)game).finish();
            		break;
            	}
            }
        }
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
        
        if(DEBUG_BOUNDS == true){
        	drawDebugBounds(g, mBounds);
        }
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
