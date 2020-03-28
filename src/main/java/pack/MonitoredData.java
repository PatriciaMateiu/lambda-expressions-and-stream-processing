package pack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonitoredData {

    private Date start_time;
    private Date end_time;
    private String activity_label;
    private int linenr;


    SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat foo = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MonitoredData(String start_time, String end_time, String activity_label, int linenr){

        try {
            this.start_time = format.parse(start_time);
            this.end_time = format.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.activity_label = activity_label;
        this.linenr = linenr;
    }

    public Date getStart_time() {
        return start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public String dateToString(Date d){
        return format.format(d);
    }

    public String getActivity_label() {
        return activity_label;
    }

    public String getStart_day(){
        return fo.format(getStart_time());
    }
    public String getEnd_day(){
        return fo.format(getEnd_time());
    }

    public int hashCode(){
        return getStart_time().hashCode() + getActivity_label().hashCode();// + getEnd_time().hashCode();
    }

    public long getMillis(){
        return getEnd_time().getTime() - getStart_time().getTime();

    }
    public String getDataInfo(){
        String s = getActivity_label() + " " + dateToString(getStart_time()) + "-->" + dateToString(getEnd_time());
        return  s;
    }

    public int getLinenr() {
        return linenr;
    }

    public String getInterval(){
        long ms = getMillis();
        long totalSecs = ms/1000;
        long hours = (totalSecs / 3600);
        long mins = (totalSecs / 60) % 60;
        long secs = totalSecs % 60;

        String s = "";
        if (hours != 0) {
            s = s + hours + " hours ";
        }
        if(mins != 0){
            s = s + mins + " minutes ";
        }
        if(secs != 0){
            s = s + secs + " seconds ";
        }
        return s;
    }

}
