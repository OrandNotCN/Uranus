package gaia.uranus.base;

/**
 * Created by Zclent on 2016/8/2.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import gaia.uranus.R;

public abstract class BaseActivity extends Activity {

    protected abstract int getLayoutResId();

    protected abstract int getTitleResId();

    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.llContent);
        contentView.addView(LayoutInflater.from(this).inflate(getLayoutResId(), null));
        ButterKnife.bind(this, contentView);
        initHeader();
        initView();
    }

    public void initHeader() {
        TextView tvTitle = (TextView) findViewById(R.id.header_title);
        TextView tvRight = (TextView) findViewById(R.id.header_right);
        TextView tvLeft = (TextView) findViewById(R.id.header_left);
        if(getTitleResId()!=0)
            tvTitle.setText(getTitleResId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onPause(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void setTitle(String title){
        TextView tvTitle = (TextView) findViewById(R.id.header_title);
        tvTitle.setText(title);
    }


    public AlertDialog showDialog(Context context, View content) {
        return getDialog(context, 0, content);
    }

    public AlertDialog getDialog(Context context, int title, View content) {
        return getDialog(context, title, content, null, null);
    }

    public AlertDialog getDialog(Context context, int title, View content,
                                  DialogInterface.OnClickListener negativeButtonListener,
                                  DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (content != null)
            builder.setView(content);
        builder.setNegativeButton(R.string.txt_dialog_negative, negativeButtonListener);
        builder.setPositiveButton(R.string.txt_dialog_positive, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    public AlertDialog getDialog(Context context, String title, View content,
                                  String negativeButtonStr,
                                  String positiveButtonStr,
                                  DialogInterface.OnClickListener negativeButtonListener,
                                  DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != null)
            builder.setTitle(title);
        if (content != null)
            builder.setView(content);
        builder.setNegativeButton(negativeButtonStr, negativeButtonListener);
        builder.setPositiveButton(positiveButtonStr, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    protected AlertDialog getDialogMsg(Context context, int title, View view, DialogInterface.OnClickListener positiveButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (view != null)
            builder.setView(view);
        builder.setPositiveButton(R.string.txt_dialog_positive, positiveButtonListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }

    protected AlertDialog getDialogShare(Context context, int title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
        if (title != 0)
            builder.setTitle(title);
        if (view != null)
            builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        return alertDialog;
    }
}
