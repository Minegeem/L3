package fr.uge.mecanics;

public record Intermediaire() implements Mode {
	
	public int nb_point(int point) {
        if (point == 2) return 5;
        if (point == 3) return 8;
        if (point >= 4) return 12;
        return 0;
	}
}
