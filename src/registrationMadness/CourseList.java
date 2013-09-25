package registrationMadness;

import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
/**
 * CourseList is an object that contains all Courses that
 * students can register for
 * @author Abeer Minhas
 *
 */
public class CourseList {
    public static int NUMCLASSES = 12;
    public static int FIRSTCOURSEID = 101;
    Vector<Course> classList; 
    
    /**
     * Constructor for a CourseList
     */
    public CourseList() {
        classList = new Vector<Course>();
        createRegistrar();
    }
    
    /**
     * Returns an iterator for the CourseList object
     * @return
     */
    public Iterator<Course> iterator() {
        return getList().iterator();
    }
    
    /**
     * Adds a course to the CourseList
     */
    public void add(Course course) {
        if(!getList().contains(course)) {
            getList().add(course);
        }
    }
    
    /**
     * Returns true if the CourseList contains the Course
     */
    public boolean contains(Course course) {
        return getList().contains(course);
    }
    
    /**
     * Creates courses to add to the CourseList 
     */
    public void createRegistrar() {
        for (int i =FIRSTCOURSEID ; i < (FIRSTCOURSEID + NUMCLASSES); i++) {
            Course course = new Course(i);
            add(course);
        }
    }
    
    /**
     * Removes a students from all waiting lists
     */
    synchronized public void removeFromAllWaitingLists(Student student) {
        Iterator<Course> itr = getList().iterator();
        while(itr.hasNext()) {
            Course course = itr.next();
            course.removeFromWaitlist(student);
        }
    }
    
    /**
     * Clears the entire CourseList object
     */
    public void clear() {
        for(int i = 0; i < getList().size(); i++) {
            Course course = getList().get(i);
            Vector<Student> morningRoster =  course.getMorningRoster();
            Vector<Student> eveningRoster = course.getEveningRoster();
            Queue<Student> waitlist = course.getWaitlist();
            morningRoster = null;
            eveningRoster = null;
            waitlist = null;
            course = null;
        }
        classList = new Vector<Course>();
        createRegistrar();
    }
    
    /**
     * Removes a class from the ClassList
     */
    public void remove(Course course) {
        if(getList().contains(course)) {
            getList().remove(course);
        }
    }
    
    /**
     * Returns the classList
     */
    public Vector<Course> getList() {
        return classList;
    }
    
    /**
     * Gets a class based on the CourseID
     */
    public Course get(int id) {
        Iterator<Course> itr = getList().iterator();
        while(itr.hasNext()) {
            Course course = itr.next();
            if(course.getID() == id) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Gets a class based on its index number
     */
    public Course getIndex(int index) {
        return getList().get(index);
    }
    
    /**
     * returns the size of the CourseList, i.e. the number of classes
     */
    public int size() {
        return getList().size();
    }

    /**
     * Prints out rosters
     */
    public void printRosters() {
        Iterator<Course> itr = iterator();
        int totalEnrolled = 0;
        System.out.println("\n\n ============== ROSTERS FOR EACH CLASS ===================");
        while(itr.hasNext()) {
            Course course = itr.next();
            totalEnrolled += course.getTotalEnrolled();
            System.out.println(course + "     Total Students: " + course.getTotalEnrolled() + "\n" +
            		"\tRegistered Morning: " + course.getMorningRoster() + "\n" + 
            		"\tRegistered Evening: " + course.getEveningRoster());
        }
        System.out.println("Total Enrolled in All Classes: " + totalEnrolled);
    }
    
    
}
