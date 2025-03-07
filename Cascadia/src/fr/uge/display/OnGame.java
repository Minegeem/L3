package fr.uge.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.ScreenInfo;

import fr.uge.main.Game;
import fr.uge.mecanics.Famille;
import fr.uge.mecanics.Intermediaire;
import fr.uge.mecanics.Mode;
import fr.uge.mecanics.Point;
import fr.uge.player.Input;
import fr.uge.player.Output;
import fr.uge.player.Player;
import fr.uge.player.Players;
import fr.uge.tile.Animal;
import fr.uge.tile.GamePieces;
import fr.uge.tile.GridMaker;
import fr.uge.tile.Pile;
import fr.uge.tile.TileConstructor;

public class OnGame {

	private static ApplicationContext context;
	private static ScreenInfo screenInfo;
	private static List<Button> buttonsTile = new ArrayList<>();

	public OnGame(ApplicationContext context, ScreenInfo screenInfo) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(screenInfo);
		OnGame.context = context;
		OnGame.screenInfo = screenInfo;
	}

	private static Mode modeQuestion() {
		var input = new Input();
		var choice = input.makeAchoice("Choisissez le mode famille(1) ou le mode intermédiare(2) : ");
		return switch (choice) {
		case 1 -> new Famille();
		case 2 -> new Intermediaire();
		default -> new Famille();
		};
	}

	private static int playersQuestion() {
		var input = new Input();
		var choice = 0;
		do {
			choice = input.makeAchoice("Choisissez le nombre de joueur ");
		} while (choice < 2 || choice > 4);
		return choice;
	}

	private static void drawPlayer(Players player, int tile, Graphics graphics) {
		var stringPlayer = "";
		switch (player) {
		case Players.PLAYER1 -> {
			stringPlayer = "Player 1";
		}
		case Players.PLAYER2 -> {
			stringPlayer = "Player 2";
		}
		case Players.PLAYER3 -> {
			stringPlayer = "Player 3";
		}
		case Players.PLAYER4 -> {
			stringPlayer = "Player 4";
		}
		}
		int textX = 10;
		int textY = 10 + (tile + 10) * 2 + 20;
		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		graphics.drawString(stringPlayer, textX, textY);
	}

	private static void drawBack(Graphics2D graphics, ScreenInfo screenInfo, Button quitButton, Players players,
			int tour, Player player, DisplayShape display, Point cursor) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, screenInfo.width(), screenInfo.height());
		int tile = 150;
		int padding = 10;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				int x = padding + (tile + padding) * i;
				int y = padding + (tile + padding) * j;
				Button button = new Button("", x, y, tile, tile);
				buttonsTile.add(button);
				button.render(graphics);
			}
		}
		int board = 900;
		int centerX = (screenInfo.width() - board) / 2;
		int centerY = (screenInfo.height() - board) / 2;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(centerX, centerY, board, board);
		graphics.setColor(Color.BLACK);
		graphics.drawRect(centerX - 1, centerY - 1, board + 1, board + 1);
		graphics.drawString("" + tour, screenInfo.width() / 2, screenInfo.height() / 2 - 500);
		drawPlayer(players, tile, graphics);
		display.graphicDisplay(graphics, player.listplayer(), screenInfo, cursor);

	}

	private static void firstTileChoiceDisplay(List<Player> currentPlayers, TileConstructor colection, int height,
			int weight, ApplicationContext context) {
		IntputDisplay.firstTile("Choisissez la tuile de depart", currentPlayers, colection, height, weight, context);
	}

	private static void renderFrame(Graphics2D graphics, ScreenInfo screenInfo, Button quitButton, Players players,
			int tour, Player player, DisplayShape display, Point cursor) {
		drawBack(graphics, screenInfo, quitButton, players, tour, player, display, cursor);
	}

	private static void renderOnGame(ApplicationContext context, ScreenInfo screenInfo, Button quitButton,
			Players players, int tour, Player player, DisplayShape display, Point cursor) {
		context.renderFrame(
				graphics -> renderFrame(graphics, screenInfo, quitButton, players, tour, player, display, cursor));

	}

	private IdStage quit(Button quitButton) {
		Event event = context.pollEvent();
		if ((event != null) && (event.getClass().equals(PointerEvent.class))) {
			PointerEvent pointerEvent = (PointerEvent) event;
			if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
				PointerEvent.Location location = pointerEvent.location();
				if (quitButton.isClicked(location.x(), location.y())) {
					return IdStage.LEAVE;
				}
			}
		}
		return IdStage.ONGAME;
	}

	private static void drawTileAround(List<Button> buttonsTileAround, ApplicationContext context,
			List<GamePieces> takenSet, Player player) {
		var posTile = false;
		while (!posTile) {
			context.renderFrame(graphics -> {
				graphics.setColor(Color.WHITE);
				graphics.fillRect(20, 450, 300, 50);
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Arial", Font.PLAIN, 30));
				graphics.drawString("Poser la tuile", 20, 500);
				for (var elem : buttonsTileAround) {
					graphics.setColor(Color.RED);
					graphics.fillRect(elem.x(), elem.y(), elem.width(), elem.height());
				}
			});
			Event event = context.pollEvent();
			if (event instanceof PointerEvent pointerEvent) {
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					PointerEvent.Location location = pointerEvent.location();
					for (var elem : buttonsTileAround) {
						if (elem.isClicked(location.x(), location.y())) {
							player.addSet(takenSet, elem.i(), elem.j());
							for (var elem2 : buttonsTileAround) {
								context.renderFrame(graphics -> {
									elem2.renderSquare(graphics, elem2.i(), elem2.j(),
											player.listplayer().coord(elem2.i(), elem2.j()));
								});
							}
							posTile = true;
						}
					}
				}
			}
		}
	}

	private static void playerPutTileDisplay(ApplicationContext context, List<GamePieces> takenSet, Player player,
			Point cursor) {
		var buttonsTileAround = new ArrayList<Button>();
		GridMaker grid = player.listplayer();
		var x = 17;
		for (int i = (cursor.x() - x); i <= (cursor.x() + x); i += 2) {
			for (int j = (cursor.y() - x); j <= (cursor.y() + x); j += 2) {
				if (Output.checkConnections(grid, i, j)) {
					for (var elem : DisplaySquare.buttonplateau()) {
						if (elem.i() == i && elem.j() == j) {
							buttonsTileAround.add(elem);
						}
					}
				}
			}
		}
		drawTileAround(buttonsTileAround, context, takenSet, player);
	}

	public static void drawAnimals(Graphics2D graphics, int x, int y, Color color) {
		var squareSize = 50;
		var circleDiameter = squareSize / 3;
		graphics.setColor(color);
		graphics.fillOval(x, y, circleDiameter, circleDiameter);
	}

	public int drawTokenDisplay(List<Animal> animals) {
		List<Button> buttonsListToken = new ArrayList<Button>();
		System.out.println(animals);
		context.renderFrame(graphics -> {
			var j = 0;
			for (var i = 0; i < animals.size(); i++) {
				var button = new Button("Token" + i, 30 + i * 30, 700 + j, 30, 30);
				buttonsListToken.add(button);
				button.renderSquare(graphics, i, j, null);
				Button.buildSquareAnimals(graphics, 30 + i * 30, 700 + j, animals.get(i));
				if (i % 5 == 0) {
					j *= 50;
				}
			}
			graphics.setColor(Color.WHITE);
			graphics.fillRect(20, 450, 350, 50);
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("Arial", Font.PLAIN, 30));
			graphics.drawString("Choisissez le Token", 20, 500);

		});
		System.out.println(buttonsListToken);
		while (true) {
			Event event = context.pollEvent();
			if (event instanceof PointerEvent pointerEvent) {
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					PointerEvent.Location location = pointerEvent.location();
					for (var elem : buttonsListToken) {
						if (elem.isClicked(location.x(), location.y())) {
							return elem.i();
						}
					}
				}
			}
		}
	}

	private static void placePlayerToken(List<Animal> animals, int index, Player player) {
		var buttonsList = new ArrayList<Button>();
		context.renderFrame(graphics -> {
			var animal = animals.get(index);
			graphics.setColor(Color.WHITE);
			graphics.fillRect(20, 450, 350, 50);
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("Arial", Font.PLAIN, 24));
			graphics.drawString("Poser le token", 20, 500);
			for (var elem : DisplaySquare.buttonplateau()) {
				if (player.listplayer().coord(elem.i(), elem.j()) != null) {
					for (var elem2 : player.listplayer().coord(elem.i(), elem.j()).getAnimal())
						if (animal.equals(elem2)) {
							buttonsList.add(elem);
							graphics.setColor(Color.RED);
							graphics.fillRect(elem.x(), elem.y(), elem.width(), elem.height());
						}
				}
			}

		});
		if(buttonsList.size() == 0) return;
		var isChoice = false;
		while (!isChoice) {
			Event event = context.pollEvent();
			if (event instanceof PointerEvent pointerEvent) {
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					PointerEvent.Location location = pointerEvent.location();
					for (var elem : buttonsList) {
						if (elem.isClicked(location.x(), location.y())) {
							player.placePlayerToken(elem.i(), elem.j(), index);
							isChoice = true;
						}
					}
				}
			}
		}
	}

	private void playerPutToken(Player player) {
		List<Animal> animalsListToken = new ArrayList<Animal>();
		if (player.playerHand() == null) {
			return;
		}
		var result = IdStage.NO;
		result = IntputDisplay.MakeAChoice("Voulez vous poser un token ?", context);

		if (result.equals(IdStage.YES)) {
			for (var i = 0; i < player.playerHand().size(); i++) {
				var animals = player.playerHand().get(i).getAnimal();
				for (var j = 0; j < animals.size(); j++) {
					animalsListToken.add(animals.get(j));
				}
			}
			if (animalsListToken.size() == 1) {
				placePlayerToken(animalsListToken, 0, player);
			} else {
				var indexanimal = drawTokenDisplay(animalsListToken);
				placePlayerToken(animalsListToken, indexanimal, player);
			}
		}
	}

	private static void playerTakeSetDisplay(Pile pile, TileConstructor collection, ApplicationContext context,
			Player player, Point cursor) {
		List<Button> buttons = new ArrayList<>();
		buttons.add(new Button("Tile 1", 60, 60, 50, 50));
		buttons.add(new Button("Tile 2", 218, 60, 50, 50));
		buttons.add(new Button("Tile 3", 218, 218, 50, 50));
		buttons.add(new Button("Tile 4", 60, 218, 50, 50));

		boolean tilePlaced = false;
		while (!tilePlaced) {
			context.renderFrame(graphics -> {
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Arial", Font.PLAIN, 30));
				graphics.drawString("Choisissez une tuile", 20, 500);
				for (var elem : buttons) {
					graphics.setColor(Color.WHITE);
					graphics.fillRect(elem.x(), elem.y(), elem.width(), elem.height());
				}
				for (int i = 0; i < buttons.size(); i++) {
					graphics.setColor(Color.WHITE);
					buttons.get(i).renderSquare(graphics, buttons.get(i).j(), buttons.get(i).i(),
							pile.environmentsPile().get(i));
					Button.buildSquareAnimals(graphics, buttons.get(i).x() + 55, buttons.get(i).y() , pile.animalsPile().get(i).getAnimal().get(0));
				}
				
			});
			Event event = context.pollEvent();
			if (event instanceof PointerEvent pointerEvent) {
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					PointerEvent.Location location = pointerEvent.location();
					if (buttons.get(0).isClicked(location.x(), location.y())) {
						var takenSet = pile.takeSet(collection, 0);
						playerPutTileDisplay(context, takenSet, player, cursor);
						tilePlaced = true;
					} else if (buttons.get(1).isClicked(location.x(), location.y())) {
						var takenSet = pile.takeSet(collection, 1);
						playerPutTileDisplay(context, takenSet, player, cursor);
						tilePlaced = true;
					} else if (buttons.get(2).isClicked(location.x(), location.y())) {
						var takenSet = pile.takeSet(collection, 2);
						playerPutTileDisplay(context, takenSet, player, cursor);
						tilePlaced = true;
					} else if (buttons.get(3).isClicked(location.x(), location.y())) {
						var takenSet = pile.takeSet(collection, 3);
						playerPutTileDisplay(context, takenSet, player, cursor);
						tilePlaced = true;
					}
				}
			}
		}

	}

	public IdStage onGame() {
		var inGame = IdStage.ONGAME;
		int centerX = screenInfo.width() / 2;
		Button quitButton = new Button("QUITTER", centerX - 40, screenInfo.height() - 60, 100, 40);
		var mode = modeQuestion();
		var nbPlayer = playersQuestion();
		var currentPlayers = Game.initializePlayers(nbPlayer, 51, 51);
		var colection = new TileConstructor();
		var pile = new Pile();
		var tour = 1;
		colection.initializeTiles();
		firstTileChoiceDisplay(currentPlayers, colection, 51, 51, context);
		ArrayList<Players> players = new ArrayList<>(
				List.of(Players.PLAYER1, Players.PLAYER2, Players.PLAYER3, Players.PLAYER4));
		var playerActuelle = 0;
		var test = new DisplaySquare();
		var cursor = new Point(25, 25);
		while (inGame.equals(IdStage.ONGAME) && colection.getSize() > 4) {
			pile.initializePile(colection);
			renderOnGame(context, screenInfo, quitButton, players.get(playerActuelle), tour,
					currentPlayers.get(playerActuelle), test, cursor);
			playerTakeSetDisplay(pile, colection, context, currentPlayers.get(playerActuelle), cursor);
			playerPutToken(currentPlayers.get(playerActuelle));
			playerActuelle++;
			if (playerActuelle >= nbPlayer) {
				playerActuelle = 0;
			}
			inGame = quit(quitButton);
			tour++;
		}
		Game.showScore(currentPlayers, mode);
		return IdStage.LEAVE;
	}
}
