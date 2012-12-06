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

import junit.framework.TestCase;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * assure proper functionality of histogram filter
 */
public class HistogramTest extends TestCase {
    int[] values = new int[]{0, 1, 1, 2, 2, 2, 253, 253, 253, 254, 254, 255};

    /**
     * histogram values shall be collected properly
     */
    public void testHistogrammCollectionAndReset() {
        PixelImage image = new PixelImage(values, 1, 12);

        HistogramFilter filter = new HistogramFilter();
        filter.process(image);

        assertEquals(12, filter.getTotalCount());
        assertEquals(1, filter.getHistogramm()[0]);
        assertEquals(2, filter.getHistogramm()[1]);
        assertEquals(3, filter.getHistogramm()[2]);
        for (int i = 3; i < 253; i++)
            assertEquals(0, filter.getHistogramm()[i]);
        assertEquals(3, filter.getHistogramm()[253]);
        assertEquals(2, filter.getHistogramm()[254]);
        assertEquals(1, filter.getHistogramm()[255]);


        filter.reset();
        for (int i = 0; i < 256; i++)
            assertEquals(0, filter.getHistogramm()[i]);
        assertEquals(0,filter.getTotalCount());
    }

    /**
     * shall perform folding properly
     * - clamp values on border to 0
     * - perform proper computations of values
     */
    public void testFolding() {
        int[] folder = new int[]{1, 1, 1};
        PixelImage image = new PixelImage(values, 1, 12);
        HistogramFilter filter = new HistogramFilter();
        filter.process(image);
        // fold it
        filter.fold(folder);

        //first one shall be 0   / clamped
        assertEquals(0, filter.getHistogramm()[0]);

        // (1 * 1 + 1 * 2 + 1 * 3)/3 -> 2
        assertEquals(2, filter.getHistogramm()[1]);
        // (1 * 2 + 1* 3+ 1 * 0)/3 -> 1
        assertEquals(1, filter.getHistogramm()[2]);
        // (1 * 3 + 1* 0+ 1 * 0)/3 -> 1
        assertEquals(1, filter.getHistogramm()[3]);
        for (int i = 4; i < 252; i++)
            assertEquals(0, filter.getHistogramm()[i]);

        // reverse
        assertEquals(1, filter.getHistogramm()[252]);
        assertEquals(1, filter.getHistogramm()[253]);
        assertEquals(2, filter.getHistogramm()[254]);
        assertEquals(0, filter.getHistogramm()[255]);
    }


    public void testEvenSizeProducesIAE() {
        int[] folder = new int[]{1, 1, 1, 1};
        HistogramFilter filter = new HistogramFilter();
        try {
            filter.fold(folder);
            fail("no exception thrown on even golding array size");
        } catch (IllegalArgumentException e) {
            // thar's ok, we await this one
        }
    }


    /**
     * adaptive threshold computation shall be performed properl. it shall
     * place threshold direct between 2 prominent modes.
     */
    public void testAdaptiveThresholdComputation() {
        HistogramFilter histogram = new HistogramFilter();
        histogram.getHistogramm()[10] = 100;
        histogram.getHistogramm()[50] = 100;

        // we await it on position 29 ( middle of interval, 30 -1 )
        assertEquals(30, histogram.adaptiveThreshold());

        histogram.reset();

        histogram.getHistogramm()[10] = 100;
        histogram.getHistogramm()[210] = 200;
        // we await it on position 110 - right in the middle
        assertEquals(110, histogram.adaptiveThreshold());

        histogram.reset();
        histogram.getHistogramm()[150] = 100;
        histogram.getHistogramm()[250] = 100;

        // we await it on position 180
        assertEquals(100, histogram.adaptiveThreshold(90));
        
    }
}
