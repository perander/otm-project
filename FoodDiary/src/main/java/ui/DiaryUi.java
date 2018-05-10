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
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

    private static DecimalFormat df2 = new DecimalFormat(".##");

    /**
     * initialize the database and data access object classes
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {

        File dbDirectory = new File("db");
        dbDirectory.mkdir();

        Database database = new Database("jdbc:sqlite:db" + File.separator + "fooddiary.db");
        database.init();

        UserDao userDao = new UserDao(database);
        FoodDao foodDao = new FoodDao(database);

        //entrydao not needed?
        EntryDao entryDao = new EntryDao(database);
        diary = new Diary(foodDao, userDao, entryDao);
        userLoggedIn = new User(1, "hello", "salasana");

    }

    /**
     * create an line with the entered food and its nutritional value
     *
     * @param food food to be displayed
     * @return the view with a food name and amount of carbohydrates, protein
     * and fat
     */
    public Node createFoodNode(Food food) {

        Double amount = food.getAmount() / 100;
        HBox box = new HBox(50);
        Label label = new Label(food.getName() + ", " + food.getAmount()
                + " grams\n(carbohydrates: " + df2.format(food.getCarb() * amount) + "g, protein: "
                + df2.format(food.getProtein() * amount) + "g, fat: "
                + df2.format(food.getFat() * amount) + "g)");

        label.setMinHeight(28);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.setPadding(new Insets(0, 5, 0, 5));

        box.getChildren().addAll(label, spacer);
        return box;
    }

    /**
     * creates a node with the current date
     *
     * @param date date to be displayed in the node
     * @return node with the current date
     */
    public Node dateStamp(LocalDate date) {

        HBox box = new HBox(50);
        Label label = new Label(date.toString());

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

        List<Food> foods = diary.getUsersCollection(userLoggedIn);

        
        //doesn't keep the old date in place
        if (foods != null && !foods.isEmpty()) {
            foodNodes.getChildren().add(dateStamp(foods.get(0).getDate()));
            int size = foods.size() - 1;
            for (int i = size; i >= 0; i--) {
                Food food = foods.get(i);
                if (i < size) {
                    Food prev = foods.get(i + 1);
                    if (!food.getDate().equals(prev.getDate())) {
                        foodNodes.getChildren().add(dateStamp(prev.getDate()));
                    }
                }
                foodNodes.getChildren().add(createFoodNode(food));
            }

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
        loginPane.setPadding(new Insets(5));
        Label loginLabel = new Label("username");
        TextField usernameInput = new TextField();

        HBox passwordPane = new HBox(10);
        Label passwordLabel = new Label("password");
        PasswordField passwordInput = new PasswordField();

        inputPane.getChildren().addAll(loginLabel, usernameInput);
        passwordPane.getChildren().addAll(passwordLabel, passwordInput);

        Label loginMessage = new Label();

        Button loginButton = new Button("login");
        Button createButton = new Button("create new user");
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            menuLabel.setText(username + " logged in");
            try {
                if (diary.login(username, password)) {
                    loginMessage.setText("logging in successful");
                    userLoggedIn = diary.getLoggedUser();
                    loginMessage.setTextFill(Color.GREEN);
                    redrawFoodlist();
                    stage.setScene(foodScene);
                    usernameInput.setText("");
                    passwordInput.setText("");
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
            passwordInput.setText("");
            stage.setScene(newUserScene);
        });

        loginPane.getChildren().addAll(loginMessage, inputPane, passwordPane, loginButton, createButton);

        loginScene = new Scene(loginPane, 300, 250);

        // new createNewUserScene
        VBox newUserPane = new VBox(10);

        HBox newUsernamePane = new HBox(10);
        newUsernamePane.setPadding(new Insets(10));
        TextField newUsernameInput = new TextField();
        Label newUsernameLabel = new Label("username");
        newUsernameLabel.setPrefWidth(100);
        newUsernamePane.getChildren().addAll(newUsernameLabel, newUsernameInput);

        HBox newPasswordPane = new HBox(10);
        newPasswordPane.setPadding(new Insets(10));
        PasswordField newPasswordInput = new PasswordField();
        Label newPasswordLabel = new Label("password");
        newPasswordLabel.setPrefWidth(100);
        newPasswordPane.getChildren().addAll(newPasswordLabel, newPasswordInput);
        Label userCreationMessage = new Label();

        Button createNewUserButton = new Button("create");
        createNewUserButton.setPadding(new Insets(10));

        Button goBackButton = new Button("back to login");
        createNewUserButton.setPadding(new Insets(10));

        createNewUserButton.setOnAction(e -> {
            String username = newUsernameInput.getText();
            String password = newPasswordInput.getText();

            if (username.length() < 3) {
                userCreationMessage.setText("username too short");
                userCreationMessage.setTextFill(Color.RED);
            } else if (password.length() < 3) {
                userCreationMessage.setText("password too short");
                userCreationMessage.setTextFill(Color.RED);
            } else {
                try {
                    if (diary.createUser(username, password)) {
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

        goBackButton.setOnAction(e -> {
            stage.setScene(loginScene);
        });

        newUserPane.getChildren().addAll(userCreationMessage, newUsernamePane,
                newPasswordPane, createNewUserButton, goBackButton);

        newUserScene = new Scene(newUserPane, 300, 250);

        // main scene
        ScrollPane foodScollbar = new ScrollPane();
        BorderPane mainPane = new BorderPane(foodScollbar);
        foodScene = new Scene(mainPane, 700, 800);

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
        Button createFood = new Button("Add");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox namePane = new VBox(5);
        Label nameLabel = new Label("Food name:");
        TextField nameInput = new TextField();
        namePane.getChildren().addAll(nameLabel, nameInput);

        VBox carbPane = new VBox(5);
        Label carbLabel = new Label("Carbohydrates per 100g:");
        TextField carbInput = new TextField();
        carbPane.getChildren().addAll(carbLabel, carbInput);

        VBox proteinPane = new VBox(5);
        Label proteinLabel = new Label("Proteins per 100g:");
        TextField proteinInput = new TextField();
        proteinPane.getChildren().addAll(proteinLabel, proteinInput);

        VBox fatPane = new VBox(5);
        Label fatLabel = new Label("Fat per 100g:");
        TextField fatInput = new TextField();
        fatPane.getChildren().addAll(fatLabel, fatInput);

        VBox amountPane = new VBox(5);
        Label amountLabel = new Label("Amount in grams:");
        TextField amountInput = new TextField();
        amountPane.getChildren().addAll(amountLabel, amountInput);

        Label title = new Label("Add a new food!");

        createForm.getChildren().addAll(title, namePane, carbPane, proteinPane,
                fatPane, amountPane, createFood);

        foodNodes = new VBox(10);
        foodNodes.setMaxWidth(500);
        foodNodes.setMinWidth(280);

        foodScollbar.setContent(foodNodes);
        mainPane.setRight(createForm);
        mainPane.setTop(menuPane);

        createFood.setOnAction(e -> {
            Food f = new Food(userLoggedIn.getId(), nameInput.getText(),
                    Double.parseDouble(carbInput.getText()),
                    Double.parseDouble(proteinInput.getText()),
                    Double.parseDouble(fatInput.getText()),
                    Double.parseDouble(amountInput.getText()),
                    LocalDate.now());
            try {
                diary.addFood(f);
            } catch (SQLException ex) {
                Logger.getLogger(DiaryUi.class.getName()).log(Level.SEVERE, null, ex);
            }

            nameInput.setText("");
            carbInput.setText("");
            proteinInput.setText("");
            fatInput.setText("");
            amountInput.setText("");

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

    public static void main(String[] args) {
        launch(args);
    }

}
