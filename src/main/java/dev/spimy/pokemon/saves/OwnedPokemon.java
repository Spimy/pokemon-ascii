package dev.spimy.pokemon.saves;

import dev.spimy.pokemon.pokemon.Pokemon;
import dev.spimy.pokemon.pokemon.PokemonType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class OwnedPokemon extends SaveFileHandler<Pokemon> {
    public OwnedPokemon() {
        super("pokemons", new String[]{
            "Name", "Type", "Max HP", "Current HP", "Attack Power", "Speed", "Crit Rate", "Exp"
        });
    }

    @Override
    protected Pokemon parseData(final String[] rawDataRow) {
        try {
            return new Pokemon(
                rawDataRow[0],
                PokemonType.valueOf(rawDataRow[1]),
                Integer.parseInt(rawDataRow[2]),
                Integer.parseInt(rawDataRow[3]),
                Integer.parseInt(rawDataRow[4]),
                Integer.parseInt(rawDataRow[5]),
                Integer.parseInt(rawDataRow[6]),
                Integer.parseInt(rawDataRow[7])
            );

        } catch (NumberFormatException _) {
            return null;
        }
    }

    @Override
    protected String[] createColumns(final Pokemon pokemon) {
        return pokemon.toString().split(",");
    }

    public void addPokemon(final Pokemon pokemon) {
        this.getData().add(pokemon);
    }

    @SuppressWarnings("StringTemplateMigration")
    public void tabulate() {
        final List<String[]> flattened = new ArrayList<>(IntStream.range(0, this.getData().size())
                .mapToObj(index -> ((index + 1) + "," + this.getData().get(index).toString()).split(","))
                .toList());

        String[] headers = new String[this.headers.length + 1];
        headers[0] = "Index";
        System.arraycopy(this.headers, 0, headers, 1, this.headers.length);
        flattened.add(headers);

        int[] columnWidths = new int[headers.length];

        // Initialize column widths with header lengths
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }

        // Update column widths based on the data
        for (String[] row : flattened) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }

        StringBuilder headerLineBuilder = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i];
            int width = columnWidths[i];

            String formattedHeader = this.centerString(header, width);
            headerLineBuilder.append(formattedHeader);

            if (i < headers.length - 1) {
                headerLineBuilder.append(" | ");
            }
        }

        String headerLine = headerLineBuilder.toString();
        System.out.println(headerLine);
        System.out.println("-".repeat(headerLine.length()));

        // Print each row formatted according to column widths
        for (int i = 0; i < flattened.size() - 1; i++) {
            final String rowLine = this.getRowLine(flattened.get(i), columnWidths);
            System.out.println(rowLine);
        }
    }

    private String getRowLine(final String[] row, final int[] columnWidths) {
        StringBuilder rowLineBuilder = new StringBuilder();

        for (int i = 0; i < row.length; i++) {
            String item = row[i];
            int width = columnWidths[i];

            String formattedItem = this.centerString(item, width);
            rowLineBuilder.append(formattedItem);

            if (i < row.length - 1) {
                rowLineBuilder.append(" | ");
            }
        }

        return rowLineBuilder.toString();
    }

    private String centerString(final String str, final int width) {
        if (str.length() >= width) return str;

        final int padding = (width - str.length()) / 2;
        final StringBuilder centered = new StringBuilder();

        centered.append(" ".repeat(padding));
        centered.append(str);

        while (centered.length() < width) centered.append(" ");
        return centered.toString();
    }
}
