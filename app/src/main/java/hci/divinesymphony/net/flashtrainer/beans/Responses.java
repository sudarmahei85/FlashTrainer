import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * Response class that creates response object which I want to store the response group,
 * groupID, response text & responseID. It needs to be modified to 
 */

public class Responses {

	private final String groupName;
	private final String groupID;
	private final List<String> responseID;
	private final List<String> responseslist;

	public Responses(String groupName, String groupID, List<String> responseslist, List<String> responseID ){
		this.groupName = groupName;
		this.groupID = groupID;
		this.responseslist = responseslist;
		this.responseID = responseID;

	}
	
	public String getGroupName(){
		return this.groupName;
	}
	public String getGroupID(){
		return this.groupID;
	}
	public List<String> getResponse(){
		return this.responseslist;
	}
	public List<String> getResponseID(){
		return this.responseID;
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
