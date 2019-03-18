/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Sakel
 */
class Student implements Comparable {
    
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private int tuitionFees;
    private ArrayList<Assignment> assignments;

    Student(int mode) {
        
        int numberOfAssignments;
        
        switch(mode){
            //manual mode
            case 1:

                while(true){
                    firstName = Utils.askForString("Student First Name");
                    lastName = Utils.askForString("Student Last Name");
                    birthDate = Utils.askForDate("Birthday [DD-MM-YYY]");
                    
                    //check if entered student already exists
                    if ( School.allStudents.containsKey(Objects.hash(firstName, lastName, birthDate)) ) {
                        System.out.println("Student already exists, please try again");
                    }
                    else {
                        tuitionFees = Utils.askForInt("Tuition Fees");
                        break;
                    }
                    
                }
                
                //ask for number of assignments to insert to the student
                numberOfAssignments = Utils.askForInt("How many ASSIGNMENTS do you want to insert to student \"" + firstName + " " + lastName + "\", born on "+birthDate+"?");

                break;
                
            //automatic mode (if mode is not 1 then it most definitely is 2 = automatic)
            default:
                
                while(true){
                    firstName = Utils.FIRSTNAMES[ThreadLocalRandom.current().nextInt(Utils.FIRSTNAMES.length)];
                    lastName = Utils.LASTNAMES[ThreadLocalRandom.current().nextInt(Utils.LASTNAMES.length)];

                    long minDay = LocalDate.of(1970, 1, 1).toEpochDay();//no dogs or anyone over (now - 1970) allowed :)
                    long maxDay = LocalDate.now().minusYears(19).toEpochDay();//youngest possible auto generated student is 19yo
                    long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
                    birthDate = LocalDate.ofEpochDay(randomDay);
                    
                    //check if random student already exists
                    if ( !School.allStudents.containsKey(Objects.hash(firstName, lastName, birthDate)) ) {
                        tuitionFees = ThreadLocalRandom.current().nextInt(500, 4000);
                        break;
                    }
                    
                }
                
                //random number of assignments inserted
                numberOfAssignments = ThreadLocalRandom.current().nextInt(0, 5);
                
                break;
        }
        
        assignments = new ArrayList<>();
        //create new assignments automatically, numberOfAssignments times
        for( int i = 0; i < numberOfAssignments; i++ ){
            assignments.add(new Assignment(mode));
        }
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName, this.birthDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.birthDate, other.birthDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + birthDate;
    }
    
    @Override
    public int compareTo(Object o) {
        return o.hashCode() - this.hashCode();
    }
    
    public String show() {
        
        //build assignments string
        String allTasks = "";
        for( Assignment task : assignments ){
        
            allTasks += task.toString() + "\n";
            
        }
        allTasks = ( !allTasks.isEmpty() ) ? ( "\n" + allTasks ) : "";
        
        //build courses string
        String studentCourses = "";
        //this loop runs only once since child TreeMap has only 1 element
        for( Map.Entry<Student, ArrayList<Integer>> thisStudentCourses : School.allStudents.get(this.hashCode()).entrySet() ){
            for( int i : thisStudentCourses.getValue() ){
                studentCourses += "\n   Course : " + School.getCourse(i).getTitle() + " | " + School.getCourse(i).getStream() + " | " + School.getCourse(i).getType();
            }
        }
        
        return firstName + " " + lastName + ", birthDate = " + birthDate + ", tuitionFees = " + tuitionFees + ", assignments = " + assignments.size() + studentCourses + allTasks + "\n";
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignments(int mode, int courseID) {
        
        int howMany = ( mode == 1 ) ? Utils.askForInt("How many ASSIGNMENTS do you want to insert FOR THIS COURSE to student \"" + firstName + " " + lastName + "\", born on "+birthDate+"?") : ThreadLocalRandom.current().nextInt(0, 4);
        
        if ( assignments == null ) assignments = new ArrayList<>();
        
        //create new assignments automatically or manually, howMany times
        for( int i = 0; i < howMany; i++ ){
            Assignment thisAssignment = new Assignment(mode);
            thisAssignment.setCourseID(courseID);
            assignments.add(thisAssignment);
        }
        
    }
    
    
    
    
    
}
