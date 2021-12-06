package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jkcq.hrwtv.R;

public class DialogYesOrNo extends Dialog {
    private Context mContext;
    public OnButtonClick onClickShowView;
    public String titleMsg;

    public DialogYesOrNo(Context context, OnButtonClick onClickShowView, String titelMsg) {

        this(context, R.style.MyDialogStyle, onClickShowView, titelMsg);

    }

    public DialogYesOrNo(Context context, int themeResId, OnButtonClick onClickShowView, String titleMsg) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickShowView = onClickShowView;
        this.titleMsg = titleMsg;
        initView();
    }

    protected DialogYesOrNo(Context context, boolean cancelable, OnCancelListener cancelListener, OnButtonClick onClickShowView) {
        super(context, cancelable, cancelListener);
        this.onClickShowView = onClickShowView;
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_yesorno, null);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        //设置Dialog大小位置
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        int desity = (int) ScreenUtils.getScreenDensity();
//        lp.width= LinearLayout.LayoutParams.MATCH_PARENT;
//        lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
       /* int width = (int) (ScreenUtils.getScreenWidth() * 0.4);
        int height = (int) (width * 0.6);*/
        int width = (int) (mContext.getResources().getDimension(R.dimen.dialog_yes_with));
        int height = (int) (mContext.getResources().getDimension(R.dimen.dialog_yes_height));
        lp.width = width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);

        final TextView btn_cancel = view.findViewById(R.id.btn_cancel);
        final TextView btn_sure = view.findViewById(R.id.btn_sure);
        final TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(titleMsg);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickShowView != null) {
                    onClickShowView.onButtonClickSure();
                    dismiss();
                }
            }
        });
        btn_sure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btn_sure.setBackgroundResource(com.beyondworlds.managersetting.R.drawable.shape_btn_selected_bg);
                } else {
                    btn_sure.setBackgroundResource(com.beyondworlds.managersetting.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickShowView != null) {
                    onClickShowView.onButtonClickCancel();
                    dismiss();
                }
            }
        });
        btn_cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btn_cancel.setBackgroundResource(com.beyondworlds.managersetting.R.drawable.shape_btn_selected_bg);
                } else {
                    btn_cancel.setBackgroundResource(com.beyondworlds.managersetting.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
    }


    public interface OnButtonClick {
        public void onButtonClickSure();

        public void onButtonClickCancel();
    }
}
