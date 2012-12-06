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
import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ocr.AbstractLinearImage;

/**
 * test proper conversion from grayscale to RGBA
 */
public class GrayscaleToRGBATestCase extends TestCase {


    public void testPixelConversion() {
        GrayscaleToRGBA filter = new GrayscaleToRGBA();
        TestLinearImage image = new TestLinearImage(0x15);
        filter.processPixel(image);

        assertEquals(0xff151515, image.get());

        image.put(0xff);
        filter.processPixel(image);
        assertEquals(0xffffffff, image.get());

        image.put(0x00);
        filter.processPixel(image);
        assertEquals(0xff000000, image.get());
    }


    class TestLinearImage extends AbstractLinearImage {
        int pixel;

        protected TestLinearImage(int value) {
            super(1, 1);
            pixel = value;
        }

        @Override
        public int get() {
            return pixel;
        }

        @Override
        public void put(int value) {
            pixel = value;
        }

        public Image chisel(int fromX, int fromY, int width, int height) {
            return null; 
        }
    }

}
