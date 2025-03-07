package fr.uge.hexagon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.cards.BearRule;
import fr.uge.mecanics.Point;
import fr.uge.tile.Animal;
import fr.uge.tile.AnimalToken;
import fr.uge.tile.Environment;
import fr.uge.tile.GamePieces;
import fr.uge.tile.StartTile;
import fr.uge.tile.Tile;

public class TestHexagon {

	private static final List<List<GamePieces>> grid = new ArrayList<>();

	public static void initializeGrid(int height, int weight) {
		if (height < 0 || weight < 0) {
			throw new IllegalArgumentException("Height and Weight value must be postive");
		}
		for (int i = 0; i < height; i++) {
			List<GamePieces> field = new ArrayList<>();

			// Fill lists with null
			for (int j = 0; j < weight; j++) {
				field.add(null);
			}
			grid.add(field);
		}
	}

	public static boolean checkConnections(List<List<GamePieces>> grid, int height, int weight) {

		Objects.requireNonNull(grid);
		// 4 directions
		var east = weight + 2;
		var west = weight - 2;

		if (height < 0 || height >= grid.size() || weight < 0 || weight >= grid.size()) // looks for coordinates to be
		                                                                              // inside a grid and positive
			return false;
		if (grid.get(height).get(weight) != null) // looks if a particular square is empty
			return false;
		
		
		// particular check for each direction

		if (height + 1 < grid.size() && weight + 1 < grid.size() && grid.get(height + 1).get(weight + 1) != null) { // south east
			System.out.println("south east");
			return true;
		}
		if (height - 1 >= 0 && weight + 1 < grid.size() && grid.get(height - 1).get(weight + 1) != null) { // north east
			System.out.println("north east");
			return true;
		}
		if (height - 1 >= 0 && weight - 1 > 0 && grid.get(height - 1).get(weight - 1) != null) { // north west
			System.out.println("north west");
			return true;
		}
		if (height + 1 < grid.size() && weight - 1 >= 0 && height < grid.size() && grid.get(height + 1).get(weight - 1) != null) { // south west
			System.out.println("south east");
			return true;
		}

//    if (south < grid.size() && (grid.get(south).get(weight) != null)) { 
//      return true;
//    }
//    if (north >= 0 && (grid.get(north).get(weight) != null)) {
//      return true;
//    }

		if (east < grid.size() && (grid.get(height).get(east) != null)) {
			return true;
		}
		if (west >= 0 && (grid.get(height).get(west) != null)) {
			return true;
		}
		return false;
	}

	public static boolean addTile(int height, int weight, GamePieces tile) {
		Objects.requireNonNull(tile);
		if (checkConnections(grid, height, weight)) { // if true we add a tile
			grid.get(height).set(weight, tile);
			return true;
		}
		return false;
	}

	public static boolean placeFirstTile(int height, int weight, StartTile tile) {
		Objects.requireNonNull(tile);
		if (height < 0 || weight < 0) {
			// throw new IllegalStateException("height and weight must be positive");
			return false;
		}
		for (var i = 0; i < tile.size(); i += 3) {
			grid.get(height + i).set(weight + i, tile.pos(i));
			grid.get(height - 1).set(weight - 1 + i, tile.pos(i + 1));
			grid.get(height - 1 + i).set(weight + 1, tile.pos(i + 2));
		}
		return true;
	}

	private static String addcoord(List<List<GamePieces>> grid, int i, int j) {
		var valeur = grid.get(i).get(j);
		var line = new StringBuilder();
		if (valeur == null) {
			if (checkConnections(grid, j, i)) {
				line.append("|");
				var newi = "" + i;
				var newj = "" + j;
				if (i < 10) {
					newi = "O" + i;
				}
				if (j < 10) {
					newj = "O" + j;
				}
				line.append(newj + ", " + newi);
			}
		} else {
			if (valeur.getEnvironments().get(0).equals(Environment.MARAIS)) {
				var actualTile = new StringBuilder(valeur.getValue());
				actualTile.setCharAt(0, Character.toLowerCase(actualTile.charAt(0)));
				line.append("|").append(actualTile).append(" ");
			} else {
				line.append("|").append(valeur.getValue()).append(" ");
			}
		}
		return line.toString();
	}

// return a minimum number of spaces 
	private static int minSpace(List<List<GamePieces>> grid) {
		var countspacemin = grid.size();
		for (var i = 0; grid.size() > i; i += 2) {
			for (var j = 0; grid.get(i).size() > j; j += 2) {
				if (grid.get(j).get(i) != null || checkConnections(grid, j, i)) {
					if (countspacemin > j) {
						countspacemin = j;
					}
				}
			}
		}
		return countspacemin;
	}

