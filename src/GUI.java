import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.effect.InnerShadow;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import java.io.*;
import java.util.*;
import javafx.geometry.*;

public class GUI extends Application{

  //standard window dimensions
  private int winX = 300;
  private int winY = 200;

  //list of programs
  private ArrayList<Program> programList = new ArrayList<Program>();

  //strings of program components, used to display in the text fields during editing and possibly to view program
  private String viewID = "";
  private String viewTitle = "";
  private String viewDesc = "";
  private String viewDepart = "";
  private String viewElect = "";
  private String viewRequire = "";

  //program selected to edit saved for editing scene
  Program programToEdit;

  public void start(Stage primaryStage){

    //gridpane and scene for the main menu

    GridPane menuGrid = new GridPane();
    menuGrid.setHgap(10);
    menuGrid.setVgap(10);
    menuGrid.setPadding(new Insets(20,20,20,20));

    Button listProgramsButton = new Button();
    listProgramsButton.setText("View Programs");
    menuGrid.add(listProgramsButton, 0, 0);

    Button addProgramButton = new Button();
    addProgramButton.setText("Add a New Program");
    menuGrid.add(addProgramButton, 0, 1);

    Button editProgramButton = new Button();
    editProgramButton.setText("Edit a Program");
    menuGrid.add(editProgramButton, 0, 2);

    Button listCoursesButton = new Button();
    listCoursesButton.setText("View Courses");
    menuGrid.add(listCoursesButton, 1, 0);

    Button editCourseButton = new Button();
    editCourseButton.setText("Edit a Course");
    menuGrid.add(editCourseButton, 1, 2);

    Button addCourseButton = new Button();
    addCourseButton.setText("Add a New Course");
    menuGrid.add(addCourseButton, 1, 1);

    Button quitButton = new Button();
    quitButton.setText("Exit and Save");
    menuGrid.add(quitButton, 0, 3);

    Scene menuScene = new Scene(menuGrid, winX, winY);
    primaryStage.setTitle("Program and Course Management");
    primaryStage.setScene(menuScene);
    primaryStage.show();

    //vbox and scene for the viewing programs

    VBox listProgramBox = new VBox();
    listProgramBox.setAlignment(Pos.CENTER);
    listProgramBox.setSpacing(10);

    ChoiceBox<String> programListChoiceBox = new ChoiceBox<String>();
    for (int i = 0; i < programList.size(); i++){
      Program currentProgram = programList.get(i);
      String s = currentProgram.getProgramID();
      programListChoiceBox.getItems().add(s);
    }

    Button viewProgramButton = new Button();
    viewProgramButton.setText("View Program");

    Button backButton = new Button();
    backButton.setText("Back to Menu");

    listProgramBox.getChildren().addAll(programListChoiceBox, viewProgramButton, backButton);

    Scene listProgramsScene = new Scene(listProgramBox, winX, winY);

    //view program scene, currently not using this scene, viewing program just displays to console

    VBox viewProgramBox = new VBox();
    viewProgramBox.setAlignment(Pos.CENTER);
    viewProgramBox.setSpacing(10);

    Label viewidLbl = new Label("ID: " + viewID);

    Label viewtitleLbl = new Label("Title: " + viewTitle);

    Label viewdescLbl = new Label("Description: " + viewDesc);

    Label viewdepartLbl = new Label("Departments: " + viewDepart);

    Label viewelectLbl = new Label("Elective Courses: " + viewDepart);

    Label viewrequireLbl = new Label("Required Courses: " + viewRequire);

    Button backToListProgramBoxButton = new Button();
    backToListProgramBoxButton.setText("Back");

    viewProgramBox.getChildren().addAll(viewidLbl, viewtitleLbl, viewdescLbl, viewdepartLbl, viewelectLbl, viewrequireLbl, backToListProgramBoxButton);
    Scene viewProgramScene = new Scene(viewProgramBox, winX, winY);

    //vbox and scene for adding a program

    VBox addProgramBox = new VBox();
    addProgramBox.setAlignment(Pos.CENTER);
    addProgramBox.setSpacing(10);

    Label idLbl = new Label("Enter ID of the Program");
    TextField idTxt = new TextField();

    Label titleLbl = new Label("Enter Title of the Program");
    TextField titleTxt = new TextField();

    Label descLbl = new Label("Enter Description of the Program");
    TextField descTxt = new TextField();

    Label departLbl = new Label("Enter the Departments the Program Belongs to Separated by Commas");
    TextField departTxt = new TextField();

    Label electLbl = new Label("Enter the Elective Courses the Program Offers Separated by Commas");
    TextField electTxt = new TextField();

    Label requireLbl = new Label("Enter the Required Courses of the Program Separated by Commas");
    TextField requireTxt = new TextField();

    Button enterNewProgramButton = new Button();
    enterNewProgramButton.setText("Add New Program");

    addProgramBox.getChildren().addAll(idLbl, idTxt, titleLbl, titleTxt, descLbl, descTxt, departLbl, departTxt, electLbl, electTxt, requireLbl, requireTxt, enterNewProgramButton);

    Scene addProgramScene = new Scene(addProgramBox, 500, 500);

    //vbox and scene for choosing which program to edit

    VBox editProgramBox = new VBox();
    editProgramBox.setAlignment(Pos.CENTER);
    editProgramBox.setSpacing(10);

    ChoiceBox<String> editProgramListChoiceBox = new ChoiceBox<String>();
    for (int i = 0; i < programList.size(); i++){
      Program currentProgram = programList.get(i);
      String s = currentProgram.getProgramID();
      editProgramListChoiceBox.getItems().add(s);
    }

    Button editThisProgramButton = new Button();
    editThisProgramButton.setText("Edit Program");

    Button editBackButton = new Button();
    editBackButton.setText("Back to Menu");

    editProgramBox.getChildren().addAll(editProgramListChoiceBox, editThisProgramButton, editBackButton);

    Scene editProgramScene = new Scene(editProgramBox, winX, winY);

    //vbox and scene where actual editing of program happens

    VBox editThisProgramBox = new VBox();
    editThisProgramBox.setAlignment(Pos.CENTER);
    editThisProgramBox.setSpacing(10);

    Label editidLbl = new Label("Edit ID of the Program");
    TextField editidTxt = new TextField(viewID);

    Label edittitleLbl = new Label("Edit Title of the Program");
    TextField edittitleTxt = new TextField(viewTitle);

    Label editdescLbl = new Label("Edit Description of the Program");
    TextField editdescTxt = new TextField(viewDesc);

    Label editdepartLbl = new Label("Edit the Departments the Program Belongs to Separated by Commas");
    TextField editdepartTxt = new TextField(viewDepart);

    Label editelectLbl = new Label("Edit the Elective Courses the Program Offers Separated by Commas");
    TextField editelectTxt = new TextField(viewElect);

    Label editrequireLbl = new Label("Edit the Required Courses of the Program Separated by Commas");
    TextField editrequireTxt = new TextField(viewRequire);

    Button submitEditButton = new Button();
    submitEditButton.setText("Submit Edits");

    editThisProgramBox.getChildren().addAll(editidLbl, editidTxt, edittitleLbl, edittitleTxt, editdescLbl, editdescTxt, editdepartLbl, editdepartTxt, editelectLbl, editelectTxt, editrequireLbl, editrequireTxt, submitEditButton);

    Scene editThisProgramScene = new Scene(editThisProgramBox, 500, 500);



    //all the button actions

    //this button changes to the list programs scene
    listProgramsButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(listProgramsScene);
      }
    });

    //this button changes the scene back to the main menu
    backButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(menuScene);
      }
    });

    //this button changes the scene to the add program scene
    addProgramButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(addProgramScene);
      }
    });

    //this button takes the inputs from the text fields and creates a new program, changes scene back to main menu
    enterNewProgramButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        Program program = new Program();

        program.setProgramID(idTxt.getText());
        program.setProgramTitle(titleTxt.getText());
        program.setProgramDesc(descTxt.getText());

        String[] departments = departTxt.getText().split(", ");
    		for (String s: departments)
    			program.addDepartment(s);

        String[] electives = electTxt.getText().split(", ");
      	for (String s: electives)
      		program.add_Elective(s);

        String[] requiredCourses = requireTxt.getText().split(", ");
      	for (String s: requiredCourses)
      		program.addRequiredCourses(s);

        programList.add(program);

        start(primaryStage);
      }
    });

    //this button currently displays the selected program to console, should be changed to displaying the program within the GUI
    viewProgramButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        String currentProgram = programListChoiceBox.getValue();
        for(int i = 0 ; i < programList.size() ; i++){
          if(programList.get(i).getProgramID() == currentProgram){
            programList.get(i).displayProgram();

          }
        }

        /* temporarily just displaying to console instead
        for(int i = 0 ; i < programList.size() ; i++){
          if(programList.get(i).getProgramID() == currentProgram){
            viewID = programList.get(i).getProgramID();
            viewTitle = programList.get(i).getProgramTitle();
            viewDesc = programList.get(i).getProgramDesc();


            /*for(String d: programList.get(i).getDepartments())
              System.out.print(d + " ");



            viewID = programList.get(i).getProgramID();
            viewID = programList.get(i).getProgramID();
          }
        }
        primaryStage.setScene(viewProgramScene); */

      }
    });

    //this button changes scene back to listing the program scene, not in use currenty
    backToListProgramBoxButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(listProgramsScene);
      }
    });

    //this button exits the program, I'm guessing this button could also be when file i/o is implemented
    quitButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        //file i/o stuff??
        System.exit(0);
      }
    });

    //this button changes the scene to the edit program scene
    editProgramButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(editProgramScene);
      }
    });

    //this button changes the scene from the edit program to the main menu
    editBackButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(menuScene);
      }
    });

    //this button takes the selected program from the choice box, and puts the components of the program into variables to be used in the edit this program scene, also switches scenes to edit this program scene
    editThisProgramButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        String currentProgram = editProgramListChoiceBox.getValue();
        for(int i = 0 ; i < programList.size() ; i++){
          if(programList.get(i).getProgramID() == currentProgram){
            programToEdit = programList.get(i);
            viewID = programList.get(i).getProgramID();

            viewTitle = programList.get(i).getProgramTitle();
            viewDesc = programList.get(i).getProgramDesc();
          }
        }

        ArrayList<String> departments = programToEdit.getDepartments();
        for (String s: departments)
          viewDepart = viewDepart + s;

        ArrayList<String> electives = programToEdit.getElectives();
        for (String s: electives)
          viewElect = viewElect + s;

        ArrayList<String> required = programToEdit.getRequiredCourses();
        for (String s: required)
          viewRequire = viewRequire + s;

        //this is the exact same VBox and scene that was already created
        //it is here again so that the textfields contain the components of the program to be edited
        //im sure there is a better way to do this, but idk how, this works for now
        //with this bit not commented out however, the submitEditButton will not work
        /*VBox editThisProgramBox = new VBox();
        editThisProgramBox.setAlignment(Pos.CENTER);
        editThisProgramBox.setSpacing(10);

        Label editidLbl = new Label("Edit ID of the Program");
        TextField editidTxt = new TextField(viewID);

        Label edittitleLbl = new Label("Edit Title of the Program");
        TextField edittitleTxt = new TextField(viewTitle);

        Label editdescLbl = new Label("Edit Description of the Program");
        TextField editdescTxt = new TextField(viewDesc);

        Label editdepartLbl = new Label("Edit the Departments the Program Belongs to Separated by Commas");
        TextField editdepartTxt = new TextField(viewDepart);

        Label editelectLbl = new Label("Edit the Elective Courses the Program Offers Separated by Commas");
        TextField editelectTxt = new TextField(viewElect);

        Label editrequireLbl = new Label("Edit the Required Courses of the Program Separated by Commas");
        TextField editrequireTxt = new TextField(viewRequire);

        Button submitEditButton = new Button();
        submitEditButton.setText("Submit Edits");

        editThisProgramBox.getChildren().addAll(editidLbl, editidTxt, edittitleLbl, edittitleTxt, editdescLbl, editdescTxt, editdepartLbl, editdepartTxt, editelectLbl, editelectTxt, editrequireLbl, editrequireTxt, submitEditButton);

        Scene editThisProgramScene = new Scene(editThisProgramBox, 500, 500);*/

        start(primaryStage);start(primaryStage);start(primaryStage);
        primaryStage.setScene(editThisProgramScene);
      }
    });

    //this button takes the changed inputs and edits the current program, back to menu scene
    submitEditButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        programToEdit.setProgramID(editidTxt.getText());
        programToEdit.setProgramTitle(edittitleTxt.getText());
        programToEdit.setProgramDesc(editdescTxt.getText());

        programToEdit.getDepartments();

        programToEdit.removeAllRequiredCourses();
        programToEdit.removeAllElectives();
        programToEdit.removeAllDepartments();

        String[] departments = editdepartTxt.getText().split(", ");
    		for (String s: departments)
    			programToEdit.addDepartment(s);

        String[] electives = editelectTxt.getText().split(", ");
      	for (String s: electives)
      		programToEdit.add_Elective(s);

        String[] requiredCourses = editrequireTxt.getText().split(", ");
      	for (String s: requiredCourses)
      		programToEdit.addRequiredCourses(s);

        start(primaryStage);
      }
    });
  }
}