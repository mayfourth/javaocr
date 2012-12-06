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
import net.sourceforge.javaocr.ocr.PixelImage;
import org.junit.Test;

/**
 * assure proper functionality of integral image filter
 */
public class IntegralFilterTest extends TestCase {


    /**
     * shall integrate pixels in the image
     */
    @Test
    public void testIntegration() {
        int[] source = new int[]{0, 1, 2, 3, 4, 5};

        Image src = new PixelImage(source, 2, 3);

        int[] result = new int[6];
        Image dst = new PixelImage(result, 2, 3);

        IntegralImageFilter iff = new IntegralImageFilter(dst);

        iff.process(src);

        /**
         * source image:
         *    0,1
         *    2,3
         *    4,5
         *
         * awaiting result:
         *    0,1
         *    2,6
         *    6,15
         *
         */
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
        assertEquals(2, result[2]);
        assertEquals(6, result[3]);
        assertEquals(6, result[4]);
        assertEquals(15, result[5]);

        // test window computation
        // sum of values inside window tlbr is:  br - bl - tr + tl
        assertEquals(14,iff.windowValue(0,1,1,2));
    }
}
