package support.bean;

import java.io.Serializable;

public class IDObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected final String mId;
	
	public IDObject(String id){
		mId = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}
		if(o != null && getClass().isInstance(o)){
			return getId().equals(((IDObject)o).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {

		return getId().hashCode();
	}

	public String getId(){
		return mId;
	}
}
