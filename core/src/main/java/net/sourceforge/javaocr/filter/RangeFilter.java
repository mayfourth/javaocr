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

import net.sourceforge.javaocr.Image;

/**
 * gather min and max values from supplied pixels
 *
 * @author Konstantin Pribluda
 */
public class RangeFilter extends AbstractSinglePixelFilter {
    int min = 255;
    int max = 0;

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    @Override
    protected void processPixel(Image image) {
        final int pixel = image.next();        
        if (pixel < min) min = pixel;
        if (pixel > max) max = pixel;      
    }
}
