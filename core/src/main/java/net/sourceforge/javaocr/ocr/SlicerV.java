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
 * slice image in vertical chunks
 */
public class SlicerV extends AbstractBaseSlicer {

    public SlicerV(Image image, int empty) {
        super(empty, image, image.getWidth());
    }


    @Override
    protected Image chisel(int imageStart) {
        return image.chisel(imageStart, 0, currentPosition - imageStart, image.getHeight());
    }

    @Override
    protected void iterateSpan() {
        image.iterateV(currentPosition);
    }
}
