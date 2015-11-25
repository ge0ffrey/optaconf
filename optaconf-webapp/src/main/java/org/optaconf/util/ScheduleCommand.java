package org.optaconf.util;


public class ScheduleCommand {

    private String action;
    
    private Long id;

    
    public String getAction() {
        return action;
    }

    
    public void setAction(String action) {
        this.action = action;
    }

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ScheduleCommand [action=" + action + ", id=" + id + "]";
    }
    
    
    
}
