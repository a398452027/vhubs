package support.db;

import android.content.Context;

public abstract class IMModule {

	protected boolean mIsInitial;
	
	public void initial(Context context){
		if(mIsInitial){
			release();
		}
		onInitial(context);
		mIsInitial = true;
	}
	
	public void release(){
		onRelease();
		mIsInitial = false;
	}
	
	protected abstract void onInitial(Context context);
	
	protected abstract void onRelease();
}
