package com.jkcq.hrwtv.wu.newversion.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ToastUtils
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.DisplayUtils
import com.jkcq.hrwtv.wu.newversion.adapter.CourseSelectItemDecoration
import com.jkcq.hrwtv.wu.newversion.adapter.UserSelectAdapter
import com.jkcq.hrwtv.wu.newversion.adapter.UserSelectAdapter.Isecelt
import com.jkcq.hrwtv.wu.newversion.adapter.UserSelectItemDecoration
import com.jkcq.hrwtv.wu.newversion.bean.SelectUserBean
import com.jkcq.hrwtv.wu.obsever.AddUseObservable
import kotlinx.android.synthetic.main.activity_flash.*
import kotlinx.android.synthetic.main.include_course_select_title.*
import kotlinx.android.synthetic.main.layout_course_select.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

//PK模式选择课程后选择学员页面
class UserSelectActivity : AppCompatActivity(), Observer {

    var currentMode = Constant.MODE_COURSE

    //临时存储USer对象
    var reMoveList = mutableListOf<Int>()

    var firstCome = false

    var sumCount = 0
    var sumRed = 0
    var sumBlue = 0
    var mDataShowBeans = CopyOnWriteArrayList<UserBean>();
    private var mCourseDatas = arrayListOf<SelectUserBean>()
    lateinit var mAdapter: UserSelectAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_select)
        initView()
        initData()
    }

    fun initView() {
        val layoutManager = GridLayoutManager(this, 4)
        tv_recyclerview.layoutManager = layoutManager
        mAdapter = UserSelectAdapter(this, mCourseDatas)
        mAdapter.setIsecletListen1(object : Isecelt {
            override fun fouce(position: Int) {
                Log.e("fouce", "position=" + position)
                setDotView(false, getCurrentPage(position))
            }

            override fun cancleCheackBox(isSelect: Boolean, position: Int) {
                mCourseDatas[position].isSelect = isSelect
                mALlData!!.get(mCourseDatas.get(position).sn)!!.isSelect = isSelect
                if (isSelect) {
                    mALlData!!.get(mCourseDatas.get(position).sn)!!.currentMod = currentMode
                    mCourseDatas.get(position).currentMod = currentMode
                } else {
                    mCourseDatas.get(position).currentMod = Constant.MODE_ALL
                    mALlData!!.get(mCourseDatas.get(position).sn)!!.currentMod = Constant.MODE_ALL
                }

                calNumberCount()
            }

        })
        tv_recyclerview.addItemDecoration(
            UserSelectItemDecoration(
                DisplayUtils.dip2px(
                    this,
                    15f
                )
            )
        )
        tv_recyclerview.adapter = mAdapter
        updateTime()
        initEvent()
    }

    //计算当前的position在第第几页
    fun getCurrentPage(position: Int): Int {
        for (i in 0..(mTotalPage - 1)) {
            if (position >= i * 12 && position < (i + 1) * 12) {
                return i
            }
        }
        return 0
    }

    fun initEvent() {
        fl_back.setOnClickListener {

            finish()
        }

        fl_sure.setOnClickListener {
            var seletCount = 0
            var redCount = 0;
            var blueCount = 0;
            mALlData.forEach {
                if (it.value!!.isSelect && it.value!!.currentMod == Constant.MODE_PK_BLUE) {
                    blueCount++
                } else if (it.value!!.isSelect && it.value!!.currentMod == Constant.MODE_PK_RED) {
                    redCount++
                }
                if (it.value!!.isSelect) {
                    seletCount++
                }
            }
            when (currentMode) {
                Constant.MODE_COURSE -> {
                    if (seletCount == 0) {
                        ToastUtils.showShort("请选择上课用户")
                        return@setOnClickListener
                    }
                }
                else -> {
                    if (blueCount == 0 || redCount == 0) {
                        ToastUtils.showShort("请选择PK用户")
                        return@setOnClickListener
                    }
                }
            }
            mALlData.forEach {
                if (UserContans.userInfoHashMap.containsKey(it.value.sn)) {
                    var ben = UserContans.userInfoHashMap.get(it.value.sn)
                    ben!!.isSelect = it.value.isSelect
                    ben!!.currentMod = it.value.currentMod
                    if (ben!!.isSelect && ben!!.currentMod == Constant.MODE_PK_BLUE) {
                        blueCount++
                    } else if (ben!!.isSelect && ben!!.currentMod == Constant.MODE_PK_RED) {
                        redCount++
                    }
                    if (ben!!.isSelect) {
                        seletCount++
                    }
                }
            }

            CacheDataUtil.saveUserMap()
            if (firstCome) {
                when (currentMode) {
                    Constant.MODE_COURSE -> {
                        startActivity(
                            Intent(
                                this@UserSelectActivity,
                                NCourseActivity::class.java
                            )
                        )
                    }
                    else -> {
                        startActivity(
                            Intent(
                                this@UserSelectActivity,
                                NPkActivity::class.java
                            )
                        )
                    }
                }
            }
            finish()
        }
    }

    /**
     * 更新标题栏的时间显示
     */
    private fun updateTime() {
    }

    override fun onDestroy() {
        super.onDestroy()
        AddUseObservable.getInstance().deleteObserver(this)
    }

    fun initData() {
        AddUseObservable.getInstance().addObserver(this)
        firstCome = intent.getBooleanExtra("firstCome", false);
        currentMode = intent.getIntExtra("currentMode", Constant.MODE_COURSE);
        getAllData()

        fl_sure.nextFocusDownId = R.id.fl_sure
        fl_sure.nextFocusRightId = R.id.fl_sure
        fl_sure.nextFocusUpId = R.id.fl_sure

        if (currentMode == Constant.MODE_COURSE) {
            fl_sure.nextFocusLeftId = R.id.fl_back
            fl_back.nextFocusUpId = R.id.fl_back
            fl_back.nextFocusRightId = R.id.fl_sure

            layout_red.visibility = View.GONE
            layout_blue.visibility = View.GONE
        } else {
            fl_back.nextFocusUpId = R.id.fl_back
            fl_sure.nextFocusLeftId = R.id.layout_blue
            layout_blue.nextFocusLeftId = R.id.layout_red
            layout_red.nextFocusLeftId = R.id.fl_back
            fl_back.nextFocusRightId = R.id.layout_red
            layout_red.nextFocusRightId = R.id.layout_blue
            layout_blue.nextFocusRightId = R.id.fl_sure


            layout_red.visibility = View.VISIBLE
            layout_blue.visibility = View.VISIBLE
            currentMode = Constant.MODE_PK_RED
            layout_red.requestFocus()
        }
        layout_red.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentMode = Constant.MODE_PK_RED
                getUsers()
            } else {
            }
        }
        layout_blue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentMode = Constant.MODE_PK_BLUE
                getUsers()
            } else {
            }
        }
        getHeartRateClass()
    }

    fun getHeartRateClass() {
        getUsers()

    }

    var mALlData = ConcurrentHashMap<String, SelectUserBean>()

    fun getAllData() {
        mALlData.clear()
        UserContans.userInfoHashMap.forEach {
            mALlData.put(
                it.value.sn,
                SelectUserBean(
                    it.value.sn,
                    it.value.nickname,
                    it.value.avatar,
                    it.value.currentMod,
                    it.value.isSelect,
                    it.value.joinTime
                )
            )
        }
    }

    //第一次初始化数据

    fun getUsers() {
        sumCount = 0
        sumRed = 0
        sumBlue = 0
        mAdapter.setCurrentMode(currentMode)
        mCourseDatas.clear()
        mALlData.forEach {
            Log.e(
                "getUsers",
                "currentMode=" + currentMode + " it.value.currentMod=" + it.value.currentMod
            )
            when (currentMode) {
                Constant.MODE_COURSE -> {
                    it.value.currentMod = currentMode
                    mCourseDatas.add(
                        it.value
                    )
                }
                Constant.MODE_PK_BLUE -> {
                    if (it.value.isSelect) {
                        sumCount++
                        if (it.value.currentMod == Constant.MODE_PK_RED) {
                            sumRed++

                        } else {
                            sumBlue++
                            mCourseDatas.add(
                                it.value
                            )
                        }

                        //  it.value.currentMod = currentMode
                    } else {
                        it.value.currentMod = Constant.MODE_ALL
                        mCourseDatas.add(
                            it.value
                        )
                    }


                }
                Constant.MODE_PK_RED -> {
                    if (it.value.isSelect) {
                        sumCount++
                        if (it.value.currentMod == Constant.MODE_PK_BLUE) {
                            sumBlue++
                        } else {
                            sumRed++
                            mCourseDatas.add(
                                SelectUserBean(
                                    it.value.sn,
                                    it.value.nickname,
                                    it.value.avatar,
                                    it.value.currentMod,
                                    it.value.isSelect, it.value.jointime
                                )
                            )
                        }
                        //it.value.currentMod = currentMode
                    } else {
                        it.value.currentMod = Constant.MODE_ALL
                        mCourseDatas.add(
                            it.value
                        )
                    }
                }
                else -> {

                }
            }

        }
        Log.e("getUsers", "getUsers----" + mCourseDatas.size)
        mCourseDatas.sortBy { Constant.TYPE_CAL }
        mAdapter.replaceData(mCourseDatas)
        calNumberCount()
        var size = mCourseDatas.size / 12
        if (mCourseDatas.size % 12 != 0) {
            size += 1
        }
        mTotalPage = size
        setDotView(true, 0)
    }


    fun calNumberCount() {
        sumCount = 0
        sumRed = 0
        sumBlue = 0
        mALlData.forEach {
            if (it.value.isSelect) {
                sumCount++
                when (it.value.currentMod) {
                    Constant.MODE_PK_RED -> {
                        sumRed++
                    }
                    Constant.MODE_PK_BLUE -> {

                        sumBlue++
                    }
                    Constant.MODE_COURSE -> {

                    }
                }
            }
        }

        setNumberCount()
    }

    fun setNumberCount() {
        tv_number_count.text = sumCount.toString()
        tv_pk_red_count.text = sumRed.toString()
        tv_pk_blue_count.text = sumBlue.toString()
    }

    var mTotalPage = 0

    /**
     * 轮播点的绘制
     */
    private var mDotViews = ArrayList<ImageView>();

    private fun setDotView(isReset: Boolean = true, indexChecked: Int = 0) {
//        Log.e("test", "isrReset=" + isReset + "indexChecked=" + indexChecked + "dotSize=" + mDotViews.size)
        if (mTotalPage < 1) {
            return;
        }
        if (isReset) {
            ll_point.removeAllViews()
            mDotViews.clear()
            for (i in 0..(mTotalPage - 1)) {
                val image = ImageView(this)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = DisplayUtils.dip2px(this, 8f)
                if (i == indexChecked) {
                    image.setImageResource(R.drawable.icon_point_select)
                } else {
                    image.setImageResource(R.drawable.icon_point_unselect)
                }
                ll_point.addView(image, params)
                mDotViews.add(image)
            }
        } else {
            for (i in 0..(mTotalPage - 1)) {
                if (i == indexChecked) {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_select)
                } else {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_unselect)
                }
            }
        }
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * `notifyObservers` method to have all the object's
     * observers notified of the change.
     *
     * @param   o     the observable object.
     * @param   arg   an argument passed to the `notifyObservers`
     * method.
     */
    override fun update(o: Observable?, arg: Any?) {
        if (o is AddUseObservable) {
            updateuserInfo()
        }
    }

    fun updateuserInfo() {

        //  mALlData.clear()

        Log.e(
            "AddUseObservable",
            "addUserNotify2 userInfoHashMap" + UserContans.userInfoHashMap.size
        )
        UserContans.userInfoHashMap.forEach {

            if (!mALlData.containsKey(it.key)) {
                mALlData.put(
                    it.key,
                    SelectUserBean(
                        it.value.sn,
                        it.value.nickname,
                        it.value.avatar,
                        it.value.currentMod,
                        it.value.isSelect,
                        it.value.joinTime
                    )
                )
            }

        }
        Log.e("AddUseObservable", "addUserNotify3")
        getUsers()
    }

}
