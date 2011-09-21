package com.androidgames.mrmunch;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.AndroidGame;

public class HighscoreScreen extends Screen {
	
	private final int BUTTON_PREV_X = 0;
	private final int BUTTON_PREV_Y = 416;
	
	private final int BUTTON_RESET_X = 100;
	private final int BUTTON_RESET_Y = 416;
	
	private final int HIGHSCORE_IMAGE_X = 64;
	private final int HIGHSCORE_IMAGE_Y = 20;
	
	private final int HIGHSCORE_ITEM_SPACING = 50;
	private final int HIGHSCORE_ITEM_INIT_Y = 100;
	private final int HIGHSCORE_ITEM_X = 20;
	private final int HIGHSCORE_SCORE_X = 200;
	
    private List<Bounds> mBounds;
	
	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_BACK = 0;
	private final int CLICK_RESET = 1;
	
    String nameLines[] = new String[5];
    String scoreLines[] = new String[5];

    public HighscoreScreen(Game game) {
        super(game);

        getSettingsData();
        
        Graphics g = game.getGraphics();
        //Defining the BOUNDS where some CLICK_EVENT should happen
        mBounds = new ArrayList<Bounds>();
        mBounds.add(new Bounds(CLICK_BACK, BUTTON_PREV_X, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
        mBounds.add(new Bounds(CLICK_RESET, BUTTON_RESET_X, BUTTON_RESET_Y, 2*Assets.BUTTON_WIDTH, 2*Assets.BUTTON_HEIGHT));
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
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == KeyEvent.KEY_UP)
	    		game.setScreen(new MainMenuScreen(game));
        }
        
        //Dealing with TOUCH EVENTS
        len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
            	
                clickEvent = eventInBounds(mBounds, event);
                
                //play sound if clicked a item and sound is enabled
            	if(clickEvent != CLICK_NO_EVENT && Settings.soundEnabled)
            		Assets.click.play(1);
        		
        		switch(clickEvent){
            	case CLICK_BACK:
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
        g.drawPixmap(Assets.mainMenu, HIGHSCORE_IMAGE_X, HIGHSCORE_IMAGE_Y, Assets.HIGHSCORE_SCRX, Assets.HIGHSCORE_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);

        int y = HIGHSCORE_ITEM_INIT_Y;
        for (int i = 0; i < 5; i++) {
            g.drawText(g, Assets.characters, nameLines[i], HIGHSCORE_ITEM_X, y);
            g.drawText(g, Assets.characters, scoreLines[i], HIGHSCORE_SCORE_X, y);
            y += HIGHSCORE_ITEM_SPACING;
        }

        g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.buttons, BUTTON_RESET_X, BUTTON_RESET_Y, Assets.BUTTON_RESET_SCRX, Assets.BUTTON_RESET_SCRY, 2*Assets.BUTTON_WIDTH, 2*Assets.BUTTON_HEIGHT);
        
        if(DEBUG_BOUNDS == true){
        	drawDebugBounds(g, mBounds);
        }
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
    
    public void getSettingsData(){
    	for (int i = 0; i < 5; i++) {
        	nameLines[i] = (i + 1) + ". " + Settings.playerNames[i];
        	scoreLines[i] = "" + Settings.highscores[i];
        }
    }
    
    public void confirmReset() {
    	((AndroidGame)game).runOnUiThread(new Runnable() {
    		@Override
    		public void run() {
    			final AlertDialog.Builder alert = new AlertDialog.Builder((MrMunchGame)game);
    			alert.setTitle("Highscores reset!!!");
    			alert.setMessage("Do you really want to reset all scores stored?");
    			
    			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int whichButton) {
    					Settings.resetScore();
                		Settings.save(game.getFileIO());
                		getSettingsData();
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
