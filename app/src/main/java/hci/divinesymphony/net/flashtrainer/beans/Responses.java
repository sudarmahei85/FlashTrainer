package hci.divinesymphony.net.flashtrainer.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 *
 * Response class that creates response object which I want to store the response group,
 * groupID, response text & responseID. It needs to be modified to
 */

public class Responses {

    private String groupName;
    private Integer groupID;
    private HashMap<Integer,String> response;


    public Responses(){
    }

    public Responses(String groupName, Integer groupID, HashMap<Integer,String> response){
        this.groupName = groupName;
        this.groupID = groupID;
        this.response = response;

    }

    public String getGroupName(){
        return groupName;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public HashMap<Integer, String> getResponse() {
        return response;
    }

    public void setResponse(HashMap<Integer, String> response) {
        this.response = response;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Group Name: " + getGroupName());
        sb.append("\n");
        sb.append("Group ID: " + getGroupID());
        sb.append("\n");
        sb.append("Text: " + getResponse());
        sb.append("\n");

        return sb.toString();
    }


}