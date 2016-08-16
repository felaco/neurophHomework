package org.FelipeProjects.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.FelipeProjects.training.TrainingBase;
import org.FelipeProjects.util.FeaturesStringParser;
import org.FelipeProjects.util.ReadExcel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.RBFLearning;
import org.neuroph.util.NeuralNetworkFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by felipe on 09-08-2016.
 */
public class controller
{
    private File trainExcel;
    private File testExcel;
    XYChart.Series trainingSeries;
    RBFNetwork network;
    int iteration = 0;
    DataSet trainDataset;
    DataSet testDataset;

    @FXML
    private Button trainExcelButton;

    @FXML
    private Button testExcelButton;

    @FXML
    private TextField trainExcelTextfield;

    @FXML
    private TextField testExcelTestfield;

    @FXML
    private Spinner<Number> maxEpochSpinner;

    @FXML
    private Spinner<Number> maxErrorSpinner;

    @FXML
    private LineChart<Number, Number> chart;

    @FXML
    private Button trainButton;

    @FXML
    private TextField featuresTextfield;

    @FXML
    private Spinner<Number> hiddenNodesTextfield;

    @FXML
    private Spinner<Number> ParticlesSpinner;

    @FXML
    private TextArea textArea;

    @FXML
    private Button TestButton;

    @FXML
    private void initialize()
    {
        SpinnerValueFactory maxEpochFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, 10);
        maxEpochSpinner.setValueFactory(maxEpochFactory);
        maxEpochSpinner.setEditable(true);