	private static Point maxSpace(List<List<GamePieces>> grid, int i) {
		var count = 0;
		var max = grid.size();
		for (var j = 0; grid.get(i).size() > j; j += 2) {
			if (grid.get(j).get(i) != null || checkConnections(grid, j, i)) {
				count++;
				if (max > j) {
					max = j;
				}
			}
		}
		return new Point(max, count);
	}

	public static void addConstructorGrid(int count, int max, int countspacemin, StringBuilder line, List<List<GamePieces>> grid, int i) {
		if (count > 0) {
			for (int m = (max - countspacemin) / 2; m > 0; m--) {
				line.append("       ");
			}
			for (var j = 0; grid.get(i).size() > j; j += 2) {
				line.append(addcoord(grid, i, j));
			}
			line.append("|\n"); // Change a line after each line
		}
	}

	public static void constructGrid(List<List<GamePieces>> grid) {
		var line = new StringBuilder();
		var countspacemin = minSpace(grid);
		var count = 0;
		var max = grid.size();
		for (var i = 0; grid.size() > i; i += 2) {
			var newpoint = maxSpace(grid, i);
			max = newpoint.x();
			count = newpoint.y();
			addConstructorGrid(count, max, countspacemin, line, grid, i);
		}
		System.out.println(line);
	}
	
  public static String showGrid() {
    var builder = new StringBuilder();
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid.size(); j++) {
      	
        if (grid.get(i).get(j) != null) {
          builder.append(grid.get(i).get(j).getAnimals()).append(i).append(j).append(" ");
        } else {
        	
          builder.append("n").append(" ");
        }
      }
      builder.append("\n");
      
    }
    return builder.toString();
  }
  
  // That function place a token a on a grid
  // using 2 Integer height and weight and an Object Token
  // return true if token was placed and false otherwise
  public static boolean placeToken(int height, int weight, GamePieces token) {
  	Objects.requireNonNull(token);
  	if(height < 0 || weight < 0) {
  		throw new IllegalArgumentException("Your values of height and weight must to be positive");
  	}
  	if(height > grid.size() || weight > grid.size()) {
  		throw new IllegalArgumentException("Your values of height and weight must to be under a grid size");
  	}
  	var currentTile = grid.get(height).get(weight);
		if(currentTile == null) {
			return false;
		}
  	var currentTileEnvironments = currentTile.getEnvironments();
  	if(canIplaceToken(height, weight, token.getValue(),currentTile.getAnimals())) {
  		var tile = new Tile(currentTileEnvironments,token.getAnimal(),true);
  		grid.get(height).set(weight, tile);
  		return true;
  	}
  	return false;
  }
  
  // return true if we can place a token and false otherwise
  private static boolean canIplaceToken(int height, int weight, String animal,String tileAnimals) {
  	Objects.requireNonNull(animal);
  	var tile = grid.get(height).get(weight);
  	if(tile.getOccupied() || !(tileAnimals.contains(animal))) { // check if tile is occupied 
  		return false;
  	}
  	return true;
  }

	public static void main(String[] args) {
		var rule = new BearRule();
		
		initializeGrid(11, 11);
		var tile4 = new Tile(List.of(Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET), List.of(Animal.WAPITI,Animal.OURS));
		var tile = new Tile(List.of(Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET,Environment.FORET), List.of(Animal.RENARD,Animal.OURS));
		var tile2 = new Tile(List.of(Environment.MONTAGNE,Environment.MONTAGNE,Environment.MONTAGNE,Environment.MONTAGNE,Environment.MONTAGNE,Environment.MONTAGNE), List.of(Animal.RENARD,Animal.OURS));
		var tile3 = new Tile(List.of(Environment.RIVIERE,Environment.RIVIERE,Environment.RIVIERE,Environment.RIVIERE,Environment.RIVIERE,Environment.RIVIERE), List.of(Animal.RENARD,Animal.OURS));
		var startTile = new StartTile(List.of(tile, tile2, tile3));
		placeFirstTile(5, 5, startTile);
		addTile(3,3,tile4);
		addTile(3,5,tile4);
		addTile(3,7,tile4);
		addTile(3,1,tile4);
		addTile(3,9,tile4);
//		addTile(6,4,tile4);
//		addTile(4,8,tile4);
//		addTile(5,1,tile4);
//		addTile(2,2,tile4);
		System.out.println(showGrid());
		var token = new AnimalToken(List.of(Animal.OURS));
		placeToken(3, 3, token);
		placeToken(4, 4, token);
		//placeToken(3, 5, token);
		placeToken(3, 9, token);
		placeToken(3, 7, token);
//		placeToken(2, 2, token);
		System.out.println(showGrid());
		System.out.println("Points pour ours :" + rule.BearRuleManager(grid, 1));
		//System.out.println("Points pour wapiti :" + rule.DeerRuleManager(grid, 1));
//		System.out.println("Points pour buse :" + rule.HawkRuleManager(grid, 1));
	}
}
