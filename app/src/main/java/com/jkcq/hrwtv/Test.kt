package com.jkcq.hrwtv

import android.os.CountDownTimer

class Test(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
    }

    override fun onFinish() {

    }
}
