package PresentationSchedule;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class HasConflictTest {

    static PresentationSchedule presentationSchedule;

    @BeforeAll
    public static void setup() {
        presentationSchedule = new PresentationSchedule(); // Saturday, 09:00, 10:30
    }

    @Test
    public void dayOfWeekTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Sunday", "09:00", "10:30");
        assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    // no conflict
    @Test
    public void startTimeAfterEndTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:31", "12:00");
        assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void endTimeBeforeStartTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "07:30", "08:59");
        assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    // tangent conflict
    @Test
    public void startTimeEqualEndTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "10:30", "12:00");
        assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void endTimeEqualStartTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "07:30", "09:00");
        assertFalse(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    // conflict
    @Test
    public void startTimeBeforeEndTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "09:30", "11:00");
        assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @Test
    public void endTimeAfterStartTimeTest() throws ExceptionList {
        PresentationSchedule otherPresentationSchedule = new PresentationSchedule("Saturday", "08:00", "09:30");
        assertTrue(presentationSchedule.hasConflict(otherPresentationSchedule));
    }

    @AfterAll
    public static void reset() {
        presentationSchedule = null;
    }

}