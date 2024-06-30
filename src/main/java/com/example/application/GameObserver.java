package com.example.application;

import javafx.scene.control.Button;

public interface GameObserver {

    void onScoreUpdate(int score);
    void onGameOver();

    void onGameRestart();
}
