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

import net.sourceforge.javaocr.Image;

/**
 * class performing image slicing.  it's a good idea to pre process image and made it binary.
 * image slicing does not move data around and does not modify it.  slicer is not thread safe.
 *
 * @author Konstantin Pribluda
 */
public class SlicerH extends AbstractBaseSlicer  {

    /**
     * @param image image to be sliced
     * @param empty empty empty
     */
    public SlicerH(Image image, int empty) {
        super(empty, image, image.getHeight());
    }

    @Override
    protected void iterateSpan() {
        image.iterateH(currentPosition);
    }


    @Override
    protected Image chisel(int imageStart) {
        return image.chisel(0, imageStart, image.getWidth(), currentPosition - imageStart);
    }
}
