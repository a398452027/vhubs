package org.gtq.vhubs.dao;

import java.util.List;

import support.bean.IDObject;

/**
 * Created by guotengqian on 2016/6/1.
 */
public class ClassificationItem {

    /**
     * id : 8
     * name : sdrew
     * logo : http://115.28.54.54:81/219/402/6332/56ba16b2-d2d3-481c-93f4-418911bf144f.png
     * sort : 1
     * index_show : 0
     * create_time : 1462637424
     * update_time : 1462981968
     */

    private List<HotsBean> hots;
    /**
     * id : 11
     * name : guochan
     * logo : http://115.28.54.54:81/267/067/0766/74294e43-870f-4ac7-9950-0fecb31354c4.png
     * sort : 4
     * index_show : 0
     * create_time : 1462637523
     * update_time : 1462637523
     */

    private List<NormalsBean> normals;



    public List<HotsBean> getHots() { return hots;}

    public void setHots(List<HotsBean> hots) { this.hots = hots;}

    public List<NormalsBean> getNormals() { return normals;}

    public void setNormals(List<NormalsBean> normals) { this.normals = normals;}

    public static class HotsBean {
        private String id;
        private String name;
        private String logo;
        private String sort;
        private String index_show;
        private String create_time;
        private String update_time;

        public String getId() { return id;}

        public void setId(String id) { this.id = id;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        public String getLogo() { return logo;}

        public void setLogo(String logo) { this.logo = logo;}

        public String getSort() { return sort;}

        public void setSort(String sort) { this.sort = sort;}

        public String getIndex_show() { return index_show;}

        public void setIndex_show(String index_show) { this.index_show = index_show;}

        public String getCreate_time() { return create_time;}

        public void setCreate_time(String create_time) { this.create_time = create_time;}

        public String getUpdate_time() { return update_time;}

        public void setUpdate_time(String update_time) { this.update_time = update_time;}
    }

    public static class NormalsBean extends IDObject{
        private String id;
        private String name;
        private String logo;
        private String sort;
        private String index_show;
        private String create_time;
        private String update_time;

        public NormalsBean() {
            super("");
        }

        public String getId() { return id;}

        public void setId(String id) { this.id = id;}

        public String getName() { return name;}

        public void setName(String name) { this.name = name;}

        public String getLogo() { return logo;}

        public void setLogo(String logo) { this.logo = logo;}

        public String getSort() { return sort;}

        public void setSort(String sort) { this.sort = sort;}

        public String getIndex_show() { return index_show;}

        public void setIndex_show(String index_show) { this.index_show = index_show;}

        public String getCreate_time() { return create_time;}

        public void setCreate_time(String create_time) { this.create_time = create_time;}

        public String getUpdate_time() { return update_time;}

        public void setUpdate_time(String update_time) { this.update_time = update_time;}
    }
}
