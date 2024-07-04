package dev.spimy.pokemon.saves;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class Scoreboard extends SaveFileHandler<Integer> {
    public Scoreboard() {
        super("scoreboard", new String[]{"Top 5 Score"});
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
            printWriter.println(this.headers[0]);

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

        this.data.set(this.data.size() - 1, score);
        this.data.sort(Collections.reverseOrder());
    }
}
