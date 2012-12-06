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
import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ocr.ByteImage;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * ensure proper function of hu moment calculations
 */
public class HuMomentsTest extends TestCase {


    /**
     * shall return 7 moments
     */
    public void testThat7MomentsAreDelivered() {
        byte[] data = new byte[]{0, 1};
        Image image = new ByteImage(data, 1, 2);

        final double[] moments = (new HuMoments()).extract(image);
        assertEquals(8, moments.length);
       
    }


    public void testHTranslationInvariance() {
        int[] data = new int[]
                {
                        0, 0, 0, 0,
                        0, 0, 1, 0,
                        0, 0, 1, 0,
                        0, 1, 1, 0,
                };

        Image full = new PixelImage(data, 4, 4);
        Image left = new PixelImage(data, 4, 4, 0, 0, 3, 4);
        Image right = new PixelImage(data, 4, 4, 1, 0, 3, 4);
        Image wrap = new PixelImage(data, 4, 4, 1, 0, 2, 4);

        double[] momentsFull = (new HuMoments()).extract(full);
        double[] momentsLeft = (new HuMoments()).extract(left);
        double[] momentsRight = (new HuMoments()).extract(right);
        double[] momentsWrap = (new HuMoments()).extract(wrap);

        printMoment("full :\t", momentsFull);
        printMoment("left :\t", momentsLeft);
        printMoment("right :\t", momentsRight);
        printMoment("wrap :\t", momentsWrap);

        for (int i = 0; i < 7; i++) {
            assertEquals("left " + i, momentsFull[i], momentsLeft[i]);
            assertEquals("right " + i, momentsFull[i], momentsRight[i]);
            assertEquals("wrap " + i, momentsFull[i], momentsWrap[i]);

        }
    }


    public void testVTranslationInvariance() {
        int[] data = new int[]
                {
                        0, 0, 0, 0,
                        0, 1, 1, 1,
                        0, 0, 0, 1,
                        0, 0, 0, 0,
                };

        Image full = new PixelImage(data, 4, 4);
        Image top = new PixelImage(data, 4, 4, 0, 0, 4, 3);
        Image bottom = new PixelImage(data, 4, 4, 0, 1, 4, 3);
        Image wrap = new PixelImage(data, 4, 4, 0, 1, 4, 2);

        double[] momentsFull = (new HuMoments()).extract(full);
        double[] momentsTop = (new HuMoments()).extract(top);
        double[] momentsBottom = (new HuMoments()).extract(bottom);
        double[] momentsWrap = (new HuMoments()).extract(wrap);

        printMoment("full:\t", momentsFull);
        printMoment("top:\t", momentsTop);
        printMoment("bottom:\t", momentsBottom);
        printMoment("wrap:\t", momentsWrap);

        for (int i = 0; i < 7; i++) {
            assertEquals("top " + i, momentsFull[i], momentsTop[i]);
            assertEquals("bottom " + i, momentsFull[i], momentsBottom[i]);
            assertEquals("wrap " + i, momentsFull[i], momentsWrap[i]);

        }
    }

    /**
     * moments shall be invariant to mirroring
     */
    public void testMirroringInvariance() {
        int[] data = new int[]
                {
                        0, 0, 0, 0,
                        0, 1, 1, 1,
                        0, 0, 0, 1,
                        0, 0, 0, 0,
                };

        int[] mirrorData = new int[]
                {
                        0, 0, 0, 0,
                        0, 0, 0, 1,
                        0, 1, 1, 1,
                        0, 0, 0, 0,
                };

        Image full = new PixelImage(data, 4, 4);
        Image mirror = new PixelImage(mirrorData, 4, 4);

        double[] moments = (new HuMoments()).extract(full);
        double[] momentsMirror = (new HuMoments()).extract(mirror);

        System.err.println("-----------------");
        printMoment("full:\t", moments);
        printMoment("mirror:\t", momentsMirror);

        for (int i = 0; i < 6; i++) {
            assertEquals("mirror " + i, moments[i], momentsMirror[i]);
        }
        // phi 7 changes sign on mirroring
        assertEquals(moments[6], -1 * momentsMirror[6]);

    }


    public void testRotationIrrelevance() {
        int[] data = new int[]
                {
                        0, 0, 0, 0,
                        0, 1, 1, 1,
                        0, 0, 0, 1,
                        0, 0, 0, 0,
                };

        int[] rotatedData = new int[]
                {
                        0, 0, 1, 0,
                        0, 0, 1, 0,
                        0, 1, 1, 0,
                        0, 0, 0, 0,
                };

        Image full = new PixelImage(data, 4, 4);
        Image rotated = new PixelImage(rotatedData, 4, 4);


        double[] moments = (new HuMoments()).extract(full);
        double[] momentsRotated = (new HuMoments()).extract(rotated);

        System.err.println("-----------------");
        printMoment("full:\t", moments);
        printMoment("rotated:\t", momentsRotated);

        for (int i = 0; i < 7; i++) {
            assertEquals("rotated " + i, moments[i], momentsRotated[i]);
        }

    }

    private void printMoment(final String label, double[] momentsFull) {
        StringBuilder builder = new StringBuilder();
        for (double moment : momentsFull)
            builder.append(moment).append("\t");
        System.err.println(label + " " + builder.toString());
    }

}
