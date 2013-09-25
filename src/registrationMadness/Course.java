package registrationMadness;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Vector;
/**
 * Defines a Course object which contains a courseID,
 * a waitlist and a class roster containing a list
 * of student enrolled in a course.
 * @author aminhas
 *
 */
public class Course{
    public static final int MAXROSTER = 25; 
    public static final int MAXCOURSESPERSTUDENT  = 3;
    private int courseID;
    private Vector<Student> morningRoster;
    private Queue<Student> waitlist;
    private Vector<Student> eveningRoster;

    
    /**
     * Constructor for Course object, which initializes 
     * the roster and waitlists as well as sets the courseID
     * to the new ID
     * @param courseID
     */
    public Course(int courseID) {
        this.courseID = courseID;
        morningRoster = new Vector<Student>();
        waitlist = new LinkedList<Student>();
        eveningRoster = new Vector<Student>();
    }
    
    /**
     * Returns the course ID
     */
    public int getID() {
        return courseID;
    }
    
    /**
     * Returns a roster of enrolled students for the morning Course
     */
    public Vector<Student> getMorningRoster() {
        return morningRoster;
    }
    
    /**
     * Returns a roster of enrolled students for the morning Course
     */
    public Vector<Student> getEveningRoster() {
        return eveningRoster;
    }
    
    
    /**
     * Returns a roster of a waitlist for the  Course
     * @return
     */
    public Queue<Student> getWaitlist(){
        return waitlist;
    }
    
    /**
     * Returns true if the morning section is full
     */
    public boolean isFullMorning() {
        return morningRoster.size() == MAXROSTER;
    }
    
    /**
     * Returns true if the evening section is full
     */
    public boolean isFullEvening() {
        return eveningRoster.size() == MAXROSTER;
    }
    
    /**
     * returns the total enrolled in the morning schedule
     */
    protected int getTotalEnrolledMorning() {
        return getMorningRoster().size();
    }
  
    /**
     * Returns the total enrolled for the evening section
     * @return
     */
    protected int getTotalEnrolledEvening() {
        return getEveningRoster().size();
    }
    
    /**
     * Returns the total enrolled for both the morning and evening
     * section
     */
    protected int getTotalEnrolled() {
        return getTotalEnrolledEvening() + getTotalEnrolledMorning();
    }


    /**
     * Checks to see if a student has registered for a class or
     * is on the waitlist
     */
    public boolean isOnAList(Student student) {
        return (getWaitlist().contains(student) || getMorningRoster().contains(student) 
                || getEveningRoster().contains(student));
    }
    
    /**
     * Adds a student to a roster so that they are officially
     * enrolled in a Class
     */
    synchronized public boolean addToMorningRoster(Student student){
        if(getWaitlist().contains(student)) {
            getWaitlist().remove(student);
        }
        if(getTotalEnrolledMorning() < MAXROSTER) {
            getMorningRoster().add(student);
            return true;
        }
        else if(getTotalEnrolledEvening() < MAXROSTER) {
            getEveningRoster().add(student);
            
            return true;
        }
        else {
            getWaitlist().add(student);
        }
        return false;
    } 

    /**
     * Adds a student to a roster so that they are officially
     * enrolled in a Class
     */
    synchronized public boolean addToEveningRoster(Student student){
        if(getWaitlist().contains(student)) {
            getWaitlist().remove(student);
        }
        if(getTotalEnrolledEvening() < MAXROSTER) {
            getEveningRoster().add(student);
            return true;
        }
        else if(getTotalEnrolledMorning() <MAXROSTER) {
            getMorningRoster().add(student);
            return true;
        }
        else {
            getWaitlist().add(student);
        }
        return false;
    }

    /**
     * Adds a student to a waitlist so that they are officially 
     * on the waitlist for the course
     */
    synchronized public void addToWaitlist(Student student) {
        if(!getMorningRoster().contains(student) && !getEveningRoster().contains(student)) {
            if(!getWaitlist().contains(student)) {
                getWaitlist().add(student);
            }
        }
    }
    
    /**
     * Removes a student from the Class roster. if possible.
     */
    synchronized public void removeFromRoster(Student student) {
        if(getEveningRoster().contains(student)) {
            getEveningRoster().remove(student);
            updateWaitlist();
        }
        else {
            getMorningRoster().remove(student);
            updateWaitlist();
        }
    }
    
    /**
     * Removes and returns the first student in the waitlist
     */
    synchronized public Student removeFromWaitlist() {
        try {
            return getWaitlist().remove();
        }
        catch(NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Student is removed from the waitlist they
     * are registered for the course
     * @return
     */
    synchronized public boolean updateWaitlist() {
        Student student = removeFromWaitlist();
        if(student != null) {
            if(student.getTotalCourses() < MAXCOURSESPERSTUDENT) {
                student.register(this);
            }   
        }              
        return false;
    }
    
    /**
     * Removes a student from the Course waitlist, if possible
     * @param student
     */
    synchronized public void removeFromWaitlist(Student student) {
        if(getWaitlist().contains(student)) {
            getWaitlist().remove(student);
       }
    }
    
    
    /**
     * Returns a String of the courseID number
     */
    public String toString() {
        return ""+courseID;
    }
}
