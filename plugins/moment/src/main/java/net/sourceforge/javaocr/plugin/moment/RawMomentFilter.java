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

package net.sourceforge.javaocr.plugin.moment;

import net.sourceforge.javaocr.Image;

/**
 * process image and compute raw image moment. does not modify the image.  this filter is stateful and not thread safe
 */
public class RawMomentFilter extends AbstractMomentFilter {


    /**
     * filter computing moment with given cardinality
     *
     * @param p
     * @param q
     */
    public RawMomentFilter(int p, int q) {
        super(p, q);
    }

   

    @Override
    protected double[] precomputeX(Image image) {
        final double[] doubles = new double[image.getWidth()];
        for (int i = 0; i < doubles.length; i++)
            doubles[i] = Math.pow(i, p);
        return doubles;
    }

    @Override
    protected double[] precomputeY(Image image) {
        final double[] doubles = new double[image.getHeight()];
        for (int i = 0; i < doubles.length; i++)
            doubles[i] = Math.pow(i, q);
        return doubles;
    }

}
