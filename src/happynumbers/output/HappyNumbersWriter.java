package happynumbers.output;

import happynumbers.HappyNumbersCalculator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

public class HappyNumbersWriter {

    private final File _directory;

    public HappyNumbersWriter(File directory) {
        this._directory = directory;
    }

    public void write(HappyNumbersCalculator detector, String filename) {
        BufferedWriter writer = null;
        try {
            File file = new File(_directory.getCanonicalPath() + System.getProperty("file.separator") + filename);

            String lineSeparator = System.getProperty("line.separator");

            writer = new BufferedWriter(new FileWriter(file));
            Iterator<String> iterator = detector.getHappyNumbers(true).iterator();
            while (iterator.hasNext()) {
                writer.write(iterator.next() + lineSeparator);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getLocalizedMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
