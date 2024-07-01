import java.util.List;

public enum type {
	FIRE,
	WATER,
	GRASS;
	
	private List<type> weakAgainst;
	
	static {
		FIRE.weakAgainst = List.of(WATER);
		WATER.weakAgainst = List.of(GRASS);
		GRASS.weakAgainst = List.of(FIRE);
	}
	
	public List<type> getWeakAgainst(){
		return weakAgainst;
	}
}

