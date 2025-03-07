package fr.uge.mecanics;

public record Famille() implements Mode{
	
	public int nb_point(int point) {
		if (point == 1) return 2;
		if (point == 2) return 5;
		if (point >= 3) return 9;
		return 0;
	}
}
