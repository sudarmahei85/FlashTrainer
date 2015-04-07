package hci.divinesymphony.net.flashtrainer.backend;

import android.util.Log;

import hci.divinesymphony.net.flashtrainer.beans.Problem;
import hci.divinesymphony.net.flashtrainer.beans.ProblemSet;

/**
 * Created by rick on 3/8/15.
 */
public class AnswerChecker {

    public static boolean isCorrect(ProblemSet probSet, int responseId) {

        boolean correct = false;
        if ( (probSet != null) ) {
            Problem prob = probSet.getProblem();
            if (prob != null) {
                int answerId = prob.getAnswerId();
                Log.v(AnswerChecker.class.getName(), "ResponseId selected: "+responseId);
                Log.v(AnswerChecker.class.getName(), "Correct answer: "+answerId);
                correct = (answerId == responseId);
            }
        }
        return correct;
    }

}
