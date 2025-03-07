package fr.uge.tile;

import java.util.List;
import java.util.Objects;

public record Tile(List<Environment> environments, List<Animal> animals, boolean occupied) implements GamePieces{

  public Tile {
    Objects.requireNonNull(animals);
    Objects.requireNonNull(environments);
  }
  
  public Tile(List<Environment> environments, List<Animal> animals) {
  	this(environments,animals,false);
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    var separator = "";
    Environment save = null;
    for (var elem : environments) {
      if(!elem.equals(save)) {
        builder.append(separator).append(elem);
        separator = "/";
      }
      save = elem;
    }
    return "environements : " + builder.toString() + " animaux : " + animals;
  }

  @Override
  public boolean equals(Object object) {
    return object instanceof Tile tile && tile.environments.equals(environments) && tile.animals.equals(animals);
  }
  
  //return a string of first letters of environments 
  //exemple of return : [Foret,Foret,Foret,Foret,Foret,Foret] -> F
  //                    [Foret,Foret,Foret,Prarie,Praire,Prarie] -> FP
  public String getFirstEnvironmentLetter() {
    var builder = new StringBuilder();
    var firstEnvironment = environments.get(0);
    var secondEnvironment = environments.get(3);
    for (var environment : environments) {
      if (!environment.equals(firstEnvironment)) {
        secondEnvironment = environment;
      }
    }
    if (firstEnvironment.equals(secondEnvironment)) {
      var environmentLetter = firstEnvironment.toString().charAt(0);
      return builder.append(environmentLetter).toString();
    }
    var firstEnvironmentLetter = firstEnvironment.toString().charAt(0);
    var secondEnvironmentLetter = secondEnvironment.toString().charAt(0);
    return builder.append(firstEnvironmentLetter).append(secondEnvironmentLetter).toString();
  }
  
  
  //return a string of first animals letters
  //exemple of return : Ours/Wapitit -> OW
  public String getFirstAnimalLetter() {
    var builder = new StringBuilder();
    for (var animal : animals) {
      builder.append(animal.toString().charAt(0));
    }
    return builder.toString();
  }

  //result of this function combine results of getFirstAnimalLetter and getFirstEnvironmentLetter
  //return for exemple : FP/OW for ForetPrairie/OursWapiti
  public String getValue() {
    var environments = getFirstEnvironmentLetter();
    var animals = getFirstAnimalLetter();
    var combination = environments + "/" + animals;
    var builder = new StringBuilder();
    builder.append(combination);
    for (int i = combination.length(); i < 5; i++) {
      builder.append(" ");
    }
    return builder.toString();
  }
  
  public List<Animal> getAnimal(){
	  return animals;
  }
  
  public List<Environment> getEnvironments() {
  	return environments;
  }
  
  public String getAnimals() {
  	var animals = getFirstAnimalLetter();
  	return animals;
  }

  @Override
  public boolean getOccupied() {
	return occupied;
  }

	@Override
	public Animal getAnimalFromList(Animal token) {
		Objects.requireNonNull(token);
		for(var animal : animals) {
			if(animal == token) {
				return animal;
			}
		}
		return null;
	}
}
