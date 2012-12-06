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

package net.sourceforge.javaocr.cluster;

/**
 * metric computes "distance" between feature cluster extracted from candidate image
 * and data stored inside match.
 * @author Konstantin Pribluda
 */
public interface Metric {

    /**
     * compute distance between metric and feature vectors. Individual plugins will define concrete
     * metric implementation ( euclidian,  mahalonoubis, battacharya etc. )
     * @param features   amoutn of fweatures shall correspond to amount dimenstions
     * @return distance between features and vector
     */
    double distance(double[] features);

    /**
     * amount of dimensions of feature vectors
     * @return amount of dimensions
     */
    int getDimensions();
   
}
