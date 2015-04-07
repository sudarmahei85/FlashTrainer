package hci.divinesymphony.net.flashtrainer.beans;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by rick on 3/8/15.
 */
public class ProblemSet {

    private final Problem problem;
    private final List<DisplayItem> responses;

    public ProblemSet(Problem problem, List<DisplayItem> responses) {
        this.problem = problem;
        this.responses = new ArrayList<DisplayItem>(responses.size());
        this.responses.addAll(responses);
    }

    public Problem getProblem() {
        return problem;
    }

    public List<DisplayItem> getResponses() {
        return this.responses;
    }
}
