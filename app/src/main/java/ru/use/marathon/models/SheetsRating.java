package ru.use.marathon.models;

import android.support.annotation.NonNull;

/**
 * Created by ilyas on 10-Oct-18.
 */

public class SheetsRating implements Comparable<SheetsRating> {

    public String fullname;
    public String vk_id;
    public int total;

    public SheetsRating(String fullname, String vk_id, int total) {
        this.fullname = fullname;
        this.vk_id = vk_id;
        this.total = total;
    }

    public String getFullname() {
        return fullname;
    }

    public String getVk_id() {
        return vk_id;
    }

    public int getTotal() {
        return total;
    }

    public String toString(){
        return ("rating:("+ fullname + ", "+vk_id+", "+total+")");

    }

    @Override
    public int compareTo(@NonNull SheetsRating sheetsRating) {
        return  getTotal() - sheetsRating.getTotal();
    }
}
