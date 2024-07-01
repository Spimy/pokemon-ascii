package dev.spimy.pokemon.pokemon;

import java.util.List;

public enum PokemonType {
	FIRE,
	WATER,
	GRASS;
	
	private List<PokemonType> weakAgainst;
	
	static {
		FIRE.weakAgainst = List.of(WATER);
		WATER.weakAgainst = List.of(GRASS);
		GRASS.weakAgainst = List.of(FIRE);
	}
	
	public List<PokemonType> getWeakAgainst(){
		return weakAgainst;
	}
}

