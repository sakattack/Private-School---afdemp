/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privateschool;

import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author Sakel
 */
//A
public final class Utils {
    
    public static Boolean RUN = true;
    
    //DUMMY DATA
    public final static String[] FIRSTNAMES = {"Andrew","Sophia","John","Peter","Nick","James","Jim","Kate","Helen","Julia","Paul","Alice","Bruce","David","Laura","Ray","Steve","Tim"};
    public final static String[] LASTNAMES = {"Taylor","Wilson","Johnson","Williams","Brown","Smith","Jones","Davies","Walker","Morgan","Young","Ross","Quinn","Sanders","Lewis","Lee","Bennett","Cooper"};
    
    public final static String[] COURSETITLES = {"Java 101","Java 102","Java 103","C# 101","C# 102","C# 103","Intro to programming","OOP"};
    public final static String[] COURSESTREAMS = {"Java","C#"};//apart from dummy data they also play the role of predefined options for user to select from
    public final static String[] COURSETYPES = {"Full Time","Part Time"};//apart from dummy data they also play the role of predefined options for user to select from
    
    public final static String[] ASSIGNMENTTITLES = {"Title1","Title2","Title3","Title4","Title5","Title6","Title7","Title8","Title9","Title10"};
    public final static String[] ASSIGNMENTDESCRIPTIONS = {"Description1","Description2","Description3","Description4","Description5","Description6","Description7","Description8","Description9","Description10"};
    
    //INSERTION MODES
    public final static String[] INSERTIONMODES = {"Manual","Automatic"};//predefined options for user to select from

    //private and empty constructor. Never to be instantiated
    private Utils() {
    }
    
    //returns an int[] containing sequential numbers from 1 to as many needed according to how many elements the parameter array has (used for generating permitted values to be used with askForInt)
    public final static int[] getOptionsNumbers(Object[] options){
        
        int[] tmp = new int[options.length];
        
        for( int i=0; i<tmp.length; i++ ){
            tmp[i] = i + 1;
        }
        
        return tmp;
        
    }
    
    //returns a string containing all the elements of its parameter in the form of [1]element1 [2]element2 etc
    public final static String getOptionsNames(Object[] options){
        
        String tmp = "";
        
        for( int i=0; i<options.length; i++ ){
            tmp += " ["+(i + 1)+"] "+options[i];
        }
        
        return tmp;
        
    }
    
    public final static int askForInt(String question){

        Scanner sc = new Scanner(System.in);
        System.out.print(question + " : ");

        while (true) {
            if (!sc.hasNextInt()) {
                System.out.print("please try again (write an integer) : ");
                sc.next();
            } else {
                break;
            }
        }

        return sc.nextInt();

    }
    
    //overloaded version that keeps asking for integer within bounds
    public final static int askForInt(String question, int start, int end){

        Scanner sc = new Scanner(System.in);
        System.out.print(question + " [" + start + "-" + (end - 1) + "] : ");

        while (true) {
            if (!sc.hasNextInt()) {
                System.out.print("please try again (write an integer) : ");
                sc.next();
            } else {
                int n = sc.nextInt();
                if ( n >= start && n < end ) {
                    return n;
                }
                else {
                    System.out.print("please try again (choose from available choices) : ");
                }
                
            }
        }

    }
    
    //overloaded version that takes in permitted integer values and keeps asking for correct input if none is entered
    public final static int askForInt(String question, int[] availableChoices){

        Scanner sc = new Scanner(System.in);
        System.out.print(question + " : ");

        while (true) {
            if (!sc.hasNextInt()) {
                System.out.print("please try again (write an integer) : ");
                sc.next();
            } else {
                int n = sc.nextInt();
                for( int i : availableChoices ){
                    if ( n == i ) { return n; }
                }
                System.out.print("please try again (choose from available choices) : ");
            }
        }

    }
    
    public final static String askForString(String question){

        Scanner sc = new Scanner(System.in);
        System.out.print(question + " : ");

        while (true) {
            if (!sc.hasNextLine()) {
                System.out.print("please try again : ");
                sc.nextLine();
            } else {
                break;
            }
        }

        return sc.nextLine();

    }
    
    public final static LocalDate askForDate(String question){

        LocalDate date;
        Scanner sc = new Scanner(System.in);
        System.out.print(question + " : ");

        while (true) {
            
            if (!sc.hasNextLine()) {
                
                System.out.print("please try again : ");
                sc.nextLine();
                
            } else {
                
                String possibleDate = sc.nextLine();
                String[] dateParts = possibleDate.split("-");
                
                if ( dateParts.length != 3 ) {
                    System.out.print("please try again : ");
                    continue;
                }
                
                try{
                    date = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                }
                catch(Exception e){
                    System.out.print("please try again : ");
                    continue;
                }
                
                break;
                
            }
            
        }
        
        return date;

    }
    
}
