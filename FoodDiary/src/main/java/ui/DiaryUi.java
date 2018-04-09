/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.Database;
import dao.EntryDao;
import dao.FoodDao;
import dao.UserDao;
import domain.Diary;
import domain.Entry;
import domain.Food;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author sperande
 */
public class DiaryUi extends Application {
    private Diary diary;
    
    private Scene foodScene;
    private Scene newUserScene;
    private Scene loginScene;
    
    private VBox FoodNodes;

    private Label menuLabel = new Label();
    
    @Override
    public void init() throws Exception {
        
        Database database = new Database("jdbc:sqlite:fooddiary.db");
        
        UserDao userDao = new UserDao(database);
        FoodDao foodDao = new FoodDao(database);
        EntryDao entryDao = new EntryDao(database);
        diary = new Diary(foodDao, userDao, entryDao);
    }
    
    public Node createFoodNode(Food food) {
        HBox box = new HBox(10);
        Label label = new Label(food.getName());
        label.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));

        box.getChildren().addAll(label, spacer);
        return box;
    }
    
    public void redrawFoodlist() throws SQLException {
        FoodNodes.getChildren().clear();

        //change to entries
        List<Food> foods = diary.getUsersCollection();
        foods.forEach(food -> {
            FoodNodes.getChildren().add(createFoodNode(food));
        });
    }

    
    @Override
    public void start(Stage stage) throws Exception {

        // login scene
        VBox loginPane = new VBox(10);
        HBox inputPane = new HBox(10);
        loginPane.setPadding(new Insets(10));
        Label loginLabel = new Label("username");
        TextField usernameInput = new TextField();

        inputPane.getChildren().addAll(loginLabel, usernameInput);
        Label loginMessage = new Label();

        Button loginButton = new Button("login");
        Button createButton = new Button("create new user");
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            menuLabel.setText(username + " logged in...");
            try {
                if (diary.login(username)) {
                    loginMessage.setText("");
                    redrawFoodlist();
                    stage.setScene(foodScene);
                    usernameInput.setText("");
                } else {
                    loginMessage.setText("use does not exist");
                    loginMessage.setTextFill(Color.RED);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DiaryUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        createButton.setOnAction(e -> {
            usernameInput.setText("");
            stage.setScene(newUserScene);
        });

        loginPane.getChildren().addAll(loginMessage, inputPane, loginButton, createButton);

        loginScene = new Scene(loginPane, 300, 250);
        
        // new createNewUserScene
        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField();
        Label newUsernameLabel = new Label("username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);

//        HBox newNamePane = new HBox(10);
//        newNamePane.setPadding(new Insets(10));
//        TextField newNameInput = new TextField();
//        Label newNameLabel = new Label("name");
//        newNameLabel.setPrefWidth(100);
//        newNamePane.getChildren().addAll(newNameLabel, newNameInput);

        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("create");
        createNewUserButton.setPadding(new Insets(10));

        createNewUserButton.setOnAction(e -> {
            String username = newUsernameInput.getText();
//            String name = newNameInput.getText();

            if (username.length() < 3) {
                userCreationMessage.setText("username or name too short");
                userCreationMessage.setTextFill(Color.RED);
            } else try {
                if (diary.createUser(username)) {
                    userCreationMessage.setText("");
                    loginMessage.setText("new user created");
                    loginMessage.setTextFill(Color.GREEN);
                    stage.setScene(loginScene);
                } else {
                    userCreationMessage.setText("username has to be unique");
                    userCreationMessage.setTextFill(Color.RED);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DiaryUi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, createNewUserButton);

        newUserScene = new Scene(newUserPane, 300, 250);
        
        //setup stage
        
        stage.setTitle("Foods");
        stage.setScene(loginScene);
        stage.show();
//        stage.setOnCloseRequest(e -> {
//            System.out.println("closing");
//            System.out.println(diary.getLoggedUser());
//            if (diary.getLoggedUser() != null) {
//                e.consume();
//            }

//        });

    }
    
    @Override
    public void stop() {
        // tee lopetustoimenpiteet täällä
        System.out.println("sovellus sulkeutuu");
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}