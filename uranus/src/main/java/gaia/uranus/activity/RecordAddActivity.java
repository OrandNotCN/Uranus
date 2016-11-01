package gaia.uranus.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.factory.presenter.RecordAddPresenter;
import com.zj.factory.view.RecordAddView;
import com.zj.toolslib.date.DateManager;
import com.zj.toolslib.date.Resource;
import com.zj.toolslib.date.TipsCalender;

import java.util.Calendar;
import java.util.Random;

import gaia.uranus.R;
import gaia.uranus.base.BaseActivity;
import gaia.uranus.common.BlurBehind;
import gaia.uranus.utils.LogUtils;


public class RecordAddActivity extends BaseActivity implements RecordAddView,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    protected EditText mBrief;
    protected RadioGroup mType;
    protected RadioGroup mTimeType;
    protected TextView mTime;
    protected View mColor;
    protected TextView mTxtContact;

    private RecordAddPresenter mPresenter;

//    public static void actionStart(BaseActivity context) {
//        Intent intent = new Intent(context, RecordAddActivity.class);
//        context.setBlur(context);
//        context.startActivity(intent);
//    }
//
//    public static void actionStart(BaseActivity context, int type, String brief, int color, long date) {
//        Intent intent = new Intent(context, RecordAddActivity.class);
//        intent.putExtra("type", type);
//        intent.putExtra("brief", brief);
//        intent.putExtra("color", color);
//        intent.putExtra("date", date);
//        context.setBlur(context);
//        context.startActivity(intent);
//    }


    protected void onInitBlurBackground() {
        BlurBehind.getInstance()//在你需要添加模糊或者透明的背景中只需要设置这几行简单的代码就可以了
                .withAlpha(50)
                .withFilterColor(Color.parseColor("#0075c0"))
                .setBackground(this);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit;
    }

    @Override
    protected int getTitleResId() {
        return 0;
    }

    @Override
    protected void initView() {
        onInitBlurBackground();
        mBrief = (EditText) findViewById(R.id.edit_edit_brief);
        mTimeType = (RadioGroup) findViewById(R.id.edit_radio_time_type);
        mTime = (TextView) findViewById(R.id.edit_txt_time);
        mColor = findViewById(R.id.edit_view_color);
        mType = (RadioGroup) findViewById(R.id.edit_radio_type);
        mTxtContact = (TextView) findViewById(R.id.txt_view_contact_name);

        // Set Listener
        mTime.setOnClickListener(this);
        mColor.setOnClickListener(this);
        mTxtContact.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

        // Init value
//        onInitValues();
//        onInitValues(getIntent().getExtras());

        // Init presenter
        onInitPresenter();
        onInitValues();
        // Add CheckedChangeListener
        mTimeType.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        final LinearLayout layout = (LinearLayout) findViewById(R.id.lay_data);
        assert layout != null;
        layout.setVisibility(View.VISIBLE);b
        layout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Animation animation = AnimationUtils.loadAnimation(RecordAddActivity.this, R.anim.anim_in_slide_alpha_bottom_long);
                LayoutAnimationController lac = new LayoutAnimationController(animation);
                lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
                lac.setDelay(0.28f);
                layout.setLayoutAnimation(lac);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        */
    }


    protected void onInitValues() {
        // Check
        mTimeType.check(R.id.edit_radio_time_type_solar);

        // Color
        int color = Resource.Color.COLORS[new Random().nextInt(20) + 1];
        mColor.setTag(color);
        mColor.setBackgroundColor(color);

        // Time
        Calendar calendar = Calendar.getInstance();

        String year = String.format("%04d", calendar.get(Calendar.YEAR));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
         String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));

        TipsCalender date = new TipsCalender(year + month + day + "0");
        mTime.setTag(date);
        mTime.setText(year + "-" + month + "-" + day);
        mTxtContact.setTag(1);
    }

    protected void onInitPresenter() {
        mPresenter = new RecordAddPresenter(this);
    }

    @Override
    public int getType() {
        int id = mType.getCheckedRadioButtonId();
        if (id == R.id.edit_radio_type_birthday)
            return 1;
        else if (id == R.id.edit_radio_type_memorial)
            return 2;
        else //if (id == R.id.edit_radio_type_future)
            return 3;
    }

    @Override
    public String getBrief() {
        return mBrief.getText().toString();
    }

    @Override
    public TipsCalender getDate() {
        TipsCalender date = (TipsCalender) mTime.getTag();
        date.setLunar(isLunar());
        return date;
    }

    public boolean isLunar() {
        int id = mTimeType.getCheckedRadioButtonId();
        return (id == R.id.edit_radio_time_type_lunar);
    }


    @Override
    public int getColor() {
        return (int) mColor.getTag();
    }

    @Override
    public void setStatus(long status) {
        if (status == -1)
            Toast.makeText(this, R.string.txt_status_title_null, Toast.LENGTH_SHORT).show();
        else if (status >= 0) {
            //Toast.makeText(this, "New: " + status, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edit_txt_time) {
            final DateManager selector = new DateManager(getLayoutInflater(), getDate());

            AlertDialog dialog = getDialog(RecordAddActivity.this, R.string.title_select_date,
                    selector.getView(), null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TipsCalender date = selector.getDate();
                            mTime.setTag(date);
                            mTime.setText(date.toDate());
                        }
                    });
            dialog.show();
        } else if (id == R.id.edit_view_color) {

            // Create
//            final ColorSelector selector = new ColorSelector(getLayoutInflater(), (int) mColor.getTag());
//
//            //  Add Dialog
//            AlertDialog dialog = showDialog(RecordAddActivity.this, R.string.title_select_color, selector.getView(), null,
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            callBackColor(selector.getColor());
//                        }
//                    });
//            dialog.show();
        } else if (id == R.id.btn_save) {
            // Create
            mPresenter.create();
        } else if (id == R.id.txt_view_contact_name) {
            //选择联系人
//            View mRoot = getLayoutInflater().inflate(R.layout.fragment_contacts, null);
//            final RecyclerView mRecycler = (RecyclerView) mRoot.findViewById(R.id.time_line_recycler);
//            TextView mStatus = (TextView) mRoot.findViewById(R.id.text_status);
//            final AlertDialog dialog = showDialog(RecordAddActivity.this,
//                    R.string.txt_contacts_concern_relation, mRoot);
//            mAdapter = new ContactsAdapter(mRecycler, mStatus, new AdapterSelectCallback() {
//                @Override
//                public void onItemSelected(UUID id) {
//                    mContactModel = ContactModel.get(id);
//                    mTxtContact.setText(mContactModel.getName());
//                    dialog.cancel();
//                }
//
//                @Override
//                public void setLoading(boolean isLoad) {
//
//                }
//            });
//            mAdapter.refresh();
//            //  Add Dialog
//
//            dialog.show();
        }
    }

    private void callBackColor(int color) {
        mColor.setTag(color);
        mColor.setBackgroundColor(color);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        TipsCalender date = (TipsCalender) mTime.getTag();
//        int dateDay = TipsCalender.coercionDay(date.getYear(), date.getMonth(), date.getDay()
//                , isLunar());
//        if (date.getDay() > dateDay) {
//            date.setDay(dateDay);
//            mTime.setTag(date);
//            mTime.setText(date.toDate());
//        }
    }
}
