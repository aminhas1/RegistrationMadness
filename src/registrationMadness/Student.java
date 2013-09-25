package registrationMadness;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
/**
 * This class creates a Student object that can
 * register, withdraw, and inquire about classes. 
 * @author Abeer Minhas
 *
 */
public class Student implements Runnable{
    public static final int MAXCOURSES = 3;
    public static final int NUMPREFERREDCOURSES = 3;
    String name;
    int totalCourses;
    Vector<Course> preferredCourses;
    Vector<Course> registeredCourses;
    boolean triedRegisteringForPreferred;
    enum TimePreference {
        MORNING, EVENING
        } 
    TimePreference preferredTime;
    static CourseList registrar = new CourseList();
    
    /**
     * Constructor creates a Student object that randomly
     * sets up the preferred courses and preferred section time
     * for course registration
     */
    public Student(String name) {
        this.name = name;
        totalCourses = 0;
        preferredCourses = new Vector<Course>();
        registeredCourses = new Vector<Course>();
        triedRegisteringForPreferred = false;
        setPreferredClasses();
        setPreferredTime();
    }
    
    /**
     * @returns the max number of courses a student can take
     */
    public int getMaxCourses() {
        return MAXCOURSES;
    }
    
    /**
     * Returns the name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the registrar, which is the same for every student
     */
    synchronized public CourseList getRegistrar() {
        return registrar;
    }
    
    /**
     * Randomly sets preferred classes for the student
     **/
    public void setPreferredClasses(){
        int size = getRegistrar().size();
        Random rand = new Random();
        while(getPreferredCourses().size() != NUMPREFERREDCOURSES) {
            int randID = rand.nextInt(size);
            if(!getPreferredCourses().contains(getRegistrar().getIndex(randID))) {
                getPreferredCourses().add(getRegistrar().getIndex(randID));
            }
        }
    }
    
    /**
     * Randomly determines which section time they prefer. 
     */
    public void setPreferredTime() {
        Random rand = new Random();
        int section = rand.nextInt(2);
        preferredTime = (section == 0) ? 
                TimePreference.MORNING : TimePreference.EVENING;
        
    }
    /**
     * Returns the preferred class time of the Student
     */
    public TimePreference getPreferredTime() {
        return preferredTime;
    }
    
    /**
     * Returns the students preferred courses
     */
    public Vector<Course> getPreferredCourses(){
        return preferredCourses;
    }
    
    /**
     * Returns the students registered courses
     */
    public Vector<Course> getRegisteredCourses(){
        return registeredCourses;
    }
    
    /**
     * Returns the Student's preferred section time
     */
    public TimePreference getPreferredSection() {
        return preferredTime;
    }
   
    /**
     * Returns the total number of courses the student is registered for
     */
    public int getTotalCourses() {
        return getRegisteredCourses().size();
    }
    
    /**
     * Ask whether there is room in a given section for a course (that is, the roster is not yet full).
     */
    public boolean askIfFull(Course course) {
        if (getPreferredSection() == TimePreference.MORNING) {
            if(course.isFullMorning()) {
                //Student will settle for other section if preferred section time is full
                return course.isFullEvening();
            }
        }
        if (getPreferredSection() == TimePreference.EVENING) {
            if(course.isFullEvening()) {
                return course.isFullMorning();
            }
        }
          
        return false;
    }
    
    /**
     * Try to register for a given section. 
     * Returns true if the registration succeeds
     */
    public boolean register(Course course) {
        if(finishedRegistration()) return false;
        if(getRegisteredCourses().contains(course)) return false;
        if(getPreferredTime() == TimePreference.MORNING) {
            if(course.addToMorningRoster(this)) {
                getRegisteredCourses().add(course);
                return true;
            }
        }
        else {
            if(course.addToEveningRoster(this)) {
                getRegisteredCourses().add(course);
                return true;
            }
        }
        return false;
    }
   
