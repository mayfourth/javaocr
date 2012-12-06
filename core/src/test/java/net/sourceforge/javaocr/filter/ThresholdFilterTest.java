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
 * test threshold computation
 */
public class ThresholdFilterTest extends TestCase {

    public void testThreasholdProcessing() {
        ThresholdFilter filter = new ThresholdFilter(150);
        int[] data = new int[]{3, 150, 151};

        filter.process(new PixelImage(data, 1, 3));
        assertEquals(0, data[0]);
        assertEquals(0, data[1]);
        assertEquals(255, data[2]);
    }
}
