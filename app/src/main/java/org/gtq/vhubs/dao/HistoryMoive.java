package org.gtq.vhubs.dao;

import support.bean.IDObject;

/**
 * Created by guo on 2016/6/5.
 */
public class HistoryMoive  extends IDObject{

    private String id;
    private String name;
    private String desc;
    private String play_time;
    private String show_play_time;
    private String duration;
    private String cover_img;
    private String category_id;
    private String language;
    private String grade;
    private String create_time;
    private String vedio_url;
    private String update_time;
    private long watch_time;

    public HistoryMoive(HMoiveItem hMoiveItem,long watch_time) {
        super("");
        this.watch_time=watch_time;
        setId(hMoiveItem.getId());
        setName(hMoiveItem.getName());
        setDesc(hMoiveItem.getDesc());
        setPlay_time(hMoiveItem.getPlay_time());
        setShow_play_time(hMoiveItem.getShow_play_time());
        setDuration(hMoiveItem.getDuration());
        setCover_img(hMoiveItem.getCover_img());
        setCategory_id(hMoiveItem.getCategory_id());
        setLanguage(hMoiveItem.getLanguage());
        setGrade(hMoiveItem.getGrade());
        setCreate_time(hMoiveItem.getCreate_time());
        setVedio_url(hMoiveItem.getVedio_url());
        setUpdate_time(hMoiveItem.getUpdate_time());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        mId=id;
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

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
