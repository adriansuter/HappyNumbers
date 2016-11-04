package happynumbers.output;

import happynumbers.HappyNumbersDetector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class GraphGenerator {

    private final File _directory;
    private final File _dotExe;
    private final boolean _deleteSource;
    private final String _dotLog;

    public GraphGenerator(File directory, File dotExe, boolean deleteSource, String dotLog) {
        this._directory = directory;
        this._dotExe = dotExe;
        this._deleteSource = deleteSource;
        this._dotLog = dotLog;
    }

    public void generate(HappyNumbersDetector detector, String gvFilename, String pngFilename) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {");
        sb.append("label=\"Base: ").append(detector.getRadix()).append(", Mapper: ").append(detector.getMapper().getLabel()).append("\";");

        Iterator<String> happyNumbersIterator = detector.getHappyNumbers().iterator();
        while (happyNumbersIterator.hasNext()) {
            sb.append("\"").append(happyNumbersIterator.next()).append("\" [shape=box];");
        }

        detector.getNumberMap().entrySet().stream().forEach((pair) -> {
            sb.append("\"").append(pair.getKey()).append("\"->\"").append(pair.getValue()).append("\";");
        });

        sb.append("}");

        BufferedWriter writer = null;
        try {
            File gvFile = new File(this._directory.getCanonicalPath() + System.getProperty("file.separator") + gvFilename);

            // Write the garphviz file.
            writer = new BufferedWriter(new FileWriter(gvFile));
            writer.write(sb.toString());
            writer.close();

            // Define the desired output png file.
            File pngFile = new File(this._directory.getCanonicalPath() + System.getProperty("file.separator") + pngFilename);

            // Define the process builder to run "dot.exe".
            ProcessBuilder pb = new ProcessBuilder(
                    _dotExe.getCanonicalPath(),
                    "-Tpng",
                    gvFile.getCanonicalPath(),
                    "-o" + pngFile.getCanonicalPath()
            );

            // Start the "dot.exe" process.
            File logFile = null;
            if (!this._dotLog.isEmpty()) {
                logFile = new File(this._directory.getCanonicalPath() + System.getProperty("file.separator") + this._dotLog);
                pb.redirectErrorStream(true);
                pb.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            }

            Process p = pb.start();
            assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
            if (!this._dotLog.isEmpty()) {
                assert pb.redirectOutput().file() == logFile;
            }
            assert p.getInputStream().read() == -1;

            p.waitFor();

            if (this._deleteSource) {
                gvFile.delete();
            }
        } catch (IOException | InterruptedException e) {
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
