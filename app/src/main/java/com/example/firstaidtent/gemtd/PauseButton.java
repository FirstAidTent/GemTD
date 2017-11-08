package com.example.firstaidtent.gemtd;

class PauseButton extends Button {
    PauseButton(int x, int y) {
        super(x, y, Constants.BTN_PAUSE[2], Constants.BTN_PAUSE[3], Assets.btnPause);
    }

    @Override
    void actions(GameScreen game) {
        game.pause();
    }
}
