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

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.cluster.FeatureExtractor;

/**
 * composite feature extractor - combines several extractors into one
 *
 * @author Konstantin Pribluda
 */
public class CompositeExtractor implements FeatureExtractor {

    int size;
    FeatureExtractor[] extractors;

    public CompositeExtractor(FeatureExtractor... extractors) {
        this.extractors = extractors;
        for (FeatureExtractor extractor : extractors)
            size += extractor.getSize();
    }

    public int getSize() {
        return size;
    }

    /**
     * delegate feature extraction from multiple backend extractors
     *
     * @param image image to process
     * @return combined feature vector
     */
    public double[] extract(Image image) {
        double[] features = new double[getSize()];
        int idx = 0;
        for (FeatureExtractor extractor : extractors)
            for (double feature : extractor.extract(image))
                features[idx++] = feature;
        return features;
    }
}
