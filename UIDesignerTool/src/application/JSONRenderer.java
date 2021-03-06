package application;

import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class JSONRenderer {
	private ArrayList<Button> buttons;
	private ArrayList<Label> labels;
	private ArrayList<TextField> textFields;
	
	public JSONRenderer(ArrayList<Button> buttons, ArrayList<Label> labels, ArrayList<TextField> textFields) {
		this.buttons = buttons;
		this.labels = labels;
		this.textFields = textFields;
	}
	
	public String render() {
		String code = "";
		
		code += "{\n";
		
		if(buttons.size() != 0) {
			code += "\t \"buttons\" : [\n";
			code += renderButtons();
			code += "\t]\n";
		}
		
		if(labels.size() != 0) {
			code += ",\t \"label\" : [\n";
			code += renderLabels();
			code += "\t]\n";
		}
		
		if(textFields.size() != 0) {
			code += ",\t \"textfield\" : [\n";
			code += renderTextFields();
			code += "\t]\n";
		}
		
		code += "}";
		
		return code;
	}
	
	public String renderButtons() {
		String code = "";
		
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
	        double x = btn.getTranslateX();
	        double y = btn.getTranslateY();
	        double width = btn.getWidth();
	        double height = btn.getHeight();
			
			code += "\t{\n";
			code += "\t\t \"text\" : \""+ btn.getText() +"\",\n";
			code += "\t\t \"width\" : "+ width +",\n";
			code += "\t\t \"height\" : "+ height +",\n";
			code += "\t\t \"xPos\" : "+ x +",\n";
			code += "\t\t \"yPos\" : "+ y +"\n";
			code += "\t}";
			if(!(i == buttons.size()-1)) code += ",";
			code += "\n";
		}
		
		return code;
	}
	
	public String renderLabels() {
		String code = "";
		
		for(int i = 0; i < labels.size(); i++) {
			Label lbl = labels.get(i);
	        double x = lbl.getTranslateX();
	        double y = lbl.getTranslateY();
	        double width = lbl.getWidth();
	        double height = lbl.getHeight();
			
	        code += "\t{\n";
			code += "\t\t \"text\" : \""+ lbl.getText() +"\",\n";
			code += "\t\t \"width\" : "+ width +",\n";
			code += "\t\t \"height\" : "+ height +",\n";
			code += "\t\t \"xPos\" : "+ x +",\n";
			code += "\t\t \"yPos\" : "+ y +"\n";
			code += "\t}";
			if(!(i == labels.size()-1)) code += ",";
			code += "\n";
		}
		
		return code;
	}
	
	public String renderTextFields() {
		String code = "";
		
		for(int i = 0; i < textFields.size(); i++) {
			TextField tf = textFields.get(i);
			double x = tf.getTranslateX();
	        double y = tf.getTranslateY();
	        double width = tf.getWidth();
	        double height = tf.getHeight();
			
	        code += "\t{\n";
			code += "\t\t \"text\" : \""+ tf.getPromptText() +"\",\n";
			code += "\t\t \"width\" : "+ width +",\n";
			code += "\t\t \"height\" : "+ height +",\n";
			code += "\t\t \"xPos\" : "+ x +",\n";
			code += "\t\t \"yPos\" : "+ y +"\n";
			code += "\t}";
			if(!(i == textFields.size()-1)) code += ",";
			code += "\n";
		}
		
		return code;
	}
}
