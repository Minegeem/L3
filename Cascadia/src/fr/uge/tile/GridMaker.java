package fr.uge.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridMaker {

  private final List<List<GamePieces>> grid = new ArrayList<>();

  // Initializing a grid
  public void initializeGrid(int height, int weight) {
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

  // Internal Version of showGrid
  // Show spaces that are filled by tiles
  @Override
  public String toString() {
    var builder = new StringBuilder();
    var count = 1;
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid.size(); j++) {
        if (grid.get(i).get(j) != null) {
          builder.append("T").append(count).append(" ");
          count++;
        } else {
          builder.append("null").append(" ");
        }
      }
      builder.append("\n");
    }
    return builder.toString();
  }

  // Looking for the adjacent tiles
  public static boolean checkConnections(List<List<GamePieces>> grid, int height, int weight) {

    Objects.requireNonNull(grid);
    //4 directions
    var south = height + 2;
    var north = height - 2;
    var east = weight + 2;
    var west = weight - 2;

    if (height < 0 || height >= grid.size() || weight < 0 || weight >= grid.size()) // looks for coordinates to be inside a grid and positive
      return false;
    if (grid.get(height).get(weight) != null) //looks if a particular square is empty
      return false;
    
    // particular check for each direction 
    if (south < grid.size() && (grid.get(south).get(weight) != null)) { 
      return true;
    }
    if (north >= 0 && (grid.get(north).get(weight) != null)) {
      return true;
    }
    if (east < grid.size() && (grid.get(height).get(east) != null)) {
      return true;
    }
    if (west >= 0 && (grid.get(height).get(west) != null)) {
      return true;
    }
    return false;
  }

// Type of return is boolean so we can know if we added the tile
  public boolean addTile(int height, int weight, GamePieces tile) {
    Objects.requireNonNull(tile);
    if (checkConnections(grid, height, weight)) { // if true we add a tile
      grid.get(height).set(weight, tile);
      return true;
    }
    return false;
  }

  //This function DOESN'T use checkConnections() because the first tile doesn't need to be connected to an existing tile
  public boolean placeFirstTile(int height, int weight, StartTile tile) {
    Objects.requireNonNull(tile);
    if (height < 0 || weight < 0) {
      //throw new IllegalStateException("height and weight must be positive");
    	return false;
    }
    for(var i = 0; i < tile.size(); i += 3) {
        grid.get(height + i).set(weight + i, tile.pos(i));
        grid.get(height).set(weight + 2 + i, tile.pos(i + 1));
        grid.get(height - 2 + i).set(weight, tile.pos(i + 2));
    }
    return true;
  }

  // That function place a token a on a grid
  // using 2 Integer height and weight and an Object Token
  // return true if token was placed and false otherwise
  public boolean placeToken(int height, int weight, GamePieces token) {
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
  private boolean canIplaceToken(int height, int weight, String animal,String tileAnimals) {
  	Objects.requireNonNull(animal);
  	var tile = grid.get(height).get(weight);
  	if(tile.getOccupied() || !(tileAnimals.contains(animal))) { // check if tile is occupied 
  		return false;
  	}
  	return true;
  }
  
  public GamePieces coord(int x, int y) {
	return grid.get(x).get(y);
  }
  
  public List<GamePieces> listOfTheGrid(int x) {
  	return grid.get(x);
  }
  
  public int size() {
	return grid.size();
  }
  
  // return grid
  public List<List<GamePieces>> list() {
	return grid;
  }
}
