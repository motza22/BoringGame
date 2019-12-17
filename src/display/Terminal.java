package display;

import java.util.Scanner;

import core.Game;

public class Terminal extends Thread {
	private Scanner mScanner = new Scanner(System.in);
	private String mString = null;
	private int mInputRange = 0;
	
	private Terminal(String aString) {
		mString = aString;
		mInputRange = 0;
	}
	
	private Terminal(String aString, int aInputRange) {
		mString = aString;
		mInputRange = aInputRange;
	}
	
	public static void DisplayMenuHeader(MenuOption aMenuOption) {
		Terminal t = new Terminal(aMenuOption.mString);
		t.start();
	}
	
	public static void DisplayMenuOptions(MenuOption... aOptions) {
		String string = "";
		int inputRange = 0;
		
		for(MenuOption option : aOptions) {
			string += option.mString + "\n";
			inputRange++;
		}
		
		Terminal t = new Terminal(string, inputRange);
		t.start();
	}
	
	@Override
	public void run() {
		System.out.println(mString);
		if(mInputRange > 0) {
			Game.GetInstance().Notify(mScanner.nextInt());
		}
	}
}
