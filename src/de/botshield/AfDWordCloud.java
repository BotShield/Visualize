package de.botshield;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.RectangleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

public class AfDWordCloud {

    private final Random RANDOM = new Random();
    private PGDBConnection dbConn;

    private ColorPalette buildRandomColorPalette(int n) {
        final Color[] colors = new Color[n];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(RANDOM.nextInt(230) + 25,
                    RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25);
        }
        return new ColorPalette(colors);
    }

    private Set<String> loadStopWords() {
        try {
            final List<String> lines = IOUtils
                    .readLines(getInputStream("text/stop_words.txt"));
            return new HashSet<>(lines);

        } catch (IOException e) {
            // LOGGER.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    private InputStream getInputStream(final String path) {
        return Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(path);
    }

    public void createCloud() {
        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        List<WordFrequency> wordFrequencies = null;
        try {
            wordFrequencies = frequencyAnalyzer
                    .load(getInputStream("data_status_text.csv"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Dimension dimension = new Dimension(600, 600);
        final WordCloud wordCloud = new WordCloud(dimension,
                CollisionMode.RECTANGLE);
        wordCloud.setPadding(0);
        wordCloud.setBackground(new RectangleBackground(dimension));
        wordCloud.setColorPalette(buildRandomColorPalette(20));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("wordcloud_rectangle.png");
    }

    /**
     * @return -1 if connection could not be established
     */
    public int setupConnection() {
        dbConn = new PGDBConnection();
        return dbConn.establishConnection("jstrebel", "", "twitter"); // login
        // meiner
        // Testdatenbank
        // auf localhost
    }

    public static void main(String[] args) {
        AfDWordCloud objWC = new AfDWordCloud();
        // objWC.setupConnection();
        objWC.createCloud();
    }

}
