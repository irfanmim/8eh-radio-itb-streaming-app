package id.ac.itb.a8eh.radiostreaming.model;

/**
 * Created by irfan_mim on 01/09/17.
 */

public class ProgramSchedule {

    private int id;
    private String title;
    private String time;
    private String announcer;

    public ProgramSchedule() {
        //
    }

    public ProgramSchedule(int id, String title, String time, String announcer) {

        this.id = id;
        this.title = title;
        this.time = time;
        this.announcer = announcer;
    }



    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(String announcer) {
        this.announcer = announcer;
    }
}
