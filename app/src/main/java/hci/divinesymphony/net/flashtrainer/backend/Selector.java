package hci.divinesymphony.net.flashtrainer.backend;



import android.content.res.AssetManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;



import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;
import hci.divinesymphony.net.flashtrainer.beans.Problem;
import hci.divinesymphony.net.flashtrainer.beans.ProblemSet;
import hci.divinesymphony.net.flashtrainer.beans.Responses;

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
    private final DomParser dom;

    public Selector(InputStream is) {
        this.dom = new DomParser(is);
    }

    // Five Random weight is got and then it's checked against the weight in problem list.
    // One problem is choosen, the id is added to queue to keep track of recent onces and the first one is remove when queue reaches size 10.
    public ProblemSet getProblemSet() {
        //boolean choosenProblem = false;

        List<Problem> weighted = new ArrayList();
        for (Problem prob : this.dom.getQuestions()) {
            for (int i = prob.getWeight(); i > 0; i--) {
                weighted.add(prob);
            }
        }

        int index = (int)( Math.random()*weighted.size() );
        Problem problem = weighted.get(index);

        return new ProblemSet(problem.getContent(), answerChoice(problem));
    }



    public DisplayItem getReward() {
        List<DisplayItem> rewards = this.getRewards();
        int index = (int)( Math.random()*rewards.size() );
        return rewards.get(index);
    }

    public List<DisplayItem> getRewards() {
        return this.dom.getRewards();
    }

    public List<DisplayItem> getMedia() {
        return this.dom.getMedia();
    }

    public List<Responses> getResponses() {
        return this.dom.getResponseslist();
    }

    //TODO - The answer needs to be worked on, the parsing is missing Response object.
    public List<DisplayItem> answerChoice(Problem problem)
    {
        //get these from the XML file instead
        List<DisplayItem> responses = new ArrayList<DisplayItem>(4);
        List<Responses> responsesList = getResponses();
        HashMap<Integer,String> response = new HashMap<Integer,String>();
        for(Responses responses1:responsesList) {
            if(responses1.getGroupID()==problem.getGroupId()){
                response.putAll(responses1.getResponse());
            }
        }

        List<Integer> keys = new ArrayList<Integer>();
        keys.add(problem.getAnswerId());
        int count=3;
        for(int i=0; i<count;){
          int val= answerchoicekey(response);
           if(!keys.contains(val)){
               keys.add(val);
               i++;
           }
        }
        Collections.shuffle(keys);
        for(int j: keys) {
            responses.add(new DisplayItem(response.get(j),String.valueOf(j)));
        }

        return responses;
    }

    public Integer answerchoicekey(HashMap<Integer,String> response){

        Object[] responseKeys = response.keySet().toArray();
        Integer key =(Integer)responseKeys[new Random().nextInt(responseKeys.length)];
        return key;

    }
}