package com.jkcq.hrwtv.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.BaseResponse
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.http.widget.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_one_layout.*

class TestOne : AppCompatActivity() {

    private  val tgs = "TestOne"

    private var testBtn1 : Button ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_one_layout)


        initViews();
    }



    private fun initViews(){

        testBtn1 = findViewById(R.id.testBtn1)


        testBtn1?.setOnClickListener {
            getHeartRateClass()
        }
    }


    fun getHeartRateClass() {
        RetrofitHelper.service.getCourseList(
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<List<CourseInfo>>>() {
                override fun onSuccess(baseResponse: BaseResponse<List<CourseInfo>>) {
                    baseResponse.data?.let {

                        Log.e(tgs, "----="+Gson().toJson(baseResponse))
                    }
                }
            })


    }

}