/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Sakel
 */
class Course {
    
    private static int courseCount = 0;//keep a score of the courses created (used as id for any new course created, but no reason to save it individually in every course as well, since their index in School's allCourses array serves the same purpose)
    private String title;
    private String stream; //it can be only what is in Utils.COURSESTREAMS
    private String type; //it can be only what is in Utils.COURSETYPES
    private TreeSet<LocalDate> duration;
    private ArrayList<Trainer> trainers;//all the trainers of this course
    private ArrayList<Student> students;//all the students of this course
    
    //mode -> 1 = manual, 2 = automatic
    Course(int mode){
        
        int numberOfTrainers, numberOfStudents;//how many trainers to add to the course
        int action;//1 = new, 2 = existing | for trainer and student insertion
        TreeMap<Trainer, ArrayList<Integer>> innerTrainer;
        TreeMap<Student, ArrayList<Integer>> innerStudent;
        
        switch(mode){
            //manual mode
            case 1:

                //ask for course title
                title = Utils.askForString("Course Title");
                
                //ask for course stream
                stream = Utils.COURSESTREAMS[Utils.askForInt("Course Stream -> " + Utils.getOptionsNames(Utils.COURSESTREAMS), Utils.getOptionsNumbers(Utils.COURSESTREAMS)) - 1];
                
                //ask for course type
                type = Utils.COURSETYPES[Utils.askForInt("Course Type -> " + Utils.getOptionsNames(Utils.COURSETYPES), Utils.getOptionsNumbers(Utils.COURSETYPES)) - 1];
                
                //ask for start and end dates to form course duration
                LocalDate startDate = Utils.askForDate("Start Date [DD-MM-YYY]");
                LocalDate endDate = Utils.askForDate("End Date [DD-MM-YYY]");
                duration = new TreeSet<>();
                duration.add(startDate);
                duration.add(endDate);
                
                ////////////////ADD TRAINERS/////////////////////
                //ask for number of trainers to insert to the course
                numberOfTrainers = Utils.askForInt("How many TRAINERS do you want to insert to course \"" + title + "\"?");

                trainers = new ArrayList<>();
                String[] options = {"New","Existing"};//predefined options for user to select from
                //create new trainers manually, numberOfTrainers times
                for( int i = 0; i < numberOfTrainers; i++ ){

                    //ask user if trainer is a new entry or an existing one to select from, but only if there is at least one trainer in the pseudo database that does not already belong to this course
                    action = ( School.allTrainers.size() > trainers.size() ) ? Utils.askForInt("Trainer " + i + " is -> " + Utils.getOptionsNames(options), Utils.getOptionsNumbers(options)) : 1;
                    if ( action == 1 ) {//new entry

                        //create new Trainer object
                        Trainer newTrainer = new Trainer(mode);
                        //put it in this course's trainers
                        trainers.add(newTrainer);
                        //put new trainer to school's allTrainers master map. hashed trainer => ( trainer => ( array of course ids ) )
                        ArrayList<Integer> trainerCourses = new ArrayList<>();
                        trainerCourses.add(courseCount);
                        innerTrainer = new TreeMap<>();
                        innerTrainer.put(newTrainer, trainerCourses);
                        School.allTrainers.put(newTrainer.hashCode(), innerTrainer);

                    }
                    else {//select from existing trainers

                        //build list with available trainers to select from, that do not already belong to this course
                        TreeMap<Integer, String> trainerOptionsList = new TreeMap<>();
                        
                        for( Map.Entry<Integer, TreeMap<Trainer, ArrayList<Integer>>> trainerEntry : School.allTrainers.entrySet() ){

                            //this loop runs only once since child TreeMap has only 1 element
                            for( Map.Entry<Trainer, ArrayList<Integer>> thisTrainer : trainerEntry.getValue().entrySet() ){
                                if ( !thisTrainer.getValue().contains(courseCount) ) {//if this trainer's courses don't already contain this course
                                    trainerOptionsList.put(trainerEntry.getKey(), thisTrainer.getKey().toString() + "\n");
                                }
                            }
                            
                        }
                        
                        //ask user to select from available trainers
                        Object[] trainerOptions = trainerOptionsList.values().toArray();
                        int trainerSelection = Utils.askForInt(Utils.getOptionsNames(trainerOptions) + "\nPlease make a selection", Utils.getOptionsNumbers(trainerOptions));
                        
                        for( Map.Entry<Integer, String> availableTrainer : trainerOptionsList.entrySet() ){
                        
                            if ( availableTrainer.getValue().equals(trainerOptions[trainerSelection - 1].toString()) ) {
                                //add existing trainer(from another course) to this course's trainers
                                trainers.add(School.allTrainers.get(availableTrainer.getKey()).firstKey());
                                //add this course's id to existing trainer's courses
                                School.allTrainers.get(availableTrainer.getKey()).firstEntry().getValue().add(courseCount);
                            }
                        
                        }

                    }

                }
                
                /////////////////////ADD STUDENTS///////////////////////
                //ask for number of students to insert to the course
                numberOfStudents = Utils.askForInt("How many STUDENTS do you want to insert to course \"" + title + "\"?");

                students = new ArrayList<>();
                //create new students manually, numberOfStudents times
                for( int i = 0; i < numberOfStudents; i++ ){

                    //ask user if student is a new entry or an existing one to select from, but only if there is at least one student in the pseudo database that does not already belong to this course
                    action = ( School.allStudents.size() > students.size() ) ? Utils.askForInt("Student " + i + " is -> " + Utils.getOptionsNames(options), Utils.getOptionsNumbers(options)) : 1;
                    if ( action == 1 ) {//new entry

                        //create new Student object
                        Student newStudent = new Student(mode);
                        //set student's assignment's course
                        for( Assignment thisAssignment : newStudent.getAssignments() ){
                            thisAssignment.setCourseID(courseCount);
                        }
                        
                        //put it in this course's students
                        students.add(newStudent);
                        //put new student to school's allStudents master map. hashed student => ( student => ( array of course ids ) )
                        ArrayList<Integer> studentCourses = new ArrayList<>();
                        studentCourses.add(courseCount);
                        innerStudent = new TreeMap<>();
                        innerStudent.put(newStudent, studentCourses);
                        School.allStudents.put(newStudent.hashCode(), innerStudent);

                    }
                    else {//select from existing students

                        //build list with available students to select from, that do not already belong to this course
                        TreeMap<Integer, String> studentOptionsList = new TreeMap<>();

                        for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : School.allStudents.entrySet() ){

                            //this loop runs only once since child TreeMap has only 1 element
                            for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                if ( !thisStudent.getValue().contains(courseCount) ) {//if this student's courses don't already contain this course
                                    studentOptionsList.put(studentEntry.getKey(), thisStudent.getKey().toString() + "\n");
                                }
                            }

                        }

                        //ask user to select from available students
                        Object[] studentOptions = studentOptionsList.values().toArray();
                        int studentSelection = Utils.askForInt(Utils.getOptionsNames(studentOptions) + "\nPlease make a selection", Utils.getOptionsNumbers(studentOptions));

                        for( Map.Entry<Integer, String> availableStudent : studentOptionsList.entrySet() ){

                            if ( availableStudent.getValue().equals(studentOptions[studentSelection - 1].toString()) ) {
                                //add this course's id to existing student's courses
                                School.allStudents.get(availableStudent.getKey()).firstEntry().getValue().add(courseCount);
                                //possibly add extra assignments to this pre-existing student, for this course specifically
                                School.allStudents.get(availableStudent.getKey()).firstKey().addAssignments(mode,courseCount);
                                //add existing student(from another course) to this course's students
                                students.add(School.allStudents.get(availableStudent.getKey()).firstKey());
                            }

                        }

                    }

                }

