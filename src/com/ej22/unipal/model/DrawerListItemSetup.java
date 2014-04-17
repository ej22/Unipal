package com.ej22.unipal.model;

public class DrawerListItemSetup
{

	private String title;
	private int icon;

	public DrawerListItemSetup()
	{
	}

	public DrawerListItemSetup(String title, int icon)
	{
		this.title = title;
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public int getIcon() {
		return icon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}
