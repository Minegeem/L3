package fr.uge.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.Application;
import fr.uge.display.DisplayGame;
import fr.uge.mecanics.Famille;
import fr.uge.mecanics.Intermediaire;
import fr.uge.mecanics.Mode;
import fr.uge.mecanics.PointsMecanic;
import fr.uge.player.Input;
import fr.uge.player.Output;
import fr.uge.player.Player;
import fr.uge.tile.GamePieces;
import fr.uge.tile.Pile;
import fr.uge.tile.TileConstructor;

public class Game {

	public static int addFirstTileChoice(Input input, TileConstructor colection) {
		var choice = input.makeAchoice();
		while (choice < 1 || choice > colection.getSizeStartingTiles()) {
			choice = input.makeAchoice("Votre choix doit etre un chiffre compris entre 1 et "
					+ colection.getSizeStartingTiles() + ". Essayer encore :");
		}
		return choice;
	}

	// That function permits to a player to choose his first Tile
	public static void firstTileChoice(List<Player> players, TileConstructor colection, int height, int weight) {
		var input = new Input();
		var output = new Output();
		for (var player : players) {
			colection.showStartingTiles();
			var choice = addFirstTileChoice(input, colection);
			player.placeFirstTile(height / 2 + 1, weight / 2 + 1, colection.getStartingTile(choice - 1));
			colection.removeTileFromStartingTiles(choice - 1);
			output.showGrid(player.listplayer());
		}
	}

	// That function List<GamePieces> permits a player to take a set of (tile,token)
	private static List<GamePieces> playerTakeSet(Pile pile, TileConstructor colection, Player player) {
		System.out.println(pile);
		var input = new Input();
		var choice = input.makeAchoice();
		while (choice < 1 || choice > 4) {
			choice = input.makeAchoice();
		}

		var takenSet = pile.takeSet(colection, choice - 1);
		System.out.println("Vous avez pris la tuile " + takenSet.get(0) + " et le token " + takenSet.get(1));
		return takenSet;
	}

	// That function make a player put a tile on a grid
	public static void playerPutTile(List<GamePieces> takenSet, Player player) {
		var output = new Output();
		output.showGrid(player.listplayer());
		var input = new Input();
		System.out.println("Faites le choix parmis les cases dont vous voyez les coordonées");
		var height = input.makeAchoice("La 1er coordonée : ");
		var weight = input.makeAchoice("La 2éme coordonée : ");
		while (!player.addSet(takenSet, height, weight)) {
			height = input.makeAchoice(
					"Encore une fois ! La 1er coordonée (votre choix doit etre un chiffre parmis ceux que vous voyez) : ");
			weight = input.makeAchoice(
					"Econre une fois ! La 2eme coordonée (votre choix doit etre un chiffre parmis ceux que vous voyez): ");
		}
		player.showPlayerHand();
		output.showGrid(player.listplayer());
	}

	// That function make a player put a token on a grid
	public static void playerPutToken(Player player) {
		var input = new Input();
		var output = new Output();
		var choice = input.makeAchoice("Vous voulez placer votre token 1=oui ?");
		if (choice == 1) {
			System.out.println("Ou vous voulez placer votre token ?");
			var height = input.makeAchoice("La 1er coordonée : ");
			var weight = input.makeAchoice("La 2éme coordonée : ");
			var token = input.makeAchoice("Quel token souhaite tu placer ?");
			while (!(player.placePlayerToken(height, weight, token - 1))) {
				choice = input.makeAchoice("Vous voulez toujours placer votre token 1=oui ?");
				if (choice != 1)
					break;
				height = input.makeAchoice("Encore une fois ! La 1er coordonée : ");
				weight = input.makeAchoice("Encore une fois ! La 2éme coordonée : ");
				token = input.makeAchoice("Quel token souhaite tu placer ?");
			}
		}
		player.showPlayerHand();
		output.showGrid(player.listplayer());
	}

	// That function show a score of each player using selected mode
	public static void showScore(List<Player> players, Mode mode) {
		var builder = new StringBuilder();
		var counter = 1;
		var separator = "";
		for (var player : players) {
			var score = new PointsMecanic();
			score.TotalAnimalsPoints(player.listplayer());
			score.TotalEnvironmentsPoints(player.listplayer());
			builder.append(separator).append("Score du joueur ").append(counter).append("\n").append(score.score(mode));
			separator = "\n";
			counter++;
		}
		System.out.println(builder);
	}

	// That function initialize an exact numbers of players using height and weight
	public static List<Player> initializePlayers(int numberOfPlayers, int height, int weight) {
		var players = new ArrayList<Player>();
		for (int i = 0; i < numberOfPlayers; i++) {
			var player = new Player(height, weight);
			players.add(player);
		}
		return players;
	}

	private static void addRunGame(List<Player> currentPlayers, int tour, Output output, int playerTurn, Pile pile,
			TileConstructor colection) {
		for (var player : currentPlayers) {
			System.out.println("On est au tour n° " + tour);
			System.out.println("On est au tour du joueur " + playerTurn + " voici votre grille :");
			output.showGrid(player.listplayer());
			pile.initializePile(colection);
			var takenSet = playerTakeSet(pile, colection, player); // Choix d'une tuile dans la pioche
			playerPutTile(takenSet, player); // On place la tuile sur le terrain
			playerPutToken(player);// On place le token
			playerTurn++;
		}
	}

	// Use all of functions to make a game work
	public static void runGame(List<Player> currentPlayers, TileConstructor colection, Pile pile, int height,
			int weight, Mode mode) {
		firstTileChoice(currentPlayers, colection, height, weight);// choix des tuiles du début
		var output = new Output();
		var tour = 1;
		var playerTurn = 1;
		while (colection.getSize() > 43) {
			addRunGame(currentPlayers, tour, output, playerTurn, pile, colection);
			tour++;
			playerTurn = 1;
		}
		showScore(currentPlayers, mode);
	}

	public static void main(String[] args) {
		var input = new Input();
		var gameMode = input.makeAchoice("Choisissez le mode graphique(1) ou le mode terminal(2) : ");
		if (gameMode == 1) {
			Application.run(Color.WHITE, DisplayGame::onStart);
		} else {
			var currentPlayers = initializePlayers(2, 51, 51);
			var colection = new TileConstructor();
			var pile = new Pile();
			colection.initializeTiles();
			var choice = input.makeAchoice("Choisissez le mode famille(1) ou le mode intermédiare(2) : ");
			Mode mode;
			switch (choice) {
			case 1 -> mode = new Famille();
			case 2 -> mode = new Intermediaire();
			default -> mode = new Famille();
			}
			runGame(currentPlayers, colection, pile, 51, 51, mode);
		}
	}
}