                break;
                
            //automatic mode (if mode is not 1 then it most definitely is 2 = automatic)
            default:
                
                //get random course title
                title = Utils.COURSETITLES[new Random().nextInt(Utils.COURSETITLES.length)];
                
                //get random course stream depending on title
                for( String str : Utils.COURSESTREAMS ){
                    if ( title.contains(str) ) {
                        stream = str;
                    }
                }
                //if stream hasnt become one of the available options yet, because the course's title is not indicative, then randomize it
                if ( stream == null ) {
                    stream = Utils.COURSESTREAMS[new Random().nextInt(Utils.COURSESTREAMS.length)];
                }
                
                //get random course type
                type = Utils.COURSETYPES[new Random().nextInt(Utils.COURSETYPES.length)];
                
                //get random start and end dates (now + 0-8 weeks, startdate + 4-12 weeks) to form course duration
                LocalDate rndStartDate = LocalDate.now().plusWeeks(new Random().nextInt(9));
                LocalDate rndEndDate = rndStartDate.plusWeeks(new Random().nextInt(9) + 4);
                duration = new TreeSet<>();
                duration.add(rndStartDate);
                duration.add(rndEndDate);
                
                //////////////////////ADD TRAINERS////////////////////
                //random number of trainers to add
                numberOfTrainers = ThreadLocalRandom.current().nextInt(1, 4);

