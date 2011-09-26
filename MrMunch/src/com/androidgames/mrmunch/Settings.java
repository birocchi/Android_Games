package com.androidgames.mrmunch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.androidgames.framework.FileIO;

public class Settings {
    public static boolean soundEnabled = true;
    public static int[] highscores = new int[] { 0, 0, 0, 0, 0 };
    public static String[] playerNames = new String[] { ".....", ".....", ".....", ".....", "....." };
    public static int gameSpeed = 3;

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
    }
}
