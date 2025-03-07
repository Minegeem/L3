package fr.uge.tile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record StartTile(List<Tile> tiles) {
	
	public StartTile {
	  for(var elem : tiles) {
		Objects.requireNonNull(elem);
	  }
	}

	@Override
	public String toString() {
	  return tiles.stream().map(Tile::toString).collect(Collectors.joining("\n"));
	}
	
	public int size() {
	  return tiles.size();
	}
	
	public Tile pos(int number) {
	  return tiles.get(number);
	}
}