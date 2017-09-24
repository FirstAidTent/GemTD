package com.example.firstaidtent.gemtd;

class PlaceGemButton extends Button {
    PlaceGemButton(int x, int y) {
        super(x, y, 200, 50, Assets.btnPlaceGem[0]);
    }

    @Override
    void actions(GameScreen game) {
        if (!isActive()) {
            setActive(true);
            this.setImg(Assets.btnPlaceGem[1]);
        } else {
            setActive(false);
            this.setImg(Assets.btnPlaceGem[0]);
        }

    }
}
