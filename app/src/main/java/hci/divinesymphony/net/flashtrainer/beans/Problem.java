package hci.divinesymphony.net.flashtrainer.beans;


import java.lang.String;
import java.lang.StringBuffer;

public class Problem {

	private final int weight;
    private final DisplayItem item;
	
	public Problem(DisplayItem item, int weight) {
		this.item = item;
		this.weight = weight;
	}

    public DisplayItem getContent() {
        return this.item;
    }

	public int getWeight() {
		return this.weight;
	}

}
