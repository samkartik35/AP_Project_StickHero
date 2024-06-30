package com.example.application;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class Main extends Application implements GameObserver{
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Load the image
        String imagePath = getClass().getResource("/images/background2.jpeg").toExternalForm();

        Image backgroundImage = new Image(imagePath);

        // Create a background image
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        // Create a background with the image
        Background backgroundWithImage = new Background(background);




        Group group = new Group();
        StackPane stackPane = new StackPane(group);
        stackPane.setBackground(backgroundWithImage);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(700);
        primaryStage.setHeight(700);
        primaryStage.setTitle("Stick Hero");


        Stage stage = new Stage();
        VBox vBox = new VBox();
        vBox.setBackground(backgroundWithImage);
        HBox hBox = new HBox();
        Scene scene1 = new Scene(vBox);
        stage.setScene(scene1);
        stage.setWidth(700);
        stage.setHeight(700);
        stage.setResizable(false);
        stage.setTitle("Welcome");
        stage.show();
        Label label = new Label("Name:                            ");
        TextField textField = new TextField();
        Button button = new Button("OK");
        hBox.getChildren().addAll(label, textField);
        vBox.getChildren().addAll(hBox, new Separator(), button);

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Singleton Design Pattern
                startGame(event, textField, primaryStage, scene, group, stage);
            }
        });

        button.setOnAction(e -> {
            startGame(e, textField, primaryStage, scene, group, stage);
        });


    }

    private void startGame(Event e, TextField textField, Stage primaryStage, Scene scene, Group group, Stage stage) {
        if (textField.getText() != null) {
            stage.close();
            e.consume();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
            new StickHero(primaryStage, stage, scene, group, textField.getText()).addObserver(this);
        }
    }

    // Using Observer Pattern
    @Override
    public void onScoreUpdate(int score) {
        System.out.println("Score updated: " + score);
    }

    @Override
    public void onGameOver() {
        System.out.println("Game over!");
    }

    @Override
    public void onGameRestart() {
        System.out.println("Restarting the Game!");
    }
}