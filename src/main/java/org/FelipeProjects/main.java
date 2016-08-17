package org.FelipeProjects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.FelipeProjects.util.ReadExcel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.RBFLearning;
import org.neuroph.util.NeuralNetworkFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by felipe on 08-08-2016.
 */
public class main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource("gui/gui.fxml");
            loader.setLocation(url);

            primaryStage.setTitle("Red Rbf Gaussiana + Qpso v0.00001");
            Parent root =  loader.load();
            Scene primaryScene = new Scene(root);

            primaryStage.setScene(primaryScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest((e)-> {
                Platform.exit();
                System.exit(0);

        });
    }

    public void roundClass (double[] output)
    {
        for (int i = 0; i < output.length; i++)
        {
            double v = output[i];
            if (v > 0.5)
                output[i] = 1;
            else
                output[i] = 0;
        }
    }
}
