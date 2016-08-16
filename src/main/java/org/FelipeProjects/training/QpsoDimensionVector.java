package org.FelipeProjects.training;
/**
 * Created by felipe on 15-08-2016.
 */
public class QpsoDimensionVector extends QpsoDimensionBase
{
    QpsoDimensionBase[] pos;

    public QpsoDimensionVector(int dimensionCount)
    {
        pos = new QpsoDimensionBase[dimensionCount];
        for (int i = 0; i < pos.length; i++)
        {
            pos [i] = new QpsoDimensionBase();
        }
    }

    @Override
    public double getDimensionValue()
    {
        return -1;
    }

    @Override
    public void updatePosition(QpsoDimensionBase mBest,
                               QpsoDimensionBase globalBestPosition,
                               QpsoDimensionBase bestParticlePosition,
                               double random,
                               double beta,
                               double mu)
    {
        QpsoDimensionVector vmBest = (QpsoDimensionVector)mBest;
        QpsoDimensionVector gBest = (QpsoDimensionVector) globalBestPosition;
        QpsoDimensionVector pBest = (QpsoDimensionVector) bestParticlePosition;

        for (int i = 0; i < pos.length; i++)
        {
            pos[i].updatePosition(vmBest.getDimensionAt(i),
                                  gBest.getDimensionAt(i),
                                  pBest.getDimensionAt(i),
                                  random,
                                  beta,
                                  mu);
        }
    }

    QpsoDimensionBase getDimensionAt (int index)
    {
        return pos[index];
    }

    public double getDimensionValueAt (int index)
    {
        return pos[index].getDimensionValue();
    }

    public double[] getDimensionValuesAsArray()
    {
        double[] res = new double[pos.length];
        for (int i = 0; i < pos.length; i++)
        {
            res[i] = pos[i].getDimensionValue();
        }
        return res;
    }

    @Override
    public String toString()
    {
        String s = new String("[");

        for (int i = 0; i < pos.length; i++)
        {
            QpsoDimensionBase position = pos[i];
            s = s.concat(position.toString());

            if (i != pos.length - 1)
                s = s.concat(" , ");
        }
        return s.concat("]");
    }

    @Override
    public void sum(QpsoDimensionBase v)
    {
        QpsoDimensionVector vec = (QpsoDimensionVector) v;
        for (int i = 0; i < pos.length; i++)
        {
            pos [i].sum(vec.getDimensionAt(i));
        }
    }

    @Override
    public void multiply(double m)
    {
        for (int i = 0; i < pos.length; i++)
        {
            pos[i].multiply(m);
        }
    }

    @Override
    public void setToZero()
    {
        for (QpsoDimensionBase po : pos)
        {
            po.setToZero();
        }
    }
}
