package fr.uge.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
	// a method that take a player choice and try to transform into a number
	public int makeAchoice() {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		var choice = "";
		try {
			System.out.print("Faites le choix entre les options proposées : ");
			choice = reader.readLine();
		} catch (IOException e) {
			System.out.println("Erreur de lecture : " + e.getMessage());
		}
		return parseIntTest(choice);
	}

	// a method that take a player choice and try to transform into a number
	// can show a particular message
	public int makeAchoice(String message) {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		var choice = "";
		try {
			System.out.print(message);
			choice = reader.readLine();
		} catch (IOException e) {
			System.out.println("Erreur de lecture : " + e.getMessage());
		}
		return parseIntTest(choice);
	}
	
	// that function take a String and try to parse it into an Integer
	// return Integer of text or -1 if function failed
	private int parseIntTest(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
}
