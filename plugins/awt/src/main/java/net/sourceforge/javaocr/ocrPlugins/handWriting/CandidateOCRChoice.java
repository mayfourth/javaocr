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
package net.sourceforge.javaocr.ocrPlugins.handWriting;

import java.util.ArrayList;
import java.util.logging.Logger;
import net.sourceforge.javaocr.scanner.accuracy.OCRIdentification;

/**
 * Provides the different choices you have to determine a character.
 * @author William Whitney
 */
public class CandidateOCRChoice
{

    private ArrayList<OCRIdentification> rankedIdentList = new ArrayList<OCRIdentification>();
    private static final Logger LOG = Logger.getLogger(CandidateOCRChoice.class.getName());

    public ArrayList<OCRIdentification> getRankedIdentList()
    {
        return rankedIdentList;
    }

    public void addOCRIdent(OCRIdentification currIdent)
    {
        rankedIdentList.add(currIdent);
    }
  
}
