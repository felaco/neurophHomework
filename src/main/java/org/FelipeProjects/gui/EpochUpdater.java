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
            text = text.concat("iteracion: "+ iteration+" mse: " + error+"\n");
            textArea.setText(text);
            iteration++;

            chart.getYAxis().setAutoRanging(false);
            //chart.getXAxis().setAutoRanging(false);
            ((NumberAxis)chart.getYAxis()).setUpperBound(1);
            ((NumberAxis)chart.getXAxis()).setUpperBound(iteration);
            ((NumberAxis)chart.getXAxis()).setTickUnit((double) iteration/10);

        }
    }
}
