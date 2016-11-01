package gaia.uranus.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import gaia.uranus.R;
import gaia.uranus.base.BaseFragment;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    private EditText phoneEditText, passwordEditText, codeEditText, usernameEditText;
    private Button codeBt, registBt;
    private View rootView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        context = this.getActivity();
        initSMS();
        initView();
        return rootView;
    }



    private void initSMS() {
        SMSSDK.initSDK(this.getActivity(), "f70ddb9262b0", "3bad67361608894c4cb059fcb73301ce", false);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    register();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(context, "验证码已经发送", Toast.LENGTH_SHORT).show();
                }

            } else {
//				((Throwable) data).printStackTrace();
                Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();

            }


        }

        ;
    };

    private void register() {
        showProgress();
        AVUser user = new AVUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.setMobilePhoneNumber(phoneEditText.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    doneSuccessProgress("注册成功,请登录");
                    switchFragmentContent(new LoginFragment(),R.id.container,false);
                } else {
                    doneFailProgress("注册失败"+e.getMessage());
                }
            }
        });
    }


    private void initView() {
        phoneEditText = (EditText) rootView.findViewById(R.id.phone_edtext);
        passwordEditText = (EditText) rootView.findViewById(R.id.password_edtext);
        codeEditText = (EditText) rootView.findViewById(R.id.verificate_code);
        usernameEditText = (EditText) rootView.findViewById(R.id.account_edtext);

        codeBt = (Button) rootView.findViewById(R.id.get_code_button);
        registBt = (Button) rootView.findViewById(R.id.register_bt);

        codeBt.setOnClickListener(this);
        registBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code_button:
                if (!TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    SMSSDK.getVerificationCode("86", phoneEditText.getText().toString());
                } else {
                   showToast("手机号码不能为空");
                }
                break;
            case R.id.register_bt:
                register();
//                if (TextUtils.isEmpty(phoneEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString()) || TextUtils.isEmpty(usernameEditText.getText().toString()) || TextUtils.isEmpty(codeEditText.getText().toString())) {
//                    showTip("请填完整信息");
//                    return;
//                }
//                SMSSDK.submitVerificationCode("86", phoneEditText.getText().toString(), codeEditText.getText().toString());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

}
