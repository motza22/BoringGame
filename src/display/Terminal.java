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
		Terminal terminal = new Terminal(aString, 0 );
		terminal.start();
	}

	public static void DisplayMenuWResponse(String aString, int aInputRange) {
		Terminal terminal = new Terminal(aString + "\n> ", aInputRange);
		terminal.start();
	}

	@Override
	public void run() {
		System.out.print(mString);
		if(mInputRange > 0) {
			Game.GetInstance().Notify(mScanner.nextInt());
		}
	}
}
