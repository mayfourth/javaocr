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
 * computes central moments of image. see wikipedia or book of your choice about
 * details. needs M10 & M01 to be computed before using it
 */
public class CentralMomentFilter extends AbstractMomentFilter {
    double xMean;
    double yMean;


    /**
     * @param p     order of x
     * @param q     order of y
     * @param xMean M10
     * @param yMean M01
     */
    public CentralMomentFilter(int p, int q, double xMean, double yMean) {
        super(p, q);
        this.xMean = xMean;
        this.yMean = yMean;
    }


    @Override
    protected double[] precomputeX(Image image) {
        final double[] doubles = new double[image.getWidth()];
        for (int i = 0; i < doubles.length; i++)
            doubles[i] = Math.pow(i - xMean, p);
        return doubles;
    }

    @Override
    protected double[] precomputeY(Image image) {
        final double[] doubles = new double[image.getHeight()];
        for (int i = 0; i < doubles.length; i++)
            doubles[i] = Math.pow(i - yMean, q);
        return doubles;
    }

    /**
     * normalise, not sure whether this has be done here...
     *
     * @param m00
     * @return normalised moment value
     */
    public double normalise(double m00) {
        return getMoment() / Math.pow(m00, (p + q) / 2 + 1);
    }
}
