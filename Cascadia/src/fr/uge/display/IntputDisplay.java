package fr.uge.display;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;

import fr.uge.player.Player;
import fr.uge.tile.TileConstructor;

public record IntputDisplay() {
	
	public static IdStage MakeAChoice(String question, ApplicationContext context) {
		var list = new ArrayList<Button>();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(20, 450, 300, 50);
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("Arial", Font.PLAIN, 25));
			graphics.drawString(question, 20, 500);
		    Button yes = new Button("Oui", 20, 525, 100, 40);
		    Button no = new Button("Non", 140, 525, 100, 40);
		    yes.render(graphics);
		    no.render(graphics);
		    list.add(yes);
		    list.add(no);
		});
	    while(true) {
	    	Event event = context.pollEvent();
			if ((event != null) && (event.getClass().equals(PointerEvent.class))) {
				PointerEvent pointerEvent = (PointerEvent) event;
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					PointerEvent.Location location = pointerEvent.location();
					if(list.get(0).isClicked(location.x(), location.y())){
						return IdStage.YES;
					}
					else if(list.get(1).isClicked(location.x(), location.y())){
						return IdStage.NO;
					}
				}
			}
	    }
    }
    
	public static void firstTile(String message, List<Player> currentPlayers, TileConstructor collection, int height, int weight, ApplicationContext context) {
		    for (var player : currentPlayers) {
		        var startTiles = collection.startingTilesDisplay();
		        List<Button> buttons = new ArrayList<>();
		        buttons.add(new Button("TileStart 1", 213, 540, 50, 50));
		        buttons.add(new Button("TileStart 2", 853, 540, 50, 50));
		        buttons.add(new Button("TileStart 3", 1493, 540, 50, 50));
		        boolean tilePlaced = false;
		        while (!tilePlaced) {
		            context.renderFrame(graphics -> {
		                graphics.setColor(Color.WHITE);
		                graphics.fillRect(0, 0, context.getScreenInfo().width(), context.getScreenInfo().height());
		                for (int i = 0; i < buttons.size(); i++) {
		                	graphics.setColor(Color.WHITE);
		                    buttons.get(i).renderSquareTile(graphics, buttons.get(i).x(), buttons.get(i).y(), startTiles.get(i).tiles());
		                }
		                for(var i = 0; i < currentPlayers.size(); i++) {
		                	if(player.equals(currentPlayers.get(i))) {	
		                		graphics.setFont(new Font("Arial", Font.PLAIN, 20));
		                		graphics.setColor(Color.BLACK);
			                	graphics.drawString("Player " + ++i, context.getScreenInfo().width() / 2 - 50, context.getScreenInfo().height() / 2 - 400);
			                }
		                }
		            });
		            Event event = context.pollEvent();
		            if (event instanceof PointerEvent pointerEvent) {
		                if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
		                    PointerEvent.Location location = pointerEvent.location();
		                    for(var i = 0; i < collection.getSizeStartingTiles(); i++) {
		                    	if (buttons.get(i).isClicked(location.x(), location.y())) {
			                        player.placeFirstTile(height / 2 + 1, weight / 2 + 1, collection.getStartingTile(i));
			                        tilePlaced = true;
			                    }
		                    }
		                }
		            }
		        }
		    }
		}
	
	
	
}
