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

import net.sourceforge.javaocr.cluster.Metric;

import java.util.List;

/**
 * cluster represents some  feature vectors belonging together.
 * cluster can be trained with sampler
 * <p/>
 *
 * @author Konstantin Pribluda
 */
public interface Cluster extends Metric {

    /**
     * centroid of cluster
     *
     * @return centroid of cluster
     */
    public double[] center();

    /**
     * train cluster with feature vector
     *
     * @param features
     */
    public void train(double[] features);


    /**
     * computes maximal distance of sample from center of cluster
     *
     * @param samples sample group, sample size shall correspond to cluster dimensions
     */
    public double radius(List<double[]> samples);

}
