package org.gtq.vhubs.dao;

import support.bean.IDObject;

/**
 * Created by guo on 2016/6/5.
 */
public class HistoryMoive  extends IDObject{
    HMoiveItem hMoiveItem;
    long watch_time;

    public HistoryMoive(HMoiveItem hMoiveItem, long watch_time) {
        super(hMoiveItem.getmId());
        this.watch_time = watch_time;
        this.hMoiveItem = hMoiveItem;
    }

    public HMoiveItem gethMoiveItem() {
        return hMoiveItem;
    }

    public void sethMoiveItem(HMoiveItem hMoiveItem) {
        this.hMoiveItem = hMoiveItem;
    }

    public long getWatch_time() {
        return watch_time;
    }

    public void setWatch_time(long watch_time) {
        this.watch_time = watch_time;
    }
}
