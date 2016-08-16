package org.FelipeProjects.training;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by felipe on 09-08-16.
 */
public class QpsoDimensionBase implements Serializable{
    private double dimensionValue;

    public  QpsoDimensionBase()
    {
        dimensionValue = ThreadLocalRandom.current().nextDouble(0, 10);
    }


    public double getDimensionValue() {
        return dimensionValue;
    }

    public void updatePosition (QpsoDimensionBase mBest,
                                QpsoDimensionBase globalBestPosition,
                                QpsoDimensionBase bestParticlePosition,
                                double random,
                                double beta,
                                double mu)
    {
        double Pvalue = getPvalue(globalBestPosition, bestParticlePosition, random);
        if (random < 0.5)

            dimensionValue = Pvalue + beta *
                            Math.abs(mBest.getDimensionValue() - dimensionValue) *
                            Math.log(1 / mu);
        else
            dimensionValue = Pvalue - beta *
                    Math.abs(mBest.getDimensionValue() - dimensionValue) *
                    Math.log(1 / mu);
    }

    protected double getPvalue (QpsoDimensionBase globalBestPosition,
                      QpsoDimensionBase bestParticlePosition,
                      double rand)
    {
        double pValue = rand * bestParticlePosition.getDimensionValue()
                        + (1-rand) * globalBestPosition.getDimensionValue();

        return pValue;
    }



    @Override
    public String toString ()
    {
        return  new String(Double.toString(dimensionValue));
    }

    public void sum (QpsoDimensionBase v)
    {
        dimensionValue += v.getDimensionValue();
    }

    public void multiply(double m)
    {
        dimensionValue *= m;
    }

    public void setToZero ()
    {
        dimensionValue = 0;
    }
}
