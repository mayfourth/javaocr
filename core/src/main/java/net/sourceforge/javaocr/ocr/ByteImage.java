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
 * image backed by byte array
 */
public class ByteImage extends AbstractLinearImage {
    byte[] image;


    public ByteImage(int width, int height) {
        this(new byte[width * height], width, height);
    }

    public ByteImage(byte[] image, int width, int height) {
        this(image, width, height, 0, 0, width, height);
    }

    public ByteImage(byte[] image, int arrayWidth, int arrayHeight, int originX, int originY, int width, int height) {
        super(arrayWidth, arrayHeight, originX, originY, width, height);
        this.image = image;
    }

    @Override
    public int get() {
        return image[currentIndex] & 0xff;
    }

    @Override
    public void put(int value) {
        image[currentIndex] = (byte) (value);
    }

    public Image chisel(int fromX, int fromY, int width, int height) {
        return new ByteImage(image, arrayWidth, arrayHeight, originX + fromX, originY + fromY, width, height);
    }

    @Override
    public String toString() {
        return "ByteImage{} " + super.toString();
    }
}
