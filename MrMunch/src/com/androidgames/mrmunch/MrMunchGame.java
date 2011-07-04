package com.androidgames.mrmunch;

import com.androidgames.framework.Screen;
import com.androidgames.framework.impl.AndroidGame;

public class MrMunchGame extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
}