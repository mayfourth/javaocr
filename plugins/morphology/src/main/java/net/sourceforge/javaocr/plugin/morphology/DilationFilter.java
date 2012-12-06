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

package net.sourceforge.javaocr.plugin.morphology;

import net.sourceforge.javaocr.Image;

/**
 * Apply dilation by structuring element to binarized source image. 
 * TODO Works for binarized images only, could be extended to grayscale.
 * 
 * The dilation of an image A by a structuring element B can be understood as 
 * the locus of the points covered by B when the center of B moves inside A.
 * 
 * http://en.wikipedia.org/wiki/Dilation_%28morphology%29
 * 
 * @author Andrea De Pasquale
 */
public class DilationFilter extends AbstractMorphologyFilter {

  /**
   * Create a <code>DilationFilter</code> with default values
   * of 255 for the foreground and 0 for the background.
   * @param strElem Structuring element
   * @param dest Output image
   */
  public DilationFilter(Image strElem, Image dest) {
    this(strElem, dest, 255, 0);
  }
  
  /**
   * Create a <code>DilationFilter</code>.
   * @param strElem Structuring element
   * @param dest Output image
   * @param full Foreground value 
   * @param empty Background value
   */
  public DilationFilter(Image strElem, Image dest, int full, int empty) {
    super(strElem, dest, full, empty);
  }
  
  /**
   * Apply dilation to the given image, leaving borders unprocessed.
   * @param image Input image
   */
  public void process(Image image) {
    final int imageW = image.getWidth();
    final int imageH = image.getHeight();
    
    // copy four borders as they are
    image.chisel(0, 0, imageW, sizeT).copy(destImage.chisel(0, 0, imageW, sizeT));
    image.chisel(0, imageH-sizeB, imageW, sizeB).copy(destImage.chisel(0, imageH-sizeB, imageW, sizeB));
    image.chisel(0, 0, sizeL, imageH).copy(destImage.chisel(0, 0, sizeL, imageH));
    image.chisel(imageW-sizeR, 0, sizeR, imageH).copy(destImage.chisel(imageW-sizeR, 0, sizeR, imageH));
    
    // process valid area of the image
    for (int y = sizeT; y < imageH-sizeB; ++y) {
      for (int x = sizeL; x < imageW-sizeR; ++x) {
        if (image.get(x, y) == full) {
          Image nImage = destImage.chisel(x-sizeL, y-sizeT, seImageW, seImageH);
          processNeighborhood(nImage);
        }
      }
    }
  }

  protected void processNeighborhood(Image nImage) {
    for (int i = 0; i < seImageH; ++i) {
      for (seImage.iterateH(i), nImage.iterateH(i); 
           seImage.hasNext() && nImage.hasNext(); ) {
        if (seImage.next() == full)
          nImage.next(full);
        else nImage.next();
      }
    }
  }

}
