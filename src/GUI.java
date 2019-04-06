import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import java.io.*;
import java.util.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javax.swing.*;

public class GUI extends Application{
	private boolean load = true;
	private boolean loginCheck = true;
	private HashMap<String, String> users = new HashMap<String, String>();
	private HashMap<String, String> userType = new HashMap<String, String>();
  //standard window dimensions
  private int winX = 600;
  private int winY = 400;

  //list of programs
  private ArrayList<Program> programList = new ArrayList<Program>();

  //list of courses
  private ArrayList<Course> courseList = new ArrayList<Course>();

	private Map<String, CheckBox> electivesMap = new HashMap<String, CheckBox>();
	private Map<String, CheckBox> requiredMap = new HashMap<String, CheckBox>();


  //strings of program components, used to display in the text fields during editing and possibly to view program
  private String viewID = "";
  private String viewTitle = "";
  private String viewDesc = "";
  private String viewDepart = "";
  private String viewElect = "";
  private String viewRequire = "";

  //strings of course components, used to display in the text fields during editing and possibly to view course
  private String viewCourseID = "";
  private String viewCourseTitle = "";
  private String viewCourseDesc = "";
  private String viewCourseYear = "";
  private String viewCoursePreReqs = "";
  private String viewCourseMutExs = "";

  //program selected to edit saved for editing scene
  Program programToEdit;

  //Course selected to be edited
  Course courseToEdit;

