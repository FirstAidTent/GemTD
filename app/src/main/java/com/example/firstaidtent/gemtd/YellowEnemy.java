package com.example.firstaidtent.gemtd;

class YellowEnemy extends Enemy {
    YellowEnemy() {
        super(0, 0, 2, Assets.yellowEnemy[0]);
    }

    YellowEnemy(double centerX, double centerY) {
        super(centerX, centerY, 20, Assets.yellowEnemy[0]);
    }

    @Override
    double getOriginalSpeed() {
        return 150.00;
    }
}
