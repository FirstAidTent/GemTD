package com.example.firstaidtent.gemtd;

class PauseButton extends Button {
    PauseButton(int x, int y) {
        super(x, y, 50, 50, Assets.btnPause);
    }

    @Override
    void actions(GameScreen game) {
        game.pause();
    }
}
