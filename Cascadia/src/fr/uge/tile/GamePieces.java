package fr.uge.tile;

import java.util.List;

public interface GamePieces {
	String getValue();
	List<Animal> getAnimal();
	List<Environment> getEnvironments();
	boolean getOccupied();
	String getAnimals();
	Animal getAnimalFromList(Animal buse);

}
