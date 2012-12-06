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
import net.sourceforge.javaocr.ImageFilter;
import net.sourceforge.javaocr.ocr.PixelImage;

public abstract class AbstractIntegralImageFilter implements ImageFilter {

    Image resultImage;
    Image empty;

    public AbstractIntegralImageFilter(Image resultImage) {
        this.resultImage = resultImage;
        empty = new PixelImage(resultImage.getWidth(), 1);
    }

    /**
     * process image. size of image under process and configured destination
     * image must match  -  it is up to caller to ensure format compatibility
     * (otherwise be prepared to receive lots of garbled images and IOOB exceptions)
     *
     * @param image image to be processed
     */
    public void process(Image image) {
        final int height = image.getHeight();
        final int width = image.getWidth();
        int cumulated;

        // alternate variant, computing rows and then colums
        // does not provide any significant performance boost

         for (int i = 0; i < height; i++) {

         cumulated = 0;
         // iterate over all the  scans
         Image sourceScan = image.row(i);
         Image destinationScan = resultImage.row(i);
         Image previous = i > 0 ? resultImage.row(i - 1) : empty;

         for (sourceScan.iterateH(0), destinationScan.iterateH(0), previous.iterateH(0); sourceScan.hasNext();) {
         cumulated += processPixel(sourceScan.next());
         destinationScan.next(cumulated + previous.next());
         }
         }

    }

    protected abstract int processPixel(final int i);

    /**
     * compute sum of pixels in defined window, all borders are inclusive
     *
     * @return sum of pixels in defined window
     */
    public int windowValue(int left, int top, int right, int bottom) {

        int ll = left - 1;
        int tt = top - 1;


        int sum = resultImage.get(right, bottom);

        if (ll >= 0) {
            sum -= resultImage.get(ll, bottom);
        }
        if (tt >= 0) {
            sum -= resultImage.get(right, tt);
        }
        if (tt >= 0 && ll >= 0) {
            sum += resultImage.get(ll, tt);
        }
        return sum;
    }
}