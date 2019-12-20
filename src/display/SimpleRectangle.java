package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class SimpleRectangle extends Sprite {
	private Rectangle mRectangle;
	private Color mColor;
	
	public SimpleRectangle(int aX, int aY, int aWidth, int aHeight, Color aColor) {
		mRectangle = new Rectangle(aX, aY, aWidth, aHeight);
		mColor = aColor;
	}

	@Override
	public void Draw(Graphics aGraphics) {
		aGraphics.setColor(mColor);
		aGraphics.fillRect(mRectangle.x, mRectangle.y, mRectangle.width, mRectangle.height);
	}
}
