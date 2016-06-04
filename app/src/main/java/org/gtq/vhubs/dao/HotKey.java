package org.gtq.vhubs.dao;

/**
 * Created by guo on 2016/6/5.
 */
public class HotKey {

    /**
     * id : 1
     * search_txt : yazhou123
     * sort : 1
     * create_time : 1464968967
     */

    private String id;
    private String search_txt;
    private String sort;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearch_txt() {
        return search_txt;
    }

    public void setSearch_txt(String search_txt) {
        this.search_txt = search_txt;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