                trainers = new ArrayList<>();
                //create new trainers automatically, numberOfTrainers times
                for( int i = 0; i < numberOfTrainers; i++ ){

                    //randomize the new/existing decision
                    action = ( School.allTrainers.size() > trainers.size() ) ? ThreadLocalRandom.current().nextInt(1, 3) : 1;
                    if ( action == 1 ) {//new entry

                        //create new Trainer object
                        Trainer newTrainer = new Trainer(mode);
                        //put it in this course's trainers
                        trainers.add(newTrainer);
                        //put new trainer to school's allTrainers master map. hashed trainer => ( trainer => ( array of course ids ) )
                        ArrayList<Integer> trainerCourses = new ArrayList<>();
                        trainerCourses.add(courseCount);
                        innerTrainer = new TreeMap<>();
                        innerTrainer.put(newTrainer, trainerCourses);
                        School.allTrainers.put(newTrainer.hashCode(), innerTrainer);

                    }
                    else {//select from existing trainers

                        //build list with available trainers to select from, that do not already belong to this course
                        TreeSet<Integer> trainerOptionsList = new TreeSet<>();
                        for( Map.Entry<Integer, TreeMap<Trainer, ArrayList<Integer>>> trainerEntry : School.allTrainers.entrySet() ){

                            //this loop runs only once since child TreeMap has only 1 element
                            for( Map.Entry<Trainer, ArrayList<Integer>> thisTrainer : trainerEntry.getValue().entrySet() ){
                                if ( !thisTrainer.getValue().contains(courseCount) ) {//if this trainer's courses don't already contain this course
                                    trainerOptionsList.add(trainerEntry.getKey());
                                }
                            }
                            
                        }
                        
                        //randomize selection from available trainers
                        Object[] trainerOptions = trainerOptionsList.toArray();
                        int trainerSelection = ThreadLocalRandom.current().nextInt(0, trainerOptionsList.size());
                        //add randomly selected existing trainer(from another course) to this course's trainers
                        trainers.add(School.allTrainers.get((int)trainerOptions[trainerSelection]).firstKey());
                        //add this course's id to existing trainer's courses
                        School.allTrainers.get((int)trainerOptions[trainerSelection]).firstEntry().getValue().add(courseCount);

                    }

                }
                
                /////////////////////////ADD STUDENTS//////////////////////
                //random number of students to add
                numberOfStudents = ThreadLocalRandom.current().nextInt(4, 8);

                students = new ArrayList<>();
                //create new students automatically, numberOfStudents times
                for( int i = 0; i < numberOfStudents; i++ ){

                    //randomize the new/existing decision
                    action = ( School.allStudents.size() > students.size() ) ? ThreadLocalRandom.current().nextInt(1, 3) : 1;
                    if ( action == 1 ) {//new entry

                        //create new student object
                        Student newStudent = new Student(mode);
                        //set student's assignment's course
                        for( Assignment thisAssignment : newStudent.getAssignments() ){
                            thisAssignment.setCourseID(courseCount);
                        }
                        
                        //put it in this course's students
                        students.add(newStudent);
                        //put new student to school's allStudents master map. hashed student => ( student => ( array of course ids ) )
                        ArrayList<Integer> studentCourses = new ArrayList<>();
                        studentCourses.add(courseCount);
                        innerStudent = new TreeMap<>();
                        innerStudent.put(newStudent, studentCourses);
                        School.allStudents.put(newStudent.hashCode(), innerStudent);

                    }
                    else {//select from existing students

                        //build list with available students to select from, that do not already belong to this course
                        TreeSet<Integer> studentOptionsList = new TreeSet<>();
                        for( Map.Entry<Integer, TreeMap<Student, ArrayList<Integer>>> studentEntry : School.allStudents.entrySet() ){

                            //this loop runs only once since child TreeMap has only 1 element
                            for( Map.Entry<Student, ArrayList<Integer>> thisStudent : studentEntry.getValue().entrySet() ){
                                if ( !thisStudent.getValue().contains(courseCount) ) {//if this student's courses don't already contain this course
                                    studentOptionsList.add(studentEntry.getKey());
                                }
                            }

                        }

                        //randomize selection from available trainers
                        Object[] studentOptions = studentOptionsList.toArray();
                        int studentSelection = ThreadLocalRandom.current().nextInt(0, studentOptionsList.size());
                        //add this course's id to existing student's courses
                        School.allStudents.get((int)studentOptions[studentSelection]).firstEntry().getValue().add(courseCount);
                        //possibly add extra assignments to this pre-existing student, for this course specifically
                        School.allStudents.get((int)studentOptions[studentSelection]).firstKey().addAssignments(mode,courseCount);
                        //add randomly selected existing student(from another course) to this course's students
                        students.add(School.allStudents.get((int)studentOptions[studentSelection]).firstKey());
                        

                    }

                }
                
                break;
        }
        
        
        //increase course count
        courseCount++;
        
        
    }

    public String getTitle() {
        return title;
    }

    public String getStream() {
        return stream;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return title + " | " + stream + " | " + type + " | " + duration.first() + " | " + duration.last();
    }
    
    public String show() {
        return "Course{" + "title=" + title + ", stream=" + stream + ", type=" + type + ", duration=" + duration + ",\n     trainers=" + trainers + "\n     students=" + students + "}\n";
    }
    
    
    
    
}
