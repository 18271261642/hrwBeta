package com.juxun.physical.http

import com.beyondworlds.managersetting.bean.VersionInfo
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.http.bean.*
import com.jkcq.hrwtv.http.bean.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*


/**
 *created by wq on 2019/3/25
 */
interface ApiService {


    /**
     * 注册设备
     *
     */

    @POST("/api/wall-managers/device-register")
    fun deviceRegister(@Body map: Map<String, String>): Observable<BaseResponse<TokenBean>>

    /**
     *获得心率墙用户登录信息
     */
    @POST("/api/wall-managers/refresh-login-info")
    fun refreshLoginInfo(@Body map: Map<String, String>): Observable<BaseResponse<AloneClubInfoBean>>


    /**
     * /api/wall-managers/logout
     */
    @POST("/api/wall-managers/logout")
    fun devcieLogout(@Body map: Map<String, String>): Observable<BaseResponse<Boolean>>


    /**
     * 通过SN吗值读取用户信息
     *api/heartratewall/member/sn-list
     */
    @GET("/api/heartratewall/member/sn-list")
    fun getSnListToUserInfo(@Query("snList") snList: String): Observable<BaseResponse<List<UserBean>>>


    /**
     * 上报数据
     *
     */
    @POST("/api/heartratewall/exercise-record/report")
    fun report(@Body body: RequestBody): Observable<BaseResponse<Boolean>>

    /**
     * 获取课程列表
     */
    @GET("/api/heartratewall/heart-rate-course/tv-course-list")
    fun getCourseList(): Observable<BaseResponse<List<CourseInfo>>>

    /**
     * 获取版本信息
     * http://localhost:48080/api/heartratewall/device-version/latest-version?deviceType=receiver
     */
    @GET("/api/heartratewall/device-version/latest-version")
    fun getVersionInfo(@Query("deviceType") deviceTypeId: String): Observable<BaseResponse<VersionInfo>>

}