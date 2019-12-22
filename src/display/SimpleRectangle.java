package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class SimpleRectangle extends Sprite {
	protected Rectangle mRectangle;
	protected Color mColor;

	public SimpleRectangle(int aX, int aY, int aWidth, int aHeight, Color aColor) {
		mRectangle = new Rectangle(aX, aY, aWidth, aHeight);
		mColor = aColor;
	}

	public SimpleRectangle(Rectangle aRectangle, Color aColor) {
		mRectangle = aRectangle;
		mColor = aColor;
	}

	public final Rectangle GetRectangle() {
		return mRectangle;
	}

	@Override
	public void Draw(Graphics aGraphics) {
		aGraphics.setColor(mColor);
		aGraphics.fillRect(mRectangle.x, mRectangle.y, mRectangle.width, mRectangle.height);
	}
}
