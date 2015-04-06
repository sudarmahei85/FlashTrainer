package hci.divinesymphony.net.flashtrainer.beans;


import java.lang.String;
import java.lang.StringBuffer;

public class Problem {

	private final int weight;
    private final Integer answerId;
    private final Integer groupId;
    private final DisplayItem item;
	
	public Problem(DisplayItem item, int weight,int answerId,int groupId) {
		this.item = item;
		this.weight = weight;
        this.answerId= answerId;
        this.groupId= groupId;
	}

    public DisplayItem getContent() {
        return this.item;
    }

	public int getWeight() {
		return this.weight;
	}

    public Integer getAnswerId() {
        return answerId;
    }

    public Integer getGroupId() {
        return groupId;
    }
}
