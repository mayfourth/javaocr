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
 * test capabilities of slicer
 *
 * @author Konstantin Pribluda
 */
public class SlicerHTest extends TestCase {

    /**
     * empty image shall produce no slices
     */
    public void testThatEmptyImageHasNothing() {
        byte[] data = new byte[]{0, 0, 0};
        Image image = new ByteImage(data, 1, 3);
        SlicerH slicer = new SlicerH(image, 0);

        slicer.slice(0);
        // must have no image
        assertFalse(slicer.hasNext());
    }

    /**
     * test image on end is recognised properly
     */
    public void testSubimageOnEnd() {
        byte[] data = new byte[]{0, 0, 1};
        Image image = new ByteImage(data, 1, 3);

        SlicerH slicer = new SlicerH(image, 0);

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


    public void testSubimageOnEndWithTolerance() {
        byte[] data = new byte[]{0, 0, 1};
        Image image = new ByteImage(data, 1, 3);

        SlicerH slicer = new SlicerH(image, 0);

        slicer.slice(0,1);
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

    /**
     *
     */
    public void testThatImageOnStartIsRecognisedProperly() {
        byte[] data = new byte[]{1, 0, 0};
        Image image = new ByteImage(data, 1, 3);

        SlicerH slicer = new SlicerH(image, 0);

        slicer.slice(0);
        // must have image
        assertTrue(slicer.hasNext());

        Image slice = slicer.next();
        System.err.println("slice:" + slice);
        assertNotNull(slice);
        assertEquals(1, slice.getHeight());
        assertEquals(1, slice.getWidth());
        assertEquals(1, slice.get(0, 0));
        // no image anymore
        assertFalse(slicer.hasNext());
    }

    /**
     * gap below tolerance shall not produce another slice
     */
    public void testToleranceIsRespected() {
        byte[] data = new byte[]{0, 0, 1, 0, 1, 0, 0, 0};

        Image image = new ByteImage(data, 1, 8);

        SlicerH slicer = new SlicerH(image, 0);

        slicer.slice(0,1);

        assertTrue(slicer.hasNext());
        Image slice = slicer.next();

        assertNotNull(slice);

        assertEquals(3, slice.getHeight());
        assertEquals(1, slice.getWidth());

        assertEquals(1, slice.get(0, 0));
        assertEquals(0, slice.get(0, 1));
        assertEquals(1, slice.get(0, 2));

        // no image anymore
        assertFalse(slicer.hasNext());

    }


 
}
