package org.gtq.vhubs.utils;

import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HistoryMoive;

import support.db.XDB;

/**
 * Created by guo on 2016/6/5.
 */
public class DBUtil {
    public static void addToFavorites(HMoiveItem hMoiveItem){
        FavoritesMoive favoritesMoive=new FavoritesMoive(hMoiveItem,System.currentTimeMillis());
        XDB.getInstance().updateOrInsert(favoritesMoive,false);
    }



    public static void addToHistory(HMoiveItem hMoiveItem){
        HistoryMoive historyMoive=new HistoryMoive(hMoiveItem,System.currentTimeMillis());
        XDB.getInstance().updateOrInsert(historyMoive,false);
    }


}
