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
 * @author Konstantin Pribluda
 *         test capabilities of pixel image
 */
public class PixelImageTest extends TestCase {


    /**
     * assure proper functionality of empty row checking.  for our test data
     * wee assume that black, non-empty is 0, everything else is  empty or white
     */
    public void testEmptyRowProcessing() {

        int data[] = {0, 1, 1, 1, 0, 1, 1, 1, 1};
        PixelImage image = new PixelImage(data, 9, 1);

        // just one pixel
        assertTrue(image.horizontalSpanEquals(0, 0, 0, 0));
        // 2 pixel,  left border is filled
        assertFalse(image.horizontalSpanEquals(0, 3, 4, 1));

        // pixel span
        assertTrue(image.horizontalSpanEquals(0, 1, 3, 1));

        // one pixel on the right border is wrong
        assertFalse(image.horizontalSpanEquals(0, 1, 4, 1));

        // pixel in the middle is wrong
        assertFalse(image.horizontalSpanEquals(0, 3, 5, 1));

        // span to the end
        assertTrue(image.horizontalSpanEquals(0, 5, 8, 1));

    }


    /**
     * assure proper finctionality of empty column testing
     */
    public void testEmptyColumnProcessing() {
        int data[] = {0, 1, 1, 1, 0, 1, 1, 1, 1};
        PixelImage image = new PixelImage(data, 1, 9);

        // just one pixel
        assertTrue(image.verticalSpanEquals(0, 0, 0, 0));
        // 2 pixel,  left border is filled
        assertFalse(image.verticalSpanEquals(0, 0, 1, 1));


        // pixel span
        assertTrue(image.verticalSpanEquals(0, 1, 3, 1));

        // one pixel on the right border
        assertFalse(image.verticalSpanEquals(0, 1, 4, 1));

        // pixel in the middle
        assertFalse(image.verticalSpanEquals(0, 3, 5, 1));

        // span to the end
        assertTrue(image.verticalSpanEquals(0, 5, 8, 1));
    }


    /**
     * pixel image shall take full area if not specified
     * explicitely
     */
    public void testBorderProcessing() {
        int data[] = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        PixelImage image = new PixelImage(data, 3, 3);

        assertEquals(3, image.getArrayWidth());
        assertEquals(3, image.getArrayHeight());
        assertEquals(8, image.get(2, 2));
    }


    /**
     * box setting shall be honored.
     */
    public void testBoxSettingAreHonored() {
        int data[] = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        PixelImage image = new PixelImage(data, 3, 3, 1, 1, 1, 1);

        assertEquals(4, image.get(0, 0));
    }

    /**
     * shall create pixel array of proper size
     */
    public void testConstructorWithImageCreation() {
        PixelImage image = new PixelImage(2, 5);
        assertEquals(10, image.pixels.length);
    }


    /**
     * shall chisel image properly
     */
    public void testImageChiseling() {
        int[] data = new int[]{0, 1, 2, 3};
        PixelImage image = new PixelImage(data, 2, 2);

        final Image chisel = image.chisel(1, 1, 1, 1);
        assertEquals(3, chisel.get(0, 0));
        assertEquals(1, chisel.getWidth());
        assertEquals(1, chisel.getHeight());
    }
}
