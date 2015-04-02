package hci.divinesymphony.net.flashtrainer.beans;//TODO figure out what this should be used for if anything and comment, or remove

public class Reward {

	private String sha1sum;
	private String guid;

	public Reward(String sha1sum, String guid) {
		this.sha1sum = sha1sum;
		this.guid = guid;
	}
	
	public String getSha1() {
		return sha1sum;
	}

	public String getGuid() {
		return guid;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Sha1: " + getSha1());
		sb.append("\n");
		sb.append("GUID: " + getGuid());
		return sb.toString();
	}

}
