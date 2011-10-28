package com.androidgames.mrmunch;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.AndroidGame;
import com.androidgames.framework.impl.AndroidPixmap;

public class AchievementsListScreen extends Screen {
	
	private final int ACHIEVEMENTS_IMAGE_X = 64;
	private final int ACHIEVEMENTS_IMAGE_Y = 20;
	
	private final int ACHIEVEMENTS_SCREEN_INIT = ACHIEVEMENTS_IMAGE_Y + Assets.MENU_ITEM_HEIGHT + 3;
	private final int ACHIEVEMENTS_SCREEN_END = game.getGraphics().getHeight() - Assets.BUTTON_HEIGHT;
	
	private final int BUTTON_BACK_X = 128;
	private final int BUTTON_BACK_Y = 416;
	
	private final int ICON_SPACING_X = 5;
	private final int ICON_SPACING_Y = 5;
	
	private final int ACHIEVEMENT_ICON_X = 0 + ICON_SPACING_X;
	private final int ACHIEVEMENT_ICON_Y = ACHIEVEMENTS_SCREEN_INIT + ICON_SPACING_Y;
	
	private SparseArray<Bounds> mBounds;
	
	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_BACK = 0;

	private int screenTopPosition;
	private int screenBottomPosition;
	private int fingerXPosition, fingerYPosition;
	private int fingerXVariation, fingerYVariation;
	private boolean isClick;
	private int listSize;
	
	
    public AchievementsListScreen(Game game) {
    	super(game);
    	
    	mBounds = new SparseArray<Bounds>();
        mBounds.append(0, new Bounds(CLICK_BACK, BUTTON_BACK_X, BUTTON_BACK_Y, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
        for(int i=1; i <= Settings.achievementsList.size(); i++){
            mBounds.append(i, new Bounds(i,
            		                     ACHIEVEMENT_ICON_X + ((i-1)%2)*Assets.ACHIEVEMENT_ICON_WIDTH  + ((i-1)%2)*2*ICON_SPACING_X,
            		                     ACHIEVEMENT_ICON_Y + ((i-1)/2)*Assets.ACHIEVEMENT_ICON_HEIGHT + ((i-1)/2)*ICON_SPACING_Y,
            		                     Assets.ACHIEVEMENT_ICON_WIDTH,
            		                     Assets.ACHIEVEMENT_ICON_HEIGHT)
                                        );
        }
        
    	listSize = (Settings.achievementsList.size()+1)/2 * (Assets.ACHIEVEMENT_ICON_HEIGHT + ICON_SPACING_Y) + ICON_SPACING_Y;
    	screenTopPosition = ACHIEVEMENTS_IMAGE_Y + Assets.MENU_ITEM_HEIGHT + 3;
    	screenBottomPosition = screenTopPosition + listSize;
    }

    @Override
    public void update(float deltaTime) {
    	
    	int fingerDeltaX, fingerDeltaY;
    	int screenDelta;
    	
    	int len, clickEvent;
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    	List<KeyEvent> keyEvents = game.getInput().getKeyEvents();

    	//Dealing with KEY EVENTS
    	len = keyEvents.size();
    	for(int i=0; i<len; i++){
    		KeyEvent kevent = keyEvents.get(i);
    		if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK  && kevent.type == KeyEvent.KEY_UP)
    			game.setScreen(new MainMenuScreen(game));
    	}
    	//Dealing with TOUCH EVENTS
    	len = touchEvents.size();
    	for (int i = 0; i < len; i++) {
    		TouchEvent event = touchEvents.get(i);
    		
    		//TOUCH DOWN
    		if (event.type == TouchEvent.TOUCH_DOWN && event.pointer == 0) {
    			fingerXPosition = event.x;
    			fingerYPosition = event.y;
    			fingerXVariation = 0;
    			fingerYVariation = 0;
    			isClick = true;
    		}
    		
    		//TOUCH UP
    		if (event.type == TouchEvent.TOUCH_UP) {
    			clickEvent = eventInBounds(mBounds, event);
    			
    			switch(clickEvent){
    			case CLICK_BACK:
    				if(Settings.soundEnabled)
        				Assets.click.play(1);
    				game.setScreen(new MainMenuScreen(game));
    				break;
    			case CLICK_NO_EVENT:
    				//Nothing is done
    				for(int j=1;j<=10;j++)
    					Settings.achievementsList.get(j).isCompleted = true;
    				break;
    			default:
    				//Show details only if the click was inside the list view
    				if(event.y < ACHIEVEMENTS_SCREEN_END && event.y > ACHIEVEMENTS_SCREEN_INIT && isClick){
    					if(Settings.soundEnabled)
            				Assets.click.play(1);
    					showAchievementDetail(Settings.achievementsList.get(clickEvent));
    				}
    				break;
    			}
    		}
    		
    		//TOUCH DRAGGED
    		if (event.type == TouchEvent.TOUCH_DRAGGED && event.pointer == 0) {

    			fingerDeltaX = fingerXPosition - event.x;
    			fingerDeltaY = fingerYPosition - event.y;
    			
    			//Calculate if it was a click or not
    			if(fingerXVariation < 10 && fingerYVariation < 10){
    				fingerXVariation += Math.abs(fingerDeltaX);
        			fingerYVariation += Math.abs(fingerDeltaY);
    			}
    			else {
    				isClick = false;
    			}

    			int oldScreenTopPosition = screenTopPosition;
    			
    			screenTopPosition -= fingerDeltaY;
    			screenBottomPosition -= fingerDeltaY;
    			
    			//Check if the list can walk up/down or not
    			if(screenBottomPosition <= ACHIEVEMENTS_SCREEN_END){
    				screenTopPosition = ACHIEVEMENTS_SCREEN_END - listSize;
    				screenBottomPosition = ACHIEVEMENTS_SCREEN_END;
    			}
    			if(screenTopPosition > ACHIEVEMENTS_SCREEN_INIT){
    				screenTopPosition = ACHIEVEMENTS_SCREEN_INIT;
    				screenBottomPosition = screenTopPosition + listSize;
    			}
    			
    			screenDelta = oldScreenTopPosition - screenTopPosition;
    			
    			fingerXPosition = event.x;
    			fingerYPosition = event.y;

    			//Update all the bounds accordingly
    			for(int j=1; j < mBounds.size(); j++){
    				Bounds newBound = mBounds.get(j);
    				updateBound(newBound, newBound.x, newBound.y -= screenDelta);
    			}
    		}
    	}
    }

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		Achievement achievement;
		Paint paint = new Paint();
		
		g.clear(Color.BLACK);
		for(int i=1, y=5, x=0; i <= Settings.achievementsList.size(); i++){
			achievement = Settings.achievementsList.get(i);
			
			if(!achievement.isCompleted){
				paint.setAlpha(100);
				ColorMatrix cm = new ColorMatrix();
				cm.setSaturation(0);
				paint.setColorFilter(new ColorMatrixColorFilter(cm));
			} else {
				paint.reset();
			}
			
			x = (i-1)%2;
			g.drawPixmap(achievement.logo, 5 + x*(10 + Assets.ACHIEVEMENT_ICON_WIDTH), screenTopPosition + y,paint);
			if(x == 1)
				y += Assets.ACHIEVEMENT_ICON_HEIGHT + 5;
		}
		
		g.drawRect(0, 0, g.getWidth(), ACHIEVEMENTS_SCREEN_INIT, Color.BLACK);
		g.drawPixmap(Assets.mainMenu, ACHIEVEMENTS_IMAGE_X, ACHIEVEMENTS_IMAGE_Y, Assets.ACHIEVEMENTS_SCRX, Assets.ACHIEVEMENTS_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);
		g.drawRect(0, ACHIEVEMENTS_SCREEN_END, g.getWidth(), g.getHeight(), Color.BLACK);
		g.drawPixmap(Assets.buttons, BUTTON_BACK_X, BUTTON_BACK_Y, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
		
		if(DEBUG_BOUNDS == true){
        	drawDebugBounds(g, mBounds);
        }
	}

	private void showAchievementDetail(Achievement achievement) {
		
		final Achievement currentAchievement = achievement;
		
		((AndroidGame)game).runOnUiThread(new Runnable() {
    		@Override
    		public void run() {
    			Display display = ((Activity)game).getWindowManager().getDefaultDisplay(); 
    			int realScreenWidth = display.getWidth();

    			final AlertDialog.Builder alert = new AlertDialog.Builder((MrMunchGame)game);
    			
    			ScrollView scrollView = new ScrollView((MrMunchGame)game);
    			
    			LinearLayout ll = new LinearLayout((MrMunchGame)game);
    			ll.setOrientation(LinearLayout.VERTICAL);
    			scrollView.addView(ll);
    			
    			ImageView iv = new ImageView((MrMunchGame)game);
    			iv.setImageBitmap(Bitmap.createScaledBitmap(((AndroidPixmap)(currentAchievement.logo)).getBitmap(), (3*realScreenWidth)/5, (3*realScreenWidth)/5, true));
    			ll.addView(iv);
    			
    			TextView tv = new TextView((MrMunchGame)game);
    			tv.setTextSize(35);
    			tv.setText("\n" + currentAchievement.description);
    			ll.addView(tv);
    			
    			alert.setView(scrollView);
    			alert.setNeutralButton("Back", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int whichButton) {
    					//Do nothing
    				}
    			});
    			
    			alert.show();
    		}

    	});
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
