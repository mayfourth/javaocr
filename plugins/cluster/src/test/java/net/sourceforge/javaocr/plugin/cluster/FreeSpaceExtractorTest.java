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

package net.sourceforge.javaocr.plugin.cluster;

import junit.framework.TestCase;
import net.sourceforge.javaocr.ocr.PixelImage;
import net.sourceforge.javaocr.plugin.cluster.extractor.FreeSpacesExtractor;
import org.junit.Test;


/**
 * test proper functions of free space extractor
 * @author Konstantin Pribluda
 */
public class FreeSpaceExtractorTest extends TestCase {

    /**
     * degenerate 1x1 image produices 0
     */
    @Test
    public void testDegenerateImage() {
        // just single taken pixel
        int[] data = new int[]{1};

        FreeSpacesExtractor fse = new FreeSpacesExtractor();

        assertEquals(0d, fse.extract(new PixelImage(data, 1, 1))[0]);

    }

    /**
     * empty single pixel image shall produce 1
     */
    @Test
    public void testDegenerateEmptyProvidesOne() {
        // just single taken pixel
        int[] data = new int[]{0};
        FreeSpacesExtractor fse = new FreeSpacesExtractor();

        assertEquals(1d, fse.extract(new PixelImage(data, 1, 1))[0]);
    }


    /**
     * contiguous areas inside ro shall  be   count as one
     */
    @Test
    public void testContiguousSpace() {
        int[] data = new int[]{0, 0, 0, 1, 0, 0, 1, 0};
        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(3d, fse.extract(new PixelImage(data, 8, 1))[0]);

        data = new int[]{1, 0, 0, 1, 0, 0, 1, 1};
        assertEquals(2d, fse.extract(new PixelImage(data, 8, 1))[0]);
    }

    /**
     * test previous row is Taken into  account
     */
    @Test
    public void testPreviousRowIsTakenIntoAccount() {
        //  with row ending to empty
        int[] data = new int[]{
                0, 0, 1, 0,
                0, 0, 1, 0
        };

        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(2d, fse.extract(new PixelImage(data, 4, 2))[0]);

        // .... and taken pixel
        data = new int[]{
                0, 0, 1, 1,
                0, 0, 1, 0
        };
        assertEquals(2d, fse.extract(new PixelImage(data, 4, 2))[0]);

        //  ....  complementar cases
        data = new int[]{
                0, 0, 1, 1,
                1, 0, 1, 0
        };
        assertEquals(2d, fse.extract(new PixelImage(data, 4, 2))[0]);

        data = new int[]{
                0, 0, 1, 0,
                1, 0, 1, 0
        };

        assertEquals(2d, fse.extract(new PixelImage(data, 4, 2))[0]);


        data = new int[]{
                0, 1, 1, 0,
                1, 0, 1, 0
        };

        assertEquals(3d, fse.extract(new PixelImage(data, 4, 2))[0]);


        data = new int[]{
                0, 1, 1, 0,
                1, 0, 0, 1
        };

        assertEquals(3d, fse.extract(new PixelImage(data, 4, 2))[0]);

        data = new int[]{
                1, 0, 1, 0,
                0, 1, 1, 0
        };

        assertEquals(3d, fse.extract(new PixelImage(data, 4, 2))[0]);


        data = new int[]{
                1, 0, 1, 1,
                0, 1, 1, 0
        };

        assertEquals(3d, fse.extract(new PixelImage(data, 4, 2))[0]);
    }


    /**
     * wedge from top is not an separato, shall be 1
     */
    @Test
    public void testWedgeFromTop() {

        int[] data = new int[]{
                1, 0, 1, 0,
                0, 0, 0, 0,

        };

        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(1d, fse.extract(new PixelImage(data, 4, 2))[0]);
    }

    /**
     * wedge from bottom is not an separato, shall be 1
     */
    @Test
    public void testWedgeFromBottom() {

        int[] data = new int[]{
                0, 0, 0, 0,
                1, 0, 1, 0,

        };
        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(1d, fse.extract(new PixelImage(data, 4, 2))[0]);
    }

    /**
     * enclise taken space shall not break
     */
    @Test
    public void testEnclosedIsle() {

        int[] data = new int[]{
                0, 0, 0, 0,
                0, 1, 1, 0,
                0, 0, 0, 0
        };
        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(1d, fse.extract(new PixelImage(data, 4, 3))[0]);
    }


    @Test
    public void testEmptyStep() {
        int[] data = new int[]{
                1, 0, 0, 1,
                0, 0, 0, 1,
                0, 0, 0, 0
        };
        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(1d, fse.extract(new PixelImage(data, 4, 3))[0]);
    }

    /**
     * test correct compuation of shape 2
     */
    @Test
    public void testTwo() {

        int[] data = new int[]{
                0, 1, 1, 0,
                1, 0, 0, 1,
                1, 0, 0, 1,
                0, 0, 0, 1,
                0, 0, 1, 0,
                0, 1, 0, 0,
                1, 0, 0, 0,
                1, 1, 1, 1

        };
        FreeSpacesExtractor fse = new FreeSpacesExtractor();
        assertEquals(4d, fse.extract(new PixelImage(data, 4, 8))[0]);
    }
}
