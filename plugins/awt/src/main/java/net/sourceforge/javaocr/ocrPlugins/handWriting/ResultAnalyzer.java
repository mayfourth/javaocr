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
 * Determines the correct character by looking at errors between
 * different recognizers.
 * @author William Whitney
 */
public class ResultAnalyzer
{

    private ArrayList<OCRIdentification> mseCharIdentList;
    private ArrayList<OCRIdentification> apsectCharIdentList;
    private int listSize;

    public ResultAnalyzer(ArrayList<OCRIdentification> mseCharIdentList, ArrayList<OCRIdentification> aspectCharIdentList)
    {
        this.mseCharIdentList = mseCharIdentList;
        this.apsectCharIdentList = aspectCharIdentList;
        this.listSize = getListSize();
    }

    public String calculateResultAndReturnString()
    {
        String result = "";

        ArrayList<CandidateOCRChoice> choices = getCharChoices();

        for (CandidateOCRChoice choice : choices)
        {
            ArrayList<OCRIdentification> OCRResultsForChar = choice.getRankedIdentList();

            result += getHighestRankResult(OCRResultsForChar);
        }

        return result;
    }

    private char getHighestRankResult(ArrayList<OCRIdentification> OCRResultsForChar)
    {
        char recommendedChar = ' ';


        for (OCRIdentification currOCRIdentification : OCRResultsForChar)
        {
            if (currOCRIdentification.getNumChars() > 0)
            {
                recommendedChar = currOCRIdentification.getCharIdx(0);
            }
        }
        return recommendedChar;
    }

    private ArrayList<CandidateOCRChoice> getCharChoices()
    {
        ArrayList<CandidateOCRChoice> choices = new ArrayList<CandidateOCRChoice>();

        for (int i = 0; i < listSize; i++)
        {
            CandidateOCRChoice cand = new CandidateOCRChoice();

            if (!mseCharIdentList.isEmpty())
            {
                OCRIdentification currIdent = mseCharIdentList.get(i);
                cand.addOCRIdent(currIdent);
            }

            if (!apsectCharIdentList.isEmpty())
            {
                OCRIdentification currIdent = apsectCharIdentList.get(i);
                cand.addOCRIdent(currIdent);
            }

            choices.add(cand);

        }

        return choices;
    }

    private int getListSize()
    {
        int size = 0;

        if (apsectCharIdentList.size() > size)
        {
            size = apsectCharIdentList.size();
        }
        if (mseCharIdentList.size() > size)
        {
            size = mseCharIdentList.size();
        }

        return size;
    }
    private static final Logger LOG = Logger.getLogger(ResultAnalyzer.class.getName());
}
