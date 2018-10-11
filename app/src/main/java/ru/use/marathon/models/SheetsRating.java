package ru.use.marathon.models;

/**
 * Created by ilyas on 10-Oct-18.
 */

public class SheetsRating {

    String fullname;
    String vk_id;
    int total;

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
}
