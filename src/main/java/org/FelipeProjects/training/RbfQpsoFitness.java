package org.FelipeProjects.training;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.ErrorFunction;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.RBFLearning;

import java.util.List;

/**
 * Created by felipe on 15-08-2016.
 */
public class RbfQpsoFitness implements QpsoFitnessFunction
{
    RBFNetwork network;
    DataSet dataSet;
    @Override
    public double calculateFitness(QpsoPosition position)
    {

        Layer hidedenLayer = network.getLayerAt(1);
        int hiddenLayerCount = hidedenLayer.getNeuronsCount();
        List<QpsoDimensionBase> centers;
        centers = position.getPositionArray().subList(0,hiddenLayerCount);

        List<QpsoDimensionBase> sigmas;
        sigmas = position.getPositionArray().subList(hiddenLayerCount, hiddenLayerCount*2);

        List<QpsoDimensionBase> weights;
        weights = position.getPositionArray().subList(hiddenLayerCount*2, hiddenLayerCount*3);

        for (int i = 0; i < hiddenLayerCount; i++)
        {
            Neuron neuron = hidedenLayer.getNeuronAt(i);
            QpsoDimensionVector neuronCenter;
            neuronCenter = (QpsoDimensionVector) centers.get(i);
            double sigma = sigmas.get(i).getDimensionValue();
            ((Gaussian)neuron.getTransferFunction()).setSigma(sigma);

            for (int j = 0; j < neuron.getInputConnections().length; j++)
            {
                Connection[] con = neuron.getInputConnections();

                con[j].getWeight().setValue(neuronCenter.getDimensionValueAt(j));
            }
        }

        Neuron outputNeuron = network.getOutputNeurons()[0];

        for (int i = 0; i < weights.size(); i++)
        {
            Weight weight = outputNeuron.getInputConnections()[i].getWeight();
            weight.setValue(weights.get(i).getDimensionValue());
        }

        TrainingBase learningRule = (TrainingBase) network.getLearningRule();
        ErrorFunction errorFunction = learningRule.getErrorFunction();
        errorFunction.reset();

        for (int i= 0; i < dataSet.size(); i++)
        {
            DataSetRow entry = dataSet.getRowAt(i);
            network.setInput(entry.getInput());
            network.calculate();
            errorFunction.calculatePatternError(network.getOutput(), entry.getDesiredOutput());
        }
        return errorFunction.getTotalError();
    }



    public void setDataSet(DataSet dataSet)
    {
        this.dataSet = dataSet;
    }

    public void setNetwork(RBFNetwork network)
    {
        this.network = network;
    }
}
