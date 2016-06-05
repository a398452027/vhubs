package support.bean;

import java.io.Serializable;

public class IDObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected String mId;
	
	public IDObject(String id){
		mId = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}
		if(o != null && getClass().isInstance(o)){
			return getmId().equals(((IDObject)o).getmId());
		}
		return false;
	}

	@Override
	public int hashCode() {

		return getmId().hashCode();
	}

	public String getmId(){
		return mId;
	}
}
