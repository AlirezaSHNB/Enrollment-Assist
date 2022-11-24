import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.*;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.enrollmentList.EnrollmentList;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CheckEnrollmentRulesTest {

    static Student studentGPA0, studentGPA11, studentGPA15, studentGPA18;
    static Course math1, math2, equations, phy1, DA, chem, AI, AP, OS, DB, PP;

    static Section math1Section, math2Section, equationsSection, phy1Section, DASection, chemSection, APSection, AISection, OSSection, DBSection, PPSection;


    @BeforeAll
    public static void setup() throws Exception {
        List<Course> courses = initCourses();
        Program program = initProgram(courses);
        initStudents(program);
        initSections();
    }

    @ParameterizedTest
    @MethodSource
    public <V extends EnrollmentRuleViolation> void test(Student student, List<Section> sections, Set<Class<V>> expectedViolationTypes) {
        EnrollmentList enrollmentList = new EnrollmentList("test", student);
        for (Section s : sections) {
            enrollmentList.addSection(s);
        }
        assertEquals(enrollmentList.checkEnrollmentRules()
                .stream()
                .map(EnrollmentRuleViolation::getClass)
                .collect(Collectors.toSet()), expectedViolationTypes);
    }

    private static Stream<Arguments> test() throws Exception {
        return Stream.of(
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(math1Section, DASection, phy1Section)), Set.of(RequestedCourseAlreadyPassed.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(equationsSection, phy1Section, DBSection)), Set.of(PrerequisiteNotTaken.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(math2Section, DBSection, OSSection)), Set.of(ConflictOfClassSchedule.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(math2Section, chemSection, DASection)), Set.of(ExamTimeCollision.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(phy1Section, DASection, phy1Section)), Set.of(CourseRequestedTwice.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(phy1Section, DASection)), Set.of(MinCreditsRequiredNotMet.class)),
                arguments(studentGPA0, new ArrayList<Section>(Arrays.asList(APSection, AISection, OSSection, DASection, chemSection)), Set.of(MaxCreditsLimitExceeded.class)),
                arguments(studentGPA15, new ArrayList<Section>(Arrays.asList(APSection, DBSection, DASection, math2Section, phy1Section, PPSection)), Set.of(MaxCreditsLimitExceeded.class)),
                arguments(studentGPA11, new ArrayList<Section>(Arrays.asList(APSection, AISection, OSSection, DASection)), Set.of(MaxCreditsLimitExceeded.class)),
                arguments(studentGPA18, new ArrayList<Section>(Arrays.asList(APSection, OSSection, DASection, phy1Section, math2Section, PPSection, AISection)), Set.of(MaxCreditsLimitExceeded.class))
        );
    }

    public static List<Course> initCourses() throws ExceptionList {
        math1 = new Course("1234501", "Mathematics1", 4, "Undergraduate");
        math2 = new Course("1234502", "Mathematics2", 4, "Undergraduate");
        equations = new Course("1234503", "Equations", 4, "Undergraduate");
        phy1 = new Course("1234504", "Physics1", 4, "Undergraduate");
        DA = new Course("1234505", "DesignAlgorithm", 4, "Undergraduate");
        chem = new Course("1234506", "Chemistry", 4, "Undergraduate");
        AP = new Course("1234507", "AdvancedProgramming", 4, "Undergraduate");
        AI = new Course("1234508", "ArtificialIntelligence", 4, "Undergraduate");
        OS = new Course("1234509", "OperatingSystem", 4, "Undergraduate");
        DB = new Course("1234510", "Database", 4, "Undergraduate");
        PP = new Course("1234511", "ParallelProgramming", 4, "Undergraduate");
        Set<Course> preqMath2 = new HashSet<>();
        preqMath2.add(math1);
        math2.setPrerequisites(preqMath2);
        Set<Course> preqEquations = new HashSet<>();
        preqEquations.add(math1);
        preqEquations.add(math2);
        equations.setPrerequisites(preqEquations);
        return Arrays.asList(math1, math2, equations, phy1, DA, chem, AI, AP, OS, DB, PP);
    }

    public static Program initProgram(List<Course> courses) throws ExceptionList {
        Major major = new Major("1", "Computer", "Engineering");
        Program program = new Program(major, "Undergraduate", 1, 140, "Major");
        for (Course c : courses) {
            program.addCourse(c);
        }
        return program;
    }

    public static void initStudents(Program program) throws Exception {
        studentGPA0 = new Student("12345001", "Undergraduate");
        studentGPA0.addProgram(program);
        studentGPA11 = new Student("12345002", "Undergraduate");
        studentGPA11.addProgram(program);
        studentGPA11.setGrade("00001", math1, 11);
        studentGPA15 = new Student("12345003", "Undergraduate");
        studentGPA15.addProgram(program);
        studentGPA15.setGrade("00001", math1, 15);
        studentGPA18 = new Student("12345004", "Undergraduate");
        studentGPA18.addProgram(program);
        studentGPA18.setGrade("00001", math1, 18);
    }

    public static void initSections() throws Exception {
        math1Section = new Section(math1, "1", new ExamTime("2022-06-21T08:00", "2022-06-21T11:00"), new HashSet<>(List.of(new PresentationSchedule("Monday", "09:00", "10:30"), new PresentationSchedule("Wednesday", "09:00", "10:30"))));
        math2Section = new Section(math2, "1", new ExamTime("2022-06-22T08:00", "2022-06-22T11:00"), new HashSet<>(List.of(new PresentationSchedule("Saturday", "09:00", "10:30"), new PresentationSchedule("Monday", "09:00", "10:30"))));
        equationsSection = new Section(equations, "1", new ExamTime("2022-06-23T14:00", "2022-06-23T17:00"), new HashSet<>(List.of(new PresentationSchedule("Sunday", "09:00", "10:30"), new PresentationSchedule("Tuesday", "09:00", "10:30"))));
        phy1Section = new Section(phy1, "1", new ExamTime("2022-06-24T08:00", "2022-06-24T11:00"), new HashSet<>(List.of(new PresentationSchedule("Saturday", "10:30", "12:00"), new PresentationSchedule("Monday", "10:30", "12:00"))));
        DASection = new Section(DA, "1", new ExamTime("2022-06-23T14:00", "2022-06-23T17:00"), new HashSet<>(List.of(new PresentationSchedule("Sunday", "16:00", "17:30"), new PresentationSchedule("Tuesday", "16:00", "17:30"))));
        chemSection = new Section(chem, "1", new ExamTime("2022-06-22T08:00", "2022-06-22T11:00"), new HashSet<>(List.of(new PresentationSchedule("Monday", "14:00", "15:30"), new PresentationSchedule("Wednesday", "14:00", "15:30"))));
        APSection = new Section(AP, "1", new ExamTime("2022-06-26T08:00", "2022-06-26T11:00"), new HashSet<>(List.of(new PresentationSchedule("Sunday", "10:30", "12:00"), new PresentationSchedule("Tuesday", "10:30", "12:00"))));
        AISection = new Section(AI, "1", new ExamTime("2022-06-27T08:00", "2022-06-27T11:00"), new HashSet<>(List.of(new PresentationSchedule("Sunday", "14:00", "15:30"), new PresentationSchedule("Tuesday", "14:00", "15:30"))));
        OSSection = new Section(OS, "1", new ExamTime("2022-06-28T08:00", "2022-06-28T11:00"), new HashSet<>(List.of(new PresentationSchedule("Saturday", "16:00", "17:30"), new PresentationSchedule("Monday", "16:00", "17:30"))));
        DBSection = new Section(DB, "1", new ExamTime("2022-06-27T14:00", "2022-06-27T17:00"), new HashSet<>(List.of(new PresentationSchedule("Saturday", "16:00", "17:30"), new PresentationSchedule("Monday", "16:00", "17:30"))));
        PPSection = new Section(PP, "1", new ExamTime("2022-06-28T14:00", "2022-06-28T17:00"), new HashSet<>(List.of(new PresentationSchedule("Saturday", "07:30", "09:00"), new PresentationSchedule("Monday", "07:30", "09:00"))));
    }

}