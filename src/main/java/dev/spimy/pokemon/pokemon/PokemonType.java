package dev.spimy.pokemon.pokemon;

import java.util.List;

public enum PokemonType {
	FIRE,
	WATER,
	GRASS,
	ELECTRIC,
	NORMAL,
	BUG,
	FAIRY,
	POISON,
	PSYCHIC,
	ROCK,
	GHOST,
	STEEL;

	private List<PokemonType> weakAgainst;
	
	static {
		FIRE.weakAgainst = List.of(WATER, ROCK);
		WATER.weakAgainst = List.of(GRASS, ELECTRIC);
		GRASS.weakAgainst = List.of(FIRE, POISON, BUG);
		ELECTRIC.weakAgainst = List.of();
		NORMAL.weakAgainst = List.of();
		BUG.weakAgainst = List.of(FIRE, ROCK);
		FAIRY.weakAgainst = List.of(POISON, STEEL);
		POISON.weakAgainst = List.of(PSYCHIC);
		PSYCHIC.weakAgainst = List.of(BUG, GHOST);
		ROCK.weakAgainst = List.of(WATER, GRASS, STEEL);
		GHOST.weakAgainst = List.of(GHOST);
		STEEL.weakAgainst = List.of(FIRE, GHOST);
	}
	
	public List<PokemonType> getWeakAgainst(){
		return weakAgainst;
	}
}

