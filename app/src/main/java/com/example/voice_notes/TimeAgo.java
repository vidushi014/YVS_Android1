package com.example.voice_notes;

import android.text.format.Time;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeAgo {
    public String getTimeAgo(long duration){
        Date now =new Date();

        long sec= TimeUnit.MILLISECONDS.toSeconds(now.getTime()-duration);
        long min= TimeUnit.MILLISECONDS.toMinutes(now.getTime()-duration);
        long hr= TimeUnit.MILLISECONDS.toHours(now.getTime()-duration);
        long days= TimeUnit.MILLISECONDS.toDays(now.getTime()-duration);
        if(sec<60){
            return "Just Now";
        }else if (min==1){
            return "a minute ago";
        }else if (min>1&&min<60){
            return min+" minutes ago";
        }else if (hr==1){
            return "an hour ago";
        }else if (hr>1&&hr<24){
            return hr+" hours ago";
        }else if (days==1){
            return "a day ago";

        }else {
            return days+" days ago";
        }
    }
}
