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

import net.sourceforge.javaocr.Image;

/**
 * extract image features to be used in cluster analysis. concrete implementations will be
 * provided by plugins.
 * @author Konstantin Pribluda
 */
public interface FeatureExtractor {
    /**
     * not sure whether we really need this,  as some extractors may provide variable
     * amount of features
     * TODO: do we really need this ?
     *
     * @return size of produced feature cluster
     */
    public int getSize();

    /**
     * extract image features
     *
     * @param image
     * @return
     */
    public double[] extract(Image image);

}
