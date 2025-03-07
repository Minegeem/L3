package fr.uge.cards;

import java.util.List;
import java.util.Objects;

import fr.uge.tile.Animal;
import fr.uge.tile.GamePieces;

public class HawkRule {
	
//	public int HawkRuleManager(GridMaker grid, int ruleNumber) {
//		return switch(ruleNumber) {
//		case 1 -> HawkRuleA(grid);
//		default -> HawkRuleA(grid);
//		};
//	}
//	
//	private static int HawkRuleA(GridMaker grid) {
//		var score = 0;
//		for(int i = 0; i < grid.size(); i ++) {
//			for(int j = 0; j < grid.size(); j++) {
//				if(grid.coord(i,j).getAnimalFromList(Animal.BUSE) != null) {
//					if(checkConnections(grid, i, j) == 2) {
//						score += 2;
//					}
//				}
//			}
//		}
//		return score;
//	}
//	
//	private static int checkConnections(GridMaker grid, int height, int weight) {
//
//		Objects.requireNonNull(grid);
//		// 4 directions
//		var east = weight + 2;
//		var west = weight - 2;
//
//		if (height < 0 || height >= grid.size() || weight < 0 || weight >= grid.size()) // looks for coordinates to be
//		                                                                              // inside a grid and positive
//			return 0;
////		if (grid.get(height).get(weight) != null) // looks if a particular square is empty
////			return ;
//		
//		
//		// particular check for each direction
//
//		if (grid.coord(height + 1, weight + 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // south east
//			return 1;
//		}
//		if (grid.coord(height - 1, weight + 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // north east
//			return 1;
//		}
//		if (grid.coord(height - 1, weight - 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // north west
//			return 1;
//		}
//		if (grid.coord(height + 1, weight - 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // south west
//			return 1;
//		}
//		if (east < grid.size() && (grid.coord(height, east).getAnimalFromList(Animal.BUSE) == Animal.BUSE)) {
//			return 1;
//		}
//		if (west >= 0 && (grid.coord(height, west).getAnimalFromList(Animal.BUSE) == Animal.BUSE)) {
//			return 1;
//		}
//		return 2;
//	}
	
	public int HawkRuleManager(List<List<GamePieces>> grid, int ruleNumber) {
		return switch(ruleNumber) {
		case 1 -> HawkRuleA(grid);
		default -> HawkRuleA(grid);
		};
	}
	
	private static int HawkRuleA(List<List<GamePieces>> grid) {
		var score = 0;
		var count = 0;
		for(int i = 0; i < grid.size(); i ++) {
			for(int j = 0; j < grid.size(); j++) {
				if(grid.get(i).get(j) != null ) {
					System.out.println("etape 0 :" + grid.get(i).get(j));
					if(grid.get(i).get(j).getAnimalFromList(Animal.BUSE) != null && grid.get(i).get(j).getOccupied()) {
						System.out.println("etape 1 " + i + j);
						System.out.println("etape 1 :" + grid.get(i).get(j).getAnimal());
						if(checkConnections(grid, i, j) == 2 && grid.get(i).get(j).getAnimal().size() != 2) {
							System.out.println("etape 2");
							count ++;
							if(score == 0) {
								score = 2;
							}
							else if(score < 14) {
								score += 3;
							}
							else if(score >= 14 && score < 26 ) {
								score += 4;
							}
							else if(score >= 26) {
								score = 26;
							}
						}
					}
				}
			}
		}
		System.out.println(count);
		return score;
	}
	
	
	//This function check if around one hawk there is an other one hawk
	private static int checkConnections(List<List<GamePieces>> grid, int height, int weight) {

		Objects.requireNonNull(grid);
		// 4 directions
		var east = weight + 2;
		var west = weight - 2;

		if (height < 0 || height >= grid.size() || weight < 0 || weight >= grid.size()) // looks for coordinates to be
		                                                                              // inside a grid and positive
			return 0;
//		if (grid.get(height).get(weight) != null) // looks if a particular square is empty
//			return ;
//		if() {
//			System.out.println("etape 2.1");
//			return 0;
//		}
			

		
		// particular check for each direction
		if(grid.get(height + 1).get(weight + 1) != null) {
			if (grid.get(height + 1).get(weight + 1).getOccupied() && grid.get(height + 1).get(weight + 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // south east
				return 1;
			}
		}
		if(grid.get(height - 1).get(weight + 1) != null) {
			if (grid.get(height -1).get(weight + 1).getOccupied() && grid.get(height - 1).get(weight + 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // north east
				return 1;
			}
		}
		if(grid.get(height - 1).get(weight - 1) != null ) {
			if (grid.get(height -1).get(weight - 1).getOccupied() && grid.get(height - 1).get(weight - 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // north west
				return 1;
			}
		}
		if(grid.get(height + 1).get(weight - 1) != null ) {
			if (grid.get(height + 1).get(weight - 1).getOccupied() && grid.get(height + 1).get(weight - 1).getAnimalFromList(Animal.BUSE) == Animal.BUSE) { // south west
				return 1;
			}
		}
		if(grid.get(height).get(east) != null) {
			if (east < grid.size() && (grid.get(height).get(east).getAnimalFromList(Animal.BUSE) == Animal.BUSE) && grid.get(height).get(east).getOccupied()) {
				return 1;
			}
		}
		if(west >= 0 && grid.get(height).get(west) != null) {
			if (grid.get(height).get(west).getOccupied() && (grid.get(height).get(west).getAnimalFromList(Animal.BUSE) == Animal.BUSE)) {
				return 1;
			}
		}
		return 2;
	}
}
