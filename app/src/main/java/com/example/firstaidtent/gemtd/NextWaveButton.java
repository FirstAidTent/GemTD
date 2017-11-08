package com.example.firstaidtent.gemtd;

import com.example.firstaidtent.framework.Graphics;

public class NextWaveButton extends Button {
    NextWaveButton(int x, int y) {
        super(x, y, Constants.BTN_NEXT_WAVE[2], Constants.BTN_NEXT_WAVE[3], Assets.btnNextWave);
    }

    @Override
    void actions(GameScreen game) {
        game.startWave();
        setVisible(false);
    }

    @Override
    void draw(Graphics g) {
        if (!Tower.getSelectedTowers().isEmpty()) {
            super.draw(g);
        }
    }
}
