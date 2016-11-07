/*
 * The MIT License
 *
 * Copyright 2016 Adrian Suter, https://github.com/adriansuter/.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package happynumbers.ui;

import happynumbers.HappyNumbersTask;
import happynumbers.Main;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * FXML Controller class
 *
 * @author Adrian Suter, https://github.com/adriansuter/
 */
public class MainStageController implements Initializable {

    @FXML
    private Button testMapperButton;

    @FXML
    private TextArea mapperFunctionTextArea;

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label taskMessageLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
        HappyNumbersTask task = new HappyNumbersTask();

        this.taskProgressBar.progressProperty().bind(task.progressProperty());
        this.taskMessageLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    @FXML
    protected void handleTestMapperButtonAction(ActionEvent event) {
        String mapperFunction = this.mapperFunctionTextArea.getText();

        ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager.getEngineByName("js");
        try {
            engine.eval("function mapper(number, numberBase, base) { var result = 0; " + mapperFunction + " return result; }");

            Invocable inv = (Invocable) engine;
            Object answer = inv.invokeFunction("mapper", 13, "D", 16);
            if (answer instanceof Double) {
                int g = (int) Double.parseDouble(answer.toString());
                System.out.println(g);
            }

            answer = inv.invokeFunction("mapper", 13, "13", 10);
            if (answer instanceof Double || answer instanceof Integer) {
                int g = (int) Double.parseDouble(answer.toString());
                System.out.println(g);
            }
        } catch (ScriptException | NoSuchMethodException scriptException) {
            System.err.println("ERRRRRROR");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, scriptException);
        }
    }

}
