package com.example.application;

import com.example.application.Main;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class StickHeroTest {

    @Before
    public void setUpJavaFX() {
        new JFXPanel(); // Initializes the JavaFX runtime to allow JavaFX components to be tested.
    }

    @Test
    public void testStartGame() throws InterruptedException {
    }
    @Test
    public void testOnScoreUpdate() {
        Main main = new Main();
        main.onScoreUpdate(100); // Test the onScoreUpdate method
    }
}