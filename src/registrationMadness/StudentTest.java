package registrationMadness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class StudentTest {
    Student student;
    Student student2;
    @Before
    public void setUp() throws Exception {
        student = new Student("Abeer");
        student2 = new Student("Minhas");
    }

    @Test
    public void testStudent() {
        assertTrue(student.getPreferredCourses().size() == 3);
        assertTrue(student.getRegisteredCourses().size() == 0);
        assertTrue(student.getTotalCourses() == 0);
    }

    @Test
    public void testRegister() {
        assertEquals(0,student.getTotalCourses());
        student.register(student.getRegistrar().getIndex(0));
        assertEquals(1,student.getTotalCourses());
        student.register(student.getRegistrar().getIndex(0));
        assertEquals(1,student.getTotalCourses());
        student.register(student.getRegistrar().getIndex(1));
        assertEquals(2,student.getTotalCourses());
        student2.register(student.getRegistrar().getIndex(0));
        assertEquals(1,student2.getTotalCourses());
        assertEquals(2,student.getTotalCourses());
        student.getRegistrar().clear();
        
    }

    @Test
    public void testAskIfFull() {
        assertFalse(student.askIfFull(student.getRegistrar().getIndex(3)));
        Student[] studentArray = new Student[55];
        for(int i = 0; i < 55; i++) {
            studentArray[i] = new Student(""+i);
            studentArray[i].register(studentArray[i].getRegistrar().getIndex(3));
        }
        assertTrue(student.askIfFull(student.getRegistrar().getIndex(3)));
        assertTrue(student2.askIfFull(student2.getRegistrar().getIndex(3)));
        student.getRegistrar().clear();

    }

    @Test
    public void testWithdraw() {
        student.register(student.getRegistrar().getIndex(0));
        assertEquals(1,student.getTotalCourses());
        assertEquals(1,student.getRegistrar().getIndex(0).getTotalEnrolled());
        student.withdraw(student.getRegistrar().getIndex(0));
        /*
         * Student will not withdraw a course if it is a preferred course. 
         */
        if(!student.getPreferredCourses().contains(student.getRegistrar().getIndex(0))) {
            assertEquals(0,student.getTotalCourses());
        }
        else {
            assertEquals(1,student.getTotalCourses());
        }
        student.getRegistrar().clear();

    }

    @Test
    public void testFindRandomClass() {
        Student stud = new Student("Abeer");
        Course course = stud.findRandomClass();
        assertFalse(stud.getPreferredCourses().contains(course));
        assertTrue(stud.getRegistrar().contains(course));
    }

    @Test
    public void testFindRandomRegisteredClass() {
        student.register(student.getRegistrar().getIndex(0));
        student.register(student.getRegistrar().getIndex(1));
        student.register(student.getRegistrar().getIndex(2));
        Course course = student.findRandomRegisteredOrWaitlistClass();
        assertFalse(student.getPreferredCourses().contains(course));
        assertTrue(student.getRegistrar().contains(course));
        student.getRegistrar().clear();
    }

    @Test
    public void testFinishedRegistration() {
        assertFalse(student.finishedRegistration());
        student.register(student.getRegistrar().getIndex(0));
        student.register(student.getRegistrar().getIndex(1));
        student.register(student.getRegistrar().getIndex(2));
        assertTrue(student.finishedRegistration());
        assertFalse(student2.finishedRegistration());
        student.getRegistrar().clear();
    }

}
