package gmo.library.app.Controllers;

import gmo.library.app.DTO.*;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import retrofit2.Response;

import java.io.EOFException;
import java.io.IOException;
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

    @FXML private Button createButton;

    public ReaderCreateController() {

    }

    public void init(Stage stage) throws IOException {
        updateBoxesInfo();
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
                StudyGroupDTO studyGroupDTO = new StudyGroupDTO();
                studyGroupDTO.setNumber(Integer.parseInt(studyGroupField.getText()));
                studyGroupDTO.setFaculty(facultyBox.getSelectionModel().getSelectedItem());
                try {
                    Response<StudyGroupDTO> response = Main.studyGroupRepository.addStudyGroup(studyGroupDTO).execute();
                    if(response.body() == null) {
                        Main.error("Произошла ошибка.\n" + response.toString());
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                studentDTO.setGroup(studyGroupDTO);
                try {
                    Response<StudentDTO> response = Main.studentRepository.addStudent(studentDTO).execute();
                    if(response.body() == null) {
                        Main.error("Произошла ошибка.\n" + response.toString());
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
                    Response<TeacherDTO> response = Main.teacherRepository.addTeacher(teacherDTO).execute();
                    if(response.body().getFirstName() == null) {
                        Main.error("Произошла ошибка." + response.toString());
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
                    Response<OneTimeReaderDTO> response = Main.oneTimeReaderRepository.addOneTimeReader(oneTimeReaderDTO).execute();
                    if(response.body().getFirstName() == null) {
                        Main.error("Произошла ошибка." + response.toString());
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
        });
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