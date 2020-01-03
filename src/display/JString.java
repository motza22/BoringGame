package display;

import java.awt.Graphics;

public class JString extends Sprite {
	private String mString;
	private int mX;
	private int mY;

	public JString(int aX, int aY, String aString) {
		mString = aString;
		mX = aX;
		mY = aY;
	}

	@Override
	public void Draw(Graphics aGraphics) {
		aGraphics.drawString(mString, mX, mY);
	}
}
