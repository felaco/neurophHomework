package org.FelipeProjects.training;

import org.neuroph.core.learning.error.ErrorFunction;

import java.io.Serializable;

/**
 * Created by felipe on 16-08-2016.
 */
public class myErrorFunction implements ErrorFunction, Serializable
{
    private static final long serialVersionUID = 1L;

    private double totalError;
    /**
     * Number of patterns - n
     */
    private double patternCount;

    public myErrorFunction() {
        reset();
    }


    @Override
    public void reset() {
        totalError = 0d;
        patternCount = 0;
    }


    @Override
    public double getTotalError() {
        return totalError / ( 2 * patternCount );
    }

    @Override
    public double[]calculatePatternError(double[] predictedOutput, double[] targetOutput) {
        double[] patternError = new double[targetOutput.length];

        for (int i = 0; i < predictedOutput.length; i++) {
            patternError[i] =  targetOutput[i] - predictedOutput[i];
            addError( patternError[i] * patternError[i]);
        }
        patternCount++;
        return patternError;
    }

    synchronized private void addError (double value)
    {
        totalError += value;
    }
}
