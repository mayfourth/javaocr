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

package net.sf.javaocr.demos.android.utils.image;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * image preprocessing
 * @author Konstantin Pribluda
 */
public interface ImageProcessor {
    /**
     * process image in some obscure way
     *
     * @param image incoming image.  may be modified
     * @return processed image, may be subimage over original one, and is definitely pixel image as we need integer pixel array for further processing
     * TODO this is not really nice,  but we need pixel array for further processing
     */
    PixelImage prepareImage(byte[] image, int offsetX, int offsetY);


}