  @Override
  public void start(Stage primaryStage){
    if(load) {
	  try {
      String fileName = "programSaveGUI.dat";
      File load = new File(fileName);
      FileInputStream in = new FileInputStream(load);
      ObjectInputStream reader = new ObjectInputStream(in);
      ArrayList<Program> loadedProgram = (ArrayList<Program>)reader.readObject();
      reader.close();
      programList = new ArrayList<Program>(loadedProgram);
      fileName = "courseSaveGUI.dat";
      load = new File(fileName);
      in = new FileInputStream(load);
      reader = new ObjectInputStream(in);
      ArrayList<Course> loadedCourse = (ArrayList<Course>)reader.readObject();
      reader.close();
      courseList = new ArrayList<Course>(loadedCourse);
      fileName = "userSaveGUI.dat";
      load = new File(fileName);
      in = new FileInputStream(load);
      reader = new ObjectInputStream(in);
      ArrayList<HashMap<String, String>> loadedUsers = (ArrayList<HashMap<String, String>>)reader.readObject();
      reader.close();
      users = new HashMap<String, String>(loadedUsers.get(0));
      userType = new HashMap<String, String>(loadedUsers.get(1));
      in.close();
    } catch (IOException e) {
      System.out.println("Failed to load. ");
    } catch (ClassNotFoundException e) {
      System.out.println("Class cannot be found. ");
    }
	  load=false;
    }
    //gridpane and scene for the main menu

    GridPane menuGrid = new GridPane();
    menuGrid.setStyle("-fx-background-color:#85c124;");   //change color of background with color hex
    menuGrid.setHgap(10);
    menuGrid.setVgap(10);
    menuGrid.setPadding(new Insets(20,20,20,20));


    Button listProgramsButton = new Button();
    listProgramsButton.setText("View Programs");
    menuGrid.add(listProgramsButton, 0, 1);

    Button addProgramButton = new Button();
    addProgramButton.setText("Add a New Program");
    menuGrid.add(addProgramButton, 0, 2);

    Button editProgramButton = new Button();
    editProgramButton.setText("Edit a Program");
    menuGrid.add(editProgramButton, 0, 3);

    Button listCoursesButton = new Button();
    listCoursesButton.setText("View Courses");
    menuGrid.add(listCoursesButton, 1, 1);

    Button editCourseButton = new Button();
    editCourseButton.setText("Edit a Course");
    menuGrid.add(editCourseButton, 1, 3);

    Button addCourseButton = new Button();
    addCourseButton.setText("Add a New Course");
    menuGrid.add(addCourseButton, 1, 2);

    Button quitButton = new Button();
    quitButton.setText("Exit and Save");
    menuGrid.add(quitButton, 0, 4);

    Image image1 = new Image("/UW_centre-stack_rgb-black.png", true);
    ImageView iv1 = new ImageView();
    iv1.setImage(image1);
    iv1.setFitWidth(150);
    iv1.setPreserveRatio(true);
    iv1.setSmooth(true);
    iv1.setCache(true);
    menuGrid.add(iv1, 0, 0);
    
    ImageView iv1b = new ImageView();
    iv1b.setImage(image1);
    iv1b.setFitWidth(150);
    iv1b.setPreserveRatio(true);
    iv1b.setSmooth(true);
    iv1b.setCache(true);

    Scene menuScene = new Scene(menuGrid, winX, winY);
    
    primaryStage.setScene(menuScene);
    primaryStage.show();

    //scene for students
    GridPane stdGrid = new GridPane();
    stdGrid.setStyle("-fx-background-color:#85c124;");   //change color of background with color hex
    stdGrid.setHgap(10);
    stdGrid.setVgap(10);
    stdGrid.setPadding(new Insets(20,20,20,20));

    stdGrid.add(iv1b, 0, 0);

    Button stdListProgramsButton = new Button("View Programs");
    stdGrid.add(stdListProgramsButton, 0, 1);

    Button stdListCoursesButton = new Button("View Courses");
    stdGrid.add(stdListCoursesButton, 1, 1);

    Button stdQuitButton = new Button("Exit and Save");
    stdGrid.add(stdQuitButton, 0, 4);

    Scene stdScene = new Scene(stdGrid, winX, winY);

    //scene for login
    Button newUser = new Button("New User");
    Button oldUser = new Button("Returning User");
    Button student = new Button("Proceed as Student");

    newUser.setOnAction(new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent event) {
    		Label usernameLbl = new Label("Enter username: ");
    		TextField usernameTxt = new TextField("username");

    		Label passwordLbl = new Label("Enter password: ");
    		TextField passwordTxt = new TextField("password");

    		Label userTypeLbl = new Label("User Type: ");
    		final ComboBox<String> userTypeDrop = new ComboBox<String>();
    		userTypeDrop.getItems().addAll("Administrator", "Student");
    		userTypeDrop.setValue("Student");

    		Button registerButton = new Button("Register");
    		registerButton.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				if (!users.containsKey(usernameTxt.getText())) {
    					users.put(usernameTxt.getText(), passwordTxt.getText());
    					userType.put(usernameTxt.getText(), userTypeDrop.getValue());
    					if (userTypeDrop.getValue().equals("Administrator")) {
        					Image image1 = new Image("/UW_centre-stack_rgb-black.png", true);
        				    ImageView iv1 = new ImageView();
        				    iv1.setImage(image1);
        				    iv1.setFitWidth(150);
        				    iv1.setPreserveRatio(true);
        				    iv1.setSmooth(true);
        				    iv1.setCache(true);
        				    menuGrid.add(iv1, 0, 0);
        				    menuGrid.add(stdQuitButton, 0, 4);
        				    loginCheck = false;
        				    primaryStage.close();
        					primaryStage.setScene(menuScene);
        					primaryStage.show();
        				} else {
        					primaryStage.close();
        					primaryStage.setScene(stdScene);
        					primaryStage.show();
        				}
    				} else {
    					VBox invalidUsernameBox = new VBox();
    					invalidUsernameBox.setAlignment(Pos.CENTER);
    					Label usernameExist = new Label("Username already exists. ");
    					invalidUsernameBox.getChildren().add(usernameExist);
    					Scene invalidUsernameScene = new Scene(invalidUsernameBox, 300, 100);
    					Stage invalidUsernameStage = new Stage();
    					invalidUsernameStage.setScene(invalidUsernameScene);
    					invalidUsernameStage.initModality(Modality.APPLICATION_MODAL);
    					invalidUsernameStage.show();
    				}
    			}
    		});

    		GridPane register = new GridPane();
    		register.setAlignment(Pos.CENTER);
    		register.add(usernameLbl, 0, 0);
    		register.add(usernameTxt, 1, 0);
    		register.add(passwordLbl, 0, 2);
    		register.add(passwordTxt, 1, 2);
    		register.add(userTypeLbl, 0, 4);
    		register.add(userTypeDrop, 1, 4);
    		register.add(registerButton, 0, 6);

    		Scene registerScene = new Scene(register, 450, 250);

    		Stage registerWindow = new Stage();
    		registerWindow.setScene(registerScene);
    		registerWindow.initModality(Modality.APPLICATION_MODAL);
    		registerWindow.initOwner(primaryStage);
    		registerWindow.show();

    	}
    });

    oldUser.setOnAction(new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent event) {
    		Label usernameLbl = new Label("Enter username: ");
    		TextField usernameTxt = new TextField("username");

    		Label passwordLbl = new Label("Enter password: ");
    		TextField passwordTxt = new TextField("password");

    		Button loginButton = new Button("Login");
    		loginButton.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				if (users.containsKey(usernameTxt.getText())) {
    					if (users.get(usernameTxt.getText()).equals(passwordTxt.getText())) {
    						if (userType.get(usernameTxt.getText()).equals("Administrator")) {
    							Image image1 = new Image("/UW_centre-stack_rgb-black.png", true);
    	    				    ImageView iv1 = new ImageView();
    	    				    iv1.setImage(image1);
    	    				    iv1.setFitWidth(150);
    	    				    iv1.setPreserveRatio(true);
    	    				    iv1.setSmooth(true);
    	    				    iv1.setCache(true);
    	    				    menuGrid.add(iv1, 0, 0);
    	    				    menuGrid.add(stdQuitButton, 0, 4);
    	    				    loginCheck = false;
    	    				    primaryStage.close();
    							primaryStage.setScene(menuScene);
    	    					primaryStage.show();
    						} else {
    							primaryStage.close();
    							primaryStage.setScene(stdScene);
    	    					primaryStage.show();
    						}
    					}
    				} else {
    					VBox invalidUsernameBox = new VBox();
    					invalidUsernameBox.setAlignment(Pos.CENTER);
    					Label usernameExist = new Label("Invalid username/password. ");
    					invalidUsernameBox.getChildren().add(usernameExist);
    					Scene invalidUsernameScene = new Scene(invalidUsernameBox, 300, 100);
    					Stage invalidUsernameStage = new Stage();
    					invalidUsernameStage.setScene(invalidUsernameScene);
    					invalidUsernameStage.initModality(Modality.APPLICATION_MODAL);
    					invalidUsernameStage.show();
    				}
    			}
    		});
    		GridPane login = new GridPane();
    		login.setAlignment(Pos.CENTER);
    		login.add(usernameLbl, 0, 0);
    		login.add(usernameTxt, 1, 0);
    		login.add(passwordLbl, 0, 2);
    		login.add(passwordTxt, 1, 2);
    		login.add(loginButton, 0, 4);

    		Scene loginScene = new Scene(login, 450, 250);

    		Stage loginWindow = new Stage();
    		loginWindow.setScene(loginScene);
    		loginWindow.initModality(Modality.APPLICATION_MODAL);
    		loginWindow.initOwner(primaryStage);
    		loginWindow.show();

    	}
    });

    student.setOnAction(new EventHandler<ActionEvent>() {
    	@Override
    	public void handle(ActionEvent event) {
    		primaryStage.setTitle("Program and Course Viewer");
    	    primaryStage.setScene(stdScene);
    	    primaryStage.show();
    	}
    });

    VBox root = new VBox();
    root.setAlignment(Pos.CENTER);
    root.setSpacing(20);
    root.getChildren().add(newUser);
    root.getChildren().add(oldUser);
    root.getChildren().add(student);
    Scene loginScene = new Scene(root, winX, winY);
    if (loginCheck) {
    	primaryStage.setScene(loginScene);
        primaryStage.show();
    }

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

    Label electLbl = new Label("Choose the Elective Courses the Program Offers");
    MenuButton menuButton = new MenuButton();



		for (int i = 0; i < courseList.size(); i++){
			Course currentCourse = courseList.get(i);
			String s = currentCourse.get_title();
			//String courseName = new String(s);
			CheckBox courseName = new CheckBox(s);
			CustomMenuItem item1 = new CustomMenuItem(courseName);
			electivesMap.put(s, courseName);
			item1.setHideOnClick(false);
			menuButton.getItems().add(item1);
		}


    Label requireLbl = new Label("Choose the Required Courses of the Program");
    MenuButton menuButton2 = new MenuButton();
		for (int i = 0; i < courseList.size(); i++){
			Course currentCourse = courseList.get(i);
			String s = currentCourse.get_title();
			//String courseName = new String(s);
			CheckBox courseName = new CheckBox(s);
			CustomMenuItem item1 = new CustomMenuItem(courseName);
			requiredMap.put(s, courseName);
			item1.setHideOnClick(false);
			menuButton2.getItems().add(item1);
		}


    Button enterNewProgramButton = new Button();
    enterNewProgramButton.setText("Add New Program");

    addProgramBox.getChildren().addAll(idLbl, idTxt, titleLbl, titleTxt, descLbl, descTxt, departLbl, departTxt, electLbl, menuButton, requireLbl, menuButton2, enterNewProgramButton);

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




    //vbox and scene for adding a course

    VBox addCourseBox = new VBox();
    addCourseBox.setAlignment(Pos.CENTER);
    addCourseBox.setSpacing(10);

    Label courseIDLbl = new Label("Enter ID of the Course");
    TextField courseIDTxt = new TextField();

    Label courseTitleLbl = new Label("Enter Title of the Course");
    TextField courseTitleTxt = new TextField();

    Label courseDescLbl = new Label("Enter Description of the Course");
    TextField courseDescTxt = new TextField();

    Label courseYearLbl = new Label("Enter Year of the Course");
    TextField courseYearTxt = new TextField();

    Label coursePreReqLbl = new Label("Enter the Prerequisite Courses, Separated by Commas");
    TextField coursePreReqTxt = new TextField();

    Label courseMutExLbl = new Label("Enter the Mutually Exclusive Courses, Separated by Commas");
    TextField courseMutExTxt = new TextField();

    Button enterNewCourseButton = new Button();
    enterNewCourseButton.setText("Add New Course");

    addCourseBox.getChildren().addAll(courseIDLbl, courseIDTxt, courseTitleLbl, courseTitleTxt, courseDescLbl, courseDescTxt, courseYearLbl, courseYearTxt, coursePreReqLbl, coursePreReqTxt, courseMutExLbl, courseMutExTxt, enterNewCourseButton);

    Scene addCourseScene = new Scene(addCourseBox, 500, 500);








	//vbox and scene for choosing which course to edit

    VBox editCourseBox = new VBox();
    editCourseBox.setAlignment(Pos.CENTER);
    editCourseBox.setSpacing(10);

    ChoiceBox<String> editCourseListChoiceBox = new ChoiceBox<String>();
    for (int i = 0; i < courseList.size(); i++){
      Course currentCourse = courseList.get(i);
      String s = currentCourse.get_id();
      editCourseListChoiceBox.getItems().add(s);
    }

    Button editThisCourseButton = new Button();
    editThisCourseButton.setText("Edit Course");

    Button editCourseBackButton = new Button();
    editCourseBackButton.setText("Back to Menu");

    editCourseBox.getChildren().addAll(editCourseListChoiceBox, editThisCourseButton, editCourseBackButton);

    Scene editCourseScene = new Scene(editCourseBox, winX, winY);







    //vbox and scene where actual editing of course happens

    VBox editThisCourseBox = new VBox();
    editThisCourseBox.setAlignment(Pos.CENTER);
    editThisCourseBox.setSpacing(10);

    Label editCourseIDLbl = new Label("Edit ID of the Course");
    TextField editCourseIDTxt = new TextField();

    Label editCourseTitleLbl = new Label("Edit Title of the Course");
    TextField editCourseTitleTxt = new TextField();

    Label editCourseDescLbl = new Label("Edit Description of the Course");
    TextField editCourseDescTxt = new TextField();

    Label editCourseYearLbl = new Label("Edit Year of the Course");
    TextField editCourseYearTxt = new TextField();

    Label editCoursePreReqLbl = new Label("Edit the Prerequisite Courses, Separated by Commas");
    TextField editCoursePreReqTxt = new TextField();

    Label editCourseMutExLbl = new Label("Edit the Mutually Exclusive Courses, Separated by Commas");
    TextField editCourseMutExTxt = new TextField();

    Button submitCourseEditButton = new Button();
    submitCourseEditButton.setText("Submit Edits");

    editThisCourseBox.getChildren().addAll(editCourseIDLbl, editCourseIDTxt, editCourseTitleLbl, editCourseTitleTxt, editCourseDescLbl, editCourseDescTxt, editCourseYearLbl, editCourseYearTxt, editCoursePreReqLbl, editCoursePreReqTxt, editCourseMutExLbl, editCourseMutExTxt, submitCourseEditButton);

    Scene editThisCourseScene = new Scene(editThisCourseBox, 500, 500);












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
    	  SwingUtilities.invokeLater(() -> {
        
				String id=idTxt.getText();
				String title=titleTxt.getText();

				id=Checks.ValidID(id, programList);
				title=Checks.ValidTitle(title, programList);
				if(id==null || title==null) {
				Checks.canceled();
				}else {
        program.setProgramID(id);
        program.setProgramTitle(title);
        program.setProgramDesc(descTxt.getText());

        String[] departments = departTxt.getText().split(", ");
    		for (String s: departments)
    			program.addDepartment(s);

				for (int i = 0; i < courseList.size(); i++){
					Course currentCourse = courseList.get(i);
					String s = currentCourse.get_title();
					CheckBox courseToAdd = electivesMap.get(s);
					if (courseToAdd.isSelected()){
						program.add_Elective(s);
					}
				}
				for (int i = 0; i < courseList.size(); i++){
					Course currentCourse = courseList.get(i);
					String s = currentCourse.get_title();
					CheckBox courseToAdd = requiredMap.get(s);
					if (courseToAdd.isSelected()){
						program.addRequiredCourses(s);
					}
				}

        	programList.add(program);
			}
		Platform.runLater(() -> {
        start(primaryStage);
		});
    	  });
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
			@Override
      public void handle(ActionEvent event){
        try {
          String fileName = "programSaveGUI.dat";
          FileOutputStream out = new FileOutputStream(fileName);
          out.write(("").getBytes());
          ObjectOutputStream writer = new ObjectOutputStream(out);
          writer.writeObject(programList);
          out.close();
          fileName = "courseSaveGUI.dat";
          out = new FileOutputStream(fileName);
          writer = new ObjectOutputStream(out);
          out.write(("").getBytes());
          writer.writeObject(courseList);
          out.close();
          fileName = "userSaveGUI.dat";
          out = new FileOutputStream(fileName);
          writer = new ObjectOutputStream(out);
          out.write(("").getBytes());
          ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
          usersList.add(users);
          usersList.add(userType);
          writer.writeObject(usersList);
          out.close();
        } catch (IOException e) {
          System.out.println("Failed to save. ");
        }
        System.exit(0);
      }
    });
  //this button exits the program, I'm guessing this button could also be when file i/o is implemented
    stdQuitButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
      public void handle(ActionEvent event){
        try {
          String fileName = "programSaveGUI.dat";
          FileOutputStream out = new FileOutputStream(fileName);
          out.write(("").getBytes());
          ObjectOutputStream writer = new ObjectOutputStream(out);
          writer.writeObject(programList);
          out.close();
          fileName = "courseSaveGUI.dat";
          out = new FileOutputStream(fileName);
          writer = new ObjectOutputStream(out);
          out.write(("").getBytes());
          writer.writeObject(courseList);
          out.close();
          fileName = "userSaveGUI.dat";
          out = new FileOutputStream(fileName);
          writer = new ObjectOutputStream(out);
          out.write(("").getBytes());
          ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();
          usersList.add(users);
          usersList.add(userType);
          writer.writeObject(usersList);
          out.close();
        } catch (IOException e) {
          System.out.println("Failed to save. ");
        }
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


        start(primaryStage);


		//Auto fill field with current values

		//Simple fields: ID, Titile, Description
		editidTxt.setText(programToEdit.getProgramID());
		edittitleTxt.setText(programToEdit.getProgramTitle());
		editdescTxt.setText(programToEdit.getProgramDesc());

		//Comma separated fields
		String departString;
		String electString;
		String requireString;

		StringJoiner departJoiner = new StringJoiner(",");
		StringJoiner electJoiner = new StringJoiner(",");
		StringJoiner requireJoiner = new StringJoiner(",");

		//Departments
		for (String s: programToEdit.getDepartments())
			departJoiner.add(s);
		departString = departJoiner.toString();

		editdepartTxt.setText(departString);

		//Electives
		for (String s: programToEdit.getElectives())
			electJoiner.add(s);
		electString = electJoiner.toString();

		editelectTxt.setText(electString);

		//Requirements
		for (String s: programToEdit.getRequiredCourses())
			requireJoiner.add(s);
		requireString = requireJoiner.toString();

		editrequireTxt.setText(requireString);

        primaryStage.setScene(editThisProgramScene);
      }
    });

    //this button takes the changed inputs and edits the current program, back to menu scene
    submitEditButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
    	SwingUtilities.invokeLater(() -> {
    	  String id=editidTxt.getText();
    	  String title=edittitleTxt.getText();
    	  programToEdit.setProgramID("");
          programToEdit.setProgramTitle("");
    	  id=Checks.ValidID(id, programList);
    	  title=Checks.ValidTitle(title, programList);
			if(id==null || title==null) {
			Checks.canceled();
			}else {
        programToEdit.setProgramID(id);
        programToEdit.setProgramTitle(title);
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
      }
			Platform.runLater(() -> {
		        start(primaryStage);
				});
		    	  });
      }
    });

	//////////////////
	//Course Buttons//
	//////////////////

    //this button changes the scene to the add course scene
    addCourseButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(addCourseScene);
      }
    });

    //this button takes the inputs from the text fields and creates a new course, changes scene back to main menu
    enterNewCourseButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        Course course = new Course();
        SwingUtilities.invokeLater(() -> {
        	
        	String id=courseIDTxt.getText();
			String title=courseTitleTxt.getText();

			id=Checks.ValidIDC(id, courseList);
			title=Checks.ValidTitleC(title, courseList);
			if(id==null || title==null) {
			Checks.canceled();
			}else {
        course.set_id(id);
        course.set_title(title);
        course.set_descr(courseDescTxt.getText());
		course.set_year(courseYearTxt.getText());

        String[] prereqs = coursePreReqTxt.getText().split(", ");
    		for (String s: prereqs)
    			course.add_pre_req(s);

        String[] mutEx = courseMutExTxt.getText().split(", ");
      	for (String s: mutEx)
      		course.add_mutually_exclusive(s);

        courseList.add(course);

			}
			Platform.runLater(() -> {
	        start(primaryStage);
			});
	    	  });
      }
    });



    //this button changes the scene to the edit course scene
    editCourseButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(editCourseScene);
      }
    });

    //this button changes the scene from the edit course to the main menu
    editCourseBackButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
        primaryStage.setScene(menuScene);
      }
    });

    //this button takes the selected course from the choice box, and puts the components of the course into variables to be used in the edit this course scene, also switches scenes to edit this course scene
    editThisCourseButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){

        String currentCourse = editCourseListChoiceBox.getValue();
        for(int i = 0 ; i < courseList.size() ; i++){
          if(courseList.get(i).get_id() == currentCourse){
            courseToEdit = courseList.get(i);
            viewCourseID = courseList.get(i).get_id();

            viewCourseTitle = courseList.get(i).get_title();
            viewCourseDesc = courseList.get(i).get_descr();
			viewCourseYear = courseList.get(i).get_year();
          }
        }

        ArrayList<String> prereqs = courseToEdit.get_pre_reqs();
        for (String s: prereqs)
          viewCoursePreReqs = viewCoursePreReqs + s;

        ArrayList<String> mutEx = courseToEdit.get_mutually_exclusive();
        for (String s: mutEx)
          viewCourseMutExs = viewCourseMutExs + s;

        start(primaryStage);

		//Auto fill field with current values

		//Simple fields: ID, Titile, Description
		editCourseIDTxt.setText(courseToEdit.get_id());
		editCourseTitleTxt.setText(courseToEdit.get_title());
		editCourseDescTxt.setText(courseToEdit.get_descr());
		editCourseYearTxt.setText(courseToEdit.get_year());

		//Comma separated fields
		String prereqString;
		String mutExString;

		StringJoiner prereqJoiner = new StringJoiner(",");
		StringJoiner mutExJoiner = new StringJoiner(",");

		//Pre Reqs
		for (String s: courseToEdit.get_pre_reqs())
			prereqJoiner.add(s);
		prereqString = prereqJoiner.toString();

		editCoursePreReqTxt.setText(prereqString);

		//Mutually Exclusive courses
		for (String s: courseToEdit.get_mutually_exclusive())
			mutExJoiner.add(s);
		mutExString = mutExJoiner.toString();

		editCourseMutExTxt.setText(mutExString);

        primaryStage.setScene(editThisCourseScene);
      }
    });

    //this button takes the changed inputs and edits the current course, back to menu scene
    submitCourseEditButton.setOnAction(new EventHandler<ActionEvent>(){
      public void handle(ActionEvent event){
    	  SwingUtilities.invokeLater(() -> {
          	
          	String id=editCourseIDTxt.getText();
  			String title=editCourseTitleTxt.getText();
  			courseToEdit.set_id("");
  	        courseToEdit.set_title("");
  			id=Checks.ValidIDC(id, courseList);
  			title=Checks.ValidTitleC(title, courseList);
  			if(id==null || title==null) {
  			Checks.canceled();
  			}else {
        courseToEdit.set_id(id);
        courseToEdit.set_title(title);
        courseToEdit.set_descr(editCourseDescTxt.getText());
		courseToEdit.set_year(editCourseYearTxt.getText());

        courseToEdit.removeAllPreReqs();
        courseToEdit.removeAllMutEx();

        String[] preReqs = editCoursePreReqTxt.getText().split(", ");
    		for (String s: preReqs)
    			courseToEdit.add_pre_req(s);

        String[] mutEx = editCourseMutExTxt.getText().split(", ");
      	for (String s: mutEx)
      		courseToEdit.add_mutually_exclusive(s);

  			}
			Platform.runLater(() -> {
	        start(primaryStage);
			});
	    	  });
      }
    });
  }

  public static void main(String[] args) {
      launch(args);
  }
}
