package gaia.uranus.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gaia.uranus.R;

public abstract class BaseFragment extends Fragment   implements OnClickListener {


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.topRightButton:
//			Intent intentToCartId=new Intent();
//			intentToCartId.setClass(getActivity(), CartOrderListActivity.class);
//			startActivity(intentToCartId);
//			break;

		default:
			break;
		}
		
	}

	/**
	 * * Fragment切换
	 *
	 * @Params toFragment 将要切换到的Fragment
	 * resId      装载Fragment的view Id
	 * index      Fragment的标识index
	 * toleft     判断Fragment向左切换还是向右切换，以采用不同的动画
	 * Notes:  R.anim.push_left_in等均为简单的Tranlate动画
	 * this为当前所在的Fragment，继承自BaseFragment
	 */

	protected void switchFragmentContent(Fragment toFragment, int resId, boolean toleft) {
		if (null == this || null == toFragment) {
			return;
		}
		FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		if (toleft) {
			fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
					R.anim.push_left_out);
		} else {
			fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
					R.anim.push_right_out);
		}
		if (!toFragment.isAdded()) {
			fragmentTransaction.hide(this).add(resId, toFragment).commit();
		} else {
			fragmentTransaction.hide(this).show(toFragment).commit();
		}

	}
	
	/**
	 * 隐藏键盘
	 */
	public void hideKeyboard(View view) {
		// 隐藏键盘
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	private SweetAlertDialog pDialog;
	public void showProgress(){
		pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
				.setTitleText("Loading");
		pDialog.show();
		pDialog.setCancelable(false);
	}

	public void doneProgress(){
		pDialog.dismiss();
	}
	public void doneSuccessProgress(String message){
		pDialog.setTitleText(message)
				.setConfirmText("OK")
				.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
	}

	public void doneFailProgress(String message){
		pDialog.setTitleText(message)
				.setConfirmText("OK")
				.changeAlertType(SweetAlertDialog.ERROR_TYPE);
	}

	public void showToast(String hint) {

		View toaskView = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.layout_toast, null);
		Toast toast = Toast.makeText(this.getActivity(), null,
				Toast.LENGTH_LONG);
		toast.setView(toaskView);
		((TextView) toaskView.findViewById(R.id.toaskMessage)).setText(hint);
		toast.show();
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
	}
	
}
