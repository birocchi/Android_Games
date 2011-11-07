package com.androidgames.mrmunch;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.SparseArray;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.impl.AndroidGame;

public class SettingsScreen extends Screen {
	
	private final int BUTTON_OK_X = 0;
	private final int BUTTON_OK_Y = 416;
	
	private final int BUTTON_RESET_X = game.getGraphics().getWidth()/2 - Assets.BUTTON_WIDTH;
	private final int BUTTON_RESET_Y = game.getGraphics().getHeight()/2 + Assets.initialspeed.getHeight()+10;
	
	private final int SETTINGS_IMAGE_X = 64;
	private final int SETTINGS_IMAGE_Y = 20;

	private SparseArray<Bounds> mBounds;

	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_1 = 0;
	private final int CLICK_2 = 1;
	private final int CLICK_3 = 2;
	private final int CLICK_4 = 3;
	private final int CLICK_5 = 4;
	private final int CLICK_OK = 5;
	private final int CLICK_RESET = 6;

	public SettingsScreen(Game game) {
		super(game);
		
		//Defining the BOUNDS where some CLICK_EVENT should happen
		Graphics g = game.getGraphics();
        mBounds = new SparseArray<Bounds>();
        for(int i = 0; i < 5; i++) {
        	mBounds.append(i,new Bounds(i,
        			               (i+1) * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 6,
        			               g.getHeight()/3 - 4,
        			               Assets.CHARACTER_WIDTH + 12,
        			               Assets.CHARACTER_HEIGHT + 8
        			               ));
        }
        mBounds.append(CLICK_OK,new Bounds(CLICK_OK, BUTTON_OK_X, BUTTON_OK_Y, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
        mBounds.append(CLICK_RESET,new Bounds(CLICK_RESET, BUTTON_RESET_X, BUTTON_RESET_Y, 2*Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
	}
	
	@Override
	public void update(float deltaTime) {
		int len, clickEvent;
		List<TouchEvent> events = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
		//Dealing with KEY EVENTS
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == KeyEvent.KEY_UP)
	    		game.setScreen(new MainMenuScreen(game));
        }
        
        //Dealing with TOUCH EVENTS
        len = events.size();
        for(int i=0; i<len; i++) {
        	TouchEvent event = events.get(i);
        	if (event.type == TouchEvent.TOUCH_UP) {
        		
        		clickEvent = eventInBounds(mBounds, event);

            	//play sound if clicked a item and sound is enabled
            	if(clickEvent != CLICK_NO_EVENT && Settings.soundEnabled)
            		Assets.click.play(1);

            	switch(clickEvent){
            	case CLICK_1:
            		Settings.gameSpeed = 1;
            		break;
            	case CLICK_2:
            		Settings.gameSpeed = 2;
            		break;
            	case CLICK_3:
            		Settings.gameSpeed = 3;
            		break;
            	case CLICK_4:
            		Settings.gameSpeed = 4;
            		break;
            	case CLICK_5:
            		Settings.gameSpeed = 5;
            		break;
            	case CLICK_OK:
            		game.setScreen(new MainMenuScreen(game));
            		break;
            	case CLICK_RESET:
            		confirmReset();
            		break;
            	}
        	}
        }
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.mainMenu, SETTINGS_IMAGE_X, SETTINGS_IMAGE_Y, Assets.SETTINGS_SCRX, Assets.SETTINGS_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);
        g.drawPixmap(Assets.initialspeed, g.getWidth()/2 - Assets.initialspeed.getWidth()/2, g.getHeight()/3 - Assets.initialspeed.getHeight());
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 6, g.getHeight()/3 - 4, Assets.CHARACTER_WIDTH + 12, Assets.CHARACTER_HEIGHT + 8, Color.WHITE);
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 4, g.getHeight()/3 - 2, Assets.CHARACTER_WIDTH + 8, Assets.CHARACTER_HEIGHT + 4, Color.BLACK);
        for(int i = 1; i <= 5; i++) {
        	g.drawText(g, Assets.characters, ""+i, i * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH , g.getHeight()/3);
        }
        g.drawPixmap(Assets.buttons, BUTTON_OK_X, BUTTON_OK_Y, Assets.BUTTON_OK_SCRX, Assets.BUTTON_OK_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.textreset, g.getWidth()/2 - Assets.textreset.getWidth()/2, g.getHeight()/2);
        g.drawPixmap(Assets.buttons, BUTTON_RESET_X, BUTTON_RESET_Y, Assets.BUTTON_RESET_SCRX, Assets.BUTTON_RESET_SCRY, 2*Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        
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

	public void confirmReset() {
    	((AndroidGame)game).runOnUiThread(new Runnable() {
    		@Override
    		public void run() {
    			final AlertDialog.Builder alert = new AlertDialog.Builder((MrMunchGame)game);
    			alert.setTitle("Data reset!!!");
    			alert.setMessage("Do you really want to erase all stored scores and achievements?");
    			
    			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int whichButton) {
    					Settings.resetScore();
                		Settings.save(game.getFileIO());
    				}
    			});

    			alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int whichButton) {
    					//Do nothing
    				}
    			});

    			alert.show();
    		}

    	});
    }
	
}
