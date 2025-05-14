package org.example.alphasolutions.model;

public class TimeSummary {
    private final int totalTimeEstimate;
    private final int totalTimeSpent;
    private final int taskTimeEstimate;
   private final int subProjectTimeEstimate;
    private final int taskTimeSpent;
    private final int subProjectTimeSpent;
    private final boolean onTrack;

    public TimeSummary(int totalTimeEstimate, int totalTimeSpent, int taskTimeEstimate, int taskTimeSpent,
                       int subProjectTimeEstimate,
                       int subProjectTimeSpent, boolean onTrack) {

        this.totalTimeEstimate = totalTimeEstimate;
        this.totalTimeSpent = totalTimeSpent;
        this.taskTimeEstimate = taskTimeEstimate;
        this.taskTimeSpent = taskTimeSpent;
        this.subProjectTimeEstimate = subProjectTimeEstimate;
        this.subProjectTimeSpent = subProjectTimeSpent;
        this.onTrack = onTrack;
    }

    //---total project time estimate
    public int getTotalTimeEstimate() {

        return totalTimeEstimate;
    }

    //---------total project time spent
    public int getTotalTimeSpent() {

        return totalTimeSpent;
    }

    //---------task itme estiame
    public int getTaskTimeEstimate() {

        return taskTimeEstimate;
    }

    public int getTaskTimeSpent() {

        return taskTimeSpent;
    }

    //---------subproject time estiame
    public int getSubProjectTimeEstimate() {

        return subProjectTimeEstimate;
    }

    public int getSubProjectTimeSpent() {

        return subProjectTimeSpent;
    }
    
    

    //------------- status--------
    public boolean isOnTrack() {
        return onTrack;
    }
}