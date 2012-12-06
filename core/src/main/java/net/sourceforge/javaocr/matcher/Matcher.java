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

package net.sourceforge.javaocr.matcher;

import java.util.List;

/**
 * perform matching of features to concrete characters. returned list is sorted by distance.
 * result may contain several matches for same character (for example for different fonts). Resulted lists may need come more
 * processing to improve matching results
 * 
 * @author Konstantin Pribluda
 */
public interface Matcher {
    /**
     * provide ordered list of matches for provided features
     *
     * @param features  features extracted for character image
     * @return ordered list of matches
     */
    List<Match> classify(double[] features);
}
