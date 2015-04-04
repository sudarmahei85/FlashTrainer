package hci.divinesymphony.net.flashtrainer.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import hci.divinesymphony.net.flashtrainer.beans.DisplayItem;
import hci.divinesymphony.net.flashtrainer.beans.Problem;
import hci.divinesymphony.net.flashtrainer.beans.Reward;

public class DomParser {

    private final List<Problem> questions = new ArrayList<Problem>();
    private final List<DisplayItem> rewards = new ArrayList<DisplayItem>();
	private Document dom;
    private final InputStream is;

    public DomParser(String xmlFile) throws IOException {
        this(new FileInputStream(xmlFile));
    }

    public DomParser(InputStream is) {
        this.is = is;
        this.parseXmlFile();
        this.parseDocumentProblem();
        this.parseDocumentRewards();
    }

/*
	public void runExample() {
		
		//parse the xml file and get the dom object
		parseXmlFile();
		
		//get each element and create an object
		parseDocument();
		
		//Iterate through the list and print the data
		printData();
	}
*/

    public List<Problem> getQuestions(){
        if (this.questions.size() == 0) {
            throw new RuntimeException("The problem set is empty!");
        }

        return this.questions;
    }
	//Taken from http://www.java-samples.com/showtutorial.php?tutorialid=152
	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(this.is);

		}catch(ParserConfigurationException e) {
			throw new RuntimeException(e);
		}catch(SAXException e) {
            throw new RuntimeException(e);
		}catch(IOException e) {
            throw new RuntimeException(e);
		}
	}

	private void parseDocumentProblem(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <Problem> elements
		NodeList nl = docEle.getElementsByTagName("problem");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
							
				Element el = (Element)nl.item(i);
						
				Problem e = getProblem(el);
				
				//add it to list
				questions.add(e);
			}
		}
	}
	
	private void parseDocumentRewards(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <Problem> elements
		NodeList nl = docEle.getElementsByTagName("reward");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
							
				Element el = (Element)nl.item(i);
				
				DisplayItem reward = getReward(el);
				
				//add it to list
				rewards.add(reward);
			}
		}
	}

	private Problem getProblem(Element El) {
		
		//for each <problem> element get text or int values of 
		//name ,id, age and name
		String text = getTextValue(El,"text");
		//String probIDs = getTextValue(El,"probIDs");
		String audio = getTextValue(El,"audio");
		String image = getTextValue(El,"image");

		String probID = El.getAttribute("probid");
		
		int weight = Integer.parseInt(El.getAttribute("weight"));
		
		
		//Create a new Problem with the value read from the xml nodes
		Problem e = new Problem(text,audio,image,probID,weight);
		
		return e;
	}

    private DisplayItem parseMultimediaTag(Element element) {
        DisplayItem.MediaType type;
        if ("video".equals(element.getTagName())) {
            type = DisplayItem.MediaType.VIDEO;
        } else if ("image".equals(element.getTagName())) {
            type = DisplayItem.MediaType.IMAGE;
        } else if ("sound".equals(element.getTagName())) {
            type = DisplayItem.MediaType.SOUND;
        } else {
            throw new IllegalArgumentException("No recognized multimedia tag at this location");
        }
        String sha256 = getTextValue(element,"sha256sum");
        String guid = getTextValue(element, "guid");
        return new DisplayItem(type, null, null, guid, sha256);
    }

	private DisplayItem getReward(Element el) {
        DisplayItem result = null;
        Node node = el.getFirstChild();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            result = parseMultimediaTag(element);
        }
        return result;
	}

	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <problem><text>Turtle</text></problem> xml snippet if
	 * the Element points to problem text tag  I will return Turtle  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

    /**
	 * Iterate through the list and print the 
	 * content to console
	 */
	/* private void printData(){
		
		System.out.println("No of Questions '" + questions.size() + "'.");
		
		Iterator it = questions.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}

	
	public static void main(String[] args){
		//create an instance
		DomParser dpe = new DomParser("");
		
		//call run example
		dpe.runExample();
		
	}

*/

}
