package hci.divinesymphony.net.flashtrainer.backend;



import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;
import hci.divinesymphony.net.flashtrainer.beans.Problem;
import hci.divinesymphony.net.flashtrainer.beans.ProblemSet;

/**
 * Created by wills_000 on 3/7/2015.
 */

public class Selector
{
    int match;
    int max = 100;
    int min=1;
    private Random picker;
    private final ArrayList<Problem> questions = new ArrayList<Problem>();
    //private ArrayList<Response> answers = new ArrayList<Response>();
    private Queue<String> recentproblem = new LinkedList<String>();
    private final String xmlFile;

    public Selector() {
        //TODO not sure where this should be stored or referenced in the application
        this.xmlFile = "communication.xml";
    }

    // Five Random weight is got and then it's checked against the weight in problem list.
    // One problem is choosen, the id is added to queue to keep track of recent onces and the first one is remove when queue reaches size 10.
    public ProblemSet getProblemSet(){
        //boolean choosenProblem = false;
        DomParser dom = new DomParser();
        List<Problem> problemList= dom.getQuestions();

        List<Problem> weighted = new ArrayList();
        for (Problem prob : dom.getQuestions()) {
            for (int i = prob.getWeight(); i > 0; i--) {
                weighted.add(prob);
            }
        }

        int index = (int)( Math.random()*weighted.size() );

        Problem problem = weighted.get(index);
        DisplayItem disproblem = new DisplayItem(problem.getText(), problem.getprobID());

/*
        while (!choosenProblem){
            for(int weightInder:getWeightIndex()) {
                for (Problem problem : problemList) {
                    if (Integer.getInteger(problem.getWeight()) == weightInder) {
                        if (!recentproblem.contains(problem.getprobID())) {
                            DisplayItem disproblem = new DisplayItem(problem.getText(), problem.getprobID());
                            choosenProblem= true;
                            recentproblem.add(problem.getprobID());
                            if (recentproblem.size() > 10) {
                                recentproblem.remove();
                            }
                        }
                    }

                }
            }
        }
*/
        return new ProblemSet(disproblem, answerChoice(problem));
    }

/*
    public List<Integer> getWeightIndex()
    {
        List<Integer> weightlist =  new ArrayList<Integer>();
        for(int i =0;i<5;i++) {
            weightlist.add(picker.nextInt(((max - min) + 1) + min));
        }
        Collections.sort( weightlist );
        return weightlist;

    }
*/

    // The answer needs to be worked on, the prasing is missing Response object.
    public List<DisplayItem> answerChoice(Problem problem)
    {
        List<DisplayItem> responses = new ArrayList<DisplayItem>(4);
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "45"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "af"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "4v"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "1m"));

        return responses;
    }


}