package fr.uge.display;

import java.awt.Graphics2D;

import com.github.forax.zen.ScreenInfo;

import fr.uge.mecanics.Point;
import fr.uge.tile.GridMaker;

public sealed interface DisplayShape permits DisplaySquare, DisplayHexagonal {
  void graphicDisplay(Graphics2D graphics, GridMaker grid, ScreenInfo screenInfo, Point cursor);

}
