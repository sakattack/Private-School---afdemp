/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Sakel
 */
class Assignment {
    
    private final String title;
    private final String description;
    private int courseID;
    private LocalDate submissionDate;
    private int oralPoints = 0;
    private int totalPoints = 0;

    Assignment(int mode) {
        
        switch(mode){
            //manual mode
            case 1:

                title = Utils.askForString("Assignment Title");
                description = Utils.askForString("Assignment Description");
                submissionDate = Utils.askForDate("Submission Date [DD-MM-YYY]");
                oralPoints = Utils.askForInt("Oral Points", 0, 41);
                totalPoints = Utils.askForInt("Total Points", oralPoints, 101);//total points cannot possibly be lower than oral points

                break;
                
            //automatic mode
            default:
                
                title = Utils.ASSIGNMENTTITLES[ThreadLocalRandom.current().nextInt(Utils.ASSIGNMENTTITLES.length)];
                description = Utils.ASSIGNMENTDESCRIPTIONS[ThreadLocalRandom.current().nextInt(Utils.ASSIGNMENTDESCRIPTIONS.length)];
                
                long minDay = LocalDate.now().minusWeeks(12).toEpochDay();
                long maxDay = LocalDate.now().plusWeeks(12).toEpochDay();
                long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
                submissionDate = LocalDate.ofEpochDay(randomDay);
                
                //if submission date is past due, then add points to the assignment
                if ( submissionDate.isBefore(LocalDate.now()) ) {
                
                    oralPoints = ThreadLocalRandom.current().nextInt(20, 41);
                    totalPoints = ThreadLocalRandom.current().nextInt(oralPoints + 20, oralPoints + 60);
                    
                }
                else {//if submission date is not yet due, then points are 0
                
                    oralPoints = 0;
                    totalPoints = 0;
                
                }
                
                
                
                break;
        }
        
    }

    @Override
    public String toString() {
        return "        Assignment{" + "title=" + title + ", description=" + description + ", course=" + School.getCourse(courseID).toString() + ", submissionDate=" + submissionDate + ", oralPoints=" + oralPoints + ", totalPoints=" + totalPoints + '}';
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public int getOralPoints() {
        return oralPoints;
    }

    public void setOralPoints(int oralPoints) {
        this.oralPoints = oralPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }  
    
    
}
