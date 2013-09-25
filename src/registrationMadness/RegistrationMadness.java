package registrationMadness;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The main class for the Registration Madness Project
 * Creates 190 student threads the compete to register
 * for classes 
 * @author Abeer Minhas
 *
 */
public class RegistrationMadness {
    public static final int MAXSTUDENTS = 190;
    public static final int POOLSIZE = 190;

    /**
     * Prints out the number of students that 
     * got all of their desired courses and desired time
     * @param studentArray
     */
    public static void countPerfectSchedule(Student[] studentArray) {
        int maxCourses = studentArray[0].getMaxCourses();
        int count =0;
        for(int i =0;i<MAXSTUDENTS;i++) {
            if(studentArray[i].countRegisteredPreferredTime() == maxCourses 
                    && studentArray[i].countRegisteredPreferredCourses() == maxCourses) {
                count++;
            }
        }
        double total = ((double)count / (double)MAXSTUDENTS) * 100;
        System.out.println("PERFECT SCHEDULES: Out of " + MAXSTUDENTS + " students " + count + 
                " received perfect schedules. For a total of " + total + "%.");
        
    }
    
    /**
     * Main function that creates and executes threads. Prints out
     * each student's schedule along with how many students received a
     * perfect schedule
     */
    public static void main(String[] args) {
        System.out.println("Determining Student Schedules. This should take" +
        		" about 10 - 20 seconds. \n\nEXPECTED OUTPUT: Each student's schedule will be" +
        		" printed along with their desired courses and time preference. Then the rosters for" +
        		" each course will be printed out.");
        Student[] studentArray = new Student[MAXSTUDENTS];
        long startTime = System.nanoTime();

        ExecutorService pool = Executors.newFixedThreadPool(POOLSIZE);
        for(int i = 0; i< MAXSTUDENTS; i++) {
            studentArray[i] = new Student(""+i);
            pool.execute(studentArray[i]);
        }
        
        pool.shutdown();
        try {
            pool.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        for(int i = 0; i< MAXSTUDENTS; i++) {
            studentArray[i].printSchedule();

        }
        studentArray[0].getRegistrar().printRosters();
        countPerfectSchedule(studentArray);
        System.out.println("Time: " + ((endTime - startTime)/1000000000.0)+ " seconds.");
        studentArray[0].getRegistrar().clear();
        System.out.println("Finished all threads");
    }
}
