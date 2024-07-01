import java.util.HashMap;
import java.util.Map;

public class PokemonAttributes {
	//Enum for Pokemon types
	public enum PokemonType{
		FIRE,
		WATER,
		GRASS,
		//Can add more afterwards
	}
	
	public enum ElementType{
		FIRE,
		WATER,
		GRASS,
		//Can add more afterwards
	}
	
	//Map to store Pokemon attributes
	private Map<PokemonType, Map<ElementType, Double>> weaknesses;
	
	public PokemonAttributes() {
		weaknesses = new HashMap<>();
		initializeWeaknesses();
	}
	
	//Initialize weakness for Pokemon type
	private void initializeWeaknesses() {
		for(PokemonType type: PokemonType.values()) {
			weaknesses.put(type, new HashMap<>());
		}
		
		//Set Weakness (Can customize afterwards)
		
		//Set weakness for FIRE type Pokemon
		setWeakness(PokemonType.FIRE, ElementType.WATER, 2.0);  //FIRE type Pokemon is weak against WATER type Pokemon
		setWeakness(PokemonType.FIRE, ElementType.GRASS, 0.5);  //FIRE type Pokemon is resistant against GRASS type Pokemon
		
		//Set weakness for WATER type Pokemon
		setWeakness(PokemonType.WATER, ElementType.FIRE, 0.5);  //WATER type Pokemon is resistant against FIRE type Pokemon
		setWeakness(PokemonType.WATER, ElementType.GRASS, 2.0); //WATER type Pokemon is weak against WATER type Pokemon
		
		//Set weakness for GRASS type Pokemon
		setWeakness(PokemonType.GRASS, ElementType.FIRE, 2.0);  //GRASS type Pokemon is weak against FIRE type Pokemon
		setWeakness(PokemonType.GRASS, ElementType.WATER, 0.5); //GRASS type Pokemon is resistant against WATER type Pokemon
	}
	
	//Helper method to set weakness of a PokemonType against an ElementType
	public void setWeakness(PokemonType pokemonType, ElementType elementType, double multiplier) {
		weaknesses.get(pokemonType).put(elementType, multiplier);
	}
	
	//Get weakness multiplier of a PokemonType against an ElementType
	public double getWeakness(PokemonType pokemonType, ElementType elementType) {
        return weaknesses.get(pokemonType).getOrDefault(elementType, 1.0); // Default to 1.0 (normal damage) if not found
	}
	
}
