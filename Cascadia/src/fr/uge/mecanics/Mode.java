package fr.uge.mecanics;

public sealed interface Mode permits Famille, Intermediaire{
	public int nb_point(int point);
}
