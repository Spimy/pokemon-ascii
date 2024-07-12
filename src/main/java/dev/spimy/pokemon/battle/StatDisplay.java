package dev.spimy.pokemon.battle;

import dev.spimy.pokemon.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class StatDisplay {
    private final String[] headers = new String[]{
            "Index", "Name", "Type", "Max HP", "Current HP", "Attack Power", "Speed", "Crit Rate", "Exp"
    };

    @SuppressWarnings("StringTemplateMigration")
    public void tabulate(List<Pokemon> pokemons) {
        final List<String[]> flattened = new ArrayList<>(IntStream.range(0, pokemons.size())
                .mapToObj(index -> ((index + 1) + "," + pokemons.get(index).toString()).split(","))
                .toList());
        flattened.add(this.headers);

        int[] columnWidths = new int[this.headers.length];

        // Initialize column widths with header lengths
        for (int i = 0; i < this.headers.length; i++) {
            columnWidths[i] = this.headers[i].length();
        }

        // Update column widths based on the data
        for (String[] row : flattened) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }

        StringBuilder headerLineBuilder = new StringBuilder();
        for (int i = 0; i < this.headers.length; i++) {
            String header = this.headers[i];
            int width = columnWidths[i];

            String formattedHeader = this.centerString(header, width);
            headerLineBuilder.append(formattedHeader);

            if (i < this.headers.length - 1) {
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