        maxEpochSpinner.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            //intuitive method on textField, has no effect, though
            //spinner.getEditor().commitValue();
            commitEditorText(maxEpochSpinner);
        });

        ParticlesSpinner.setEditable(true);
        SpinnerValueFactory particlesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 10);
        ParticlesSpinner.setValueFactory(particlesFactory);
        ParticlesSpinner.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            //intuitive method on textField, has no effect, though
            //spinner.getEditor().commitValue();
            commitEditorText(ParticlesSpinner);
        });

        hiddenNodesTextfield.setEditable(true);
        SpinnerValueFactory hiddenNodesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20,4,1);
        hiddenNodesTextfield.setValueFactory(hiddenNodesFactory);

        hiddenNodesTextfield.focusedProperty().addListener((s, ov, nv) -> {
            if (nv) return;
            //intuitive method on textField, has no effect, though
            //spinner.getEditor().commitValue();
            commitEditorText(hiddenNodesTextfield);
        });

        SpinnerValueFactory errorValFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1);
        errorValFactory.setConverter(new StringConverter<Double>()
        {
            private final DecimalFormat df = new DecimalFormat("#.########");

            @Override
            public String toString(Double value)
            {
                if (value == null) {
                    return "";
                }

                return df.format(value);
            }

            @Override
            public Double fromString(String value)
            {
                try {
                    // If the specified value is null or zero-length, return null
                    if (value == null) {
                        return null;
                    }

                    value = value.trim();

                    if (value.length() < 1) {
                        return null;
                    }

                    // Perform the requested parsing
                    return df.parse(value).doubleValue();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }


        });

        ((SpinnerValueFactory.DoubleSpinnerValueFactory) errorValFactory).setAmountToStepBy(0.001);
        errorValFactory.setValue(0.001);
        maxErrorSpinner.setValueFactory(errorValFactory);

        trainExcelButton.setOnAction((event)->{
            FileChooser chooser = new FileChooser();

            trainExcel = chooser.showOpenDialog(null);
            if (trainExcel != null)
                trainExcelTextfield.setText(trainExcel.getPath());

        });

        testExcelButton.setOnAction((event)->{
            FileChooser chooser = new FileChooser();

            int i;
            testExcel = chooser.showOpenDialog(null);
            if (trainExcel != null)
                trainExcelTextfield.setText(trainExcel.getPath());

        });

        trainExcelTextfield.focusedProperty().addListener((s,ov,nv)->{
            if (nv) return;
            trainExcelTextfield.commitValue();
        });

        testExcelTestfield.focusedProperty().addListener((s,ov,nv)->{
            if (nv) return;
            testExcelTestfield.commitValue();
        });


        trainButton.setOnAction(this::train);
        TestButton.setOnAction(this::test);
    }

    private <T> void commitEditorText(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

    @FXML
    private void train (ActionEvent event)
    {
        TrainHandeler trainiHandeler = new TrainHandeler();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(trainiHandeler);

    }

    private void test (ActionEvent event)
    {
        List <Integer> featuresList;
        featuresList= FeaturesStringParser.parseFeatures(featuresTextfield.getText());

        Runnable readExcelTask = ()->{
            textArea.setText(textArea.getText().concat("Leyendo Excel de entrenamiento..."));
            testDataset = ReadExcel.readExcel(testExcel.getPath(), featuresList);
            textArea.setText(textArea.getText().concat("\nExcel de entrenamiento leído\n"));
        };
        ExecutorService readExcelExecutor = Executors.newSingleThreadExecutor();
        readExcelExecutor.execute(readExcelTask);
        readExcelExecutor.shutdown();
        try
        {
            readExcelExecutor.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        ArrayList <Integer> desired = new ArrayList<>();
        ArrayList <Integer> predicted = new ArrayList<>();

        for (int i = 0; i < testDataset.size(); i++)
        {
            DataSetRow entry = testDataset.getRowAt(i);
            network.setInput(entry.getInput());
            network.calculate();
            double [] nRes = network.getOutput();
            int output = round(nRes[0]);

            desired.add((int) entry.getDesiredOutput()[0]);
            predicted.add(output);
        }

        ReadExcel.writeExcel(desired, predicted);
    }

    private class TrainHandeler implements Runnable
    {
        @Override
        public void run()
        {
            textArea.clear();

            if (testExcel == null)
            {
                testExcel = new File(testExcelTestfield.getText());
            }
            if (trainExcel == null)
            {
                trainExcel = new File(trainExcelTextfield.getText());
            }
            trainingSeries = new XYChart.Series();
            trainingSeries.setName("MSE");

            Platform.runLater(()->{
                chart.setData(FXCollections.observableArrayList());
                chart.getData().add(trainingSeries);
            });


            ArrayList<Integer> featuresList;
            featuresList = FeaturesStringParser.parseFeatures(featuresTextfield.getText());

            Runnable readExcelTask = ()->{
                textArea.setText("Leyendo Excel de entrenamiento...");
                trainDataset = ReadExcel.readExcel(trainExcel.getPath(), featuresList);
                textArea.setText(textArea.getText().concat("\nExcel de entrenamiento leído\n"));
            };
            ExecutorService readExcelExecutor = Executors.newSingleThreadExecutor();
            readExcelExecutor.execute(readExcelTask);
            readExcelExecutor.shutdown();



            network = NeuralNetworkFactory.createRbfNetwork(featuresList.size(),hiddenNodesTextfield.getValue().intValue(),1);
            //RBFLearning learning = new RBFLearning();
            TrainingBase learning = new TrainingBase();
            learning.setMaxIterations(maxEpochSpinner.getValue().intValue());
            learning.setMaxError(maxErrorSpinner.getValue().intValue());
            learning.setParticlesCount(ParticlesSpinner.getValue().intValue());
            learning.setNeuralNetwork(network);
            network.setLearningRule(learning);
            iteration = 0;
            EpochUpdater updater = new EpochUpdater(chart, textArea,trainingSeries,learning);

            learning.addListener(updater);
            Runnable learnTask = ()->{
                network.learn(trainDataset);
            };
            ExecutorService learnExecutor = Executors.newSingleThreadExecutor();

            try
            {
                readExcelExecutor.awaitTermination(1, TimeUnit.MINUTES);
                learnExecutor.execute(learnTask);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    int round (double val)
    {
        if (val > 0.5)
            return 1;
        else
            return 0;
    }
}
