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
 * perform threshold filtering
 * TODO: do we need to make this via threshold? Yep - 'if' is still faster than lookup in array
 *
 * @author Konstantin Pribluda
 */
public class ThresholdFilter extends AbstractSinglePixelFilter {
    int threshold;
    private int above;
    private int below;

    /**
     * construct with threshold and default values for above / below ( 255 / 0 )
     *
     * @param threshold threshold value
     */
    public ThresholdFilter(int threshold) {
        this(threshold, 255, 0);
    }

    /**
     * construct with specified values for above and below
     *
     * @param threshold threshold value
     * @param above     value to use for values strictly over the threshold
     * @param below     value to substitute for values below the threshold
     */
    public ThresholdFilter(final int threshold, final int above, final int below) {
        this.threshold = threshold;
        this.above = above;
        this.below = below;
    }

    @Override
    protected void processPixel(Image image) {
        image.put(image.next() > threshold ? above : below);
    }
}
