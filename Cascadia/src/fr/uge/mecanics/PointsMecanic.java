package fr.uge.mecanics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import fr.uge.tile.Environment;
import fr.uge.tile.GridMaker;

public class PointsMecanic {

	private final HashSet<Point> coord = new HashSet<Point>();
	private final List<Point> futurCoord = List.of(new Point(2, 0), new Point(-2, 0), new Point(0, 2),
			new Point(0, -2));
	private final HashMap<String, Integer> pointEnv = new HashMap<String, Integer>();
	private final HashMap<String, Integer> pointAnimals = new HashMap<String, Integer>();
	
	// That function looks if coordinate already exists in grid
	public boolean in_array(GridMaker grid, int pointx, int pointy) {
		if (pointx > grid.size() || pointy > grid.size() || pointx < 0 || pointy < 0
				|| (grid.coord(pointx, pointy) == null))
			return false;
		if (coord != null) {
			var newpoint = new Point(pointx, pointy);
			if (coord.add(newpoint)) {
				coord.remove(newpoint);
				return true;
			}
		}

		return false;
	}
	// That function is recursive and looks for the same environment in each neighbor coordinate
	public int recursiveEnvironnement(GridMaker grid, int pointx, int pointy) {
		var point = 1;

		var newpoint = new Point(pointx, pointy);
		coord.add(newpoint);
		if (grid.coord(pointx, pointy) == null)
			return 0;
		for (var element : futurCoord) {
			var newpointx = pointx + element.x();
			var newpointy = pointy + element.y();
			if (in_array(grid, newpointx, newpointy) == false)
				continue;
			if (grid.coord(pointx, pointy).getEnvironments()
					.equals(grid.coord(newpointx, newpointy).getEnvironments())) {
				point += recursiveEnvironnement(grid, newpointx, newpointy);

			}
		}

		return point;
	}
	
	// That function pass in each square of a grid and launch a recursive function on filled ones
	public int pointEnvironnement(GridMaker grid, List<Environment> list) {
		coord.clear();
		var pointenv = 0;
		var newpointenv = 0;
		for (int i = 0; i < grid.size(); i += 2) {
			for (int j = 0; j < grid.size(); j += 2) {
				if (grid.coord(i, j) == null)
					continue;
				if (grid.coord(i, j).getEnvironments().equals(list)) {

					newpointenv = recursiveEnvironnement(grid, i, j);

					if (newpointenv > pointenv)
						pointenv = newpointenv;
				}
			}
		}
		return pointenv;
	}

	// Create a section in a dictionary to represent points for each environment 
	public HashMap<String, Integer> TotalEnvironmentsPoints(GridMaker grid) {
		pointEnv.put("Forêt", 
				pointEnvironnement(grid, List.of(Environment.FORET, Environment.FORET, Environment.FORET, Environment.FORET, Environment.FORET, Environment.FORET)));
		pointEnv.put("Marais",
				pointEnvironnement(grid, List.of(Environment.MARAIS, Environment.MARAIS, Environment.MARAIS, Environment.MARAIS, Environment.MARAIS, Environment.MARAIS)));
		pointEnv.put("Montagne", 
				pointEnvironnement(grid, List.of(Environment.MONTAGNE, Environment.MONTAGNE, Environment.MONTAGNE, Environment.MONTAGNE, Environment.MONTAGNE, Environment.MONTAGNE)));
		pointEnv.put("Prarie",
				pointEnvironnement(grid, List.of(Environment.PRAIRIE, Environment.PRAIRIE, Environment.PRAIRIE, Environment.PRAIRIE, Environment.PRAIRIE, Environment.PRAIRIE)));
		pointEnv.put("Rivières", 
				pointEnvironnement(grid, List.of(Environment.RIVIERE, Environment.RIVIERE, Environment.RIVIERE, Environment.RIVIERE, Environment.RIVIERE, Environment.RIVIERE)));
		return pointEnv;
	}

	// That function is recursive and looks for the same animal in each neighbor coordinate
	public int recursiveAnimals(GridMaker grid, int pointx, int pointy) {
		var point = 1;
		// System.out.println(point);
		var newpoint = new Point(pointx, pointy);
		coord.add(newpoint);
		if (grid.coord(pointx, pointy) == null)
			return 0;
		for (var element : futurCoord) {
			var newpointx = pointx + element.x();
			var newpointy = pointy + element.y();
			if (in_array(grid, newpointx, newpointy) == false || !grid.coord(newpointx, newpointy).getOccupied())
				continue;
			if (grid.coord(pointx, pointy).getAnimals().equals(grid.coord(newpointx, newpointy).getAnimals())) {
				point += recursiveAnimals(grid, newpointx, newpointy);

			}
		}

		return point;
	}
	
	// That function pass in each square of a grid and launch a recursive function on filled ones
	public int pointAnimals(GridMaker grid, String animal) {
		coord.clear();
		var pointenv = 0;
		var newpointenv = 0;
		for (int i = 0; i < grid.size(); i += 2) {
			for (int j = 0; j < grid.size(); j += 2) {
				if (grid.coord(i, j) == null)
					continue;
				if (grid.coord(i, j).getAnimals().equals(animal)) {
					newpointenv = recursiveAnimals(grid, i, j);

					if (newpointenv > pointenv)
						pointenv = newpointenv;
				}
			}
		}
		return pointenv;
	}

	// Create a section in a dictionary to represent points for each animal 
	public HashMap<String, Integer> TotalAnimalsPoints(GridMaker grid) {
		pointAnimals.put("Ours", pointAnimals(grid, "O"));
		pointAnimals.put("Renard", pointAnimals(grid, "R"));
		pointAnimals.put("Buse", pointAnimals(grid, "B"));
		pointAnimals.put("Saumon", pointAnimals(grid, "S"));
		pointAnimals.put("Wapiti", pointAnimals(grid, "W"));
		return pointAnimals;
	}

	// That use a scoreEnvironment and return a String that contains a player Score
	// of group of animals and environments 
	public String score(Mode mode) {
		var builderAnimal = new StringBuilder();
		var points = 0;
		var separator = "";
		for (var point : pointAnimals.entrySet()) {
			var key = point.getKey();
			var value = point.getValue();
			points = mode.nb_point(value);
			builderAnimal.append(separator).append(key).append(" : ").append(points);
			separator = "\n";
		}
		var environementScore = scoreEnvironment();
		return builderAnimal + "\n" + environementScore;
	}

	// That function return a String of player environment score depending on environments in PointEnv
	private String scoreEnvironment() {

		var builderEnv = new StringBuilder();
		var separator = "";
		for (var point : pointEnv.entrySet()) {
			var key = point.getKey();
			var value = point.getValue();
			builderEnv.append(separator).append(key).append(" : ").append(value);
			separator = "\n";
		}
		return builderEnv.toString();
	}

}
