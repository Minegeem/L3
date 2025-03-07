package fr.uge.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;

import fr.uge.tile.Animal;
import fr.uge.tile.Environment;
import fr.uge.tile.GamePieces;
import fr.uge.tile.Tile;

public class Button {
	
	private int i = 0;
	private int j = 0;
	private final String text;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	
	public Button (String text, int x, int y, int width, int height) {
		this.text = Objects.requireNonNull(text);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(Graphics2D graphics) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(x, y, width, height);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(x, y, width, height);
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + 10, y + 25);
	}

	private void buildSquare(Graphics2D graphics, int x, int y, int width, int height, GamePieces tile) {
		switch (tile.getEnvironments().get(0)) {
		case Environment.FORET -> {
			graphics.setColor(Color.GREEN);
			graphics.fillRect(x, y, width, height);
		}
		case Environment.MONTAGNE -> {
			graphics.setColor(Color.GRAY);
			graphics.fillRect(x, y, width, height);
		}
		case Environment.PRAIRIE -> {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.fillRect(x, y, width, height);
		}
		case Environment.MARAIS -> {
			graphics.setColor(Color.MAGENTA);
			graphics.fillRect(x, y, width, height);
		}
		case Environment.RIVIERE -> {
			graphics.setColor(Color.BLUE);
			graphics.fillRect(x, y, width, height);
		}
		default -> {
		}
		}
	}

	public void renderSquare(Graphics2D graphics, int i, int j, GamePieces tile) {
		this.i = i;
		this.j = j;
		if (tile == null) {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(x, y, width, height);
		} else {
			buildSquare(graphics, x, y, width, height, tile);
			for(var p = 0; p < tile.getAnimal().size(); p++) {
				buildSquareAnimals(graphics, x + p * 16, y , tile.getAnimal().get(p));
			}
		}
	}

	public void renderSquareTile(Graphics2D graphics, int i, int j, List<Tile> list) {
		for (var n = 0; n < list.size(); n++) {
			var newx = x;
			var newy = y;
			switch (n) {
			case 1 -> {
				newy -= 50;
			}
			case 2 -> {
				newx += 50;
			}
			}
			buildSquare(graphics, newx, newy, width, height, list.get(n));
			for(var p = 0; p < list.get(n).getAnimal().size(); p++) {
				buildSquareAnimals(graphics, newx + p * 16, newy, list.get(n).getAnimal().get(p));
			}
		}
	}
	
	public void renderCircleButton(int x, int y) {
		
	}
	
	public static void buildSquareAnimals(Graphics2D graphics, int x, int y, Animal animal) {
		switch (animal) {
		case Animal.BUSE -> {
			Color buseColor = new Color(160, 82, 45); 
			OnGame.drawAnimals(graphics, x, y, buseColor);
		}
		case Animal.OURS -> {
			Color oursColor = new Color(101, 67, 33);
			OnGame.drawAnimals(graphics, x, y, oursColor);
		}
		case Animal.RENARD -> {
			Color renardColor = new Color(255, 69, 0);
			OnGame.drawAnimals(graphics, x, y, renardColor);
		}
		case Animal.SAUMON -> {
			Color saumonColor = new Color(250, 128, 114); 
			OnGame.drawAnimals(graphics, x, y, saumonColor);
		}
		case Animal.WAPITI -> {
			Color wapitiColor = new Color(210, 180, 140);
			OnGame.drawAnimals(graphics, x, y, wapitiColor);
		}
		default -> {
		}
		}
	}
	    
        
        
    
     
	
	public void renderAnimals(int x, int y, GamePieces tile) {
		
	}

	public static void checkButtonClick(int clickX, int clickY, List<Button> buttons) {
		for (Button button : buttons) {
			if (button.isClicked(clickX, clickY)) {
				System.out.println("Button clicked: " + button);
			}
		}
	}

	public boolean isClicked(int clickX, int clickY) {
		return clickX >= x && (clickX <= x + width) && clickY >= y && (clickY <= y + height);
	}
	
	public int i() {
		return i;
	}
	public int j() {
		return j;
	}
	public int x() {
		return x;
	}
	public int y() {
		return y;
	}
	public int height() {
		return height;
	}
	public int width() {
		return width;
	}
}