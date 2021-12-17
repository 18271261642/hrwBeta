package com.jkcq.hrwtv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.UserBean;
import com.jkcq.hrwtv.http.RetrofitHelper;
import com.jkcq.hrwtv.http.bean.BaseResponse;
import com.jkcq.hrwtv.http.widget.BaseObserver;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.UserInfoUtil;
import com.jkcq.hrwtv.wu.obsever.AddUseObservable;
import com.jkcq.hrwtv.wu.obsever.HrDataObservable;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 用于全局处理
 */
public class OperateUserSnService extends Service implements Observer{

    private static final String TAG = "OperateUserSnService";

    //用户存放ANT发送过来的sn码
    private final StringBuffer stringBuffer = new StringBuffer();



    @Override
    public void onCreate() {
        super.onCreate();
        HrDataObservable.getInstance().addObserver(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new OperateBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HrDataObservable.getInstance().deleteObserver(this);
    }

    //刷新时间，每10S刷新一次
    int count = 0;

    //接收ANT发送的sn数据
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof HrDataObservable ){
            if (count == 0 || count % 10 == 0) {    //每10s刷新一次
                doCommonHRTask(arg);

                //查询正在活动的标签
                queryActiveMarkSn();
            }
            count++;
            if (count > 10) {
                count = 0;
            }
        }
    }

    //查询正在活动的标签
    private void queryActiveMarkSn() {
        RetrofitHelper.INSTANCE.getService().getActiveMarkSnTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Object>>() {
                    @Override
                    public void onSuccess(BaseResponse<Object> stringBaseResponse) {
                        Log.e(TAG,"----正在活动的标签="+new Gson().toJson(stringBaseResponse));
                        try {
                            Map<String,List<String>> snStr = (Map<String, List<String>>) stringBaseResponse.getData();
                            if(snStr != null){
                                List<String> snList = snStr.get("snList");
                                if(snList != null && !snList.isEmpty()){
                                    for(String st : snList){
                                        UserContans.markTagsMap.put(st,-1);
                                    }

                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG,"----正在活动的标签onError="+e.getMessage());
                    }

                    @Override
                    public void dealError(String msg) {
                        super.dealError(msg);
                        Log.e(TAG,"----正在活动的标签dealError="+msg);
                    }
                });

    }

    private void doCommonHRTask(Object arg) {
        ConcurrentHashMap<String, Integer> snList = (ConcurrentHashMap<String, Integer>) arg;
        Log.e(TAG,"----service接收数据="+new Gson().toJson(snList));
        stringBuffer.delete(0,stringBuffer.length());
        for(Map.Entry<String,Integer> mmp : snList.entrySet()){
            String snStr = mmp.getKey();
            int heartV = mmp.getValue();
           // stringBuffer.append(snStr).append(",");
            if (!UserContans.userInfoHashMap.containsKey(snStr)) {
                if (!UserContans.mCacheMap.containsKey(snStr)) {
                    //1.查询用户信息
                    stringBuffer.append(snStr).append(",");
                }
            }
        }

        if(stringBuffer.length()>0)
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        uploadDevciesSN(stringBuffer.toString());
    }


    private void uploadDevciesSN(String snListStr) {
        Log.e(TAG,"----数据参数="+snListStr);
        RetrofitHelper.INSTANCE.getService().getSnListToUserInfo(snListStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<List<UserBean>>>() {

                               @Override
                               public void onSuccess(BaseResponse<List<UserBean>> listBaseResponse) {
                                   if(listBaseResponse != null)
                                    Log.e(TAG,"-------数据返回="+listBaseResponse.getData().size()+"\n"+new Gson().toJson(listBaseResponse));
                                   assert listBaseResponse != null;
                                   saveUserLists(listBaseResponse.getData());
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                               }

                               @Override
                               public void dealError(String msg) {
                                   super.dealError(msg);
                               }
                           }
                );
    }



    private void saveUserLists(List<UserBean> ltUser)  {
        try {
            CacheDataUtil.getUserMap();
            for(UserBean userBean : ltUser){
                if(userBean.getId() != null){
                    userBean.setAge( UserInfoUtil.parseAge(userBean.getBirthday()));
                    userBean.setJoinTime(System.currentTimeMillis());
                    if (!UserContans.userInfoHashMap.containsKey(userBean.getSn())) {
                        UserContans.userInfoHashMap.put(userBean.getSn(), userBean);
                    }
                    UserContans.mCacheMap.put(userBean.getSn(), userBean.getSn());
                }
            }

            CacheDataUtil.saveUserMap();

            AddUseObservable.getInstance().addUserNotify();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public class OperateBinder  extends Binder{
        public OperateUserSnService getOperateService(){
            return OperateUserSnService.this;
        }
    }


}
