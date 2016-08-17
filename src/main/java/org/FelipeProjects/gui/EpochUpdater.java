package org.FelipeProjects.gui;

import javafx.application.Platform;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import org.FelipeProjects.training.TrainingBase;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.RBFLearning;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by felipe on 15-08-2016.
 */
public class EpochUpdater implements LearningEventListener
{
    TrainingBase learning;
    XYChart.Series trainingSeries;
    TextArea textArea;
    LineChart<Number, Number> chart;
    int iteration;

    public EpochUpdater(LineChart chart,
                        TextArea textArea,
                        XYChart.Series serie,
                        TrainingBase learning)
    {
        this.learning = learning;
        this.trainingSeries = serie;
        this.textArea = textArea;
        this.chart = chart;
    }


    @Override
    public void handleLearningEvent(LearningEvent event)
    {
        if (event.getEventType() == LearningEvent.Type.EPOCH_ENDED)
        {
            double error;
            error = learning.getGlobalBestFitness();
            //System.out.println(error);
            trainingSeries.getData().add(new XYChart.Data(iteration, error));
            //System.out.println("i: "+ iteration+ "mse"+error);
            String text = textArea.getText();
            byte[] byteText = text.concat("iteracion: "+ iteration+" mse: " + error+"\n").getBytes();
            text = new String(byteText, StandardCharsets.UTF_8);
            textArea.setText(text);
            iteration++;

            chart.getYAxis().setAutoRanging(false);
            //chart.getXAxis().setAutoRanging(false);
            ((NumberAxis)chart.getYAxis()).setUpperBound(0.3);
            ((NumberAxis)chart.getYAxis()).setTickUnit(0.3 / 6);
            ((NumberAxis)chart.getXAxis()).setUpperBound(iteration);
            ((NumberAxis)chart.getXAxis()).setTickUnit((double) iteration/10);

        }
    }
}
