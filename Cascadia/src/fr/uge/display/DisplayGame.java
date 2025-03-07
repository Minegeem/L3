package fr.uge.display;


import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;



public class DisplayGame {
    
  private static IdStage switchChoice(IdStage click, ScreenInfo screenInfo, ApplicationContext context) {
	switch(click) {
	  case IdStage.LEAVE -> { 
	    if(context != null) {
	      context.dispose();
	    }
	  }
	  case IdStage.MENU -> { 
	    var gameMenu = new GameMenu(context, screenInfo);
		  click = gameMenu.displayMenu();
	  }
	  case IdStage.ONGAME -> {
		var onGame = new OnGame(context, screenInfo);
		  click = onGame.onGame();
		}
	  default -> { return click; }
	  }
	return click;
  }   
	    
  public static void onStart(ApplicationContext context) {
	ScreenInfo screenInfo = context.getScreenInfo();
	
	var click = IdStage.MENU;
	while(true) { 
		click = switchChoice(click, screenInfo, context);
	}
  }
}

