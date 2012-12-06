/*
 * Copyright (c) 2003-2012, Ronald B. Cemer , Konstantin Pribluda, William Whitney, Andrea De Pasquale
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.javaocr.plugin.cluster;

import org.apache.commons.math.linear.*;

/**
 * cluster providing Mahalanobis distance meassure
 * ( do not ask me to pronounce this )
 *
 * @author Konstantin Pribluda
 */
public class MahalanobisDistanceCluster extends AbstractBaseCluster {

    double[][] sumxy;
    double invcov[][];

    /**
     * default constructor for sake of serialisation frameworks
     */
    public MahalanobisDistanceCluster() {
    }

    /**
     * constructs   mahalanobis distance cluster
     *
     * @param dimensions amount of dimensions in  cluster
     */
    public MahalanobisDistanceCluster(int dimensions) {
        super(dimensions);
        sumxy = new double[dimensions][dimensions];
    }

    /**
     * convenience constructor to instantiate trained distance cluster
     *
     * @param mx     expectation walues
     * @param invcov inverse covariance matrix
     */
    public MahalanobisDistanceCluster(double[] mx, double[][] invcov) {
        super(mx);
        this.invcov = invcov;
    }

    /**
     * calculate mahalanubis distance
     *
     * @param features amount of features shall correspond to amount dimensions
     * @return calculated distance
     */
    public double distance(double[] features) {
        // force matrix recalculation if necessary
        getInvcov();
        // calculate mahalanobis distance
        double cumulated = 0;
        for (int i = 0; i < getDimensions(); i++) {
            double xmxc = 0;
            for (int j = 0; j < getDimensions(); j++) {
                xmxc += invcov[j][i] * (features[j] - center()[j]);
            }
            cumulated += xmxc * (features[i] - center()[i]);
        }

        //System.out.println("m cumulated:"  + cumulated);
        //TODO:  why it was negative producing NAN? Is there a mistake? 
        // does not hurt absoluting it though...
        return Math.sqrt(Math.abs(cumulated));
    }

    /**
     * gather sampler - sum of x*y into matrix
     *
     * @param samples sampler belonging to cluster
     */
    @Override
    public void train(double[] samples) {
        super.train(samples);
        // invalidate cumulated covariance
        invcov = null;
        for (int i = 0; i < getDimensions(); i++)
            for (int j = 0; j < getDimensions(); j++) {
                sumxy[i][j] += samples[i] * samples[j];
            }
    }

    /**
     * calculate covariance matrix  and invert it
     *
     * @return
     */
    double[][] matrix() {
        double cov[][] = new double[getDimensions()][getDimensions()];
        for (int i = 0; i < getDimensions(); i++) {
            for (int j = 0; j < getDimensions(); j++) {
                cov[i][j] += sumxy[i][j] / getAmountSamples() - center()[i] * center()[j];
            }
        }

        RealMatrix a = new Array2DRowRealMatrix(cov);
        DecompositionSolver solver = new LUDecompositionImpl(a, Double.MIN_VALUE).getSolver();
        try {
            final RealMatrix inverse = solver.getInverse();
            return inverse.getData();
        } catch (SingularMatrixException ex) {
            System.err.println("singular - return identity!!!!!!");
            // well, matrix is singular - return identity instead
            double[][] identity = new double[getDimensions()][getDimensions()];
            for (int i = 0; i < getDimensions(); i++) {
                identity[i][i] = 1;
            }
            return identity;
        }
    }

    public double[][] getInvcov() {
        if (invcov == null) {
            invcov = matrix();
        }
        return invcov;
    }

    public void setInvcov(double[][] invcov) {
        this.invcov = invcov;
    }


}
