package com.iiifi.shop.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.iiifi.shop.activity.LoginActivity;
import com.iiifi.shop.activity.R;
import com.iiifi.shop.activity.RegisterActivity;
import com.iiifi.shop.activity.SpeedActivity;

/**
 * Created by dmm on 2017/3/25.
 */

public class RegisterView {

    private static boolean IS_SEE=false;

    private RegisterActivity registerActivity;

    //手机号码正则表达式
    private static final String phoneRegex = "^1(3|4|5|7|8)\\d{9}";

    //密码的正则表达式
    private static final String passwordRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

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
     * 密码
     */
    private  EditText etPassword;
    private  ImageView passwordDel;
    private  ImageView see_password;

    /**
     * 注册
     */
    private Button registerBtn;

    /**
     * 第三方登录
     */
    private ImageView ivQQ;

    private ImageView ivSina;

    private ImageView ivWeixin;

    /**
     * 登录
     */
    private TextView loginBtn;

    /**
     * 快速登录
     */
    private TextView speedLogin;

    //编译控件
    public static void build(RegisterActivity registerActivity){
        new RegisterView(registerActivity);
    }

    public  RegisterView(RegisterActivity registerActivity){
        this.registerActivity=registerActivity;
        //初始化控件
        initView(registerActivity);
        //初始化事件
        initEvent();
        //初始化是否可点击
        initClickEffect();
    }


    /**
     * 初始化控件
     * @param registerActivity
     */
    public  void initView(RegisterActivity registerActivity){
        //登录名
        etLoginName= (EditText) registerActivity.findViewById(R.id.et_login_name);
        loginNameDel= (ImageView) registerActivity.findViewById(R.id.login_name_del);
        //验证码
        etSmsCode = (EditText) registerActivity.findViewById(R.id.et_sms_code);
        smsCodeDel = (ImageView) registerActivity.findViewById(R.id.sms_code_del);
        sendCode = (TextView) registerActivity.findViewById(R.id.send_code);
        //密码
        etPassword = (EditText) registerActivity.findViewById(R.id.et_paswword);
        passwordDel = (ImageView) registerActivity.findViewById(R.id.password_del);
        see_password = (ImageView) registerActivity.findViewById(R.id.see_password);
        //注册按钮
        registerBtn = (Button) registerActivity.findViewById(R.id.register_btn);
        //第三方登录
        ivQQ = (ImageView) registerActivity.findViewById(R.id.iv_qq);
        ivSina = (ImageView) registerActivity.findViewById(R.id.iv_sina);
        ivWeixin = (ImageView) registerActivity.findViewById(R.id.iv_weixin);
        //快速登录
        speedLogin = (TextView) registerActivity.findViewById(R.id.speed_login);
        //登录
        loginBtn = (TextView) registerActivity.findViewById(R.id.login_btn);
    }

    /**
     * 初始化事件
     */
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
                        sendCode.setTextColor(registerActivity.getResources().getColor(R.color.text_color_getverfy));
                        sendCode.setBackgroundResource(R.mipmap.login_testgetcode_box);
                    }else{
                        sendCode.setClickable(false);
                        sendCode.setTextColor(registerActivity.getResources().getColor(R.color.text_color_gray));
                        sendCode.setBackgroundResource(R.mipmap.login_register_code);
                    }
                } else {
                    loginNameDel.setVisibility(View.GONE);
                }
                //判断注册按钮是否可点击
                checkRegister();
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
                checkRegister();
            }
        });
        //验证码框数据清除
        smsCodeDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etSmsCode.setText("");
            }
        });
        //密码输入框设置监听
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etPassword.getText())) {
                    passwordDel.setVisibility(View.VISIBLE);
                } else {
                    passwordDel.setVisibility(View.GONE);
                }
                //判断注册按钮是否可点击
                checkRegister();
            }
        });
        //密码框数据清除
        passwordDel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                etPassword.setText("");
            }
        });
        //切换密码是否可见
        see_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (IS_SEE){
                    see_password.setImageResource(R.mipmap.not_see);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    IS_SEE=false;
                }else{
                    see_password.setImageResource(R.mipmap.can_see);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    IS_SEE=true;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"点击注册按钮",Toast.LENGTH_SHORT).show();
            }
        });

        //返回登录界面
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                registerActivity.finish();
            }
        });

        //前往快速登录界面
        speedLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(registerActivity,SpeedActivity.class);
                registerActivity.startActivity(intent);
            }
        });
    }

    //初始化点击事件效果
    public void initClickEffect(){
        sendCode.setClickable(false);
        registerBtn.setClickable(false);
    }
    //校验注册按钮是否可点击
    public void checkRegister(){
        if(checkLoginName()&&checkPassword()&&checkSmsCode()){
            registerBtn.setClickable(true);
            registerBtn.setBackgroundResource(R.mipmap.login_bluebutton_selected);
        }else{
            registerBtn.setClickable(false);
            registerBtn.setBackgroundResource(R.mipmap.login_btn_one);
        }
    }
    //校验登录名是否合法
    public  boolean checkLoginName(){
        String loginName=etLoginName.getText().toString().trim();
        return (!TextUtils.isEmpty(loginName))&&loginName.matches(phoneRegex);
    }
    //校验密码是否合法
    public boolean checkPassword(){
        String password=etPassword.getText().toString().trim();
        return (!TextUtils.isEmpty(password))&&password.matches(passwordRegex);
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
            sendCode.setTextColor(registerActivity.getResources().getColor(R.color.text_color_gray));
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
                sendCode.setTextColor(registerActivity.getResources().getColor(R.color.text_color_getverfy));
                sendCode.setBackgroundResource(R.mipmap.login_testgetcode_box);
            }else {
                sendCode.setText("获取验证码");
            }
            timer=null;
        }
    }

}
