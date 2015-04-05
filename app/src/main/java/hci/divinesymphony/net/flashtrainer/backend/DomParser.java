package hci.divinesymphony.net.flashtrainer.backend;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    public List<DisplayItem> getMedia() {
        List<DisplayItem> list = new ArrayList<DisplayItem>(this.questions.size());
        for (Problem question : this.questions) {
            list.add(question.getContent());
        }
        return list;
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

                rewards.add(reward);
			}
		}
	}

	private Problem getProblem(Element El) {
        DisplayItem item;

        //parse misc items
        String probID = El.getAttribute("probid");
        int weight = Integer.parseInt(El.getAttribute("weight"));

        //parse the text description
        String text = getTextValue(El,"text");
        if (text != null && text.isEmpty()) {
            text = null;
        }
        //parse the multimedia content
        Element element = findChildByTagName(El, "image");
        if (element != null) {
            item = this.parseMultimediaTag(element, text, probID);
        } else {
            item = new DisplayItem(text, probID);
        }

		return new Problem(item, weight);
	}

    private Element findChildByTagName(Element parent, String tagName) {
        NodeList nodes = parent.getChildNodes();
        Element node = null;
        for (int i=nodes.getLength()-1; i >= 0; i-- ) {
            Node tmpNode = nodes.item(i);
            if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                node = (Element) tmpNode;
                if (tagName.equals(node.getTagName())) {
                    break;
                } else {
                    node = null;
                }
            }
        }
        return node;
    }

    private DisplayItem parseMultimediaTag(Element element) {
        return this.parseMultimediaTag(element, null, null);
    }

    private DisplayItem parseMultimediaTag(Element element, String text, String id) {
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
        return new DisplayItem(type, id, text, guid, sha256);
    }

	private DisplayItem getReward(Element el) {
        DisplayItem result = null;

        NodeList nodes = el.getChildNodes();
        Element node = null;
        for (int i=nodes.getLength()-1; i >= 0; i-- ) {
            Node tmpNode = nodes.item(i);
            if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                node = (Element) tmpNode;
            }
        }

        if (node != null) {
            Log.v(this.getClass().getName(), "Attempting to parse a multimedia element");
            result = parseMultimediaTag(node);
        } else {
            Log.w(this.getClass().getName(), "Skipping, not an element");
        }
        return result;
	}

    public List<DisplayItem> getRewards() {
        return Collections.unmodifiableList(this.rewards);
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
