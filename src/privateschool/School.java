/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Sakel
 */
class School {
    
    private static School schoolInstance;//singleton
    private String name;
    private static ArrayList<Course> allCourses;//all the courses
    public static TreeMap<Integer, TreeMap<Trainer, ArrayList<Integer>>> allTrainers = new TreeMap();//all the UNIQUE TRAINERS from all courses in the form "hashed trainer(Objects.hash(firstName, lastName))" => {trainer => {course id,course id}}
    public static TreeMap<Integer, TreeMap<Student, ArrayList<Integer>>> allStudents = new TreeMap();//all the UNIQUE STUDENTS from all courses in the form "hashed student(Objects.hash(firstName, lastName,birthDate))" => {student => {course id,course id}}
    
    private School(String name){
        
        this.name = name;
        //ask for number of courses to insert
        int numberOfCourses = Utils.askForInt("How many COURSES do you want to insert?");
        //ask for insertion mode (restricted options)
        int mode = Utils.askForInt("Method of insertion -> " + Utils.getOptionsNames(Utils.INSERTIONMODES), Utils.getOptionsNumbers(Utils.INSERTIONMODES));
        
        allCourses = new ArrayList<>();
        //create new courses (and everything else) manually or automatically, numberOfCourses times
        for( int i = 0; i < numberOfCourses; i++ ){
            allCourses.add(new Course(mode));
        }
        
        //run main menu
        menu();
        
    }
    
    //singleton
    public static School open(String name){
        if ( schoolInstance == null ) schoolInstance = new School(name);
        return schoolInstance;
    }

    public static Course getCourse(int i) {
        return allCourses.get(i);
    }
    
