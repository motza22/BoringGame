package display;

import java.awt.Graphics;

public abstract class Sprite {
	private static int sIdKey = 0;
	public final int ID = sIdKey++;
	
	public abstract void Draw(Graphics aGraphics);
}
