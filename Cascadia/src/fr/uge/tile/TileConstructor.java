package fr.uge.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class TileConstructor {

	private final ArrayList<Tile> tileCollection = new ArrayList<>();
	private final ArrayList<StartTile> startingTiles = new ArrayList<>();
	private final int numberOfStartingTiles = 3;
	
	public List<Environment> repeatEnvironment(Environment environment) {
		return List.of(environment,environment,environment,environment,environment,environment);
	}
	/* Function that initialize tiles 
	 * that function calls 2 another functions 
	 * createTilesForFirstStage();
	 * createStartTilesForFirstStage();
	 */
	public void initializeTiles() {
		createTilesForFirstStage();
		createStartTilesForFirstStage();
	}
	
	/* That function fill ArrayList<StartTile> startingTiles
	 * with 3 Starting Tiles that contains 3 random Tiles from 
	 * ArrayList<Tile> tileCollection 
	 */
	private void createStartTilesForFirstStage() {
		var random = new Random();
		var collectionSize = tileCollection.size();
		for (int i = 0; i < numberOfStartingTiles; i++) {
			var firstTile = random.nextInt(collectionSize);
			var secondTile = random.nextInt(collectionSize);
			var thirdTile = random.nextInt(collectionSize);
			var tiles = List.of(tileCollection.get(firstTile), 
								tileCollection.get(secondTile),
								tileCollection.get(thirdTile));
			var startTile = new StartTile(tiles);
			startingTiles.add(startTile);
		}
	}

	/* That function takes an Integer and return a StartTile
	 * if position is not valid throw an IllegalArgumentException
	 */
	public StartTile getStartingTile(int position) {
		if (position < 0 || position >= startingTiles.size()) {
			throw new IllegalArgumentException("Your value must to be between 0 and maximum size of a list : " + (startingTiles.size()));
		}
		return startingTiles.get(position);
	}

	/* That function takes an Integer and remove a StartTile from startingTiles list
	 * if position is not valid throw an IllegalArgumentException
	 */
	public void removeTileFromStartingTiles(int position) {
		if (position < 0 || position >= startingTiles.size()) {
			throw new IllegalArgumentException("Your value must to be between 0 and maximum size of a list : " + (startingTiles.size() - 1));
		}
		startingTiles.remove(position);
	}
	
	/* That function print all of Starting Tiles present in startingTiles list
	 * 
	 */
	public void showStartingTiles() {
		var builder = new StringBuilder();
		var counter = 1;
		var separator = "";
		for (var startTile : startingTiles) {
			builder.append(separator).append(counter).append(" : ").append(startTile);
			separator = "\n";
			counter++;
		}
		System.out.println(builder);
	}
	
	public ArrayList<StartTile> startingTilesDisplay(){
		return startingTiles;
	}
	
	private void addCreateTilesForFirstStage(List<Environment> environments, List<Animal> animal) {
	  for (var environment : environments) {
		var tile = new Tile(repeatEnvironment(environment), animal);
		tileCollection.add(tile);
	  }
	}
	
	/* That function fills tileCollection list with objects Tile
	 * using two different lists animalPairs and environments
	 * 
	 * Environments are order by alphabetical order
	 * "Forêt","Marais","Montagne","Prarie","Rivières"
	 */
	public void createTilesForFirstStage() {
		var animalPairs = makePairs(List.of(Animal.BUSE, Animal.OURS, Animal.RENARD, Animal.SAUMON, Animal.WAPITI));
		System.out.println(animalPairs);
		var environments = List.of(Environment.FORET, Environment.MARAIS, Environment.MONTAGNE, Environment.PRAIRIE, Environment.RIVIERE);
		for (var animal : animalPairs) {
			addCreateTilesForFirstStage(environments, animal);
		}
	}
	
	/* That function take an Integer as argument than remove Tile
	 * from list tileCollection
	 */
	public void removeTile(int position) {
		Objects.requireNonNull(tileCollection);
		if (position < 0) {
			throw new IllegalArgumentException("Position must to be positive");
		}
		tileCollection.remove(position);
	}
	
	/* That function take an Integer as argument than return object Tile
	 * 
	 */
	public Tile getTile(int position) {
		Objects.requireNonNull(tileCollection);
		if (position < 0) {
			throw new IllegalArgumentException("Position must to be positive");
		}
		return tileCollection.get(position);
	}
	
	/*
	 * return tileCollection size
	 */
	public int getSize() {
		return tileCollection.size();
	}
	
	public int getSizeStartingTiles() {
		return startingTiles.size();
	}
	
	
	// Functions creating perfectTiles
	// Animals are order by alphabetical order
	// Buse, Ours, Renard, Saumon, Wapiti

	public void perfectForests() {
		var forest = List.of(List.of(Animal.OURS), List.of(Animal.OURS), List.of(Animal.RENARD), List.of(Animal.RENARD), List.of(Animal.WAPITI));
		for (var animal : forest) {
			var tile = new Tile(repeatEnvironment(Environment.FORET), animal);
			tileCollection.add(tile);
		}
	}

	public void perfectFields() {
		var fields = List.of(List.of(Animal.RENARD), List.of(Animal.SAUMON), List.of(Animal.SAUMON), List.of(Animal.WAPITI), List.of(Animal.WAPITI));
		for (var animal : fields) {
			var tile = new Tile(repeatEnvironment(Environment.PRAIRIE), animal);
			tileCollection.add(tile);
		}
	}

	public void perfectSwamps() {
		var fields = List.of(List.of(Animal.BUSE), List.of(Animal.RENARD), List.of(Animal.RENARD), List.of(Animal.SAUMON), List.of(Animal.SAUMON));
		for (var animal : fields) {
			var tile = new Tile(repeatEnvironment(Environment.MARAIS), animal);
			tileCollection.add(tile);
		}
	}

	public void perfectMountains() {
		var fields = List.of(List.of(Animal.BUSE), List.of(Animal.BUSE), List.of(Animal.OURS), List.of(Animal.WAPITI), List.of(Animal.WAPITI));
		for (var animal : fields) {
			var tile = new Tile(repeatEnvironment(Environment.MONTAGNE), animal);
			tileCollection.add(tile);
		}
	}

	public void perfectRivers() {
		var fields = List.of(List.of(Animal.BUSE), List.of(Animal.BUSE), List.of(Animal.OURS), List.of(Animal.OURS), List.of(Animal.SAUMON));
		for (var animal : fields) {
			var tile = new Tile(repeatEnvironment(Environment.RIVIERE), animal);
			tileCollection.add(tile);
		}
	}

	// Functions that works with List.of<String>
	//
	
	/* That function takes a String than duplicate it 6 times
	 * 
	 */
	public List<String> repeatString(String environment) {
		Objects.requireNonNull(environment);
		var list = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			list.add(environment);
		}
		return list;
	}

	/* That function takes 2 objects of type String than duplicate each 3 times
	 */
	public List<String> repeatString(String environment, String environment2) {
		Objects.requireNonNull(environment);
		Objects.requireNonNull(environment2);
		var list = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			if (i <= 2) {
				list.add(environment);
			} else {
				list.add(environment2);
			}
		}
		return list;
	}

	/* That function takes a List<String> and makes a List<String> make a different pair for each
	 * 
	 * [Ours,Wapitit,Saumon] -> ["Ours/Saumon", "Ours/Wapiti", "Saumon/Wapiti"]
	 */
	public List<List<Animal>> makePairs(List<Animal> animal) {
		Objects.requireNonNull(animal);
		var animalPairs = new ArrayList<Animal>();
		var animalPairs2 = new ArrayList<List<Animal>>();
		for (int i = 0; i < animal.size(); i++) {
			for (int j = i + 1; j < animal.size(); j++) {
				animalPairs.add(animal.get(i));
				animalPairs.add(animal.get(j));
				@SuppressWarnings("unchecked")
				var animalPairs3 = (List<Animal>)animalPairs.clone();
				animalPairs2.add(animalPairs3);
				animalPairs.clear();
			}
		}
		return animalPairs2;
	}

	/* That function takes 2 List<String> and return a list with only different String
	 * 
	 */
	public List<String> findDifferentElements(List<String> list1, List<String> list2) {
		List<String> diffrence = new ArrayList<>();
		for (var str1 : list1) {
			if (list2.contains(str1)) {
				list2.remove(str1);
			}
			diffrence.add(str1);
		}
		for (var str2 : list2) {
			diffrence.add(str2);
		}
		return diffrence;
	}

	@Override
	public String toString() {
		var builder = new StringBuilder();
		for (int i = 0; i < tileCollection.size(); i++) {
			builder.append(i + 1).append(".").append(tileCollection.get(i)).append("\n"); // Je sais qu'il ne faut pas mettre																																						// "+" dans les append...
		}
		return builder.toString();
	}
}

