package com.jkcq.hrwtv.wu.newversion.activity.mamager;


import com.beyondworlds.managersetting.BaseView;
import com.beyondworlds.managersetting.bean.BrandInfo;
import com.beyondworlds.managersetting.bean.ClassRoomInfo;
import com.beyondworlds.managersetting.bean.DeviceTypeInfo;
import com.beyondworlds.managersetting.bean.RegisterInfo;
import com.beyondworlds.managersetting.bean.VersionInfo;

import java.util.List;

/*
 *
 *
 * @author mhj
 * Create at 2019/1/29 14:52
 */public interface ManagerMainView extends BaseView {


    void onCheckUpdateSuccess(VersionInfo versionInfo);

    void onClearCacheSuccess();


}
