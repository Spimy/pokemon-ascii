package dev.spimy.pokemon.saves;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Scoreboard extends SaveFileHandler<Integer> {
    private int lowestScoreIndex = 0;

    public Scoreboard() {
        super("scoreboard.csv");
        this.data.sort(Collections.reverseOrder());
    }

    @Override
    protected Integer parseData(String[] rawDataRow) {
        try {
            return Integer.parseInt(rawDataRow[0]);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    @Override
    public void updateSaveFile() {
        try (final PrintWriter printWriter = new PrintWriter(this.getSaveFile().getAbsoluteFile())) {
            printWriter.println("Top 5 Score");

            for (Integer score : this.data) {
                printWriter.println(score);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBattleScore(final int score) {
        if (this.data.size() < 5) {
            this.data.add(score);
            this.data.sort(Collections.reverseOrder());
            return;
        }

        // Last score is always the lowest score
        if (score < this.data.getLast()) return;

        this.data.set(this.lowestScoreIndex, score);
        this.data.sort(Collections.reverseOrder());
    }
}
