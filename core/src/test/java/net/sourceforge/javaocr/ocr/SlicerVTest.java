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
 * test capabilities of image slicing with vertical lines
 * since most features are tested by slicerHTest, we check only differing ones here
 *
 * @author Konstantin Pribluda
 */
public class SlicerVTest extends TestCase {
    /**
     * test image on end is recognised properly
     */
    public void testSubimageOnEnd() {
        byte[] data = new byte[]{0, 0, 1};
        Image image = new ByteImage(data, 3, 1);

        SlicerV slicer = new SlicerV(image, 0);

        slicer.slice(0);
        // must have image
        assertTrue(slicer.hasNext());


        Image slice = slicer.next();

        assertNotNull(slice);
        assertEquals(1, slice.getHeight());
        assertEquals(1, slice.getWidth());
        assertEquals(1, slice.get(0, 0));

        // no image anymore
        assertFalse(slicer.hasNext());
    }

}
