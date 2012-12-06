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
 * naive test to check sauvola filter functionality,
 * no assertions just demonstration
 */
public class SauvolaBinatisationFilterTest extends TestCase {

    @Test
    public void testComputing() {
      int[] samples = new int[] { 0,0,0,0,1,0,0,0,0};

        Image source =  new PixelImage(samples,3,3);
        PixelImage destination = new PixelImage(3,3);
        SauvolaBinarisationFilter filter = new SauvolaBinarisationFilter(1,0,destination,256,0.35,2);

        filter.process(source);

            for(int y = 0; y < destination.getHeight(); y++) {
                StringBuilder sb = new StringBuilder();
                destination.iterateH(y);

                while(destination.hasNext()) {
                    sb.append(destination.next());
                    if(destination.hasNext())
                            sb.append(",");
                }
                System.err.println(sb.toString());
            }
    }
}
