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

package net.sourceforge.javaocr.plugin.fir;

import java.util.Arrays;

import net.sourceforge.javaocr.Image;

/**
 * Apply median filter to image, useful to clean up after thresholding.
 * TODO Works for binarized images only, could be extended to grayscale.
 * @author Andrea De Pasquale
 */
public class MedianFilter extends AbstractNeighborhoodFilter {
	
	private int[] pixels;
	private int counter;

	/**
	 * Create a <code>MedianFilter</code>, which selects median value from 
	 * pixels inside a given neighborhood.
	 * 
	 * @param width Neighborhood width; if even, filter expands to the right only.
	 * @param height Neighborhood height; if even, filter expands to the bottom only.
	 * @param dest The image to be filled up during processing.
	 */
	public MedianFilter(int width, int height, Image dest) {
		super(width, height, dest);
		pixels = new int[filterW * filterH];
	}

	protected int processNeighborhood(Image nImage) {
    counter = 0;
    
    int nHeight = nImage.getHeight();
    for (int i = 0; i < nHeight; i++) {
      for (nImage.iterateH(i); nImage.hasNext();) {
      	pixels[counter++] = nImage.next();
      }
    }

    Arrays.sort(pixels, 0, counter);
    if (counter % 2 == 0) {
    	return (pixels[counter/2] + pixels[(counter/2)-1]) / 2;
    } else {
    	return pixels[(counter-1)/2];
    }
	}

}
