package org.FelipeProjects.training;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by felipe on 09-08-16.
 */
public class QpsoPosition implements Serializable{
    ArrayList<QpsoDimensionBase> position;
    double fitness;

    public QpsoPosition(int dimensionCount, int inputCount)
    {
        position = new ArrayList<>();

        for (int i = 0; i < dimensionCount ; i++) {
            //QpsoDimensionBase b = new QpsoDimensionBase();
            QpsoDimensionVector b = new QpsoDimensionVector(inputCount);
            position.add(b);
        }

        for (int i = 0; i < dimensionCount * 2; i++)
        {
            QpsoDimensionBase b = new QpsoDimensionBase();
            position.add(b);
        }
    }


    public void updatePosition (QpsoPosition mBest,
                                QpsoPosition globalBestPosition,
                                QpsoPosition bestParticlePosition,
                                double random,
                                double beta,
                                double mu)
    {
        for (int i = 0; i < position.size(); i++)
        {
            QpsoDimensionBase dimensionBase = position.get(i);
            QpsoDimensionBase meanBest = mBest.getDimentionAt(i);
            QpsoDimensionBase gBest = globalBestPosition.getDimentionAt(i);
            QpsoDimensionBase pBest = bestParticlePosition.getDimentionAt(i);

            dimensionBase.updatePosition(   meanBest,
                                            gBest,
                                            pBest,
                                            random,
                                            beta,
                                            mu
                                        );
        }
    }

    public ArrayList<QpsoDimensionBase> getPositionArray()
    {
        return position;
    }

    public QpsoDimensionBase getDimentionAt(int pos)
    {
        return position.get(pos);
    }

    public int dimensionCount()
    {
        return position.size();
    }

    public QpsoPosition getDeepCoopy ()
    {
        return SerializationUtils.clone(this);
    }

    @Override
    public String toString ()
    {
        String s = new String("[");

        for (int i = 0; i < position.size(); i++)
        {
            QpsoDimensionBase pos = position.get(i);
            s = s.concat(pos.toString());

            if (i != position.size() - 1)
                s = s.concat(" , ");
        }
        return s.concat("]");
    }

    public QpsoPosition sum(QpsoPosition sum)
    {
        QpsoPosition newPos = SerializationUtils.clone(this);
        ArrayList<QpsoDimensionBase> pos;
        pos = newPos.getPositionArray();

        int j;
        for (int i = 0; i < pos.size(); i++)
        {
            QpsoDimensionBase p = pos.get(i);
            QpsoDimensionBase o = sum.getDimentionAt(i);
            p.sum(o);
        }
        return newPos;
    }

    public QpsoPosition multiply (double mult)
    {
        QpsoPosition newPos = SerializationUtils.clone(this);
        ArrayList<QpsoDimensionBase> pos;
        pos = newPos.getPositionArray();

        for (int i = 0; i < pos.size(); i++)
        {
            QpsoDimensionBase p = pos.get(i);
            p.multiply(mult);
        }
        return newPos;
    }

    public void setToZero()
    {
        for (QpsoDimensionBase p : position)
        {
            p.setToZero();
        }
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }
}
