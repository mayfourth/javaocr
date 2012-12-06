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
import net.sourceforge.javaocr.ImageFilter;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * Apply closing by structuring element to binarized source image.
 * TODO Works for binarized images only, could be extended to grayscale.
 * 
 * The closing of an image A by a structuring element B is obtained by the 
 * dilation of A by B, followed by erosion of the resulting structure by B.
 * 
 * http://en.wikipedia.org/wiki/Closing_(morphology)
 * 
 * @author Andrea De Pasquale
 */
public class ClosingFilter implements ImageFilter {

  protected DilationFilter dilationFilter;
  protected Image tempImage;
  protected ErosionFilter erosionFilter;
  protected Image destImage;
  
  /**
   * Create a <code>ClosingFilter</code> with default values
   * of 255 for the foreground and 0 for the background.
   * @param strElem Structuring element
   * @param dest Output image
   */
  public ClosingFilter(Image strElem, Image dest) {
    this(strElem, dest, 255, 0);
  }
  
  /**
   * Create a <code>ClosingFilter</code>.
   * @param strElem Structuring element
   * @param dest Output image
   * @param full Foreground value 
   * @param empty Background value
   */
  public ClosingFilter(Image strElem, Image dest, int full, int empty) {
    tempImage = new PixelImage(dest.getWidth(), dest.getHeight());
    dilationFilter = new DilationFilter(strElem, tempImage, full, empty);
    destImage = dest;
    erosionFilter = new ErosionFilter(strElem, destImage, full, empty);
  }
  
  @Override
  public void process(Image image) {
    dilationFilter.process(image);
    erosionFilter.process(tempImage);
  }

}
