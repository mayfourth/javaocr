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

import net.sourceforge.javaocr.Image;

/**
 * like integral image filter, but sum up squares (useful in various calculations)
 *
 * @author Konstantin Pribluda
 */
public class SquaredIntergalImageFilter extends AbstractIntegralImageFilter {

    public SquaredIntergalImageFilter(Image resultImage) {
        super(resultImage);
    }

    @Override
    protected int processPixel(final int i) {
        return i * i;
    }
}
