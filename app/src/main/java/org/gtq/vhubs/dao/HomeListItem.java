package org.gtq.vhubs.dao;

import java.util.ArrayList;
import java.util.List;

import support.bean.IDObject;

/**
 * Created by guo on 2016/5/28.
 */
public class HomeListItem extends IDObject {


    /**
     * id : 8
     * name : sdrew
     * logo : http://115.28.54.54:81/219/402/6332/56ba16b2-d2d3-481c-93f4-418911bf144f.png
     * movices : [{"id":"3","nane":"fsdfasdfsda123","desc":"fdsfasdfasdfasda","play_time":"0","show_play_time":"150","duration":"200","cover_img":"http://115.28.54.54:81/176/130/61/c976a12f-5784-427d-8331-4e9500f84de2.jpg","language":"english","grade":"9","create_time":"1462637998","vedio_url":"http://player.youku.com/player.php/sid/XMTU2NzM3ODYwNA==/v.swf"}]
     */

    private String cid;
    private String cname;
    private String clogo;
    /**
     * id : 3
     * nane : fsdfasdfsda12
     * desc : fdsfasdfasdfasda
     * play_time : 0
     * show_play_time : 150
     * duration : 200
     * cover_img : http://115.28.54.54:81/176/130/61/c976a12f-5784-427d-8331-4e9500f84de2.jpg
     * language : english
     * grade : 9
     * create_time : 1462637998
     * vedio_url : http://player.youku.com/player.php/sid/XMTU2NzM3ODYwNA==/v.swf
     */

    private List<HMoiveItem> movices;

    public HomeListItem() {
        super("");
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getClogo() {
        return clogo;
    }

    public void setClogo(String clogo) {
        this.clogo = clogo;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<HMoiveItem> getMovices() {
        if (movices == null) {
            movices = new ArrayList<>();
        }
        return movices;
    }

    public void setMovices(List<HMoiveItem> movices) { this.movices = movices;}

//    public static class MovicesBean {
//        private String id;
//        private String name;
//        private String desc;
//        private String play_time;
//        private String show_play_time;
//        private String duration;
//        private String cover_img;
//        private String language;
//        private String grade;
//        private String create_time;
//        private String vedio_url;
//
//        public String getId() { return id;}
//
//        public void setId(String id) { this.id = id;}
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getDesc() { return desc;}
//
//        public void setDesc(String desc) { this.desc = desc;}
//
//        public String getPlay_time() { return play_time;}
//
//        public void setPlay_time(String play_time) { this.play_time = play_time;}
//
//        public String getShow_play_time() { return show_play_time;}
//
//        public void setShow_play_time(String show_play_time) { this.show_play_time = show_play_time;}
//
//        public String getDuration() { return duration;}
//
//        public void setDuration(String duration) { this.duration = duration;}
//
//        public String getCover_img() { return cover_img;}
//
//        public void setCover_img(String cover_img) { this.cover_img = cover_img;}
//
//        public String getLanguage() { return language;}
//
//        public void setLanguage(String language) { this.language = language;}
//
//        public String getGrade() { return grade;}
//
//        public void setGrade(String grade) { this.grade = grade;}
//
//        public String getCreate_time() { return create_time;}
//
//        public void setCreate_time(String create_time) { this.create_time = create_time;}
//
//        public String getVedio_url() { return vedio_url;}
//
//        public void setVedio_url(String vedio_url) { this.vedio_url = vedio_url;}
//    }
}
