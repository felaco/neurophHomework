package org.FelipeProjects.training;

import java.util.ArrayList;

/**
 * Created by felipe on 09-08-2016.
 */
public class QpsoParticle
{
    private double bestFitness;
    private QpsoFitnessFunction costFunction;
    private QpsoPosition position;
    private QpsoPosition bestParticlePosition;

    public QpsoParticle (int dimensionCount, int inputCount, QpsoFitnessFunction costFunction)
    {
        this.costFunction = costFunction;
        position = new QpsoPosition(dimensionCount, inputCount);
        //pos = ThreadLocalRandom.current().nextDouble(-10, 10);
        //particleBest = pos;

        //************************************
        //bestFitness = costFunction.calculateFitness(pos);
        bestFitness = evaluatePos();
        bestParticlePosition = position.getDeepCoopy();
    }
/*
    public void updatePos (double mBest, double gBest, double beta)
    {

        double mu = getMu();
        double random = Math.random();

        if (random < 0.5)
        {
            pos = getP(gBest,random) + beta * Math.abs(mBest - pos) * Math.log(1 / mu);
        }
        else
        {
            pos = getP(gBest,random) - beta * Math.abs(mBest - pos) * Math.log(1 / mu);
        }
        if (evaluatePos () < bestFitness)
            particleBest = pos;


    }
    */

    public void updatePos (QpsoPosition mBest,
                           QpsoPosition gBest,
                           double beta)
    {
        double mu = getMu();
        double random = Math.random();

        position.updatePosition(mBest,
                                gBest,
                                bestParticlePosition,
                                random,
                                beta,
                                mu);

        double fitness = evaluatePos();
        if (fitness < bestFitness)
        {
            bestFitness = fitness;
            bestParticlePosition = position.getDeepCoopy();
        }
    }


    public double evaluatePos ()
    {
        double fitness = costFunction.calculateFitness(position);
        return  fitness;
    }

    private double getMu ()
    {
        double mu = 0;
        while (mu == 0)
        {
            mu = Math.random();
        }
        return mu;
    }

    /**
     * retorna una Deep Copy de la posición de la particula
     * @return una copia en otro lugar de memoria de la posición
     */
    public QpsoPosition getPosition()
    {
        return position.getDeepCoopy();
    }

    public QpsoFitnessFunction getCostFunction()
    {
        return costFunction;
    }

    public void setCostFunction(QpsoFitnessFunction costFunction)
    {
        this.costFunction = costFunction;
    }

    public double getBestFitness()
    {
        return bestFitness;
    }
}
