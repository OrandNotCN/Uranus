package gaia.uranus.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import gaia.uranus.R;
import gaia.uranus.common.BlurBehind;


/**
 * Blur activity
 */
public abstract class BlurActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Background
        onInitBlurBackground();

        // Super
        super.onCreate(savedInstanceState);

        // SetContent
        setContentView(getContentView());

        // ToolBar
        onInitToolBar();

        // Notify
        onInit(savedInstanceState);
    }

    protected abstract int getContentView();

    protected abstract void onInit(Bundle savedInstanceState);

    protected void onInitBlurBackground() {
//        Drawable drawable = getBlur();
//        if (drawable == null)
//            drawable = new ColorDrawable(0xc0ffffff);
//        getWindow().getDecorView().setBackgroundDrawable(drawable);
        BlurBehind.getInstance()//在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
                .withAlpha(50)
                .withFilterColor(Color.parseColor("#0ffffff"))
                .setBackground(this);
    }

    protected void onInitToolBar() {
        // SetBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null)
            return;
        mToolbar.setOnMenuItemClickListener(this);
        onInflateMenu(mToolbar);
    }

    protected void onInflateMenu(Toolbar toolbar) {

    }

    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
