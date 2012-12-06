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
package net.sourceforge.javaocr.ocrPlugins.mseOCR;

import java.util.logging.Logger;

/**
 * Class to represent a range of character codes.
 * @author Ronald B. Cemer
 */
public class CharacterRange
{

    /**
     * The minimum character value in this range.
     */
    public final int min;
    /**
     * The maximum character value in this range.
     */
    public final int max;

    /**
     * Construct a new <code>CharacterRange</code> object for a range of
     * character codes.
     * @param min The minimum character value in this range.
     * @param max The maximum character value in this range.
     */
    public CharacterRange(int min, int max)
    {
        if (min > max)
        {
            throw new InternalError("max must be >= min");
        }
        this.min = min;
        this.max = max;
    }

    /**
     * Construct a new <code>CharacterRange</code> object for a single
     * character code.
     * @param c The character code for this range.  This code will be both
     * the minimum and maximum for this range.
     */
    public CharacterRange(int c)
    {
        this(c, c);
    }
    private static final Logger LOG = Logger.getLogger(CharacterRange.class.getName());
}
