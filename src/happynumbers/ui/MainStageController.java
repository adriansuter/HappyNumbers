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

import happynumbers.HappyNumbersResult;
import happynumbers.HappyNumbersTask;
import happynumbers.Main;
import happynumbers.provider.NumberProvider;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private FlowPane baseCheckBoxPane;

    @FXML
    private Button testMapperButton;

    @FXML
    private TextArea mapperFunctionTextArea;

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label taskMessageLabel;

    @FXML
    private TextFlow mapperFunctionTextFlow;

    @FXML
    private CheckBox numbers1CheckBox;

    @FXML
    private CheckBox numbers2CheckBox;

    @FXML
    private CheckBox numbers3CheckBox;

    @FXML
    private CheckBox numbers4CheckBox;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private TableView<HappyNumbersResult> tableView;

    @FXML
    private TableColumn<HappyNumbersResult, Integer> baseTableColumn;

    @FXML
    private TableColumn<HappyNumbersResult, Integer> numbersCountTableColumn;

    private final ObservableList<HappyNumbersResult> results = FXCollections.observableArrayList();

    public BooleanProperty running = new SimpleBooleanProperty(false);

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.startButton.disableProperty().bind(running);
        this.stopButton.visibleProperty().bind(running);
        this.taskProgressBar.visibleProperty().bind(running);

        this.tableView.setItems(this.results);
        this.baseTableColumn.setCellValueFactory(new PropertyValueFactory("base"));
        this.numbersCountTableColumn.setCellValueFactory(new PropertyValueFactory("numbersCount"));

        // Tests....
        results.add(new HappyNumbersResult(10, 16));
        results.add(new HappyNumbersResult(12, 12));

        ///
        Text text1 = new Text("Available variables:\n");
        text1.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));

        Text text2 = new Text("number [int]");
        text2.setFill(Color.BLUE);

        Text text3 = new Text(" The number in base 10.\n");

        Text text4 = new Text("numberBase [String]");
        text4.setFill(Color.BLUE);

        Text text5 = new Text(" The number in the given base.\n");

        Text text6 = new Text("base [int]");
        text6.setFill(Color.BLUE);

        Text text7 = new Text(" The base.\n");

        mapperFunctionTextFlow.getChildren().addAll(text1, text2, text3, text4, text5, text6, text7);
    }

    @FXML
    protected void handleStartButtonAction(ActionEvent event) {
        if (this.isRunning()) {
            return;
        }

        this.setRunning(true);

        NumberProvider numberProvider = new NumberProvider(
                this.numbers1CheckBox.isSelected(),
                this.numbers2CheckBox.isSelected(),
                this.numbers3CheckBox.isSelected(),
                this.numbers4CheckBox.isSelected());

        int base = 2;
        ArrayList<Integer> selectedBases = new ArrayList<>();
        Iterator<Node> iterator = this.baseCheckBoxPane.getChildren().iterator();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next instanceof CheckBox) {
                if (((CheckBox) next).isSelected()) {
                    selectedBases.add(base);
                }
                base++;
            }
        }

        task = new HappyNumbersTask(numberProvider, selectedBases);
        task.setOnSucceeded((WorkerStateEvent event1) -> {
            this.setRunning(false);
            System.out.println("DONE");
        });
        this.taskProgressBar.progressProperty().bind(task.progressProperty());
        this.taskMessageLabel.textProperty().bind(task.messageProperty());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private HappyNumbersTask task;

    @FXML
    protected void handleStopButtonAction(ActionEvent event) {
        if (this.isRunning()) {
            task.cancel();
            this.setRunning(false);
        }
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
