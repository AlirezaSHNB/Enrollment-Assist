import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.studyRecord.StudyRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class IsPassedTest {

    @ParameterizedTest
    @MethodSource
    public void passTest(double grade, GraduateLevel graduateLevel, String courseGraduateLevel) throws ExceptionList {
        Course course = new Course("1234567", "test", 2, courseGraduateLevel);
        StudyRecord studyRecord = new StudyRecord("00001", course, grade);
        assertTrue(studyRecord.isPassed(graduateLevel));
    }

    private static Stream<Arguments> passTest() {
        return Stream.of(arguments(10.01, GraduateLevel.Undergraduate, "Undergraduate"),
                arguments(10.0, GraduateLevel.Undergraduate, "Undergraduate"),
                arguments(12.01, GraduateLevel.Masters, "Masters"),
                arguments(12.0, GraduateLevel.Masters, "Masters"),
                arguments(14.01, GraduateLevel.PHD, "PHD"),
                arguments(14.0, GraduateLevel.PHD, "PHD"),
                arguments(10.01, GraduateLevel.Masters, "Undergraduate"),
                arguments(10.0, GraduateLevel.Masters, "Undergraduate"),
                arguments(10.01, GraduateLevel.PHD, "Undergraduate"),
                arguments(10.0, GraduateLevel.PHD, "Undergraduate"),
                arguments(12.01, GraduateLevel.PHD, "Masters"),
                arguments(12.0, GraduateLevel.PHD, "Masters"),
                arguments(10.01, GraduateLevel.Undergraduate, "Masters"),
                arguments(10.0, GraduateLevel.Undergraduate, "Masters"),
                arguments(10.01, GraduateLevel.Undergraduate, "PHD"),
                arguments(10.0, GraduateLevel.Undergraduate, "PHD"),
                arguments(12.01, GraduateLevel.Masters, "PHD"),
                arguments(12.0, GraduateLevel.Masters, "PHD")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void failTest(double grade, GraduateLevel graduateLevel, String courseGraduateLevel) throws ExceptionList {
        Course course = new Course("1234567", "test", 2, courseGraduateLevel);
        StudyRecord studyRecord = new StudyRecord("00001", course, grade);
        assertFalse(studyRecord.isPassed(graduateLevel));
    }

    private static Stream<Arguments> failTest() {
        return Stream.of(arguments(9.99, GraduateLevel.Undergraduate, "Undergraduate"),
                arguments(11.99, GraduateLevel.Masters, "Masters"),
                arguments(13.99, GraduateLevel.PHD, "PHD"),
                arguments(9.99, GraduateLevel.Masters, "Undergraduate"),
                arguments(9.99, GraduateLevel.PHD, "Undergraduate"),
                arguments(11.99, GraduateLevel.PHD, "Masters")
        );
    }
}
