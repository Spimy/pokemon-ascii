package dev.spimy.pokemon.player.saves;

import java.util.Collections;

public class Scoreboard extends SaveFileHandler<Integer> {
    public Scoreboard() {
        super("scoreboard", new String[]{"Top 5 Score"});
        this.data.sort(Collections.reverseOrder());
    }

    @Override
    protected Integer parseData(final String[] rawDataRow) {
        try {
            return Integer.parseInt(rawDataRow[0]);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    @Override
    protected String[] createColumns(final Integer data) {
        return new String[]{String.valueOf(data)};
    }

    public void addBattleScore(final int score) {
        if (this.data.size() < 5) {
            this.data.add(score);
            this.data.sort(Collections.reverseOrder());
            return;
        }

        // Last score is always the lowest score
        if (score < this.data.getLast()) return;

        this.data.set(this.data.size() - 1, score);
        this.data.sort(Collections.reverseOrder());
    }
}
