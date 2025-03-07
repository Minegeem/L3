package fr.uge.cards;

import java.util.List;

import fr.uge.mecanics.Point;
import fr.uge.tile.Animal;
import fr.uge.tile.GamePieces;

public class BearRule {
	public int BearRuleManager(List<List<GamePieces>> grid, int ruleNumber) {
		return switch (ruleNumber) {
		case 1 -> BearRuleA(grid);
		default -> BearRuleA(grid);
		};
	}

	public int BearRuleA(List<List<GamePieces>> grid) {
		var bearPair = 0;
		for (int i = 0; i < grid.size(); i++) {
			for (int j = 0; j < grid.size(); j++) {
				if (checkThisSquare(grid, i, j)) { // This square is occupied by a bear
					if (checkThisSquare(grid, i, j + 2) || checkThisSquare(grid, i + 1, j + 1)) { // Potential pair of bears
						if(checkBearDuo(grid, i, j + 2) && checkBearDuo(grid, i, j)) { //Only 2 cases because it's impossible to get 2 true at the same 
							bearPair ++;
						}
						if(checkBearDuo(grid, i + 1, j + 1)  && checkBearDuo(grid, i, j)) {
							bearPair ++;
						}
					}
				}
			}
		}
		return scoringA(bearPair);
	}

	private static int scoringA(int numberOfBears) {
		return switch(numberOfBears) {
		case 1 -> 4;
		case 2 -> 11;
		case 3 -> 19;
		case 4 -> 27;
		default -> 0; // 2 is a default case because we are sure that we begin with at least 1 wapiti
		};
	}

	// This function check if a square at x,y is occupied by a bear
	private static boolean checkThisSquare(List<List<GamePieces>> grid, int height, int weight) {
		if (height > 0 && height < grid.size() && weight > 0 && weight < grid.size()) {
			if (grid.get(height).get(weight) != null) {
				if (grid.get(height).get(weight).getAnimalFromList(Animal.OURS) != null
				    && grid.get(height).get(weight).getOccupied()) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean checkBearDuo(List<List<GamePieces>> grid, int height, int weight) {
		var west = new Point(height, weight + 2);
		var east = new Point(height, weight - 2);
		var northWest = new Point(height + 1, weight - 1);
		var northEast = new Point(height + 1, weight + 1);
		var southWest = new Point(height - 1, weight - 1);
		var southEast = new Point(height - 1, weight + 1);
		var bearCounter = 0;// number of bear that surround a particular bear
		var possibleBearPositions = List.of(west, east, northEast, northWest, southWest, southEast);
		for(var position: possibleBearPositions) {
			if(checkThisSquare(grid, position.x(), position.y())) {
				bearCounter ++;
			}
		}
		return (bearCounter == 1);
	}
}
