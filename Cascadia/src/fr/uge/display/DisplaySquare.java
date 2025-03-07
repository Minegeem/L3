package fr.uge.display;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.ScreenInfo;

import fr.uge.mecanics.Point;
import fr.uge.tile.GridMaker;

public final class DisplaySquare implements DisplayShape {

	private static List<Button> buttonsPlateau = new ArrayList<Button>();

	public void graphicDisplay(Graphics2D graphics, GridMaker grid, ScreenInfo screenInfo, Point cursor) {
	    int board = 900;
	    int buttonSize = 50; 
	    int centerX = (screenInfo.width() - board) / 2;
	  	int centerY = (screenInfo.height() - board) / 2;
	    buttonsPlateau.clear();
	    var x = 17;
	    for (int i = (cursor.x() - x); i <= (cursor.x() + x); i += 2) {
	        for (int j = (cursor.y() - x); j <= (cursor.y() + x); j += 2) {
                String buttonText = "Button (" + i + ", " + j + ")";
                int buttonX = centerX + (((i -(cursor.x() - x)) / 2) * buttonSize);
                int buttonY = centerY + (((j -(cursor.y() - x)) / 2)* buttonSize);
                Button newButton = new Button(buttonText, buttonX, buttonY, buttonSize, buttonSize);
                buttonsPlateau.add(newButton);
                newButton.renderSquare(graphics, j, i, grid.coord(j, i));
                if(grid.coord(j,i) != null) {
	                for(int p = 0;  p < grid.coord(j, i).getAnimal().size(); p++) {
	                	Button.buildSquareAnimals(graphics, buttonX + p * 16, buttonY, grid.coord(j, i).getAnimal().get(p));
	                }
                }
	        }
	    }
	}
	
	public static List<Button> buttonplateau(){
		return buttonsPlateau;
	}
}
