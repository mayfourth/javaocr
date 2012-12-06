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
import net.sourceforge.javaocr.ImageShrinker;

/**
 * assure proper function of image shrinkeer
 */
public class ShrinkerTest extends TestCase {

    public void testImageShrinking() {
        byte[] data = new byte[]{0, 0, 0, 0, 1, 0, 0, 0, 0};

        Image image = new ByteImage(data, 3, 3);
        ImageShrinker shrinker = new Shrinker(0);

        Image result = shrinker.shrink(image);

        assertEquals(1, result.getWidth());
        assertEquals(1, result.getHeight());
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.getOriginX());
        assertEquals(1, result.getOriginY());
    }
}
