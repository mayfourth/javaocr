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

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ImageFilter;

/**
 * Abstract base class for filters operating on pixel neighborhood
 * @author Andrea De Pasquale
 */
public abstract class AbstractNeighborhoodFilter implements ImageFilter {
	
	protected int filterW, filterH;
	private int sizeL, sizeR, sizeT, sizeB;
	private Image destImage;

	/**
	 * Initializes an <code>AbstractNeighborhoodFilter</code>
	 * 
	 * @param width Neighborhood width; if even, filter expands to the right only.
	 * @param height Neighborhood height; if even, filter expands to the bottom only.
	 * @param dest The image to be filled up during processing.
	 */
	public AbstractNeighborhoodFilter(int width, int height, Image dest) {
		filterW = (width  > 0 ? width  : 1);
		filterH = (height > 0 ? height : 1);
		sizeL = (filterW - 1) / 2;
		sizeR = (filterW - 1) - sizeL;
		sizeT = (filterH - 1) / 2;
		sizeB = (filterH - 1) - sizeT;
		destImage = dest;
	}
	
	/**
	 * Chisels out image neighborhood and then calls <code>processNeighborhood()</code>
	 */
  public void process(Image image) {
		final int imageW = image.getWidth();
		final int imageH = image.getHeight();
		
		// process valid area of the image
		for (int y = sizeT; y < imageH-sizeB; ++y) {
			for (int x = sizeL; x < imageW-sizeR; ++x) {
				Image nImage = image.chisel(x-sizeL, y-sizeT, filterW, filterH);
				destImage.put(x, y, processNeighborhood(nImage));
			}
		}
		
		// and copy four borders as they are
		image.chisel(0, 0, imageW, sizeT).copy(destImage.chisel(0, 0, imageW, sizeT));
		image.chisel(0, imageH-sizeB, imageW, sizeB).copy(destImage.chisel(0, imageH-sizeB, imageW, sizeB));
		image.chisel(0, 0, sizeL, imageH).copy(destImage.chisel(0, 0, sizeL, imageH));
		image.chisel(imageW-sizeR, 0, sizeR, imageH).copy(destImage.chisel(imageW-sizeR, 0, sizeR, imageH));
  }

  /**
   * compute pixel value by looking up values in the neighborhood image
   */
  protected abstract int processNeighborhood(Image nImage);
}
