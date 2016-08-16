package org.FelipeProjects.training;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.RBFNetwork;

import java.util.ArrayList;

/**
 * Created by felipe on 08-08-2016.
 */
public class TrainingBase extends SupervisedLearning
{
    ArrayList<QpsoParticle> particles;
    ArrayList <QpsoPosition> historicalBestPositions;
    QpsoPosition gBest;
    int particlesCount = 10;
    double globalBestFitness;
    QpsoFitnessFunction fitnessFunction;

    public TrainingBase()
    {
        setErrorFunction(new myErrorFunction());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        RbfQpsoFitness fitness = new RbfQpsoFitness();
        fitness.setDataSet(this.getTrainingSet());
        fitness.setNetwork((RBFNetwork) neuralNetwork);
        fitnessFunction = fitness;

        double bestFitness = Double.MAX_VALUE;
        particles = new ArrayList<>();
        historicalBestPositions = new ArrayList<>();
        // obtiene la cantidad de neuronas de la capa oculta
        int dimCount = neuralNetwork.getLayers()[1].getNeuronsCount();
        for (int i = 0; i < particlesCount; i++)
        {
            QpsoParticle particle = new QpsoParticle(dimCount,
                                                     neuralNetwork.getInputsCount(),
                                                     fitnessFunction);

            particles.add(particle);

            // se evalua la aptitud de la particula en el constructor
            double fitnessValue = particle.getBestFitness();
            if (fitnessValue < bestFitness)
            {
                bestFitness = fitnessValue;
                gBest = particle.getPosition();
            }
        }

        globalBestFitness = bestFitness;
        historicalBestPositions.add(gBest);
    }

    @Override
    public void doLearningEpoch(DataSet dataSet)
    {
        QpsoPosition bestPos = historicalBestPositions.get(historicalBestPositions.size()-1).getDeepCoopy();
        double bestFitness = globalBestFitness;

        for (QpsoParticle particle : particles)
        {
            QpsoPosition gBest = historicalBestPositions.get(historicalBestPositions.size()-1);
            particle.updatePos(meanBest(),
                               gBest,
                               getBeta());


            double particleFitness = particle.evaluatePos();
            if(particleFitness < bestFitness)
            {
                bestFitness = particleFitness;
                bestPos = particle.getPosition();
            }
        }

        globalBestFitness = bestFitness;
        historicalBestPositions.add(bestPos);
    }

    private QpsoPosition meanBest()
    {
        QpsoPosition res = historicalBestPositions.get(0).getDeepCoopy();
        res.setToZero();

        int i = 0;

        for (QpsoPosition bestPosition : historicalBestPositions)
        {
            res = res.sum(bestPosition);
            i++;
        }

        return res.multiply((double) 1 / i);
    }

    private double getBeta ()
    {
        return (1- 0.5) * (getMaxIterations() - currentIteration) / getMaxIterations() + 0.5;
    }

    @Override
    protected void updateNetworkWeights(double[] outputError)
    {

    }

    public int getParticlesCount()
    {
        return particlesCount;
    }

    public void setParticlesCount(int particlesCount)
    {
        this.particlesCount = particlesCount;
    }

    public double getGlobalBestFitness()
    {
        return globalBestFitness;
    }
}
