package fr.uge.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.ScreenInfo;



public class GameMenu {
	
	
	private static ApplicationContext context;
	private static ScreenInfo screenInfo;
	
	public GameMenu(ApplicationContext context, ScreenInfo screenInfo) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(screenInfo);
		GameMenu.context = context;
		GameMenu.screenInfo = screenInfo;
	}
    
    public static void renderCascadia(Graphics graphics, ScreenInfo screenInfo) {
    	int centerX = screenInfo.width() / 2;
        int centerY = screenInfo.height() / 2;
        graphics.setFont(new Font("Arial", Font.PLAIN, 50)); 
        graphics.setColor(Color.BLACK); 
        graphics.drawString("Cascadia", centerX - 100, centerY - 300);
    }
    
    public static void renderMenu(Graphics2D graphics, Button playButton, Button quitButton, ScreenInfo screenInfo) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, screenInfo.width(), screenInfo.height());
        playButton.render(graphics);
        quitButton.render(graphics);
        renderCascadia(graphics, screenInfo);
    }
    
    public IdStage displayMenu() { 
        int centerX = screenInfo.width() / 2;
        int centerY = screenInfo.height() / 2;
        Button playButton = new Button("JOUER", centerX - 50, centerY - 60, 100, 40);
        Button quitButton = new Button("QUITTER", centerX - 50, centerY + 60, 100, 40);
        context.renderFrame(graphics -> renderMenu(graphics, playButton, quitButton, screenInfo));
        Event event = context.pollEvent();
        if (event != null) {
            if (event.getClass().equals(PointerEvent.class)) {
                PointerEvent pointerEvent = (PointerEvent) event;
                if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
                    PointerEvent.Location location = pointerEvent.location();
                    if (playButton.isClicked(location.x(), location.y())) {
                        return IdStage.ONGAME;
                    } else if (quitButton.isClicked(location.x(), location.y())) {
                        return IdStage.LEAVE;
                    }
                }
            }
        }
        return IdStage.ONGAME;
    }
}
    
    
    
    
 

