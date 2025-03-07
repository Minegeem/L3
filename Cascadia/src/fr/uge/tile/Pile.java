package fr.uge.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Pile {
  
  private final ArrayList<AnimalToken> animalsPile = new ArrayList<>(); // 4 tokens
  private final ArrayList<Tile> environmentsPile = new ArrayList<>(); // 4 tiles
  private final ArrayList<AnimalToken> animalsTokens = new ArrayList<>(); // 100 animals tokens
  
  
  public ArrayList<AnimalToken> animalsPile(){
	  return animalsPile;
  }
  
  // return an Integer of different tokens in Pile
  public int howMuchDifferentToken(List<AnimalToken> pile) {
  	Objects.requireNonNull(pile);
  	var countingSet = new HashSet<>();
  	for(var token: pile) {
  		countingSet.add(token);
  	}
  	return countingSet.size();
  }
  
  public void addFillAnimalTokens(List<Animal> animal) {
    for(int i = 0; i < 20; i++) { // 20 tokens per animal
      var token = new AnimalToken(animal);
      animalsTokens.add(token);
    }
  }
  
  // fill animalsTokens with 20 tokens of each animal ( for an total of 100 tokens)
  public void fillAnimalTokens() {
    var animals = List.of(List.of(Animal.BUSE), List.of(Animal.OURS), List.of(Animal.RENARD), List.of(Animal.SAUMON), List.of(Animal.WAPITI));
    for(var animal : animals) {
      addFillAnimalTokens(animal);
    }
  }
  
  //fill environmentsPile with 4 random tiles
  public boolean fillEnviromentsPile(TileConstructor tiles) {
    Objects.requireNonNull(tiles);
    if(tiles.getSize() == 0) {
    	return false;
    }
    var pileSize = environmentsPile.size(); // between 0 and 4
    var random = new Random();
    for(int i = pileSize; i < 4; i++) { // 4 tiles in Pile
      var randomTiles = random.nextInt(tiles.getSize());
      environmentsPile.add(tiles.getTile(randomTiles));
      tiles.removeTile(randomTiles);
    }
    return true;
  }
  
  //fill animalsPile with 4 random animal token from animalsTokens
  public void fillAnimalsPile() {
    var random = new Random();
    var animalsPileSize = animalsPile.size();
    for(int i = animalsPileSize; i < 4; i++) { // 4 animals token in Pile
      var randomAnimalToken = random.nextInt(animalsTokens.size());
      animalsPile.add(animalsTokens.get(randomAnimalToken));
      animalsTokens.remove(randomAnimalToken);
    }
    var differentTokens = howMuchDifferentToken(animalsPile);
    if(differentTokens == 1) {
    	System.out.println("Oh vous avez 4 mêmes animaux, je vais remelanger!");
    	animalsPile.clear();
    	fillAnimalsPile();
    }
  }
  
  //constructor
  public boolean initializePile(TileConstructor tiles) {
    fillAnimalTokens();
    fillAnimalsPile();
    return fillEnviromentsPile(tiles);
    
  }
  
  //remove all of taken pieces from piles a return a list of them
  public List<GamePieces> takeSet(TileConstructor tiles, int position) {
    Objects.requireNonNull(tiles);
    if (position < 0) {
      throw new IllegalArgumentException("Position value must be postive");
    }
    var takenSet = new ArrayList<GamePieces>();
    takenSet.add(environmentsPile.get(position));
    takenSet.add(animalsPile.get(position));
    animalsPile.remove(position);
    environmentsPile.remove(position);
    fillAnimalsPile();
    fillEnviromentsPile(tiles);
    return takenSet;
  }
  
  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append("Voici vos tuiles :\n");
    builder.append("*======================================*\n");
    for(int i = 0; i < 4; i++) {
      builder.append("Jeton ").append(i + 1).append(" : ").append(animalsPile.get(i));
      builder.append(" Tuile ").append(i + 1).append(" : ").append(environmentsPile.get(i));
      builder.append("\n");
    }
    builder.append("*======================================*\n");
    return builder.toString();
  }

public ArrayList<Tile> environmentsPile() {
	return environmentsPile;
}
}