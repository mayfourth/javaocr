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
import net.sourceforge.javaocr.ocr.AbstractLinearImage;

import java.util.ArrayList;


/**
 * test image traversal by abstract filter
 */
public class ImageTraversalTest extends TestCase {

    /**
     * shall traverse entire image defined over subregion
     */
    public void testImageTraversal() {
        final ArrayList pixels = new ArrayList();
        Image image = new AbstractLinearImage(5, 5, 2, 2, 3, 3) {
            @Override
            public int get() {              
                pixels.add(currentIndex);
                return 0;
            }

            @Override
            public void put(int value) {
                // no op here
            }

            public Image chisel(int fromX, int fromY, int width, int height) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        AbstractMomentFilter filter = new AbstractMomentFilter(0, 0) {
         

            @Override
            protected double[] precomputeX(Image image) {
                return new double[image.getWidth()];  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            protected double[] precomputeY(Image image) {
                return new double[image.getHeight()];  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        filter.process(image);
        
        assertEquals(9, pixels.size());
        assertEquals(12, pixels.get(0));
        assertEquals(13, pixels.get(1));
        assertEquals(14, pixels.get(2));
        assertEquals(17, pixels.get(3));
        assertEquals(18, pixels.get(4));
        assertEquals(19, pixels.get(5));
        assertEquals(22, pixels.get(6));
        assertEquals(23, pixels.get(7));
        assertEquals(24, pixels.get(8));
    }
}
