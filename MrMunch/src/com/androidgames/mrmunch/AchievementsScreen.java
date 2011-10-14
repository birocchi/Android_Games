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

public class AchievementsScreen extends Screen {
	
	public SparseArray<Achievement> achievementsList;
	
	private int screenVerticalPosition;
	private int fingerPosition;

    public AchievementsScreen (Game game) {
    	super(game);
    	achievementsList = new SparseArray<Achievement>();
    	screenVerticalPosition = 0;
    	    	
    	achievementsList.append(0, new Achievement("Begginer", "Got 100 points", Assets.fruit1));
    	achievementsList.append(1, new Achievement("Intermediate", "Got 500 points", Assets.fruit2));
    	achievementsList.append(2, new Achievement("Expert", "Got 1000 points", Assets.fruit3));
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
    		}
    		if (event.type == TouchEvent.TOUCH_DRAGGED && event.pointer == 0) {
    			delta = fingerPosition - event.y;
    			screenVerticalPosition -= delta;
    			fingerPosition = event.y;
    		}
    	}
    }

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.clear(Color.BLACK);
		Achievement achievement;
		for(int i=0, y=0; i < achievementsList.size(); i++){
			achievement = achievementsList.get(i);
			Paint paint = new Paint();
			
			if(!achievement.isCompleted){
				paint.setAlpha(100);
				ColorMatrix cm = new ColorMatrix();
				cm.setSaturation(0);
				paint.setColorFilter(new ColorMatrixColorFilter(cm));
			}
			g.drawPixmap(achievement.logo, 10, screenVerticalPosition + y,paint);
			g.drawText(g, Assets.characters, achievementsList.get(i).title, 15 + achievementsList.get(i).logo.getWidth(), screenVerticalPosition + y, paint);
			g.drawText(achievementsList.get(i).description, 10, screenVerticalPosition + y + 50 , Color.WHITE, 20, paint.getAlpha());
			y += 2*Assets.CHARACTER_HEIGHT;
			g.drawLine(0, screenVerticalPosition + y-5, g.getWidth(), screenVerticalPosition + y-5, Color.WHITE);
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
