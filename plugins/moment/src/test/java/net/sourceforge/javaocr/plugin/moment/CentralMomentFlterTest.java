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

package net.sourceforge.javaocr.plugin.moment;

import junit.framework.TestCase;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * test capability of central moment filter (computation)
 */
public class CentralMomentFlterTest extends TestCase {

    /**
     * shal compute proper value
     */
    public void testMomentComputing() {
        int[] data = new int[]{
                1, 2,
                3, 4
        };
        final CentralMomentFilter rawMomentFilter = new CentralMomentFilter(2, 3, 4, 5);
        rawMomentFilter.process(new PixelImage(data, 2, 2));
        assertEquals(rawMomentFilter.getMoment(),
                Math.pow(0 - 4, 2) * Math.pow(0 - 5, 3) * 1 + Math.pow(1 - 4, 2) * Math.pow(0 - 5, 3) * 2 +
                Math.pow(0 - 4, 2) * Math.pow(1 - 5, 3) * 3 + Math.pow(1 - 4, 2) * Math.pow(1 - 5, 3) * 4
        );


    }
}
