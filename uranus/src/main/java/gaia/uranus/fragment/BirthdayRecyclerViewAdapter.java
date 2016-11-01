package gaia.uranus.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gaia.uranus.R;
import gaia.uranus.fragment.dummy.DummyContent.DummyItem;
import gaia.uranus.utils.LogUtils;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class BirthdayRecyclerViewAdapter extends RecyclerView.Adapter<BirthdayRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final BirthdayFragment.OnListFragmentInteractionListener mListener;

    public BirthdayRecyclerViewAdapter(List<DummyItem> items, BirthdayFragment.OnListFragmentInteractionListener listener) {
        LogUtils.e("items:"+items.size());
        LogUtils.e(">>>>>0>>>items:"+items.get(0).name);
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_birthday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(mValues.get(position).name);
        if(mValues.get(position).islanur==0) {
            holder.date.setText("新历"+mValues.get(position).year + "月" + mValues.get(position).month + "日");
            holder.dateType.setImageResource(R.drawable.lunar_icon);
        }else{
            holder.date.setText("农历"+mValues.get(position).year + "月" + mValues.get(position).year + "日");
            holder.dateType.setImageResource(R.drawable.solar_icon);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView date;
        public final TextView cd;
        public final ImageView dateType;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.tv_name);
            date = (TextView) view.findViewById(R.id.tv_date);
            cd = (TextView) view.findViewById(R.id.tv_cd);
            dateType = (ImageView) view.findViewById(R.id.iv_icon_date);

        }

    }
}