    /**
        Withdraw from a given section (un-register, or remove from waiting list). 
     */
    public boolean withdraw(Course course) {
        if(!course.isOnAList(this)) return false;
        if(!getPreferredCourses().contains(course)) {
            course.removeFromRoster(this);
            course.removeFromWaitlist(this);
            if(getRegisteredCourses().contains(course)) {
                getRegisteredCourses().remove(course);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Finds a random class from the registrar as long as it is not
     * a preferred course. This function is called once registering
     * for preferred courses has not been successful.
     */
    protected Course findRandomClass() {
        Random rand = new Random();
        int size = getRegistrar().size();
        Course course = getRegistrar().getIndex(rand.nextInt(size));
        while(getPreferredCourses().contains(course)) {
            course = getRegistrar().getIndex(rand.nextInt(size));
        }
        return course;
    }
    
    /**
     * Finds a random registered course or a course that a student
     * is on the waitlist of. The function is used to allow a 
     * student to withdraw from a course roster or waitlist. 
     * As such, this function will not return any of their preferred
     * courses, which will never be withdrawn. 
     */
    protected Course findRandomRegisteredOrWaitlistClass() {
        Vector<Course> allCourses = new Vector<Course>();
        Iterator<Course> itr = getRegistrar().iterator();
        while(itr.hasNext()) {
            Course course = itr.next();
            if (course.getWaitlist().contains(this) && !getRegisteredCourses().contains(course)) {
                allCourses.add(course);
            }
        }
        allCourses.addAll(getRegisteredCourses());
        Random rand = new Random();
        int size = allCourses.size();
        Course course = allCourses.get(rand.nextInt(size));

        while(getPreferredCourses().contains(course)) {
            course = getRegistrar().getIndex(rand.nextInt(size));
        }
        return course;
    }
 
    /**
     * Returns true if the student has successfully registered for three coursee
     */
    public boolean finishedRegistration() {
        return getRegisteredCourses().size() == MAXCOURSES;
    }
    
    /**
     * Used in the print Schedule function this statement returns
     * what time the student has registered a course in their
     * registration list. 
     */
    public TimePreference timeOfClass(int index) {
        try {
            Course course = getRegisteredCourses().get(index);
            int courseID = course.getID();
            if (getRegistrar().get(courseID).getMorningRoster().contains(this)) {
                return TimePreference.MORNING;
            }
            else if(getRegistrar().get(courseID).getEveningRoster().contains(this)) {
                return TimePreference.EVENING;
            }
        }
        catch(IndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }
    
    /**
     * Returns the count for how many courses registered are 
     * at the students time preference 
     */
    protected int countRegisteredPreferredTime() {
       int size = getRegisteredCourses().size();
       int count = 0;
       for (int i = 0; i < size; i++) {
           if(timeOfClass(i) == getPreferredTime()) {
               count++;
           }
       }
       return count;
    }
    
    /**
     * returns the count for how many courses registered are 
     * from the student preferred Courses list
     */
    protected int countRegisteredPreferredCourses() {
        int size = getRegisteredCourses().size();
        int count = 0;
        for(int i=0; i< size; i++) {
            if(getPreferredCourses().contains(getRegisteredCourses().get(i))) {
                count++;
            }
        }
        return count;
    }
    
    public String toString() {
        return name;
    }
    

    /**
     * Run method for each thread 
     */
    @Override
    public void run() {
        while(!finishedRegistration()) {
            try {
                //Preferred courses first and the other courses second
                if(!triedRegisteringForPreferred) {
                    for(int i = 0; i < NUMPREFERREDCOURSES; i++) {
                        register(getPreferredCourses().get(i));
                        Thread.sleep(1000);

                    }
                }
                triedRegisteringForPreferred = true;
                if(!finishedRegistration()) {
                    Course backupCourse = findRandomClass();
                    Random rand = new Random();
                    int randomDecision = rand.nextInt(3);
                    if(randomDecision == 0) {
                        if(!askIfFull(backupCourse)) {
                            Thread.sleep(1000);
                            register(backupCourse);
                        }
                    }
                    else if(randomDecision == 1){
                        register(backupCourse);
                    }
                    else {
                        Course withdrawRandom = findRandomRegisteredOrWaitlistClass();
                        withdraw(withdrawRandom);
                    }
                }
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        getRegistrar().removeFromAllWaitingLists(this);
    }
    
    /**
     * Prints the student's schedule along with their desired courses,
     * desired time. 
     */
   public void printSchedule() {
       System.out.print("Student " + getName() + " REGISTERED: [");
                for (int i =0; i< getRegisteredCourses().size() ; i++) {
                    if(i == getRegisteredCourses().size() -1 ) {
                        System.out.print(getRegisteredCourses().get(i) + " - " +
                                timeOfClass(i));
                        break;
                    }
                    System.out.print(getRegisteredCourses().get(i).toString() + 
                            " - " + timeOfClass(i) + ", ");
                }
                System.out.print("]");
                
        System.out.println("\n\t DESIRED COURSES: " + getPreferredCourses().toString() 
                + " obtained " + countRegisteredPreferredCourses() + " desired courses" + 
                " \n\t DESIRED TIME: [" + getPreferredTime() + "] obtained " + countRegisteredPreferredTime() +
                " courses at desired time preference");
        
    }

}
