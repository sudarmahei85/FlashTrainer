package hci.divinesymphony.net.flashtrainer.backend;



import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.InputStream;
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
    private final InputStream is;

    public Selector(InputStream is) {
        this.is = is;
    }

    // Five Random weight is got and then it's checked against the weight in problem list.
    // One problem is choosen, the id is added to queue to keep track of recent onces and the first one is remove when queue reaches size 10.
    public ProblemSet getProblemSet() {
        //boolean choosenProblem = false;
        DomParser dom = new DomParser(this.is);

        List<Problem> weighted = new ArrayList();
        for (Problem prob : dom.getQuestions()) {
            for (int i = prob.getWeight(); i > 0; i--) {
                weighted.add(prob);
            }
        }

        int index = (int)( Math.random()*weighted.size() );

        Problem problem = weighted.get(index);
        DisplayItem disproblem = new DisplayItem(problem.getText(), problem.getprobID());

        return new ProblemSet(disproblem, answerChoice(problem));
    }



    public DisplayItem getReward() {
        List<DisplayItem> rewards = this.getRewards();
        int index = (int)( Math.random()*rewards.size() );
        return rewards.get(index);
    }

    //TODO - change this to parse from the XML file
    public List<DisplayItem> getRewards() {
        //get this list from the XML file instead - only using the url for now -- should be the value from the xml
        //don't forget that we also need a way to fetch a List of all the files so the downloader can do it's thing
        List<DisplayItem> rewards = new ArrayList<DisplayItem>();

        rewards.add(new DisplayItem(DisplayItem.MediaType.VIDEO, "34", "Big Buck Bunny",
                "f13004eed4251c602bbe15737e8a1ecb",
                "543a4ad9fef4c9e0004ec9482cb7225c2574b0f889291e8270b1c4d61dbc1ab8"));

        return rewards;
    }

    //TODO - The answer needs to be worked on, the parsing is missing Response object.
    public List<DisplayItem> answerChoice(Problem problem)
    {
        //get these from the XML file instead
        List<DisplayItem> responses = new ArrayList<DisplayItem>(4);
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "45"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "af"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "4v"));
        responses.add(new DisplayItem(RandomStringUtils.randomAlphabetic(1), "1m"));

        return responses;
    }


}