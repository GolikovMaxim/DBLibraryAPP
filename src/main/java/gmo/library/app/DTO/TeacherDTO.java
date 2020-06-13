package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.StudentRepository;
import gmo.library.app.Repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO extends ReaderDTO {
    public static final Sort SORT_BY_DEPARTMENT = new Sort("По кафедре", "department");
    public static final Sort SORT_BY_DEGREE = new Sort("По степени", "degree");
    public static final Sort SORT_BY_GRADE = new Sort("По званию", "grade");

    private DepartmentDTO department;
    private DegreeDTO degree;
    private GradeDTO grade;
    private PointOfIssueDTO pointOfIssue;

    @Getter @Setter
    @NoArgsConstructor
    public static class TeacherHATEOAS extends ReaderHATEOAS {
        private String department;
        private String degree;
        private String grade;
        private String pointOfIssue;

        public TeacherHATEOAS(TeacherDTO teacherDTO) {
            super(teacherDTO);
            department = teacherDTO.department.getURL();
            degree = teacherDTO.degree.getURL();
            grade = teacherDTO.grade.getURL();
            pointOfIssue = teacherDTO.pointOfIssue.getURL();
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(TeacherDTO teacherDTO) {
        return Main.SERVER_URL + TeacherRepository.URL + "/" + teacherDTO.getId();
    }
}
