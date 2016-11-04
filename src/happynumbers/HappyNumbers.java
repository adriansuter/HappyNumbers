package happynumbers;

import happynumbers.mapper.SumOfDigitsSquaredMapper;
import happynumbers.mapper.IMapper;
import happynumbers.provider.TwoDigitNumberProvider;
import java.io.File;
import happynumbers.output.GraphGenerator;
import happynumbers.output.HappyNumbersWriter;
import happynumbers.provider.INumberProvider;
import happynumbers.provider.ThreeDigitNumberProvider;
import happynumbers.provider.TwoAndThreeDigitNumberProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HappyNumbers {

    public static void main(String[] args) {
        Properties properties = null;
        GraphGenerator graphGenerator = null;
        INumberProvider numberProvider = null;

        File configFile = new File("config.xml");
        try {
            try (FileInputStream fileInput = new FileInputStream(configFile)) {
                properties = new Properties();
                properties.loadFromXML(fileInput);
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file 'config.properties' could not be found.");
            System.err.println("I looked in " + configFile.getAbsolutePath());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("The config file could not be read.");
            System.exit(1);
        }

        String outputPath = properties.getProperty("outputPath", "");
        File outputDirectory = new File(outputPath);
        if (!outputDirectory.isDirectory() || !outputDirectory.exists()) {
            System.err.println("The output directory '" + outputDirectory.getAbsolutePath() + "' as provided in the config property 'outputPath' could not be found or is not a directory.");
            System.exit(1);
        }

        String dotPath = properties.getProperty("dotPath");
        if (dotPath != null) {
            File dotExeFile = new File(dotPath);
            if (!dotExeFile.exists()) {
                System.err.println("The file '" + dotExeFile.getAbsolutePath() + "' as provided in the config property 'dotPath' could not be found.");
                System.exit(1);
            }

            String dotDeleteSource = properties.getProperty("dotDeleteSource", "false");
            String dotLog = properties.getProperty("dotLog", "");

            graphGenerator = new GraphGenerator(
                    outputDirectory,
                    dotExeFile,
                    dotDeleteSource.equalsIgnoreCase("true"),
                    dotLog
            );
        }

        String[] bases = properties.getProperty("bases", "2,10,16").split(",");
        if (bases.length == 0) {
            System.err.println("No bases in config property 'bases' defined.");
            System.exit(1);
        }

        switch (properties.getProperty("numberProvider")) {
            case "TwoDigits":
                numberProvider = new TwoDigitNumberProvider();
                break;
            case "ThreeDigits":
                numberProvider = new ThreeDigitNumberProvider();
                break;
            case "TwoAndThreeDigits":
                numberProvider = new TwoAndThreeDigitNumberProvider();
                break;
            default:
                System.err.println("Number provider not found.");
                System.exit(1);
        }

        // Define the mapper.
        IMapper mapper = new SumOfDigitsSquaredMapper();

        // Define the happy numbers writer.
        HappyNumbersWriter happyNumbersWriter = new HappyNumbersWriter(outputDirectory);

        for (String base : bases) {
            try {
                int radix = Integer.parseInt(base);

                // The radix has to be between 2 and 36.
                if (radix < 2 || radix > 36) {
                    throw new NumberFormatException();
                }

                HappyNumbersCalculator detector = new HappyNumbersCalculator(radix, numberProvider, mapper);
                happyNumbersWriter.write(detector, "happy_" + radix + ".txt");

                if (graphGenerator != null) {
                    graphGenerator.generate(
                            detector,
                            "happy_" + radix + ".gv",
                            "happy_" + radix + ".png"
                    );
                }

                System.out.println("Base '" + radix + "' completed.");
            } catch (NumberFormatException e) {
                System.err.println("Base '" + base + "' invalid.");
            }
        }

        System.out.println("Done :-)");
    }

}
