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

/**
 * cluster calculating euclidian distance
 *
 * @author Konstantin Pribluda
 */
public class EuclidianDistanceCluster extends AbstractBaseCluster {
    public EuclidianDistanceCluster() {
    }

    /**
     * create cluster calculating euclidian distance between center and feature
     * vector
     *
     * @param c    assotiated character
     * @param size size of feature cluster
     */
    public EuclidianDistanceCluster(int dimensions) {
        super(dimensions);
    }

    public EuclidianDistanceCluster(double[] mx) {
        super(mx);
    }

    public double distance(double[] features) {
        double cumulated = 0;
        for (int i = 0; i < getDimensions(); i++) {
            cumulated += computeDimension(features[i], i);
        }
        return Math.sqrt(cumulated);
    }

    double computeDimension(double dimension, int i) {
        return Math.pow(center()[i] - dimension, 2);
    }
}
