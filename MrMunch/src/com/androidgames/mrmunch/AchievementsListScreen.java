package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.SparseArray;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;

public class AchievementsListScreen extends Screen {
	
	private final int ACHIEVEMENTS_IMAGE_X = 64;
	private final int ACHIEVEMENTS_IMAGE_Y = 20;
	
	private final int ACHIEVEMENTS_INITIAL_POS = ACHIEVEMENTS_IMAGE_Y + Assets.MENU_ITEM_HEIGHT + 3;
	
	public SparseArray<Achievement> achievementsList;
	
	private int screenTopPosition;
	private int screenBottomPosition;
	private int fingerPosition;
	private int listSize;

    public AchievementsListScreen (Game game) {
    	super(game);
    	achievementsList = new SparseArray<Achievement>();
    	    	
    	achievementsList.append(0, new Achievement("Begginer", "Got 100 points", Assets.achievement0));
    	achievementsList.append(1, new Achievement("Intermediate", "Got 500 points", Assets.achievement0));
    	achievementsList.append(2, new Achievement("Expert", "Got 1000 points", Assets.achievement0));
    	achievementsList.append(3, new Achievement("Master", "Got 1500 points", Assets.achievement0));
    	achievementsList.append(4, new Achievement("Tetris Square", "Finish like the Square Tetris piece", Assets.achievement0));
    	achievementsList.append(5, new Achievement("Tetris Stick", "Finish like the Stick Tetris piece", Assets.achievement0));
    	achievementsList.append(6, new Achievement("Tetris L", "Finish like the L Tetris piece", Assets.achievement0));
    	achievementsList.append(7, new Achievement("Tetris S", "Finish like the S Tetris piece", Assets.achievement0));
    	achievementsList.append(8, new Achievement("Tetris T", "Finish like the T Tetris piece", Assets.achievement0));
    	achievementsList.append(9, new Achievement("Pursuer", "Follow your tail for 20 steps", Assets.achievement0));
    	
    	listSize = (achievementsList.size()+1)/2 * 155 + 5;
    	screenTopPosition = ACHIEVEMENTS_IMAGE_Y + Assets.MENU_ITEM_HEIGHT + 3;
    	screenBottomPosition = screenTopPosition + listSize;
    }

    @Override
    public void update(float deltaTime) {
    	int delta;
    	int len;
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
    		if (event.type == TouchEvent.TOUCH_DOWN && event.pointer == 0) {
    			fingerPosition = event.y;
    			achievementsList.get(0).AchievementCompleted();
    		}
    		if (event.type == TouchEvent.TOUCH_DRAGGED && event.pointer == 0) {
    			delta = fingerPosition - event.y;
    			screenTopPosition -= delta;
    			screenBottomPosition -= delta;
    			if(screenBottomPosition <= game.getGraphics().getHeight()){
    				screenTopPosition = game.getGraphics().getHeight() - listSize;
    				screenBottomPosition = game.getGraphics().getHeight();
    			}
    			if(screenTopPosition > ACHIEVEMENTS_INITIAL_POS){
    				screenTopPosition = ACHIEVEMENTS_INITIAL_POS;
    				screenBottomPosition = screenTopPosition + listSize;
    			}
    			fingerPosition = event.y;
    		}
    	}
    }

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		Achievement achievement;
		
		g.clear(Color.BLACK);
		for(int i=0, y=5, x=0; i < achievementsList.size(); i++){
			achievement = achievementsList.get(i);
			Paint paint = new Paint();
			
			if(!achievement.isCompleted){
				paint.setAlpha(100);
				ColorMatrix cm = new ColorMatrix();
				cm.setSaturation(0);
				paint.setColorFilter(new ColorMatrixColorFilter(cm));
			}
			x = i%2;
			g.drawPixmap(achievement.logo, 5 + x*(10 + achievement.logo.getWidth()), screenTopPosition + y,paint);
			if(x == 1)
				y += achievement.logo.getHeight() + 5;
			g.drawRect(0, 0, g.getWidth(), ACHIEVEMENTS_INITIAL_POS, Color.BLACK);
			g.drawPixmap(Assets.mainMenu, ACHIEVEMENTS_IMAGE_X, ACHIEVEMENTS_IMAGE_Y, Assets.ACHIEVEMENTS_SCRX, Assets.ACHIEVEMENTS_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
