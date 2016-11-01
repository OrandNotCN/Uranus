package gaia.uranus.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import gaia.uranus.R;
import gaia.uranus.base.ActionBaseSetting;
import gaia.uranus.base.BaseActivity;
import gaia.uranus.core.ApiCore;
import gaia.uranus.utils.LogUtils;

public class ContactorInfoActivity extends BaseActivity {

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    private  String ids;
    private boolean isConected = false;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contactor_info;
    }

    @Override
    protected int getTitleResId() {
        return R.string.personal_info;
    }


    @Override
    protected void initView() {
        ids = getIntent().getStringExtra("ids");
        tvName.setText(getIntent().getStringExtra("name"));
        int sex = getIntent().getIntExtra("sex",0);
        if(sex==1){
            tvSex.setText("boy");
        }else if(sex==2){
            tvSex.setText("girl");
        }else{
            tvSex.setText("");
        }
        tvPhone.setText(getIntent().getStringExtra("phone"));
        isHaveFirends();
    }

    private void isHaveFirends() {
        ApiCore.isHaveFriends(ids, new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                LogUtils.e(avObjects.toString());
                if (avObjects == null || e!=null || avObjects.size()<=0) {
                    return;
                }else{
                    isConected = true;
                }
            }
        });

    }

    @OnClick(R.id.tv_add)
    public void onClick() {
        if(isConected){
            showToast("已经添加");
            return;
        }
        ApiCore.addNewFriends(ids, new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    showToast("添加成功");
                    sendBroadcast(new Intent(ActionBaseSetting.UPDATE_MAIN_ACTION));
                } else {
                    showToast("添加失败");
                }
            }
        });

    }
}
