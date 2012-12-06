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
import net.sourceforge.javaocr.ImageFilter;

/**
 * base functionality for moment filters (traversal 
 * and caching those values for future invocations  og filter.
 */
public abstract class AbstractMomentFilter implements ImageFilter {

    final int p;
    final int q;
    double moment;

    // precomputed coeffs
    double coeffx[], coeffy[];

    public AbstractMomentFilter(int p, int q) {
        this.p = p;
        this.q = q;
    }

    /**
     * precompute  x coefficient array
     */
    protected abstract double[] precomputeX(Image image);

    /**
     * precompute y coefficients array
     */
    protected abstract double[] precomputeY(Image image);

    /**
     * navigate through while image
     *
     * @param image
     */
    public void process(Image image) {
        int x;
        coeffx = precomputeX(image);
        coeffy = precomputeY(image);
        
        for (int y = 0; y < image.getHeight(); y++) {

            image.iterateH(y);
            x = 0;
            while (image.hasNext())
                moment += image.next() * coeffx[x++] * coeffy[y];
        }
    }

    public double getMoment() {
        return moment;
    }
}
