package app.com.pgy.Models;

import java.util.List;

public class ListBean<T> {
    List<T> list;

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }
}
