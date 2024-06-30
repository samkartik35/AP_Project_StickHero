package com.example.application;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Player {
    private Rectangle leftFoot;
    private Rectangle rightFoot;
    private Rectangle body;
    private Circle head;
    private int score;
    private Label scoreLabel;

    public Player(){
        this.leftFoot = new Rectangle(5, 25);
        rightFoot = new Rectangle(5, 25);
        body = new Rectangle(15, 20);
        head = new Circle(5);
        scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setLayoutX(175);
        score = 0;

        leftFoot.setX(-20);
        rightFoot.setX(-20);
        body.setX(-20);
        head.setCenterX(-20);

        leftFoot.setY(0);  // Set Y-coordinate based on your game's layout
        rightFoot.setY(0);
        body.setY(0);
        head.setCenterY(0);

    }

    public Rectangle getLeftFoot() {
        return leftFoot;
    }

    public void setLeftFoot(Rectangle leftFoot) {
        this.leftFoot = leftFoot;
    }

    public Rectangle getRightFoot() {
        return rightFoot;
    }

    public void setRightFoot(Rectangle rightFoot) {
        this.rightFoot = rightFoot;
    }

    public Rectangle getBody() {
        return body;
    }

    public void setBody(Rectangle body) {
        this.body = body;
    }

    public Circle getHead() {
        return head;
    }

    public void setHead(Circle head) {
        this.head = head;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        updateScoreLabel();
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    private void updateScoreLabel() {
        // Update the score label text
        scoreLabel.setText(String.valueOf(score));
    }
}
