package fr.uge.tile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record AnimalToken(List<Animal> animals) implements GamePieces {
  
	//Constructor
  public AnimalToken {
    Objects.requireNonNull(animals);
  }
  
  @Override
  public String toString() {
	    return animals.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
  }

  // Give the first letter of variable animal 
  // AnimalToken("Ours").getValue() -> "O"
	@Override
	public String getValue() {
	  var builder = new StringBuilder();
	  for(var elem : animals) {
		  builder.append(elem.toString().charAt(0));
	  }
	  return builder.toString();
	}
	
	public List<Animal> getObject() {
		  return animals;
		}
	
	public Animal getAnimalFromList(Animal token) {
		if(token == null || animals.isEmpty()) {
			return null;
		}
		for(var animal : animals) {
			if(animal == token) {
				return animal;
			}
		}
		return null;
	}
	
	//Always return null because AnimalToken does't have any environments
	@Override
	public List<Environment> getEnvironments() {
	  return null;
	}

	//Always return false because AnimalToken can't be occupied
	@Override
	public boolean getOccupied() {
	  return false;
	}

	// return getValue
	@Override
	public String getAnimals() {
	  return "" + animals.toString().charAt(0);
	}

	@Override
	public List<Animal> getAnimal() {
		return animals;
	}
}
