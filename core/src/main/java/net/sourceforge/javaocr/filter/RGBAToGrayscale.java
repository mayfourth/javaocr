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
 * converts RGBA image to grayscale
 *
 * @author Konstantin Pribluda
 */
public class RGBAToGrayscale extends AbstractSinglePixelFilter {

    final int cR;
    final int cG;
    final int cB;

    /**
     * construct with default coefficients (306,601,117)
     */
    public RGBAToGrayscale() {
        this(306, 601, 117);
    }

    /**
     * construct with weight coefficients. multiplication results are divided by 1024
     *
     * @param cR
     * @param cG
     * @param cB
     */
    public RGBAToGrayscale(int cR, int cG, int cB) {
        this.cB = cB;
        this.cG = cG;
        this.cR = cR;
    }

    /**
     * convert RGBA to grayscale
     *
     * @param image to be processed
     */
    @Override
    protected void processPixel(Image image) {
        final int pixel = image.next();
        final int r = (pixel >> 16) & 0xff;
        final int g = (pixel >> 8) & 0xff;
        final int b = pixel & 0xff;
        int Y = ((r * cR) + (g * cG) + (b * cB)) >> 10;
        if (Y < 0) {
            Y = 0;
        } else if (Y > 255) {
            Y = 255;
        }
        image.put(Y);
    }
}
