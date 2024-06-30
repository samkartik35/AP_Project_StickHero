package com.example.application;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StickHero {
    private Scene scene;
    private Group group;
    private Rectangle firstRectangle;
    private Rectangle secondRectangle;
    private Rectangle feed;
    private Rectangle obstacle1;
    private Rectangle obstacle2;
    private Line line;
    private int distance;
    private int toGo, cycle;
    private boolean adjuster;
    private boolean isEnd = false;
    private Random random;
    private Rectangle leftFoot;
    private Rectangle rightFoot;
    private Rectangle body;
    private Circle head;
    private int score = 0;
    private int cherryCount = 0;
    private Label scoreLabel;
    private Label cherryLabel;
    private String playerName;
    private int bestRecord;
    private String bestPlayerName;
    private Button restartButton;
    private Button reviveButton;
    private Button exitButton;
    private Stage init_stage;
    private Stage curr_stage;

    public StickHero(Stage primaryStage, Stage stage, Scene scene, Group group, String playerName) {
        this.scene = scene;
        this.group = group;
        this.playerName = playerName;
        this.init_stage = stage;
        this.curr_stage = primaryStage;

        initializeGame();
    }

    private void initializeGame(){
        isEnd = false;
        group.getChildren().removeAll();
        firstRectangle = new Rectangle();
        secondRectangle = new Rectangle();
        feed = new Rectangle(10, 10);

        obstacle1 = new Rectangle(10, 10);
        obstacle2 = new Rectangle(10, 10);

        feed.setFill(javafx.scene.paint.Color.RED);
        obstacle1.setFill(Color.BLACK);
        obstacle2.setFill(Color.BLACK);

        line = new Line();
        leftFoot = new Rectangle(5, 25);
        rightFoot = new Rectangle(5, 25);
        body = new Rectangle(15, 20);
        head = new Circle(5);


        restartButton = new Button("Restart");
        restartButton.setLayoutX(250);
        restartButton.setLayoutY(650);
        restartButton.setOnAction(e -> restartGame());
        restartButton.setVisible(false);
        restartButton.setDisable(true);

        reviveButton = new Button("Revive");
        reviveButton.setLayoutX(310);
        reviveButton.setLayoutY(650);
        reviveButton.setOnAction(e -> revive());
        reviveButton.setVisible(false);
        reviveButton.setDisable(true);

        exitButton = new Button("EXIT");
        exitButton.setLayoutX(370);
        exitButton.setLayoutY(650);
        exitButton.setOnAction(e -> exit());
        exitButton.setVisible(false);
        exitButton.setDisable(true);

        leftFoot.setX(-20);
        rightFoot.setX(-20);
        body.setX(-20);
        head.setCenterX(-20);
        feed.setX(-20);
        obstacle1.setX(-20);
        obstacle2.setX(-20);

        firstRectangle.setX(0);

        scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setLayoutX(175);

        cherryLabel = new Label(String.valueOf(cherryCount));
        cherryLabel.setLayoutX(250);

        random = new Random(System.currentTimeMillis());
        group.getChildren().addAll(firstRectangle, secondRectangle, scoreLabel,
                line, leftFoot, rightFoot, body, head, feed, restartButton, cherryLabel, reviveButton, exitButton, obstacle1, obstacle2);

        setBestPlayer();
        setRandoms();
        setAdjusterAndCommand();
    }

    private void exit(){
        Platform.exit();
        System.exit(0);
    }

    private void restartGame() {
        score = 0;
        Platform.runLater(() -> scoreLabel.setText(String.valueOf(score)));
        cherryCount = 0;
        Platform.runLater(() -> cherryLabel.setText(String.valueOf(cherryCount)));
        group.getChildren().clear();
        initializeGame();
    }

    private void revive(){
        if(cherryCount>0) {
            group.getChildren().clear();
            initializeGame();
            cherryCount -= 1000;
            Platform.runLater(() -> cherryLabel.setText(String.valueOf(cherryCount)));
        }else {
            exit();
        }
    }

    private void notifyRestart() {
        for (GameObserver observer : observers) {
            observer.onGameRestart();
        }
    }

    private void setBestPlayer() {
        java.util.List<String> names = new ArrayList<>();
        java.util.List<Integer> scores = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader
                    (new File("C:\\Users\\rishi\\Documents\\best.txt"))); // you may need to change this
            String nameTemp = br.readLine();
            String scoreTemp = br.readLine();

            String second = br.readLine();

            names.add(nameTemp);
            scores.add(Integer.parseInt(scoreTemp));

            while (second != null) {
                names.add(second);
                scores.add(Integer.parseInt(br.readLine()));

                second = br.readLine();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bestRecord = 0;
        for (int i = 0; i < scores.size(); i++)
            if (scores.get(i) > bestRecord) {
                bestRecord = scores.get(i);
                bestPlayerName = names.get(i);
            }
    }

    private void playGame() {

        double stickLength = line.getEndX() - line.getStartX();
        double dis = secondRectangle.getX() - firstRectangle.getX() - firstRectangle.getWidth();

        if(stickLength < dis || stickLength> secondRectangle.getWidth()+ secondRectangle.getX())isEnd = true;

        playerMove();

        if (isEnd) {
            notifyGameOver();
        }
        else {
//            score=score+100;
            Platform.runLater(() -> scoreLabel.setText(String.valueOf(score)));
            notifyScoreUpdate();
            setRandoms();
        }
    }

    private void playerMove() {
        toGo = (int) (line.getEndX() - line.getStartX() + firstRectangle.getWidth() / 2);
        cycle = toGo / 15;
        cycle *= 2; // :|

        for (int i = 0; i < cycle; i++) {
            goOneCycle();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            fixPosition();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (isEnd) {
            gameOver();
            saveRecord();
            showRecord();
        }
        else {
            score += 100;
            Platform.runLater(() -> scoreLabel.setText(String.valueOf(score)));
            setRandoms();
        }
    }

    private void showRecord() {
        Label playerNameLabel = new Label(playerName);
        Label bestPlayerNameLabel = new Label(bestPlayerName);
        Label bestPlayerScoreLabel = new Label(String.valueOf(bestRecord));

        Platform.runLater(() -> {
            HBox hBox1 = new HBox(playerNameLabel, new Separator(Orientation.VERTICAL), scoreLabel, new Separator(Orientation.VERTICAL), cherryLabel);
            HBox hBox2 = new HBox(bestPlayerNameLabel, new Separator(Orientation.VERTICAL), bestPlayerScoreLabel);
            VBox vBox = new VBox(hBox1, new Separator(), hBox2);
            vBox.setLayoutX(200);
            vBox.setLayoutY(0);
            group.getChildren().add(vBox);
        });
    }

    private void fixPosition() {
        if (cycle <= 0)
            return;

        head.setCenterX(head.getCenterX() + 7.5);
        body.setX(body.getX() + 7.5);
        leftFoot.setX(leftFoot.getX() + 7.5);
        leftFoot.setRotate(0);
        rightFoot.setX(rightFoot.getX() + 7.5);
        rightFoot.setRotate(0);

        --cycle;

        if ((obstacle1.getX() - body.getX() <= 15&& obstacle1.getX() - body.getX() >= 0&&
                obstacle1.getY() - body.getY() <= 15&& obstacle1.getY() - body.getY() >= 0)||
                (body.getX() - obstacle1.getX() <= 15&& body.getX() - obstacle1.getX() >= 0&&
                        body.getY() - obstacle1.getY() <= 15&& body.getY() - obstacle1.getY() >= 0)) {
            isEnd = true;
            Toolkit.getDefaultToolkit().beep();
        }
        if ((obstacle2.getX() - body.getX() <= 15&& obstacle2.getX() - body.getX() >= 0&&
                obstacle2.getY() - body.getY() <= 15&& obstacle2.getY() - body.getY() >= 0)||
                (body.getX() - obstacle2.getX() <= 15&& body.getX() - obstacle2.getX() >= 0&&
                        body.getY() - obstacle2.getY() <= 15&& body.getY() - obstacle2.getY() >= 0)) {
            isEnd = true;
            Toolkit.getDefaultToolkit().beep();
        }
        if ((feed.getX() - body.getX() <= 15&& feed.getX() - body.getX() >= 0&&
                feed.getY() - body.getY() <= 15&& feed.getY() - body.getY() >= 0)||
                (body.getX() - feed.getX() <= 15&& body.getX() - feed.getX() >= 0&&
                        body.getY() - feed.getY() <= 15&& body.getY() - feed.getY() >= 0)) {
            cherryCount+=1000;
            Platform.runLater(() -> cherryLabel.setText(String.valueOf(cherryCount)));
            feed.setX(-20);
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void goOneCycle() {
        if (cycle <= 0|| isEnd)
            return;

        head.setCenterX(head.getCenterX() + 7.5);
        body.setX(body.getX() + 7.5);
        leftFoot.setX(leftFoot.getX() + 7.5);
        if (head.getCenterY() < firstRectangle.getY())
            leftFoot.setRotate(20);
        else leftFoot.setRotate(-20);
        rightFoot.setX(rightFoot.getX() + 7.5);
        if (head.getCenterY() < firstRectangle.getY())
            rightFoot.setRotate(-20);
        else rightFoot.setRotate(20);
    }

    private void gameOver() {
        scene.setOnMousePressed(null);
        scene.setOnKeyPressed(null);

        PathTransition pathTransition1 = new PathTransition();
        FadeTransition fadeTransition1 = new FadeTransition();
        PathTransition pathTransition2 = new PathTransition();
        FadeTransition fadeTransition2 = new FadeTransition();
        PathTransition pathTransition3 = new PathTransition();
        FadeTransition fadeTransition3 = new FadeTransition();
        PathTransition pathTransition4 = new PathTransition();
        FadeTransition fadeTransition4 = new FadeTransition();

        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();

        path1.getElements().add(new MoveTo(head.getCenterX(), head.getCenterY() - 5));
        path1.getElements().add(new LineTo(head.getCenterX(), 700));

        path2.getElements().add(new MoveTo(body.getX() + 7.5, body.getY()));
        path2.getElements().add(new LineTo(body.getX(), 700));

        path3.getElements().add(new MoveTo(leftFoot.getX(), leftFoot.getY()));
        path3.getElements().add(new LineTo(leftFoot.getX(), 700));

        path4.getElements().add(new MoveTo(rightFoot.getX(), rightFoot.getY()));
        path4.getElements().add(new LineTo(rightFoot.getX(), 700));

        pathTransition1.setPath(path1);
        pathTransition2.setPath(path2);
        pathTransition3.setPath(path3);
        pathTransition4.setPath(path4);

        pathTransition1.setNode(head);
        pathTransition2.setNode(body);
        pathTransition3.setNode(leftFoot);
        pathTransition4.setNode(rightFoot);

        pathTransition1.setDuration(Duration.seconds(1));
        pathTransition2.setDuration(Duration.seconds(1));
        pathTransition3.setDuration(Duration.seconds(1));
        pathTransition4.setDuration(Duration.seconds(1));

        fadeTransition1.setNode(head);
        fadeTransition2.setNode(body);
        fadeTransition3.setNode(leftFoot);
        fadeTransition4.setNode(rightFoot);

        fadeTransition1.setFromValue(1);
        fadeTransition2.setFromValue(1);
        fadeTransition3.setFromValue(1);
        fadeTransition4.setFromValue(1);

        fadeTransition1.setToValue(0.3);
        fadeTransition2.setToValue(0.3);
        fadeTransition3.setToValue(0.3);
        fadeTransition4.setToValue(0.3);

        pathTransition1.play();
        pathTransition2.play();
        pathTransition3.play();
        pathTransition4.play();

        fadeTransition1.play();
        fadeTransition2.play();
        fadeTransition3.play();
        fadeTransition4.play();

        notifyGameOver();
    }

    private void saveRecord() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    "C:\\Users\\rishi\\Documents\\best.txt", true)); // you may need to change this

            bw.write(playerName);
            bw.newLine();
            bw.write(String.valueOf(score));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRandoms() {
        int minWidth = 100;
        int maxWidth = 200;
        int firstWidth = Math.abs(random.nextInt()) % (maxWidth - minWidth) + minWidth;
        int secondWidth = Math.abs(random.nextInt()) % (maxWidth - minWidth) + minWidth;
        int height = Math.abs(random.nextInt()) % 300 + 100;

        distance = Math.abs(random.nextInt()) % 400 + 100;
        firstRectangle.setWidth(firstWidth);
        firstRectangle.setHeight(height);
        secondRectangle.setWidth(secondWidth);
        secondRectangle.setHeight(height);

        firstRectangle.setY(660 - height);
        secondRectangle.setY(660 - height);
        secondRectangle.setX(secondWidth + distance);

        firstRectangle.setWidth(firstWidth);
        firstRectangle.setHeight(height);
        secondRectangle.setWidth(secondWidth);
        secondRectangle.setHeight(height);

        int obstacle1MinX = (int) (firstRectangle.getX() + firstRectangle.getWidth() + 50);
        int obstacle1MaxX = (int) (feed.getX() - firstRectangle.getWidth()) - 50;
        int obstacle1X = Math.abs(random.nextInt()) % (obstacle1MaxX - obstacle1MinX) + obstacle1MinX;
        obstacle1.setX(obstacle1X);

        int obstacle2MinX = (int) (feed.getX() + feed.getWidth() + 50);
        int obstacle2MaxX = (int) (secondRectangle.getX() - feed.getX()) - 50;
        int obstacle2X = Math.abs(random.nextInt()) % (obstacle2MaxX - obstacle2MinX) + obstacle2MinX;
        obstacle2.setX(obstacle2X);

        obstacle1.setY(firstRectangle.getY() - 40);
        obstacle2.setY(firstRectangle.getY() + 40);

        line.setStartX(firstWidth);
        line.setEndX(firstWidth);
        line.setStartY(660 - height);
        line.setEndY(660 - height);

        leftFoot.setX(firstWidth / 2);
        rightFoot.setX(firstWidth / 2 + 10);
        body.setX(firstWidth / 2);
        head.setCenterX(firstWidth / 2 + 7.5);

        leftFoot.setY(660 - height - 25);
        rightFoot.setY(660 - height - 25);
        body.setY(660 - height - 25 - 20);
        head.setCenterY(660 - height - 25 - 20 - 5);
    }

    private void setAdjusterAndCommand() {
        scene.setOnMousePressed(e -> {
            adjuster = false;

            Thread thread = new Thread() {
                @Override
                public void run() {

                    if (line.getEndX() - line.getStartX() == 0) {
                        while (!adjuster) {
                            if (e.getButton() == MouseButton.PRIMARY)
                                line.setEndY(line.getEndY() - 2);
                            else line.setEndY(line.getEndY() + 2);

                            try {
                                sleep(40);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            };

            thread.start();


        });

        scene.setOnMouseReleased(e -> adjuster = true);
        scene.setOnKeyPressed(e -> {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    if (e.getCode().equals(KeyCode.ENTER) && line.getEndY() != line.getStartY()) {
                        line.setEndX(line.getEndX() + line.getStartY() - line.getEndY());
                        line.setEndY(line.getStartY());

                        obstacle1.setVisible(true);
                        obstacle2.setVisible(true);
                        feed.setVisible(true);

                        int feedX = Math.abs(random.nextInt()) % (distance - 100) + 50;
                        if (score % 3 == 0) {
                            feed.setX(feedX + firstRectangle.getWidth());
                            if (random.nextInt() > 0)
                                feed.setY(firstRectangle.getY() + 40);
                            else feed.setY(firstRectangle.getY() - 40);
                        }

                        int obstacle1X = Math.abs(random.nextInt()) % ((int)(feed.getX() - firstRectangle.getWidth()) - 50)
                                + 50 + (int)firstRectangle.getWidth();
                        int obstacle2X = Math.abs(random.nextInt()) % ((int)(secondRectangle.getX() - feed.getX()) - 50)
                                + 50 + (int)feed.getX();

                        obstacle1.setX(obstacle1X);
                        obstacle2.setX(obstacle2X);
                        obstacle1.setY(firstRectangle.getY() - 40);
                        obstacle2.setY(firstRectangle.getY() + 40);

                        playGame();
                    }

                    if (e.getCode().equals(KeyCode.SPACE)&& line.getEndY() == line.getStartY()) {
                        moveInverse();
                    }
                }
            };
            thread.start();
        });


    }

    private void moveInverse() {
        if (head.getCenterY() < firstRectangle.getY()) {
            head.setCenterY(head.getCenterY() + 2 * (5 + 20 + 25));
            body.setY(body.getY() + 20 + 25 + 25);
            leftFoot.setY(leftFoot.getY() + 25);
            rightFoot.setY(rightFoot.getY() + 25);
        }

        else if (head.getCenterY() > firstRectangle.getY()) {
            head.setCenterY(head.getCenterY() - 2 * (5 + 20 + 25));
            body.setY(body.getY() - 20 - 25 - 25);
            leftFoot.setY(leftFoot.getY() - 25);
            rightFoot.setY(rightFoot.getY() - 25);
        }
    }

    private final List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver gameObserver){
        observers.add(gameObserver);
    }

    private void notifyScoreUpdate(){
        for(GameObserver gameObserver: observers){
            gameObserver.onScoreUpdate(score);
        }
    }

    private void notifyGameOver() {

        restartButton.setVisible(true);
        restartButton.setDisable(false);

        reviveButton.setVisible(true);
        reviveButton.setDisable(false);

        exitButton.setVisible(true);
        exitButton.setDisable(false);

        for (GameObserver observer : observers) {
            observer.onGameOver();
        }
    }

}
