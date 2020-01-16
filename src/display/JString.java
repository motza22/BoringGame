package display;

import java.awt.Color;
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

	public void SetText(String aString) {
		mString = aString;
	}

	@Override
	public void Draw(Graphics aGraphics) {
		aGraphics.setColor(Color.BLACK);
		aGraphics.drawString(mString, mX, mY);
	}
}
