package fr.uge.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.tile.GamePieces;
import fr.uge.tile.GridMaker;
import fr.uge.tile.StartTile;

public class Player {
	private final GridMaker playerGrid;
	private final ArrayList<GamePieces> playerHand = new ArrayList<>();
	
	public ArrayList<GamePieces> playerHand(){
		return playerHand;
	}
	
	public Player(int height, int weight) {
		if (height < 0 || weight < 0) {
			throw new IllegalArgumentException("You can't create a player space with negatives values");
		}
		playerGrid = new GridMaker();
		playerGrid.initializeGrid(height, weight);
	}

	public boolean placePlayerToken(int height, int weight, int tokenPosition) {
		if (tokenPosition < 0 || tokenPosition >= playerHand.size()) {
			// throw new IllegalStateException("You need to give a correct tokenPosition");
			return false;
		}
		if (playerGrid.placeToken(height, weight, playerHand.get(tokenPosition))) {
			playerHand.remove(tokenPosition);
			return true;
		}
		return false;

	}

	private boolean placeTile(int height, int weight, GamePieces tile) {
		Objects.requireNonNull(tile);
		if (playerGrid.addTile(height, weight, tile)) {
			playerHand.remove(tile);
			return true;
		}
		return false;
	}

	public boolean placeFirstTile(int height, int weight, StartTile tile) {
		Objects.requireNonNull(tile);
		return playerGrid.placeFirstTile(height, weight, tile);
	}

	/*
	 * That function takes 2 Integer height and weight and a List<GamePieces> pieces
	 * than place a tile and return true
	 * or return false if fail
	 */
	public boolean addSet(List<GamePieces> pieces, int height, int weight) {
		Objects.requireNonNull(pieces);
		if (pieces.size() != 2) {
			throw new IllegalStateException("Your set is not of size 2");
		}
		if (placeTile(height, weight, pieces.get(0))) {
			playerHand.add(pieces.get(1));
			return true;
		}

		return false;

	}

	// That function print a player hand
	public void showPlayerHand() {
		var builder = new StringBuilder();
		var separator = "";
		for (var piece : playerHand) {
		  builder.append(separator).append(piece);
		  separator = "\n";
		}
		System.out.println("Dans votre main vous avez : " + builder);
	}

	// That function return a playerGrid 
	public GridMaker listplayer() {
		return playerGrid;
	}

}
