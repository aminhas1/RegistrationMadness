package registrationMadness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CourseTest {
    Student student1;
    Student student2;
    Course course1;
   
    @Before
    public void setUp() throws Exception {
        student1 = new Student("Abeer");
        student2 = new Student("Minhas");
        course1 = new Course(1);
    }

    @Test
    public void testClass() {
        assertEquals(1,course1.getID());
        assertEquals(0,course1.getMorningRoster().size());
        assertEquals(0,course1.getEveningRoster().size());
        assertEquals(0,course1.getWaitlist().size());
        assertEquals(0,course1.getTotalEnrolled());
        assertEquals(0,course1.getTotalEnrolledMorning());
        assertEquals(0,course1.getTotalEnrolledEvening());
    }

    @Test
    public void testIsFullMorning() {
        assertFalse(course1.isFullMorning());
        Student studentArray[] = new Student[10000];
        for(int i = 0; i < studentArray.length; i++) {
            studentArray[i] = new Student(""+i);
            studentArray[i].register(course1);
        }
        assertTrue(course1.isFullMorning());
    }

    @Test
    public void testIsFullEvening() {
        assertFalse(course1.isFullEvening());
        Student studentArray[] = new Student[10000];
        for(int i = 0; i < studentArray.length; i++) {
            studentArray[i] = new Student(""+i);
            studentArray[i].register(course1);
        }
        assertTrue(course1.isFullEvening());
    }

    @Test
    public void testIsOnAList() {
        assertFalse(course1.isOnAList(student1));
        student1.register(course1);
        assertTrue(course1.isOnAList(student1));
        assertFalse(course1.isOnAList(student2));
        student2.register(course1);
        assertTrue(course1.isOnAList(student2));
        student1.withdraw(course1);
        assertFalse(course1.isOnAList(student1));
    }

    @Test
    public void testAddToMorningRoster() {
        course1.addToMorningRoster(student1);
        assertTrue(course1.isOnAList(student1));
        student1.withdraw(course1);
    }

    @Test
    public void testAddToEveningRoster() {
        course1.addToEveningRoster(student1);
        assertTrue(course1.isOnAList(student1));
        student1.withdraw(course1);
    }

    @Test
    public void testAddToWaitlist() {
        course1.addToWaitlist(student1);
        assertTrue(course1.getWaitlist().contains(student1));
    }

    @Test
    public void testRemoveFromRoster() {
        student1.register(course1);
        assertTrue(course1.isOnAList(student1));
        course1.removeFromRoster(student1);
        assertFalse(course1.isOnAList(student1));
    }

    @Test
    public void testRemoveFromWaitlist() {
        course1.addToWaitlist(student1);
        assertTrue(course1.getWaitlist().contains(student1));
        course1.removeFromWaitlist(student1);
        assertFalse(course1.getWaitlist().contains(student1));
    }

    @Test
    public void testUpdateWaitlist() {
        course1.addToWaitlist(student1);
        student2.register(course1);
        student2.withdraw(course1);
        assertFalse(course1.getWaitlist().contains(student1));
        assertTrue(student1.getRegisteredCourses().contains(course1));
    }

    @Test
    public void testRemoveFromWaitlistStudent() {
        course1.addToWaitlist(student1);
        assertTrue(course1.getWaitlist().contains(student1));
        course1.removeFromWaitlist(student1);
        assertFalse(course1.getWaitlist().contains(student1));
    }

}
