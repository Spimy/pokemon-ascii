package dev.spimy.pokemon.saves;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class Scoreboard extends CsvSerializer<Integer> {
    private int lowestScoreIndex = 0;

    public Scoreboard() {
        super("scoreboard.csv");
    }

    @Override
    protected void loadSaveFile() {
        final List<String[]> rawData = this.getRawData();

        for (int i = 0; i < rawData.size(); i++) {
            for (final String data : rawData.get(i)) {
                try {
                    final int score = Integer.parseInt(data);
                    this.data.add(Integer.parseInt(data));

                    if (score < this.data.get(this.lowestScoreIndex)) {
                        this.lowestScoreIndex = i;
                    }
                } catch (NumberFormatException _) {}
            }
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

    public void addScore(final int score) {
        if (this.data.size() < 5) {
            this.data.add(score);
            this.data.sort(Collections.reverseOrder());
            return;
        }

        if (score < this.data.get(this.lowestScoreIndex)) return;

        this.data.set(this.lowestScoreIndex, score);
        this.data.sort(Collections.reverseOrder());
    }
}
