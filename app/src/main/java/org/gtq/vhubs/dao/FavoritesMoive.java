package org.gtq.vhubs.dao;

import support.bean.IDObject;

/**
 * Created by gtq on 2016/6/7.
 */
public class FavoritesMoive extends IDObject {

    HMoiveItem hMoiveItem;
    private long save_time;

    public FavoritesMoive(HMoiveItem hMoiveItem, long save_time) {
        super(hMoiveItem.getmId());
        this.save_time = save_time;
        this.hMoiveItem = hMoiveItem;
    }

    public HMoiveItem gethMoiveItem() {
        return hMoiveItem;
    }

    public void sethMoiveItem(HMoiveItem hMoiveItem) {
        this.hMoiveItem = hMoiveItem;
    }

    public long getSave_time() {
        return save_time;
    }

    public void setSave_time(long save_time) {
        this.save_time = save_time;
    }
}
