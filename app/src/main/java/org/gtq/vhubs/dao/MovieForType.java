package org.gtq.vhubs.dao;

/**类型电影dao
 * Created by guo on 2016/6/4.
 */
public class MovieForType {

    /**
     * id : 7
     * name : 田馥甄 - 『康熙來了』深情演唱蔡依林的『舞娘』
     * desc : 田馥甄 - 『康熙來了』深情演唱蔡依林的『舞娘』
     * cover_img : http://115.28.54.54:81/747/133/328/fc852a42-848e-4c8d-898b-e95599e5648b.jpg
     * language : 中文
     * play_time : 0
     * show_play_time : 552
     * duration : 120
     * grade : 9
     * create_time : 1464872541000
     * vedio_url : http://player.youku.com/player.php/sid/XMzg5NzExNTM2/v.swf
     */

    private String id;
    private String name;
    private String desc;
    private String cover_img;
    private String language;
    private String play_time;
    private String show_play_time;
    private String duration;
    private String grade;
    private String create_time;
    private String vedio_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlay_time() {
        return play_time;
    }

    public void setPlay_time(String play_time) {
        this.play_time = play_time;
    }

    public String getShow_play_time() {
        return show_play_time;
    }

    public void setShow_play_time(String show_play_time) {
        this.show_play_time = show_play_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getVedio_url() {
        return vedio_url;
    }

    public void setVedio_url(String vedio_url) {
        this.vedio_url = vedio_url;
    }
}
