package registrationMadness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CourseListTest {
    CourseList registrar;
    Student student;
    
    @Before
    public void setUp() throws Exception {
        registrar = new CourseList();
        student = new Student("Abeer");
    }
    
    
    @Test
    public void testClassList() {
        assertTrue(registrar.getList().size() > 0);
    }

    @Test
    public void testAdd() {
        Course course = new Course(1);
        registrar.add(course);
        assertTrue(registrar.contains(course));
    }

    @Test
    public void testContains() {
        Course course = new Course(1);
        Course course2 = new Course(2);
        assertFalse(registrar.contains(course));
        registrar.add(course);
        assertTrue(registrar.contains(course));
        registrar.add(course2);
        assertTrue(registrar.contains(course2));
    }

    @Test
    public void testCreateRegistrar() {
        assertEquals(registrar.size(), CourseList.NUMCLASSES);
        assertTrue(registrar.contains(registrar.get(CourseList.FIRSTCOURSEID)));
        for(int i = CourseList.FIRSTCOURSEID; i < CourseList.NUMCLASSES; i++) {
            assertTrue(registrar.contains(registrar.get(i)));
        }
    }

    @Test
    public void testRemoveFromAllWaitingLists() {
        Course course1 = new Course(1);
        Course course2 = new Course(2);
        registrar.add(course1);
        registrar.add(course2);
        course1.addToWaitlist(student);
        course2.addToWaitlist(student);
        registrar.removeFromAllWaitingLists(student);
        assertFalse(course1.isOnAList(student));
        assertFalse(course2.isOnAList(student));
        
        
    }

    @Test
    public void testClear() {
        Course course = student.getRegistrar().getIndex(0);
        student.register(course);
        student.getRegistrar().clear();
        assertFalse(student.getRegistrar().getIndex(0).isOnAList(student));
    }

    @Test
    public void testRemove() {
        Course course = registrar.get(CourseList.FIRSTCOURSEID);
        registrar.remove(course);
        assertFalse(registrar.contains(course));
    
    
    }


    @Test
    public void testSize() {
        assertEquals(CourseList.NUMCLASSES, registrar.size());
    }



}