    private void menu(){
        
        //simulate a menu
        do{
            int[] menuAvailableChoicesLevelOne = {1,2,3,4,9};
            int menuChoiceLevelOne, menuChoiceLevelTwo, menuChoiceLevelThree;
            
            menuChoiceLevelOne = Utils.askForInt("#################### MENU LEVEL 1 ####################\n" + this.name + " -> [1]Students [2]Trainers [3]Assignments [4]Courses [9]Exit", menuAvailableChoicesLevelOne);
            
            switch(menuChoiceLevelOne){
                
                case 1://students
                    
                    do{
                        int[] menuAvailableChoicesStudents = {1,2,3,4,9};
                        
                        menuChoiceLevelTwo = Utils.askForInt("#################### MENU LEVEL 2 ####################\n" + "Students -> [1]All [2]Per Course [3]With > 1 Courses [4]Search [9]Back", menuAvailableChoicesStudents);
                        
                        if ( menuChoiceLevelTwo == 9 ) { break; }
                        switch(menuChoiceLevelTwo){
                        
                            case 1://show ALL STUDENTS
                                
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        System.out.println(thisStudent.getKey().show());
                                    }
                                }
                                
                                break;
                                
                            case 2://show STUDENTS PER COURSE
                                
                                //build list of courses for user to select from
                                Object[] courseOptions = new Object[allCourses.size()];
                                for( int i = 0; i < courseOptions.length; i++ ){
                                    courseOptions[i] = allCourses.get(i).toString() + "\n";
                                }
                                
                                //ask user to select from list
                                menuChoiceLevelThree = Utils.askForInt("\n" + Utils.getOptionsNames(courseOptions) + "\nPlease make a selection", Utils.getOptionsNumbers(courseOptions));
                                
                                //show students of selected course
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        if ( thisStudent.getValue().contains((menuChoiceLevelThree - 1)) ) { System.out.println(thisStudent.getKey().show()); }
                                    }
                                }
                                
                                break;
                                
                            case 3://show STUDENTS WITH MORE THAN 1 COURSE
                                
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        if ( thisStudent.getValue().size() > 1 ) { System.out.println(thisStudent.getKey().show()); }
                                    }
                                }
                                
                                break;
                                
                            case 4://search for STUDENTS THAT NEED TO SUBMIT AN ASSIGNMENT IN THE SAME CALENDAR WEEK AS INPUT DATE
                                
                                //ask user to search for a date
                                LocalDate dateQuery = Utils.askForDate("Date of assignment submission [DD-MM-YYY]");
                                
                                //get day of week of user's input and calculate start and end dates for the search
                                DayOfWeek searchDay = dateQuery.getDayOfWeek();
                                LocalDate start = LocalDate.now();
                                LocalDate end = LocalDate.now();

                                switch(searchDay.toString()){
                                    case "MONDAY":
                                        start = dateQuery.minusDays(1);
                                        end = dateQuery.plusDays(7);
                                        break;
                                    case "TUESDAY":
                                        start = dateQuery.minusDays(2);
                                        end = dateQuery.plusDays(6);
                                        break;
                                    case "WEDNESDAY":
                                        start = dateQuery.minusDays(3);
                                        end = dateQuery.plusDays(5);
                                        break;
                                    case "THURSDAY":
                                        start = dateQuery.minusDays(4);
                                        end = dateQuery.plusDays(4);
                                        break;
                                    case "FRIDAY":
                                        start = dateQuery.minusDays(5);
                                        end = dateQuery.plusDays(3);
                                        break;
                                    case "SATURDAY":
                                        start = dateQuery.minusDays(6);
                                        end = dateQuery.plusDays(2);
                                        break;
                                    case "SUNDAY":
                                        start = dateQuery.minusDays(7);
                                        end = dateQuery.plusDays(1);
                                        break;
                                }
                                
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        
                                        for( Assignment a : thisStudent.getKey().getAssignments() ){
                                            
                                            //if either totalPoints or oralPoints is not 0 then Assignment has been submitted and is excluded from search results
                                            if ( a.getTotalPoints() == 0 && a.getOralPoints() == 0 ) {
                                                
                                                //check if assignment submission date is within range
                                                if ( a.getSubmissionDate().isAfter(start) && a.getSubmissionDate().isBefore(end)  ) {
                                                    
                                                    System.out.println(thisStudent.getKey().show());
                                                    break;
                                                    
                                                }
                                                
                                            
                                            }
                                        
                                        }
                                    }
                                }
                                
                                break;
                        
                        }
                        
                    }
                    while(true);
                    
                    break;
                    
                case 2://trainers
                    
                    do{
                        int[] menuAvailableChoicesTrainers = {1,2,9};
                        
                        menuChoiceLevelTwo = Utils.askForInt("#################### MENU LEVEL 2 ####################\n" + "Trainers -> [1]All [2]Per Course [9]Back", menuAvailableChoicesTrainers);
                        
                        if ( menuChoiceLevelTwo == 9 ) { break; }
                        switch(menuChoiceLevelTwo){
                        
                            case 1://show ALL TRAINERS
                                
                                for( Map.Entry<Integer, TreeMap<Trainer, ArrayList<Integer>>> trainerEntry : allTrainers.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Trainer, ArrayList<Integer>> thisTrainer : trainerEntry.getValue().entrySet() ){
                                        System.out.println(thisTrainer.getKey().show());
                                    }
                                }
                                
                                break;
                                
                            case 2://show TRAINERS PER COURSE
                                
                                //build list of courses for user to select from
                                Object[] courseOptions = new Object[allCourses.size()];
                                for( int i = 0; i < courseOptions.length; i++ ){
                                    courseOptions[i] = allCourses.get(i).toString() + "\n";
                                }
                                
                                //ask user to select from list
                                menuChoiceLevelThree = Utils.askForInt("\n" + Utils.getOptionsNames(courseOptions) + "\nPlease make a selection", Utils.getOptionsNumbers(courseOptions));
                                
                                //show trainers of selected course
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Trainer, ArrayList<Integer>>> trainerEntry : allTrainers.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Trainer, ArrayList<Integer>> thisTrainer : trainerEntry.getValue().entrySet() ){
                                        if ( thisTrainer.getValue().contains((menuChoiceLevelThree - 1)) ) { System.out.println(thisTrainer.getKey().show()); }
                                    }
                                }
                                
                                break;
                        
                        }
                        
                    }
                    while(true);
                    
                    break;
                    
                case 3://assignments
                    
                    do{
                        int[] menuAvailableChoicesAssignments = {1,2,3,9};
                        
                        menuChoiceLevelTwo = Utils.askForInt("#################### MENU LEVEL 2 ####################\n" + "Assignments -> [1]All [2]Per Course [3]Per Student [9]Back", menuAvailableChoicesAssignments);
                        
                        if ( menuChoiceLevelTwo == 9 ) { break; }
                        switch(menuChoiceLevelTwo){
                        
                            case 1://show ALL ASSIGNMENTS
                                
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        thisStudent.getKey().getAssignments().forEach((n) -> System.out.println(n.toString() + " -> " + thisStudent.getKey().toString()) );
                                    }
                                }
                                System.out.println("");
                                
                                break;
                                
                            case 2://show ASSIGNMENTS PER COURSE
                                
                                //build list of courses for user to select from
                                Object[] courseOptions = new Object[allCourses.size()];
                                for( int i = 0; i < courseOptions.length; i++ ){
                                    courseOptions[i] = allCourses.get(i).toString() + "\n";
                                }
                                
                                //ask user to select from list
                                menuChoiceLevelThree = Utils.askForInt("\n" + Utils.getOptionsNames(courseOptions) + "\nPlease make a selection", Utils.getOptionsNumbers(courseOptions));
                                
                                //show assignments of selected course
                                System.out.println("");
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    //this loop runs only once since child TreeMap has only 1 element
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        if ( thisStudent.getValue().contains((menuChoiceLevelThree - 1)) ) {
                                            for( Assignment a : thisStudent.getKey().getAssignments() ){
                                                if ( a.getCourseID() == menuChoiceLevelThree - 1 ) { System.out.println(a.toString() + " -> " + thisStudent.getKey().toString()); }
                                            }
                                        }
                                    }
                                }
                                System.out.println("");
                                
                                break;
                                
                            case 3://show ASSIGNMENTS PER STUDENT
                                
                                //build list of students for user to select from
                                Object[] studentOptions = new Object[allStudents.size()];
                                Object[] studentTemp = new Object[allStudents.size()];
                                int i = 0;
                                for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : allStudents.entrySet() ){
                                    for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                        if ( thisStudent.getKey().getAssignments().size() > 0 ) {//add the student to the list only if he/she has at least 1 assignment to select
                                        
                                            studentOptions[i] = thisStudent.getKey().toString() + "\n";
                                            studentTemp[i] = thisStudent.getKey().hashCode();
                                            i++;
                                        
                                        }
                                        
                                    }
                                    
                                }
                                
                                //clean studentOptions from empty entries (array spots that were left null because there were students with no assignments)
                                Object[] studentOptions2 = Arrays.stream(studentOptions).filter(x -> x != null).toArray(Object[]::new);
                                
                                //ask user to select from list
                                menuChoiceLevelThree = Utils.askForInt("\n" + Utils.getOptionsNames(studentOptions2) + "\nPlease make a selection", Utils.getOptionsNumbers(studentOptions2));
                                
                                //show assignments of selected student
                                System.out.println("");
                                allStudents.get((int)studentTemp[menuChoiceLevelThree - 1]).entrySet().forEach((a) -> a.getKey().getAssignments().forEach((b) -> System.out.println(b.toString() + " -> " + a.getKey().toString())));
                                System.out.println("");
                                
                                break;
                        
                        }
                        
                    }
                    while(true);
                    
                    break;
                    
                case 4://courses
                    
                    System.out.println("");
                    allCourses.forEach((a) -> System.out.println(a.show()));
                    System.out.println("");
                    
                    break;
                    
                case 9://exit
                    Utils.RUN = false;
                    break;
            }
            
        }
        while(Utils.RUN);
        
    }
    
}
