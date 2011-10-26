package com.androidgames.mrmunch;

import com.androidgames.framework.Pixmap;

public class Achievement {
	public boolean isCompleted;
	public String title;
	public String description;
	public Pixmap logo;
	
	public Achievement(String title, String description, Pixmap logo) {
		isCompleted = false;
		this.title = title;
		this.description = description;
		this.logo = logo;
	}
}
