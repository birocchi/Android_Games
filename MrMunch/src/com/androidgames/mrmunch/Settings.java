package com.androidgames.mrmunch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.SparseArray;

import com.androidgames.framework.FileIO;

public class Settings {
    public static boolean soundEnabled = true;
    public static int[] highscores = new int[] { 0, 0, 0, 0, 0 };
    public static String[] playerNames = new String[] { ".....", ".....", ".....", ".....", "....." };
    public static int gameSpeed = 3;
	public static SparseArray<Achievement> achievementsList;
	static{
		achievementsList = new SparseArray<Achievement>();
    	achievementsList.append(1, new Achievement("Beginner", "Get 100 points", Assets.achievement1));
    	achievementsList.append(2, new Achievement("Intermediate", "Get 500 points", Assets.achievement2));
    	achievementsList.append(3, new Achievement("Expert", "Get 800 points", Assets.achievement3));
    	achievementsList.append(4, new Achievement("Master", "Get 1000 points", Assets.achievement4));
    	achievementsList.append(5, new Achievement("Tetris Square", "Finish like the Square Tetris piece, using at least the speed 2", Assets.achievement5));
    	achievementsList.append(6, new Achievement("Tetris Line", "Finish like the Line Tetris piece, using at least the speed 2", Assets.achievement6));
    	achievementsList.append(7, new Achievement("Tetris L", "Finish like the L Tetris piece, using at least the speed 2", Assets.achievement7));
    	achievementsList.append(8, new Achievement("Tetris S", "Finish like the S Tetris piece, using at least the speed 2", Assets.achievement8));
    	achievementsList.append(9, new Achievement("Tetris T", "Finish like the T Tetris piece, using at least the speed 2", Assets.achievement9));
    	//achievementsList.append(10,new Achievement("Pursuer", "Follow your tail for 20 steps", Assets.achievement5));
	}

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    files.readFile(".mrmunch")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
            for (int i = 0; i < 5; i++) {
                playerNames[i] = in.readLine();
            }
            gameSpeed = Integer.parseInt(in.readLine());
            for (int i = 1; i <= achievementsList.size(); i++) {
                achievementsList.get(i).isCompleted = Boolean.parseBoolean(in.readLine());
            }
            
        } catch (IOException e) {
            // :( It's ok we have defaults
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(".mrmunch")));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }
            for (int i = 0; i < 5; i++) {
                out.write(playerNames[i]);
                out.write("\n");
            }
            out.write(Integer.toString(gameSpeed));
            out.write("\n");
            for (int i = 1; i <= achievementsList.size(); i++) {
                out.write(Boolean.toString(achievementsList.get(i).isCompleted));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static void addScore(int score, String playerName) {
    	if(playerName != null){
    		for (int i = 0; i < 5; i++) {
    			if (highscores[i] < score) {
    				for (int j = 4; j > i; j--){
    					highscores[j] = highscores[j - 1];
    					playerNames[j] = playerNames[j - 1];
    				}
    				highscores[i] = score;
    				playerNames[i] = playerName;
    				break;
    			}
    		}
    	}
    }
    
    public static void resetScore() {
    	for (int i = 0; i < 5; i++) {
    		highscores[i] = 0;
    		playerNames[i] = ".....";
    	}
    	for(int j=1;j<=9;j++)
    		Settings.achievementsList.get(j).isCompleted = false;
    }
}
