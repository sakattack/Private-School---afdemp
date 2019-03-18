/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Sakel
 */
class Trainer implements Comparable {
    
    private String firstName;
    private String lastName;

    Trainer(int mode) {
        
        switch(mode){
            //manual mode
            case 1:

                while(true){
                
                    firstName = Utils.askForString("Trainer First Name");
                    lastName = Utils.askForString("Trainer Last Name");
                    
                    //check if entered trainer already exists
                    if ( School.allTrainers.containsKey(Objects.hash(firstName, lastName)) ) { System.out.println("Trainer already exists, please try again"); }
                    else { break; }
                    
                }
                

                break;
                
            //automatic mode (if mode is not 1 then it most definitely is 2 = automatic)
            default:
                
                while(true){
                    
                    firstName = Utils.FIRSTNAMES[new Random().nextInt(Utils.FIRSTNAMES.length)];
                    lastName = Utils.LASTNAMES[new Random().nextInt(Utils.LASTNAMES.length)];
                    
                    //check if random trainer already exists
                    if ( !School.allTrainers.containsKey(Objects.hash(firstName, lastName)) ) { break; }
                    
                }
                
                break;
        }
        
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName);
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
        final Trainer other = (Trainer) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Object o) {
        return o.hashCode() - this.hashCode();
    }
    
    public String show() {
        
        //build courses string
        String trainerCourses = "";
        //this loop runs only once since child TreeMap has only 1 element
        for( Map.Entry<Trainer, ArrayList<Integer>> thisTrainerCourses : School.allTrainers.get(this.hashCode()).entrySet() ){
            for( int i : thisTrainerCourses.getValue() ){
                trainerCourses += "\n   Course : " + School.getCourse(i).getTitle() + " | " + School.getCourse(i).getStream() + " | " + School.getCourse(i).getType();
            }
        }
        
        return firstName + " " + lastName + trainerCourses + "\n";
    }
    
    
}
