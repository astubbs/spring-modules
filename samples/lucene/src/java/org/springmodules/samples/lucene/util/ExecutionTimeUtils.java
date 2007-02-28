/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.samples.lucene.util;

import java.util.Date;

/**
 * @author Thierry Templier
 */
public class ExecutionTimeUtils {
    private static long beginningTime=0;

    public static void executionBeginning() {
    	Date dateBeginning = new Date();
		executionBeginning(dateBeginning.getTime());
    }

    private static void executionBeginning(long beginning) {
		beginningTime = beginning;
    }

    public static long getExecutionTime() {
    	Date endDate = new Date();
        return getExecutionTime(endDate.getTime());
    }

    private static long getExecutionTime(long endTime) {
        return endTime-beginningTime;
    }

    public static String showExecutionTime() {
    	Date endTime = new Date();
        long executionTime = endTime.getTime()-beginningTime;
        return showExecutionTime(executionTime);
    }

    public static String showExecutionTime(long executionTime) {
        int heures = 0;
        int minutes = 0;
        int secondes = 0;
        int millisecondes = 0;
        long tmp = executionTime;

        //Calcul des millisecondes
        millisecondes = (int)tmp%1000;
        tmp=tmp/1000;

        //Calcul des secondes
        secondes = (int)tmp%60;
        tmp=tmp/60;

        //Calcul des minutes
        minutes = (int)tmp%60;
        tmp=tmp/60;

        //Calcul des heures
        heures = (int)tmp%60;
        tmp=tmp/60;

        StringBuffer executionTimeString = new StringBuffer();
        if( heures!=0 ) {
			executionTimeString.append(heures+"h ");
        }
        if( minutes!=0 ) {
			executionTimeString.append(minutes+"m ");
        }
        if( secondes!=0 ) {
			executionTimeString.append(secondes+"s ");
        }
        if( millisecondes!=0 ) {
			executionTimeString.append(millisecondes+"ms");
        }
        return executionTimeString.toString();
    }
}
