package fr.uge.player;

import java.util.Objects;

import fr.uge.mecanics.Point;
import fr.uge.tile.Environment;
import fr.uge.tile.GridMaker;

public class Output {
	
  // Build a String of a grid 
  // Example | M/OB |
  private String addcoord(GridMaker grid, int i, int j) {
      var valeur = grid.coord(j, i);
      var line = new StringBuilder();
      if (valeur == null) {
          if (checkConnections(grid, j, i)) {
            line.append("|");
            var newi = "" + i;
            var newj = "" + j;
            if (i < 10) { newi = "O" + i; }
            if (j < 10) { newj = "O" + j; }
            line.append(newj + ", " + newi);
          }
        } else {
        	if(valeur.getEnvironments().get(0).equals(Environment.MARAIS)) {
        		var actualTile = new StringBuilder(valeur.getValue());
        		actualTile.setCharAt(0, Character.toLowerCase(actualTile.charAt(0)));
        		line.append("|").append(actualTile).append(" ");
        	} else {
        		line.append("|").append(valeur.getValue()).append(" ");
        	}
        }
    return line.toString();
  }
	
  // Looking for the adjacent tiles
  public static boolean checkConnections(GridMaker grid, int height, int weight) {

    Objects.requireNonNull(grid);
    //4 directions
    var south = height + 2;
    var north = height - 2;
    var east = weight + 2;
    var west = weight - 2;

    if (height < 0 || height >= grid.size() || weight < 0 || weight >= grid.size()) // looks for coordinates to be inside a grid and positive
      return false;
    if (grid.coord(height,weight) != null) //looks if a particular square is empty
      return false;
    
    // particular check for each direction 
    if (south < grid.size() && (grid.coord(south,weight) != null)) { 
      return true;
    }
    if (north >= 0 && (grid.coord(north,weight) != null)) {
      return true;
    }
    if (east < grid.size() && (grid.coord(height,east) != null)) {
      return true;
    }
    if (west >= 0 && (grid.coord(height,west) != null)) {
      return true;
    }
    return false;
  }
	
  // return a minimum number of spaces 
  private int minSpace(GridMaker grid) {
    var countspacemin = grid.size();
      for (var i = 0; grid.size() > i; i += 2) {
        for (var j = 0; grid.listOfTheGrid(i).size() > j; j += 2) {
          if (grid.coord(j,i) != null || checkConnections(grid, j, i)) {
            if (countspacemin > j) {
              countspacemin = j;
            }
          }
        }
      }
      return countspacemin;
  }
  
  private Point maxSpace(GridMaker grid, int i) {
    var count = 0;
    var max = grid.size();
    for (var j = 0; grid.listOfTheGrid(i).size() > j; j += 2) {
      if (grid.coord(j,i) != null || checkConnections(grid, j, i)) {
        count++;
        if (max > j) {
            max = j;
        }
      }
    }
    return new Point(max,count);
  }

  public void addConstructorGrid(int count, int max, int countspacemin, StringBuilder line, GridMaker grid, int i) {
	  if (count > 0) {
			for (int m = (max - countspacemin) / 2; m > 0; m--) {
				line.append("       ");
			}
			for (var j = 0; grid.listOfTheGrid(i).size() > j; j += 2) {
				line.append(addcoord(grid, i, j));
			}
			line.append("|\n"); // Change a line after each line
		}
  }
  
	public void constructGrid(GridMaker grid) {
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

	public void showGrid(GridMaker grid) {
		System.out.println("<======================================>");
		constructGrid(grid);
		System.out.println("<======================================>");
	}
	
}
	
