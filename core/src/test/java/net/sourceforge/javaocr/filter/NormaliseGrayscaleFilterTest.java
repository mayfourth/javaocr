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
 * test grayscale normalisation. this filter shall apply linear ramp
 * to pixel values. everything less or equesl than  "from" will become 0,
 * everything that bigger or equal "to" - 255 and in between shall be modified lienar
 */
public class NormaliseGrayscaleFilterTest extends TestCase {
    /**
     * assumes
     */
    public void testGrayscaleNormalisation() {
        NormaliseGrayscaleFilter filter = new NormaliseGrayscaleFilter(10, 50);
        int[] data = new int[]{0, 10, 30, 50, 210};

        filter.process(new PixelImage(data, 5, 1));
        assertEquals(0, data[0]);
        assertEquals(0, data[1]);
        assertEquals(127, data[2]);
        assertEquals(255, data[3]);
        assertEquals(255, data[4]);
    }
}
