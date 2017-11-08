package com.example.firstaidtent.gemtd;

import com.example.firstaidtent.framework.Graphics;

class RemoveGemButton extends Button {
    RemoveGemButton(int x, int y) {
        super(x, y, Constants.BTN_REMOVE_GEM[2], Constants.BTN_REMOVE_GEM[3], Assets.btnRemoveGem);
    }

    @Override
    void actions(GameScreen game) {
        for (Tower t : Tower.getSelectedTowers()) {
            t.remove();
        }
    }

    @Override
    void draw(Graphics g) {
        if (!Tower.getSelectedTowers().isEmpty()) {
            setVisible(true);
        } else {
            setVisible(false);
        }

        super.draw(g);
    }
}
