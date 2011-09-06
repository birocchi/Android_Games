package com.androidgames.accelerateit;

import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.AndroidGame;


public class AccelerateItGame extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
}
