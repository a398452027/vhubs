package support.ui.frt;

public class BaseFrtFactory {
	
	public static BaseFrt createForActivityView(String activityName){
		BaseFrt baseFrt=new BaseFrt();
		baseFrt.setActivityId(activityName);
		return baseFrt;
	}
	public static BaseFrt createForActivityView(String activityName,Object tag){
		BaseFrt baseFrt=new BaseFrt(tag);
		baseFrt.setActivityId(activityName);
		return baseFrt;
	}
	
//	public BaseFrt creatdefaultBFrt(final String name){
//		return new BaseFrt() {			
//			@Override
//			protected void initBar(ActionBar actionBar) {
//				// TODO Auto-generated method stub
//				actionBar.setNavigationMode(
//			                ActionBar.NAVIGATION_MODE_STANDARD);
//				  if(!TextUtils.isEmpty(name)){
//					  actionBar.setTitle(name);
//				  }			  
//			}
//		};
//	}
//	public BaseFrt creatdefaultBFrt(final int id){
//		return new BaseFrt() {			
//			@Override
//			protected void initBar(ActionBar actionBar) {
//				// TODO Auto-generated method stub
//				actionBar.setNavigationMode(
//			                ActionBar.NAVIGATION_MODE_STANDARD);
//				  actionBar.setIcon(id);  	  
//			}
//		};
//	}
//	public BaseFrt creatTabBFrt(final String name){
//		return new BaseFrt() {			
//			@Override
//			protected void initBar(ActionBar actionBar) {
//				// TODO Auto-generated method stub
//				actionBar.setNavigationMode(
//			                ActionBar.NAVIGATION_MODE_TABS);
//				  if(!TextUtils.isEmpty(name)){
//					  actionBar.setTitle(name);
//				  }			  
//			}
//		};
//	}
//	public BaseFrt creatTabBFrt(final int id){
//		return new BaseFrt() {			
//			@Override
//			protected void initBar(ActionBar actionBar) {
//				// TODO Auto-generated method stub
//				actionBar.setNavigationMode(
//			                ActionBar.NAVIGATION_MODE_TABS);
//				actionBar.setIcon(id);  
//			}
//		};
//	}

}
