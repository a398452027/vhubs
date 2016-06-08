package support.ui.frt;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

@SuppressLint({ "NewApi", "ValidFragment" })
public class BaseFrt extends Fragment {

	protected View currentView;
	protected FrameLayout contentView;
	protected String activityId;
	LocalActivityManager mLocalActivityManager;

	private final static String ACTIVITY_NAME = "activity_name";
	// 需要传递给activity的参数
	Object mTag;

	public final static String FRTTAG_NAME = "frttag";

	public BaseFrt() {

	}

	public BaseFrt(Object tag) {
		this.mTag = tag;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(mLocalActivityManager!=null){
			if(hidden){

				((ActivityToFragmentInterface) mLocalActivityManager
						.getActivity(activityId)).Pause();
			}else{
				((ActivityToFragmentInterface) mLocalActivityManager
						.getActivity(activityId)).Resume();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		((ActivityToFragmentInterface) mLocalActivityManager
				.getActivity(activityId)).activityResult(requestCode,
				resultCode, data);
	}




	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (activityId == null) {
			activityId = savedInstanceState.getString(ACTIVITY_NAME);

		}


		contentView = new FrameLayout(getActivity());
		mLocalActivityManager = new LocalActivityManager(getActivity(), true);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		try {
			setActivityView();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Store the game state
		outState.putString(ACTIVITY_NAME, activityId);
		((ActivityToFragmentInterface) mLocalActivityManager
				.getActivity(activityId)).SaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	protected void setActivityView() throws ClassNotFoundException {

		mLocalActivityManager.dispatchResume();
		Intent intent = new Intent(getActivity(), Class.forName(activityId));
		if (mTag != null) {
			if (mTag instanceof String) {
				String tag = (String) mTag;
				intent.putExtra(FRTTAG_NAME, tag);
			} else if (mTag instanceof Integer) {
				Integer tag = (Integer) mTag;
				intent.putExtra(FRTTAG_NAME, tag);
			} else if (mTag instanceof ArrayList<?>) {
				ArrayList<? extends Parcelable> list = (ArrayList<Parcelable>) mTag;
				intent.putExtra(FRTTAG_NAME, list);
			} else if (mTag instanceof Boolean) {
				Boolean tag = (Boolean) mTag;
				intent.putExtra(FRTTAG_NAME, tag);
			}
		}

		View v = mLocalActivityManager.startActivity(activityId, intent)
				.getDecorView();
		((ActivityToFragmentInterface) mLocalActivityManager
				.getActivity(activityId))
				.setmActionBar(((AppCompatActivity) getActivity())
						.getSupportActionBar());
		setCurrentView(v);
	}

	protected void setCurrentView(View v) {
		if (currentView != null) {
			currentView.setVisibility(View.GONE);
		}
		currentView = v;
		v.setVisibility(View.VISIBLE);
		v.setFocusableInTouchMode(true);
		((ViewGroup) v)
				.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

		contentView.addView(v, 0, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) contentView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}

		return contentView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((ActivityToFragmentInterface) mLocalActivityManager
				.getActivity(activityId)).Resume();

	}

	public ActivityToFragmentInterface getActivityToFragmentInterface() {
		if (mLocalActivityManager != null) {
			return (ActivityToFragmentInterface) mLocalActivityManager
					.getActivity(activityId);
		}
		return null;
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		// mLocalActivityManager.destroyActivity(activityId, true);
		// Log.d("ddd", "frt_Destroy");
		super.onDestroy();

	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((ActivityToFragmentInterface) mLocalActivityManager
				.getActivity(activityId)).Pause();
		// Log.d("ddd", "frt_onPause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Log.d("ddd", "frt_onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.d("ddd", "frt_onStop");
	}

	// protected void showProgressDialog(){
	// showProgressDialog(null, null);
	// }
	//
	// protected void showProgressDialog(String strTitle,int nStringId){
	// showProgressDialog(strTitle, getString(nStringId));
	// }
	//
	// protected void showProgressDialog(String strTitle,String strMessage){
	// if(mProgressDialog == null){
	// ProgressDialog.show(getSherlockActivity(), strTitle, strMessage, true,
	// false);
	// }
	// }

}
