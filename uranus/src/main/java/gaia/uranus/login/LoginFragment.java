package gaia.uranus.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import gaia.uranus.R;
import gaia.uranus.base.ActionBaseSetting;
import gaia.uranus.base.BaseFragment;

public class LoginFragment extends BaseFragment {

    private EditText eUsername,ePassword;
    private Button login;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        eUsername = (EditText)rootView.findViewById(R.id.username);
        ePassword = (EditText)rootView.findViewById(R.id.password);
        rootView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(eUsername.getText().toString()) && !TextUtils.isEmpty(ePassword.getText().toString())) {
                    showProgress();
                    AVUser.logInInBackground(eUsername.getText().toString(),ePassword.getText().toString(), new LogInCallback() {
                        public void done(AVUser user, AVException e) {
                            if (e == null) {
                                // 登录成功
                                doneProgress();
                                switchFragmentContent(new UserFragment(),R.id.container,true);
                                getActivity().sendBroadcast(new Intent(ActionBaseSetting.UPDATE_MAIN_ACTION));
                            } else {
                                doneFailProgress("登录失败"+e.getCode());
                            }
                        }
                    });
                } else {
                    showToast("账号或密码不能为空");
                }
            }
        });
        rootView.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragmentContent(new RegisterFragment(),R.id.container,true);
            }
        });

    }

}
