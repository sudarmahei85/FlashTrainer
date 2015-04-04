import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * Response class that creates response object which I want to store the response group,
 * groupID, response text & responseID. It needs to be modified to 
 */

public class Responses {

	private String groupName;
	private String groupID;
	private List<String> responseID;
	private List<String> responseslist;
	
	public Responses(){
		}
	
	public Responses(String groupName, String groupID, List<String> responseslist, List<String> responseID ){
		this.groupName = groupName;
		this.groupID = groupID;
		this.responseslist = responseslist;
		this.responseID = responseID;

	}
	
	public String getGroupName(){
		return groupName;
	}
	public String getGroupID(){
		return groupID;
	}
	public List<String> getResponse(){
		return responseslist;
	}
	public List<String> getResponseID(){
		return responseID;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Group Name: " + getGroupName());
		sb.append("\n");
		sb.append("Group ID: " + getGroupID());
		sb.append("\n");
		sb.append("Text: " + getResponse());
		sb.append("\n");
		sb.append("Item ID: " + getResponseID());

		return sb.toString();
	}


}
