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

package net.sourceforge.javaocr.filter;

/**
 * normalise grayscale pixels linear basing on min/max values
 * @author Konstantin Pribluda
 */
public class NormaliseGrayscaleFilter extends LookupTableFilter {
   

    /**
     * create and initialise lookup table
     * @param max max pixel value in image
     * @param min min pixel value in image
     */
    public NormaliseGrayscaleFilter(int min, int max) {
        super(new int[256]);
        int range = max - min;
        for(int i = 0; i < 256; i++){
            lut[i] = Math.min(255, Math.max(0, ((i - min) * 255) / range));           
        }
    }

}
