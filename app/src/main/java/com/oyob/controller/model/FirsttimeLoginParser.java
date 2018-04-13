package com.oyob.controller.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FirsttimeLoginParser {
	public static int first_login_id = 0;
	public static String firsttimeloginresponce;

	public void IdParser(String res) {

		try {
			String _node, _element;

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db;

			db = factory.newDocumentBuilder();

			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(res));
			Document doc = db.parse(inStream);
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("*");
			_node = new String();
			_element = new String();

			for (int i = 0; i < list.getLength(); i++) {
				Node value = list.item(i).getChildNodes().item(0);
				_node = list.item(i).getNodeName();
				if (value != null) {
					_element = value.getNodeValue();
					updateField(_node, _element);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateField(String _node, String _element) {
		if (_node.equals("first_status")) {
			try {
				if (_element.equalsIgnoreCase("FAILURE")) {
					firsttimeloginresponce = "TRUE";
				} else {
					firsttimeloginresponce = "FALSE";
					first_login_id = Integer.parseInt(_element);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
