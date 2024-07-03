package dev.spimy.pokemon.saves;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvSerializer<T> {
    private final String saveFile;
    protected final URL root;

    protected List<T> data = new ArrayList<>();

    public CsvSerializer(final String saveFile) {
        this.saveFile = saveFile;
        this.root = getClass().getProtectionDomain().getCodeSource().getLocation();
        this.loadSaveFile();
    }

    protected File getSaveFile() {
        try {
            final File dataFolder = new File(
                    Paths.get(
                            Paths.get(this.root.toURI()).toString(),
                            "..",
                            "data"
                    ).toString()
            );

            if (!dataFolder.exists()) dataFolder.mkdir();

            return new File(Paths.get(dataFolder.getAbsolutePath(), this.saveFile).toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void loadSaveFile();

    public abstract void updateSaveFile();

    protected List<String[]> getRawData() {
        final File save = this.getSaveFile();
        final List<String[]> rawData = new ArrayList<>();

        try (final FileReader fileReader = new FileReader(save.getAbsoluteFile())) {
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int index = 0;

            while ((line = bufferedReader.readLine()) != null) {
                // Skip header
                if (index == 0) {
                    index++;
                    continue;
                }
                ;

                String[] lineData = line.split(",");
                rawData.add(lineData);

                index++;
            }

        } catch (final IOException e) {
            return rawData;
        }

        return rawData;
    }
}
