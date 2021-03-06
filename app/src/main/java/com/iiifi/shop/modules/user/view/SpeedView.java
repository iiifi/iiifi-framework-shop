package com.iiifi.shop.modules.user.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iiifi.shop.modules.syscenter.activity.CommonWebActivity;
import com.iiifi.shop.activity.R;
import com.iiifi.shop.modules.user.activity.SpeedActivity;
import com.iiifi.shop.common.base.view.BaseActivityView;
import com.iiifi.shop.common.util.ToolBarUtil;

/**
 * Created by dmm on 2017/3/25.
 */

public class SpeedView extends BaseActivityView<SpeedActivity>{


    private SpeedActivity speedActivity;

    /**
     * title
     */
    private static final String TOOL_TITLE="快速登录";

    //手机号码正则表达式
    private static final String phoneRegex = "^1(3|4|5|7|8)\\d{9}";

    //验证码正则表达式
    private static final String smsCodeRegex = "^\\d{4}";

    //发送验证码定时器
    private VerifyCodeTimer timer;
    /**
     * 登录名
     */
    private EditText etLoginName;
    private ImageView loginNameDel;

    /**
     * 验证码
     */
    private EditText etSmsCode;
    private ImageView smsCodeDel;
    private TextView sendCode;

    /**
     * 注册
     */
    private Button speedLoginBtn;
    /**
     * 用户协议
     */
    private TextView userProtocol;
    /**
     * 第三方登录
     */
    private ImageView ivQQ;

    private ImageView ivSina;

    private ImageView ivWeixin;




    public  SpeedView(SpeedActivity speedActivity){
        super(speedActivity);
    }

    //编译控件
    @Override
    public void build(SpeedActivity speedActivity){
        this.speedActivity=speedActivity;
    }

    /**
     * 初始化控件
     */
    @Override
    public  void initView(){
        //设置个性化ToolBar
        ToolBarUtil.buildActivityToolBar(speedActivity,true,true,TOOL_TITLE,true,false,0);
        //登录名
        etLoginName= (EditText) speedActivity.findViewById(R.id.et_login_name);
        loginNameDel= (ImageView) speedActivity.findViewById(R.id.login_name_del);
        //验证码
        etSmsCode = (EditText) speedActivity.findViewById(R.id.et_sms_code);
        smsCodeDel = (ImageView) speedActivity.findViewById(R.id.sms_code_del);
        sendCode = (TextView) speedActivity.findViewById(R.id.send_code);
        //注册按钮
        speedLoginBtn = (Button) speedActivity.findViewById(R.id.speed_login);
        //用户协议
        userProtocol = (TextView) speedActivity.findViewById(R.id.user_protocol);
        //第三方登录
        ivQQ = (ImageView) speedActivity.findViewById(R.id.iv_qq);
        ivSina = (ImageView) speedActivity.findViewById(R.id.iv_sina);
        ivWeixin = (ImageView) speedActivity.findViewById(R.id.iv_weixin);
    }

    /**
     * 初始化事件
     */
    @Override
    public void initEvent(){
        //登录名输入框设置监听
        etLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber=etLoginName.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    loginNameDel.setVisibility(View.VISIBLE);
                    //根据手机号状态判发送验证码是否可点击
                    if(checkLoginName()&&isCreate()){
                        sendCode.setClickable(true);
                        etLoginName.setVisibility(View.VISIBLE);
                        sendCode.setTextColor(speedActivity.getResources().getColor(R.color.text_color_getverfy));
                        sendCode.setBackgroundResource(R.mipmap.login_testgetcode_box);
                    }else{
                        sendCode.setClickable(false);
                        sendCode.setTextColor(speedActivity.getResources().getColor(R.color.text_color_gray));
                        sendCode.setBackgroundResource(R.mipmap.login_register_code);
                    }
                } else {
                    loginNameDel.setVisibility(View.GONE);
                }
                //判断注册按钮是否可点击
                checkSppedLogin();
            }
        });
        //登录框数据清除
        loginNameDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etLoginName.setText("");
            }
        });

        //发送验证码事件
        sendCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //判断登录名是否合法
                if(!checkLoginName()){
                    sendCode.setClickable(false);
                }else{
                    etLoginName.clearFocus();
                    etSmsCode.requestFocus();
                    //发送验证码处理事件
                    sendCode.setClickable(false);
                    timer = new VerifyCodeTimer(6000, 1000);
                    timer.start();
                }
            }
        });

        //验证码输入框设置监听
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etSmsCode.getText())) {
                    smsCodeDel.setVisibility(View.VISIBLE);
                } else {
                    smsCodeDel.setVisibility(View.GONE);
                }
                //判断注册按钮是否可点击
                checkSppedLogin();
            }
        });
        //验证码框数据清除
        smsCodeDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etSmsCode.setText("");
            }
        });

        /**
         * 快速登录
         */
        speedLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击注册按钮",Toast.LENGTH_SHORT).show();
            }
        });
        //用户协议
        //用户协议
        userProtocol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(speedActivity, CommonWebActivity.class);
                intent.putExtra("loadUrl","http://wap.edencity.com/wx/html/user/tashuoService.html");
                speedActivity.startActivity(intent);
            }
        });
    }

    //初始化点击事件效果
    @Override
    public void initClickEffect(){
        sendCode.setClickable(false);
        speedLoginBtn.setClickable(false);
    }

    @Override
    public void initData() {

    }

    //校验注册按钮是否可点击
    public void checkSppedLogin(){
        if(checkLoginName()&&checkSmsCode()){
            speedLoginBtn.setClickable(true);
            speedLoginBtn.setBackgroundResource(R.mipmap.login_bluebutton_selected);
        }else{
            speedLoginBtn.setClickable(false);
            speedLoginBtn.setBackgroundResource(R.mipmap.login_btn_one);
        }
    }
    //校验登录名是否合法
    public  boolean checkLoginName(){
        String loginName=etLoginName.getText().toString().trim();
        return (!TextUtils.isEmpty(loginName))&&loginName.matches(phoneRegex);
    }
    //校验验证码是否合法
    public boolean checkSmsCode(){
        String smsCode=etSmsCode.getText().toString().trim();
        return (!TextUtils.isEmpty(smsCode))&&smsCode.matches(smsCodeRegex);
    }
    //判断定时器是否被创建
    private boolean isCreate() {
        if (timer == null) {
            return true;
        }
        return false;
    }

    /**
     * 获取验证码的定时器
     */
    class VerifyCodeTimer extends CountDownTimer {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public VerifyCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            //定时器被创建的时候获取验证码的按钮不能点击
            sendCode.setClickable(false);
//            et_phone.setEnabled(false);
            sendCode.setTextColor(speedActivity.getResources().getColor(R.color.text_color_gray));
            sendCode.setBackgroundResource(R.mipmap.login_register_code);
        }


        //计时开始时的方法
        @Override
        public void onTick(long millisUntilFinished) {
            sendCode.setText(millisUntilFinished / 1000 + "s");
            sendCode.setClickable(false);
        }

        //计时结束后的方法
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onFinish() {
            //将获取验证码变成可点击的
            if(checkLoginName()){
                sendCode.setClickable(true);
                sendCode.setText("重新获取");
                sendCode.setTextColor(speedActivity.getResources().getColor(R.color.text_color_getverfy));
                sendCode.setBackgroundResource(R.mipmap.login_testgetcode_box);
            }else {
                sendCode.setText("获取验证码");
            }
            timer=null;
        }
    }

}
