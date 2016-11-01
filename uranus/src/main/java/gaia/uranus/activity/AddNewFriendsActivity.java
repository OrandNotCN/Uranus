package gaia.uranus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gaia.uranus.R;
import gaia.uranus.core.ApiCore;
import gaia.uranus.utils.LogUtils;

public class AddNewFriendsActivity extends Activity {


    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.header_delete)
    ImageView headerDelete;
    @Bind(R.id.tip)
    TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchPhone();
                }
                return false;
            }

        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tip.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phone.getText().length() > 0) {
                    headerDelete.setVisibility(View.VISIBLE);
                } else {
                    headerDelete.setVisibility(View.GONE);
                }
            }
        });
    }

    private void searchPhone() {
//        if (!StringUtils.isMobileNO(phone.getText().toString().trim())) {
//            showToast("输入手机格式有误");
//            return;
//        }
        ApiCore.searchFriends(phone.getText().toString().trim(), new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (avObjects == null || avObjects.size()<=0) {
                    tip.setText("无此用户");
                    return;
                }
                AVObject avoj = avObjects.get(0);
                if (e == null) {
                    if (AVUser.getCurrentUser().getObjectId().equals(avoj.getObjectId())) {
                        tip.setText("不能添加自己");
                    } else {
//                        Log.e("<<<<<<<<<<<<", avObjects.get(0).get("objectId").toString());
                        Intent intent = new Intent(AddNewFriendsActivity.this, ContactorInfoActivity.class);
                        intent.putExtra("ids", avoj.getObjectId());
                        intent.putExtra("name", avoj.getString("username"));
                        intent.putExtra("phone", avoj.getString("mobilePhoneNumber"));
                        intent.putExtra("sex", avoj.getInt("sex"));
                        startActivity(intent);
                    }
                } else {
                    LogUtils.e("---------searchFriends-------"+e.getMessage());
                    showToast("查询错误: " + e.getMessage());
                }
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @OnClick({R.id.header_left, R.id.header_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left:
                finish();
                break;
            case R.id.header_delete:
                phone.setText("");
                break;
        }
    }
}
