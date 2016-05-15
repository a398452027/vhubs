package support.ui.frt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;


public interface ActivityToFragmentInterface {
	public void setmActionBar(ActionBar mActionBar);

	public void Resume();

	public void Pause();

	public void activityResult(int requestCode, int resultCode, Intent data);
	
	public void	SaveInstanceState(Bundle outState);
	
	public void initInstanceState(Bundle outState);
}
