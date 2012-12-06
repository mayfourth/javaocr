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

package net.sf.javaocr.demos.android.utils.image;

import net.sourceforge.javaocr.filter.SauvolaBinarisationFilter;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * process image with median and Sauvola filter.
 * image supplied for processing shall be of sufficient size to accomodate
 * image region specified on creation
 * @author Konstantin Pribluda
 */
public class SauvolaImageProcessor implements ImageProcessor {

    public static final int SAUVOLA_WINDOW = 50;
    public static final int MEDIAN_WINDOW = 3;
    private static final double SAUVOLA_WEIGHT = 0.20;

    final int w;
    final int h;
    final int arrayWidth;
    final int arrayHeight;
    PixelImage processImage;
    PixelImage returnImage;

    //  private MedianFilter medianFilter;
    private SauvolaBinarisationFilter sauvolaBinarisationFilter;


    /**
     * create image processor working over region of supplied  image.
     * take care in specifying offsets - window size applies to them
     *
     * @param w     width of region of interest
     * @param h     height of region of interest
     * @param above value for pixels above threshold
     * @param below value for pixels below threshold
     */

    public SauvolaImageProcessor(int arrayWidth, int arrayHeight, int w, int h, int above, int below) {
        this.h = h;
        this.w = w;
        this.arrayHeight = arrayHeight;
        this.arrayWidth = arrayWidth;

        // process image has borders of proper size (half window)
        processImage = new PixelImage(w + SAUVOLA_WINDOW, h + SAUVOLA_WINDOW);
        // return image is chiseled out of process image (less borders)
        returnImage = (PixelImage) processImage.chisel(SAUVOLA_WINDOW / 2, SAUVOLA_WINDOW / 2, w, h);

        // median filter for preprocessing
        //medianFilter = new MedianFilter(processImage, MEDIAN_WINDOW);

        // and local threshold for it  - note that we like to have dark as 1 and lite as 0
        //sauvolaBinarisationFilter = new SauvolaBinarisationFilter(above, below, processImage, 256, SAUVOLA_WEIGHT, SAUVOLA_WINDOW);

        sauvolaBinarisationFilter = new SauvolaBinarisationFilter(above, below, processImage, 256, SAUVOLA_WEIGHT, SAUVOLA_WINDOW);
    }

    /**
     * process incoming image
     *
     * @param image   incoming image.  may be modified
     * @param offsetX region of interest offset. this data is not available at creation time
     * @param offsetY region of interest offset. this data is not available at creation time
     * @return processed image
     */
    public PixelImage prepareImage(byte[] image, int offsetX, int offsetY) {

        int[] pixels = processImage.pixels;
        //System.err.println("charger: destination pixels:" + pixels.length);
        //System.err.println("charger: incoming pixels:" + image.length);
        final int firstLine = offsetY - SAUVOLA_WINDOW / 2;
        //System.err.println("charger: first line:" + firstLine);

        final int lastLine = offsetY + h + SAUVOLA_WINDOW / 2;
        //System.err.println("charger: last line:" + lastLine);

        int scanStart = offsetX - SAUVOLA_WINDOW / 2 + arrayWidth * firstLine;
        //System.err.println("charger: scan start:" + scanStart);
        //System.err.println("charger: arraywidth:" + arrayWidth);
        int desIdx = 0;

        for (int i = firstLine; i < lastLine; i++) {
            //System.err.println("charger: processing line:" + i);
            //System.err.println("charger: scan start:" + scanStart);
            final int scanEnd = scanStart + SAUVOLA_WINDOW + w;
            //System.err.println("charger: scan end:" + scanEnd);
            for (int j = scanStart; j < scanEnd; j++) {
                pixels[desIdx++] = image[j] & 0xff;
            }

            // advance to the next value
            scanStart += arrayWidth;

        }

        sauvolaBinarisationFilter.process(processImage);
        return (PixelImage) returnImage;
    }
}
