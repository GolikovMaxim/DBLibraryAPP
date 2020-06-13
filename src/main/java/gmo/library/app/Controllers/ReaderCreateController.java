package gmo.library.app.Controllers;

import com.google.gson.JsonElement;
import gmo.library.app.DTO.*;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import okio.Buffer;
import okio.BufferedSink;
import org.json.JSONObject;
import retrofit2.Response;

import java.io.EOFException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReaderCreateController {
    @FXML private TabPane typePane;
    @FXML private Tab studentTab;
    @FXML private Tab teacherTab;
    @FXML private Tab oneTimeReaderTab;
    //общее
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField secondNameField;
    @FXML private DatePicker birthdayPicker;
    //студент
    @FXML private TextField studyGroupField;
    @FXML private ComboBox<FacultyDTO> facultyBox;
    @FXML private ComboBox<PointOfIssueDTO> studentPointOfIssueBox;
    //преподаватель
    @FXML private ComboBox<DepartmentDTO> departmentBox;
    @FXML private ComboBox<DegreeDTO> degreeBox;
    @FXML private ComboBox<GradeDTO> gradeBox;
    @FXML private ComboBox<PointOfIssueDTO> teacherPointOfIssueBox;
    //одноразовый читатель
    @FXML private ComboBox<ReadingRoomDTO> readingRoomBox;
    @FXML private DatePicker takeDatePicker;
    //заголовок
    @FXML private Label titleLabel;

    @FXML private Button createButton;

    public ReaderCreateController() {

    }

    public void init(Stage stage, FullReader reader, Controller controller) throws IOException {
        updateBoxesInfo();
        if(reader != null) {
            stage.setTitle("Изменение читателя");
        }
        updateReaderInfo(reader);
        createButton.setOnAction(event -> {
            if(lastNameField.getText().equals("") || firstNameField.getText().equals("") ||
                    secondNameField.getText().equals("") || birthdayPicker.getValue() == null) {
                Main.error("Заполните все общие поля.");
                return;
            }
            if(typePane.getSelectionModel().getSelectedItem() == studentTab) {
                try {
                    Integer.parseInt(studyGroupField.getText());
                }
                catch (NumberFormatException nfe) {
                    Main.error("Введите правильный номер группы.");
                    return;
                }
                StudentDTO studentDTO = new StudentDTO();
                setCommonFields(studentDTO);
                studentDTO.setPointOfIssue(studentPointOfIssueBox.getValue());

                StudyGroupDTO studyGroupDTO = null;
                try {
                    studyGroupDTO = Main.studyGroupRepository.getGroupByNumber(
                            Integer.parseInt(studyGroupField.getText())).execute().body();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(studyGroupDTO == null) {
                    Response<StudyGroupDTO> studyGroup;
                    try {
                        studyGroupDTO = new StudyGroupDTO();
                        studyGroupDTO.setNumber(Integer.parseInt(studyGroupField.getText()));
                        studyGroupDTO.setFaculty(facultyBox.getValue());
                        studyGroup = Main.studyGroupRepository.addStudyGroup(new StudyGroupDTO.StudyGroupHATEOAS(studyGroupDTO)).execute();
                        studentDTO.setGroup(studyGroup.body());
                        controller.updateSearchInfo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    studentDTO.setGroup(studyGroupDTO);
                }

                try {
                    Response<StudentDTO> response;
                    if(reader == null) {
                        response = Main.studentRepository.createStudent(new StudentDTO.StudentHATEOAS(studentDTO)).execute();
                    }
                    else {
                        response = Main.studentRepository.updateStudent(reader.getId().toString(),
                                new StudentDTO.StudentHATEOAS(studentDTO)).execute();
                    }
                    if(!response.isSuccessful()) {
                        Main.error("Произошла ошибка.\n" + response.errorBody().string());
                        return;
                    }
                    stage.close();
                }
                catch (EOFException e) {
                    stage.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(typePane.getSelectionModel().getSelectedItem() == teacherTab) {
                TeacherDTO teacherDTO = new TeacherDTO();
                setCommonFields(teacherDTO);
                teacherDTO.setDegree(degreeBox.getSelectionModel().getSelectedItem());
                teacherDTO.setDepartment(departmentBox.getSelectionModel().getSelectedItem());
                teacherDTO.setGrade(gradeBox.getSelectionModel().getSelectedItem());
                teacherDTO.setPointOfIssue(teacherPointOfIssueBox.getSelectionModel().getSelectedItem());
                try {
                    Response<TeacherDTO> response;
                    if(reader == null) {
                        response = Main.teacherRepository.createTeacher(new TeacherDTO.TeacherHATEOAS(teacherDTO)).execute();
                    }
                    else {
                        response = Main.teacherRepository.updateTeacher(reader.getId().toString(),
                                new TeacherDTO.TeacherHATEOAS(teacherDTO)).execute();
                    }
                    if(!response.isSuccessful()) {
                        Main.error("Произошла ошибка." + response.errorBody());
                        return;
                    }
                    stage.close();
                }
                catch (EOFException e) {
                    stage.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(typePane.getSelectionModel().getSelectedItem() == oneTimeReaderTab) {
                if(takeDatePicker.getValue() == null) {
                    Main.error("Выберите дату получения читательского билета.");
                    return;
                }
                OneTimeReaderDTO oneTimeReaderDTO = new OneTimeReaderDTO();
                setCommonFields(oneTimeReaderDTO);
                oneTimeReaderDTO.setReadingRoom(readingRoomBox.getSelectionModel().getSelectedItem());
                oneTimeReaderDTO.setTakeDate(takeDatePicker.getValue().toString());
                try {
                    Response<OneTimeReaderDTO> response;
                    if(reader == null) {
                        response = Main.oneTimeReaderRepository.createOneTimeReader(
                                new OneTimeReaderDTO.OneTimeReaderHATEOAS(oneTimeReaderDTO)).execute();
                    }
                    else {
                        response = Main.oneTimeReaderRepository.updateOneTimeReader(reader.getId().toString(),
                                new OneTimeReaderDTO.OneTimeReaderHATEOAS(oneTimeReaderDTO)).execute();
                    }
                    if(!response.isSuccessful()) {
                        Main.error("Произошла ошибка." + response.errorBody());
                        return;
                    }
                    stage.close();
                }
                catch (EOFException e) {
                    stage.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                controller.fillReaderTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateReaderInfo(FullReader reader) {
        if(reader == null) {
            return;
        }
        titleLabel.setText("Изменение читателя");
        createButton.setText("Изменить");
        lastNameField.setText(reader.getLastName());
        firstNameField.setText(reader.getFirstName());
        secondNameField.setText(reader.getSecondName());
        birthdayPicker.setValue(LocalDate.parse(reader.getBirthday()));
        if(reader.getStudyGroup() != null) {
            teacherTab.setDisable(true);
            oneTimeReaderTab.setDisable(true);
            typePane.getSelectionModel().select(studentTab);
            studyGroupField.setText(reader.getStudyGroup().toString());
            for(FacultyDTO faculty : facultyBox.getItems()) {
                if(faculty.getName().equals(reader.getFaculty())) {
                    facultyBox.getSelectionModel().select(faculty);
                }
            }
            studentPointOfIssueBox.getSelectionModel().select(reader.getPointOfIssue());
        }
        if(reader.getDegree() != null) {
            studentTab.setDisable(true);
            oneTimeReaderTab.setDisable(true);
            typePane.getSelectionModel().select(teacherTab);
            for(DepartmentDTO department : departmentBox.getItems()) {
                if(department.getName().equals(reader.getDepartment())) {
                    departmentBox.getSelectionModel().select(department);
                }
            }
            for(DegreeDTO degree : degreeBox.getItems()) {
                if(degree.getName().equals(reader.getDegree())) {
                    degreeBox.getSelectionModel().select(degree);
                }
            }
            for(GradeDTO grade : gradeBox.getItems()) {
                if(grade.getName().equals(reader.getDegree())) {
                    gradeBox.getSelectionModel().select(grade);
                }
            }
            teacherPointOfIssueBox.getSelectionModel().select(reader.getPointOfIssue());
        }
        if(reader.getTakeDate() != null) {
            studentTab.setDisable(true);
            teacherTab.setDisable(true);
            typePane.getSelectionModel().select(oneTimeReaderTab);
            readingRoomBox.getSelectionModel().select((ReadingRoomDTO) reader.getPointOfIssue());
            takeDatePicker.setValue(LocalDate.parse(reader.getTakeDate()));
        }
    }

    private void setCommonFields(ReaderDTO readerDTO) {
        readerDTO.setLastName(lastNameField.getText());
        readerDTO.setFirstName(firstNameField.getText());
        readerDTO.setSecondName(secondNameField.getText());
        readerDTO.setBirthday(birthdayPicker.getValue().toString());
    }

    private void updateBoxesInfo() throws IOException {
        Controller.updatePointOfIssues(studentPointOfIssueBox, false);
        Controller.updatePointOfIssues(teacherPointOfIssueBox, false);
        Controller.updateDepartments(departmentBox, false);
        Controller.updateFaculties(facultyBox, false);

        Response<SpringJson<List<DegreeDTO>>> degrees = Main.degreeRepository.getAllDegrees().execute();
        Controller.update(degreeBox, degrees.body().getContent(), false);

        Response<SpringJson<List<GradeDTO>>> grades = Main.gradeRepository.getAllGrades().execute();
        Controller.update(gradeBox, grades.body().getContent(), false);

        Response<SpringJson<List<ReadingRoomDTO>>> readingRooms = Main.readingRoomRepository.getAllReadingRooms().execute();
        Controller.update(readingRoomBox, readingRooms.body().getContent(), false);
    }
}
