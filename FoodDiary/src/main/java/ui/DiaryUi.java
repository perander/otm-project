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
import domain.User;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

    private User userLoggedIn;
    private VBox foodNodes;

    private Label menuLabel = new Label();

    /**
     * initialize the database and data access object classes
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {

        Database database = new Database("jdbc:sqlite:fooddiary.db");
        database.init();

        UserDao userDao = new UserDao(database);
        FoodDao foodDao = new FoodDao(database);
        
        //entrydao not needed?
        EntryDao entryDao = new EntryDao(database);
        diary = new Diary(foodDao, userDao, entryDao);
        userLoggedIn = new User(1, "hello");
        
    }

    /**
     * create an line with the entered food and its nutritional value
     *
     * @param food food to be displayed
     * @return the view with a food name and amount of carbohydrates, protein
     * and fat
     */
    public Node createFoodNode(Food food) {
        
        HBox box = new HBox(50);
        Label label = new Label(food.getName() + ", carbohydrates: " + food.getCarb()
                + "g, protein: " + food.getProtein() + "g, fat: " + food.getFat() + "g");
        label.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));

        box.getChildren().addAll(label, spacer);
        return box;
    }

    /**
     * update the food list when a new one is added
     *
     * @throws SQLException
     */
    public void redrawFoodlist() throws SQLException {
        foodNodes.getChildren().clear();

        System.out.println("user: " + userLoggedIn.getUsername() + ", " + userLoggedIn.getId());
        List<Food> foods = diary.getUsersCollection(userLoggedIn);
        
        if(foods != null){
            foods.forEach(food -> {
                foodNodes.getChildren().add(createFoodNode(food));
            });
        }
        
    }

    /**
     * manage the scenes: login, creating a new user, displaying the food list
     *
     * @param stage
     * @throws Exception
     */
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
            menuLabel.setText(username + " logged in");
            try {
                if (diary.login(username)) {
                    loginMessage.setText("logging in successful");
                    userLoggedIn = diary.getLoggedUser();
                    loginMessage.setTextFill(Color.GREEN);
                    redrawFoodlist();
                    stage.setScene(foodScene);
                    usernameInput.setText("");
                } else {
                    loginMessage.setText("user does not exist");
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
            } else {
                try {
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
            }

        });

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane, createNewUserButton);

        newUserScene = new Scene(newUserPane, 300, 250);

        // main scene
        ScrollPane foodScollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(foodScollbar);
        foodScene = new Scene(mainPane, 700, 600);

        HBox menuPane = new HBox(10);
        Region menuSpacer = new Region();
        HBox.setHgrow(menuSpacer, Priority.ALWAYS);
        Button logoutButton = new Button("logout");
        menuPane.getChildren().addAll(menuLabel, menuSpacer, logoutButton);
        logoutButton.setOnAction(e -> {
            diary.logout();
            loginMessage.setText("logging out successful");
            stage.setScene(loginScene);
        });

        VBox createForm = new VBox(50);
        Button createFood = new Button("add");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox namePane = new VBox(5);
        Label nameLabel = new Label("food name");
        TextField nameInput = new TextField();
        namePane.getChildren().addAll(nameLabel, nameInput);

        VBox carbPane = new VBox(5);
        Label carbLabel = new Label("carbohydrates");
        TextField carbInput = new TextField();
        carbPane.getChildren().addAll(carbLabel, carbInput);

        VBox proteinPane = new VBox(5);
        Label proteinLabel = new Label("protein");
        TextField proteinInput = new TextField();
        proteinPane.getChildren().addAll(proteinLabel, proteinInput);

        VBox fatPane = new VBox(5);
        Label fatLabel = new Label("fat");
        TextField fatInput = new TextField();
        fatPane.getChildren().addAll(fatLabel, fatInput);

        Label title = new Label("Add a new food");

        createForm.getChildren().addAll(title, namePane, carbPane, proteinPane,
                fatPane, createFood);

        foodNodes = new VBox(10);
        foodNodes.setMaxWidth(500);
        foodNodes.setMinWidth(280);
        redrawFoodlist();

        foodScollbar.setContent(foodNodes);
        mainPane.setRight(createForm);
        mainPane.setTop(menuPane);

        createFood.setOnAction(e -> {
            Food f = new Food(userLoggedIn.getId(), nameInput.getText(),
                    Double.parseDouble(carbInput.getText()),
                    Double.parseDouble(proteinInput.getText()),
                    Double.parseDouble(fatInput.getText()));
            try {
                diary.addFood(f);
            } catch (SQLException ex) {
                Logger.getLogger(DiaryUi.class.getName()).log(Level.SEVERE, null, ex);
            }
            nameInput.setText("");
            carbInput.setText("");
            proteinInput.setText("");
            fatInput.setText("");

            try {
                redrawFoodlist();
            } catch (SQLException ex) {
                Logger.getLogger(DiaryUi.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //setup stage
        stage.setTitle("Foods");
        stage.setScene(loginScene);
        stage.show();
        stage.setOnCloseRequest(e -> {
            System.out.println("closing");
            System.out.println(diary.getLoggedUser());
            if (diary.getLoggedUser() != null) {
                e.consume();
            }

        });
    }

    /**
     * displaying a message when closing
     */
    @Override
    public void stop() {
        // tee lopetustoimenpiteet täällä
        System.out.println("thank you, welcome again");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
