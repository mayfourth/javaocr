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

import java.util.ArrayList;
import java.util.List;

/**
 * cluster with normally distributed features. this abstract provides computation of
 * expectation and standard deviation without storing sample values
 *
 * @author Konstantin Pribluda
 */
public abstract class NormalDistributionCluster extends EuclidianDistanceCluster {
    double[] quads;
    double[] var;


    /**
     * default constructor for sake of serialisation frameworks
     */
    protected NormalDistributionCluster() {
    }

    /**
     * constructs
     *
     * @param dimensions amount of dimenstions
     */
    public NormalDistributionCluster(int dimensions) {
        super(dimensions);
        quads = new double[dimensions];
    }

    /**
     * convenience constructor to create already trained distribution cluster
     *
     * @param mx  precooked expectation values
     * @param var precooked variance
     */
    public NormalDistributionCluster(double[] mx, double[] var) {
        super(mx);
        this.var = var;
    }

    /**
     * lazily calculate and return variance cluster
     *
     * @return variance cluster
     */
    public double[] getVar() {
        if (var == null) {
            var = new double[getDimensions()];
            for (int i = 0; i < getDimensions(); i++) {
                var[i] = getAmountSamples() == 0 ? 0 : (quads[i] - getSum()[i] * getSum()[i] / getAmountSamples()) / getAmountSamples();
            }
        }
        return var;
    }

    /**
     * perform sample image training - cumulate values and compute
     * moments
     *
     * @param samples
     */
    public void train(double samples[]) {
        super.train(samples);
        var = null;
        for (int i = 0; i < getDimensions(); i++) {
            quads[i] += samples[i] * samples[i];
        }
    }


    public double[] getQuads() {
        return quads;
    }

    public void setQuads(double[] quads) {
        this.quads = quads;
    }


    public void setVar(double[] var) {
        this.var = var;
    }

  
}
