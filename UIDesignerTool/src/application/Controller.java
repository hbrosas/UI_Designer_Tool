package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class Controller implements Initializable {
	private TextInputDialog dialog;
	private ArrayList<Button> buttons;
	private ArrayList<Label> labels;
	private ArrayList<TextField> textFields;
	private boolean toggleRender;
	private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private Button tcBtn;
    private Label tcLbl;
    private TextField tcTxtFld;
	
	@FXML
	private Button addButtonBtn, addLabelBtn, addTxtFldBtn, clearBtn, renderBtn;
	
	@FXML
	private Label rcLbl;
	
	@FXML
	private TextArea rcTextArea;
	
	@FXML
	private Pane workspace; 
	
	@FXML
	private TextField tcField, textTF, widthTF, heightTF;
	
	@FXML
	private TitledPane Layout;
  
   @Override
   public void initialize(URL location, ResourceBundle resources) {
	   buttons = new ArrayList<Button>();
	   labels = new ArrayList<Label>();
	   textFields = new ArrayList<TextField>();
	   toggleRender = false;
	   
	   rcLbl.setVisible(false);
	   rcTextArea.setVisible(false);
	   inputDialog();
	   
	   Layout.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				// TODO Auto-generated method stub
				textTF.setText("");
				widthTF.setText("0");
				heightTF.setText("0");
			}
       });
   }
   
   public void inputDialog() {
	   dialog = new TextInputDialog();
	   dialog.setTitle("Save File");
	   dialog.setHeaderText("Enter filename: (Should contain .json ex. hello.json)");
   }
   
   public void saveFile(ActionEvent event) {
       System.out.println("Save");
       
       Optional<String> result = dialog.showAndWait();
	   String filename = "none.";

	   if (result.isPresent()) {
	       filename = result.get();
	       if(filename.contains(".json")) {
	    	   JSONRenderer jsr = new JSONRenderer(buttons, labels, textFields);
	    	   String text = jsr.render();
			   try (FileWriter file = new FileWriter(filename)) {
					file.write(text);
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       } else {
	    	   Alert alert = new Alert(AlertType.ERROR);
	    	   alert.setTitle("Error");
	    	   alert.setContentText("File not saved. Must be .json");
	    	   alert.showAndWait();	    	   
	       }
	   }
   }
   
   public void loadFile(ActionEvent event) {
       System.out.println("Load");
       FileChooser fileChooser = new FileChooser();
       File selectedFile = fileChooser.showOpenDialog(null);

       if (selectedFile != null) {
    	   if(selectedFile.getName().contains(".json")) {
    		   try (FileReader reader = new FileReader(selectedFile)) {
    			   JSONParser parser = new JSONParser();
    			   
    			   try {
    		            Object obj = parser.parse(reader);

    		            JSONObject jsonObject = (JSONObject) obj;
    		            System.out.println(jsonObject.toJSONString());
    		            
    		            // BUTTONS
    		            JSONArray buttons = (JSONArray) jsonObject.get("buttons");
    		            System.out.println(buttons);
    		            Iterator<JSONObject> iterator = buttons.iterator();
    		            while (iterator.hasNext()) {
    		                JSONObject jobj = iterator.next();
    		                String text = (String) jobj.get("text");
    		                double width = (double) jobj.get("width");
    		                double height = (double) jobj.get("height");
    		                double xPos = (double) jobj.get("xPos");
    		                double yPos = (double) jobj.get("yPos");
    		                addButtonLoad(text, width, height, xPos, yPos);
    		            }
    		            
    		            // LABELS
    		            JSONArray labels = (JSONArray) jsonObject.get("label");
    		            System.out.println(labels);
    		            iterator = labels.iterator();
    		            while (iterator.hasNext()) {
    		                JSONObject jobj = iterator.next();
    		                String text = (String) jobj.get("text");
    		                double width = (double) jobj.get("width");
    		                double height = (double) jobj.get("height");
    		                double xPos = (double) jobj.get("xPos");
    		                double yPos = (double) jobj.get("yPos");
    		                addLabelLoad(text, width, height, xPos, yPos);
    		            }
    		            
    		            // TEXTFIELDS
    		            JSONArray tfs = (JSONArray) jsonObject.get("textfield");
    		            System.out.println(tfs);
    		            iterator = tfs.iterator();
    		            while (iterator.hasNext()) {
    		                JSONObject jobj = iterator.next();
    		                String text = (String) jobj.get("text");
    		                double width = (double) jobj.get("width");
    		                double height = (double) jobj.get("height");
    		                double xPos = (double) jobj.get("xPos");
    		                double yPos = (double) jobj.get("yPos");
    		                addTextFieldLoad(text, width, height, xPos, yPos);
    		            }
    		            
    		        } catch (ParseException e) {
    		            e.printStackTrace();
    		        }
    			   
    		   } catch (FileNotFoundException e) {
    			   e.printStackTrace();
    		   } catch (IOException e) {
    			   e.printStackTrace();
    		   }
    	   } else {
    		   Alert alert = new Alert(AlertType.ERROR);
	    	   alert.setTitle("Error");
	    	   alert.setContentText("File invalid.");
	    	   alert.showAndWait();	 
    	   }
       }
       else {
           System.out.println("File selection cancelled.");
       }
   }

   public void addButtonLoad(String text, double width, double height, double xPos, double yPos) {
       System.out.println("Add Button Load");
       System.out.println("xpos: " + xPos);
       Button button = new Button(text);
       button.setPrefWidth(width);
       button.setPrefHeight(height);
       button.setTranslateX(xPos);
       button.setTranslateY(yPos);
       button.setOnMousePressed(buttonOnMousePressedEventHandler);
       button.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
       buttons.add(button);
       workspace.getChildren().add(button);
   }
   
   public void addLabelLoad(String text, double width, double height, double xPos, double yPos) {
       System.out.println("Add Label Load");
       Label label = new Label(text);
       label.setPrefWidth(width);
       label.setPrefHeight(height);
       label.setTranslateX(xPos);
       label.setTranslateY(yPos);
       label.setOnMousePressed(labelOnMousePressedEventHandler);
       label.setOnMouseDragged(labelOnMouseDraggedEventHandler);
       labels.add(label);
       workspace.getChildren().add(label);
   }
   
   public void addTextFieldLoad(String text, double width, double height, double xPos, double yPos) {
       System.out.println("Add TextFieeeld!");
       
       TextField tf = new TextField();
       tf.setPromptText(text);
       tf.setPrefWidth(width);
       tf.setPrefHeight(height);
       tf.setTranslateX(xPos);
       tf.setTranslateY(yPos);
       tf.setOnMousePressed(textFieldOnMousePressedEventHandler);
       tf.setOnMouseDragged(textFieldOnMouseDraggedEventHandler);
       textFields.add(tf);
       workspace.getChildren().add(tf);
   }
   
   public void addButton(ActionEvent event) {
       System.out.println("Add Button");
       
       Button button = new Button("btn" + buttons.size());
       button.setOnMousePressed(buttonOnMousePressedEventHandler);
       button.setOnMouseDragged(buttonOnMouseDraggedEventHandler);
       buttons.add(button);
       workspace.getChildren().add(button);
   }
   
   public void addLabel(ActionEvent event) {
       System.out.println("Add Label");
       
       Label label = new Label("label " + labels.size());
       label.setOnMousePressed(labelOnMousePressedEventHandler);
       label.setOnMouseDragged(labelOnMouseDraggedEventHandler);
       labels.add(label);
       workspace.getChildren().add(label);
   }
   
   public void addTextField(ActionEvent event) {
       System.out.println("Add TextFieeeld!");
       
       TextField tf = new TextField();
       tf.setPromptText("Text Field " + textFields.size());
       tf.setOnMousePressed(textFieldOnMousePressedEventHandler);
       tf.setOnMouseDragged(textFieldOnMouseDraggedEventHandler);
       textFields.add(tf);
       workspace.getChildren().add(tf);
   }
   
   public void clearScreen(ActionEvent event) {
       System.out.println("Clear screen!");
       buttons.clear();
       labels.clear();
       textFields.clear();
       workspace.getChildren().clear();
   }
   
   public void renderDesign(ActionEvent event) {
       System.out.println("Render Design");	   
	   if(!toggleRender) {
		   JSONRenderer jsr = new JSONRenderer(buttons, labels, textFields);
		   rcTextArea.setText(jsr.render());
		   rcLbl.setVisible(true);
		   rcTextArea.setVisible(true);
		   workspace.setVisible(false);
		   toggleRender = true;
	   } else {
		   rcLbl.setVisible(false);
		   rcTextArea.setVisible(false);
		   workspace.setVisible(true);
		   toggleRender = false;
	   }
   }
   
   public void updateLayout(ActionEvent event) {
	   System.out.println("Update Layout!!");
	   if(tcBtn != null || tcLbl != null || tcTxtFld != null) {
		   String text = textTF.getText();
		   String width = widthTF.getText();
		   String height = heightTF.getText();
		   
		   if(tcBtn != null)
			   updateBtn(text, Double.parseDouble(width), Double.parseDouble(height));
		   else if (tcLbl != null)
			   updateLbl(text, Double.parseDouble(width), Double.parseDouble(height));
		   else if (tcTxtFld != null)
			   updateTF(text, Double.parseDouble(width), Double.parseDouble(height));
	   }
   }
   
   public void updateBtn(String text, double width, double height) {
	   if(!text.equals(""))
		   tcBtn.setText(text);
	   if(width != 0)
		   tcBtn.setPrefWidth(width);
	   if(height != 0)
		   tcBtn.setPrefHeight(height);
   }
   
   public void updateLbl(String text, double width, double height) {
	   if(!text.equals(""))
		   tcLbl.setText(text);
	   if(width != 0)
		   tcLbl.setPrefWidth(width);
	   if(height != 0)
		   tcLbl.setPrefHeight(height);
   }
   
   public void updateTF(String text, double width, double height) {
	   if(!text.equals(""))
		   tcTxtFld.setPromptText(text);
	   if(width != 0)
		   tcTxtFld.setPrefWidth(width);
	   if(height != 0)
		   tcTxtFld.setPrefHeight(height);
   }
   
   // BUTTON MOUSE EVENTS
   EventHandler<MouseEvent> buttonOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
        	targetComponent((Button)t.getSource(), null, null);
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Button)(t.getSource())).getTranslateX();
            orgTranslateY = ((Button)(t.getSource())).getTranslateY();
        }
    };
    
    EventHandler<MouseEvent> buttonOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((Button)(t.getSource())).setTranslateX(newTranslateX);
            ((Button)(t.getSource())).setTranslateY(newTranslateY);
        }
    };
    
    // LABEL MOUSE EVENTS
    EventHandler<MouseEvent> labelOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
        	targetComponent(null, (Label)t.getSource(), null);
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Label)(t.getSource())).getTranslateX();
            orgTranslateY = ((Label)(t.getSource())).getTranslateY();
        }
    };
    
    EventHandler<MouseEvent> labelOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((Label)(t.getSource())).setTranslateX(newTranslateX);
            ((Label)(t.getSource())).setTranslateY(newTranslateY);
        }
    };
    
    // TEXT FIELD MOUSE EVENTS
    EventHandler<MouseEvent> textFieldOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
        	targetComponent(null, null, (TextField)t.getSource());
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((TextField)(t.getSource())).getTranslateX();
            orgTranslateY = ((TextField)(t.getSource())).getTranslateY();
        }
    };
    
    EventHandler<MouseEvent> textFieldOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((TextField)(t.getSource())).setTranslateX(newTranslateX);
            ((TextField)(t.getSource())).setTranslateY(newTranslateY);
        }
    };
    
    public void targetComponent(Button btn, Label lbl, TextField tf) {
    	tcBtn = btn;
    	tcLbl = lbl;
    	tcTxtFld = tf;
    	
    	if(btn != null) {
    		tcField.setText(btn.getText());
    	} else if (lbl != null) {
    		tcField.setText(lbl.getText());
    	} else if (tf != null) {
    		tcField.setText(tf.getPromptText());
    	}
    }
}
