package fr.uge.cards;

import java.util.ArrayList;
import java.util.List;

import fr.uge.mecanics.Point;
import fr.uge.tile.Animal;
import fr.uge.tile.GamePieces;

public class DeerRule {

	private static final ArrayList<Point> wapitiPositions = new ArrayList<>();

	public int DeerRuleManager(List<List<GamePieces>> grid, int ruleNumber) {
		return switch (ruleNumber) {
		case 1 -> DeerRuleA(grid);
		default -> DeerRuleA(grid);
		};
	}

	public int DeerRuleA(List<List<GamePieces>> grid) {
		var score = 0;
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				if(checkThisSquare(grid, i, j)) {
					var point = new Point(i,j);
					if(grid.get(i).get(j).getAnimal().size() == 1 && !wapitiPositions.contains(point)) {
						score += checkConnections(grid, i, j);
					}
				}
			}
		}
		return score;
	}

	private static int checkConnections(List<List<GamePieces>> grid, int height, int weight) {
		var wapitiCounter = 1;
		var east = weight + 2;
		var step = 2;
		var actualWapitiPosition = new Point(height,weight); 
		wapitiPositions.add(actualWapitiPosition);// adding actual Wapiti position
		if(east < grid.size()) { // We can never go back so no need to check if can go in negative
			for(int i = 0; i < 3; i ++ ) { // We add +2 only 3 times because when we launch checkConnections we already known that we are on the Wapiti token
				var actualPosition = new Point(height,weight + step); // actual position of for loop
				if(checkThisSquare(grid, height, weight + step) && !wapitiPositions.contains(actualPosition)) {
					var point = new Point(height,weight + step);
					step += 2;
					wapitiCounter++;
					wapitiPositions.add(point);
				}
				else {
					break;
				}
			}
		}
		return switch(wapitiCounter) {
		case 1 -> 2;
		case 2 -> 5;
		case 3 -> 9;
		case 4 -> 13;
		default -> 2; // 2 is a default case because we are sure that we begin with at least 1 wapiti
		};
	}

	
	// This function check if a square at x,y is occupied by a wapiti 
	private static boolean checkThisSquare(List<List<GamePieces>> grid, int height, int weight) {
		if(height > 0 && height < grid.size() && weight > 0 && weight < grid.size()) {
			if (grid.get(height).get(weight) != null) {
				if (grid.get(height).get(weight).getAnimalFromList(Animal.WAPITI) != null
				    && grid.get(height).get(weight).getOccupied()) {
					return true;
				}
			}
		}
		return false;
	}
}
