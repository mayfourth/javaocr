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

import java.util.ArrayList;

/**
 * test capabilities of abstract linear image - correct traversal order
 *
 * @author Konstantin Pribluda
 */
public class LinearImageTest extends TestCase {


    /**
     * aspect ratio setting shall be set  properly   and taken from box size
     */
    public void testAspectRatioSetting() {
        final TestLinearImage testLinearImage = new TestLinearImage(20, 30, 7, 8, 4, 3);
        assertEquals(4f / 3f, testLinearImage.getAspectRatio());
    }


    public void testWidthAndHeightAreTakenFromBox() {

        final TestLinearImage testLinearImage = new TestLinearImage(4, 3, 1, 1, 1, 2);
        assertEquals(1, testLinearImage.getWidth());
        assertEquals(2, testLinearImage.getHeight());
    }


    /**
     * test cartesian to linear conversion
     * coordinate is processed as: w * (originY+y) + originX + x
     */
    public void testPixelCoordinateProcessing() {
        final TestLinearImage testLinearImage = new TestLinearImage(4, 3, 2, 1, 2, 2);

        assertEquals(4 * 1 + 2, testLinearImage.get(0, 0));
        assertEquals(4 * (1 + 2) + 2 + 2, testLinearImage.get(2, 2));
    }


    /**
     * assure proper functionality of H-Iterator
     * - step of 1
     * - proper beginning  (before next)
     * - proper termination
     */
    public void testHIteratorSetting() {
        final TestLinearImage testLinearImage = new TestLinearImage(20, 30, 0, 0, 4, 3);
        testLinearImage.iterateH(1);

        // positioned before first pixel (last ion prev row)
        assertEquals(19, testLinearImage.get());
        // and one is available
        assertTrue(testLinearImage.hasNext());

        assertEquals(20, testLinearImage.next());
        assertTrue(testLinearImage.hasNext());
        assertEquals(21, testLinearImage.next());
        assertTrue(testLinearImage.hasNext());
        assertEquals(22, testLinearImage.next());
        assertTrue(testLinearImage.hasNext());
        assertEquals(23, testLinearImage.next());
        assertFalse(testLinearImage.hasNext());
    }

    /**
     * assure proper working of H-Iterator
     */
    public void testVIteratorSetting() {
        final TestLinearImage testLinearImage = new TestLinearImage(10, 12, 0, 0, 4, 3);
        testLinearImage.iterateV(1);
        // before first row
        assertEquals(-9, testLinearImage.get());
        // and available
        assertTrue(testLinearImage.hasNext());

        assertEquals(1, testLinearImage.next());
        assertTrue(testLinearImage.hasNext());

        assertEquals(11, testLinearImage.next());
        assertTrue(testLinearImage.hasNext());

        assertEquals(21, testLinearImage.next());
        assertFalse(testLinearImage.hasNext());
    }

    /**
     * single pixel H-iterator shall work properly
     */
    public void testSinglePixelIteratorH() {
        final TestLinearImage testLinearImage = new TestLinearImage(4, 3);
        testLinearImage.iterateH(1, 1, 1);
        // before first
        assertEquals(4, testLinearImage.get());
        assertTrue(testLinearImage.hasNext());

        assertEquals(5, testLinearImage.next());
        assertFalse(testLinearImage.hasNext());
    }

    /**
     * single pixel v-interation shall work properly
     */
    public void testSinglePixelIteratorV() {
        final TestLinearImage testLinearImage = new TestLinearImage(4, 3);
        testLinearImage.iterateV(2, 2, 2);
        // before first
        assertEquals(6, testLinearImage.get());
        // and available
        assertTrue(testLinearImage.hasNext());
        // step
        assertEquals(10, testLinearImage.next());
        // nothing more
        assertFalse(testLinearImage.hasNext());
    }


    /**
     * image copy shall move pixels from one image to another
     */
    public void testImageCopy() {
        final TestLinearImage src = new TestLinearImage(2, 3);
        final TestLinearImage dst = new TestLinearImage(2, 3);
        src.copy(dst);
        assertEquals(0, dst.values.get(0));
        assertEquals(1, dst.values.get(1));
        assertEquals(2, dst.values.get(2));
        assertEquals(3, dst.values.get(3));
        assertEquals(4, dst.values.get(4));
        assertEquals(5, dst.values.get(5));

    }

    /**
      * image flip shall switch dimensions while copying
      */
     public void testImageFlip() {
         final TestLinearImage src = new TestLinearImage(2, 3);
         final TestLinearImage dst = new TestLinearImage(3, 2);
         src.flip(dst);
         assertEquals(0, dst.values.get(0));
         assertEquals(2, dst.values.get(1));
         assertEquals(4, dst.values.get(2));
         assertEquals(1, dst.values.get(3));
         assertEquals(3, dst.values.get(4));
         assertEquals(5, dst.values.get(5));

     }

    public class TestLinearImage extends AbstractLinearImage {
        ArrayList values = new ArrayList();

        protected TestLinearImage(int width, int height) {
            super(width, height);
        }

        protected TestLinearImage(int width, int height, int originX, int originY, int boxW, int boxH) {
            super(width, height, originX, originY, boxW, boxH);
        }

        @Override
        public int get() {
            return currentIndex;
        }

        @Override
        public void put(int value) {
            values.add(value);
        }

        public Image chisel(int fromX, int fromY, int width, int height) {
            return null;  
        }
    }


}
