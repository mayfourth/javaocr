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

package net.sourceforge.javaocr.ocr;

import junit.framework.TestCase;
import net.sourceforge.javaocr.Image;


/**
 * test capabilities of byte images
 */
public class ByteImageTest extends TestCase {
    /**
     * must allow empty setting and retrieval as unsigned
     */
    public void testValuesAreSetAndRetrievedProperlyAsUnsigned() {
        ByteImage image = new ByteImage(1, 1);

        image.setCurrentIndex(0, 0);
        image.put(255);
        assertEquals(255, image.get());

        image.put(129);
        assertEquals(129, image.get());
    }

    /**
     * shall chisel image properly
     */
    public void testImageChiseling() {
        byte[] data = new byte[]{0, 1, 2, 3};
        ByteImage image = new ByteImage(data, 2, 2);

        final Image chisel = image.chisel(1, 1, 1, 1);
        assertEquals(3, chisel.get(0, 0));
        assertEquals(1, chisel.getWidth());
        assertEquals(1, chisel.getHeight());
    }

    /**
     * TODO:  this is only for performance testing and shall go somewhere more appropriate
     */
    /*
    public void testPerformance() {
        byte[] pixels = new byte[720 * 480];

        ByteImage image = new ByteImage(pixels, 720, 480, 360, 0, 133, 480);
        PixelImage dest = new PixelImage(480, 133);
        PixelImage median = new PixelImage(480, 133);

        HistogramFilter histogramm = new HistogramFilter();
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            image.flip(dest);
            long flip = System.nanoTime();
            histogramm.process(dest);
            long hist = System.nanoTime();
            final int thr = histogramm.adaptiveThreshold();

            (new ThresholdFilter(thr, 255, 0)).process(dest);
            long threshold = System.nanoTime();
            (new MedianFilter(3, 3, median)).process(dest);
            long med = System.nanoTime();
            (new GrayscaleToRGBA()).process(median);
            long gtrgb = System.nanoTime();

            System.err.println("start:" + start);
            System.err.println("flip:" + (flip - start));
            System.err.println("hist:" + (hist - flip));
            System.err.println("thr:" + (threshold - hist));
            System.err.println("median:" + (med - threshold));
            System.err.println("to rgb:" + (gtrgb - med));
            System.err.println("=================================");
        }
    }  */
}
