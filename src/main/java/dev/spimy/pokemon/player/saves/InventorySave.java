package dev.spimy.pokemon.player.saves;

import dev.spimy.pokemon.player.Inventory;
import dev.spimy.pokemon.player.Pokeball;

import java.util.HashMap;

public class InventorySave extends SaveFileHandler<Inventory> {
    public InventorySave() {
        super(
            "inventory",
            new String[]{
                    "exp",
                    "money",
                    Pokeball.NORMAL.toString(),
                    Pokeball.ULTRA.toString(),
                    Pokeball.MASTER.toString()
            }
        );

        if (this.getData().isEmpty()) {
            final HashMap<Pokeball, Integer> pokeballs = new HashMap<>();
            pokeballs.put(Pokeball.NORMAL, 0);
            pokeballs.put(Pokeball.ULTRA, 0);
            pokeballs.put(Pokeball.MASTER, 0);

            this.getData().add(new Inventory(0, 0, pokeballs));
            this.updateSaveFile();
        }
    }

    @Override
    protected Inventory parseData(String[] rawDataRow) {
        try {
            final HashMap<Pokeball, Integer> pokeballs = new HashMap<>();
            pokeballs.put(Pokeball.NORMAL, Integer.parseInt(rawDataRow[2]));
            pokeballs.put(Pokeball.ULTRA, Integer.parseInt(rawDataRow[3]));
            pokeballs.put(Pokeball.MASTER, Integer.parseInt(rawDataRow[4]));

            return new Inventory(Integer.parseInt(rawDataRow[0]), Integer.parseInt(rawDataRow[1]), pokeballs);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    @Override
    protected String[] createColumns(Inventory data) {
        return new String[]{
                String.valueOf(data.getExp()),
                String.valueOf(data.getMoney()),
                String.valueOf(data.getPokeballs().get(Pokeball.NORMAL)),
                String.valueOf(data.getPokeballs().get(Pokeball.ULTRA)),
                String.valueOf(data.getPokeballs().get(Pokeball.MASTER))
        };
    }


    /**
     * Solely for getting the pokeballs. Should not update through this method.
     *
     * @return pokeballs that the player has
     */
    public HashMap<Pokeball, Integer> getPokeballs() {
        return this.getData().getFirst().getPokeballs();
    }

    public int getTotalPokeballs() {
        return this.getPokeballs().values().stream().reduce(0, Integer::sum);
    }
}
