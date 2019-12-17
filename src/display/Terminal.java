package display;

import java.util.Scanner;

import core.Game;

public class Terminal extends Thread {
	private Scanner mScanner = new Scanner(System.in);
	private String mString = null;
	private int mInputRange = 0;
	
	private Terminal(String aString, int aInputRange) {
		mString = aString;
		mInputRange = aInputRange;
	}
	
	public static void DisplayMenu(String aString) {
		Terminal t = new Terminal(aString, 0 );
		t.start();
	}
	
	public static void DisplayMenuWResponse(String aString, int aInputRange) {
		Terminal t = new Terminal(aString + "\n> ", aInputRange);
		t.start();
	}
	
	@Override
	public void run() {
		System.out.print(mString);
		if(mInputRange > 0) {
			Game.GetInstance().Notify(mScanner.nextInt());
		}
	}
}
