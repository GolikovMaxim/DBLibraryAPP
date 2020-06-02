package gmo.library.app.Controllers;

import gmo.library.app.DTO.*;
import gmo.library.app.Main;
import gmo.library.app.Repositories.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Controller {
    @FXML private Button readerSearchButton;
    @FXML private Button readerCreateButton;
    //данные для поиска
    @FXML private ComboBox<PointOfIssueDTO> pointOfIssueBox;
    @FXML private ComboBox<DepartmentDTO> departmentBox;
    @FXML private ComboBox<FacultyDTO> facultyBox;
    @FXML private ComboBox<StudyGroupDTO> groupBox;
    @FXML private TextField fullNameField;
    //таблица
    @FXML private TableView<FullReader> readerTable;
    @FXML private TableColumn<FullReader, String> readerColumnName;
    @FXML private TableColumn<FullReader, String> readerColumnBirthday;
    @FXML private TableColumn<FullReader, String> readerColumnGroup;
    @FXML private TableColumn<FullReader, String> readerColumnFaculty;
    @FXML private TableColumn<FullReader, String> readerColumnDepartment;
    @FXML private TableColumn<FullReader, String> readerColumnDegree;
    @FXML private TableColumn<FullReader, String> readerColumnGrade;

    public Controller() {

    }

    public void init() {
        readerColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName() + " " + cellData.getValue().getSecondName()));
        readerColumnBirthday.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthday()));
        readerColumnGroup.setCellValueFactory(cellData -> new SimpleStringProperty("" + (cellData.getValue().getStudyGroup() == null ? "" : cellData.getValue().getStudyGroup())));
        readerColumnFaculty.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFaculty()));
        readerColumnDepartment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        readerColumnDegree.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDegree()));
        readerColumnGrade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
        readerSearchButton.setOnAction(event -> {
            try {
                fillReaderTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            updateSearchInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readerCreateButton.setOnAction(event -> {
            try {
                Stage readerCreate = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(new File("readerCreate.fxml").toURI().toURL());
                Scene scene = new Scene(fxmlLoader.load());
                ((ReaderCreateController)fxmlLoader.getController()).init(readerCreate);

                readerCreate.setTitle("Добавление читателя");
                readerCreate.setScene(scene);
                readerCreate.setResizable(false);
                readerCreate.initModality(Modality.APPLICATION_MODAL);
                readerCreate.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateSearchInfo() throws IOException {
        updatePointOfIssues(pointOfIssueBox, true);
        updateDepartments(departmentBox, true);
        updateFaculties(facultyBox, true);

        Response<SpringJson<List<StudyGroupDTO>>> studyGroups = Main.studyGroupRepository.getAllStudyGroups().execute();
        groupBox.getItems().clear();
        groupBox.getItems().add(null);
        groupBox.getSelectionModel().selectFirst();
        groupBox.getItems().addAll(studyGroups.body().getContent());
    }

    public static void updateDepartments(ComboBox<DepartmentDTO> departmentBox, boolean addNull) throws IOException {
        Response<SpringJson<List<DepartmentDTO>>> departments = Main.departmentRepository.getAllDepartments().execute();
        update(departmentBox, departments.body().getContent(), addNull);
    }

    public static void updateFaculties(ComboBox<FacultyDTO> facultyBox, boolean addNull) throws IOException {
        Response<SpringJson<List<FacultyDTO>>> faculties = Main.facultyRepository.getAllFaculties().execute();
        update(facultyBox, faculties.body().getContent(), addNull);
    }

    public static void updatePointOfIssues(ComboBox<PointOfIssueDTO> pointOfIssueBox, boolean addNull) throws IOException {
        Response<SpringJson<List<TicketDTO>>> tickets = Main.ticketRepository.getAllTickets().execute();
        Response<SpringJson<List<ReadingRoomDTO>>> readingRooms = Main.readingRoomRepository.getAllReadingRooms().execute();
        pointOfIssueBox.getItems().clear();
        if(addNull) {
            pointOfIssueBox.getItems().add(null);
        }
        pointOfIssueBox.getItems().addAll(tickets.body().getContent());
        pointOfIssueBox.getItems().addAll(readingRooms.body().getContent());
        pointOfIssueBox.getSelectionModel().selectFirst();
    }

    public static <T> void update(ComboBox<T> box, List<T> list, boolean addNull) {
        box.getItems().clear();
        if(addNull) {
            box.getItems().add(null);
        }
        box.getItems().addAll(list);
        box.getSelectionModel().selectFirst();
    }

    private void fillReaderTableByRetrofit() throws IOException {
        StudyGroupDTO studyGroupDTO = groupBox.getSelectionModel().getSelectedItem();
        PointOfIssueDTO pointOfIssueDTO = pointOfIssueBox.getSelectionModel().getSelectedItem();
        DepartmentDTO departmentDTO = departmentBox.getSelectionModel().getSelectedItem();
        FacultyDTO facultyDTO = facultyBox.getSelectionModel().getSelectedItem();
        String[] nameArray = fullNameField.getText().split(" ");
        List<String> nameList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            try {
                nameList.add(nameArray[i]);
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                nameList.add("");
            }
        }

        readerTable.getItems().clear();

        if(departmentDTO == null) {
            Response<SpringJson<List<StudentDTO>>> students = Main.studentRepository.getStudentsByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), studyGroupDTO == null ? 0 : studyGroupDTO.getId(),
                    pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId()).execute();
            processReaders(students.body().getContent());
        }
        if(studyGroupDTO == null) {
            Response<SpringJson<List<TeacherDTO>>> teachers = Main.teacherRepository.getTeachersByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), departmentDTO == null ? 0 : departmentDTO.getId(),
                    pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId()).execute();
            processReaders(teachers.body().getContent());
        }
        if(studyGroupDTO == null && departmentDTO == null && facultyDTO == null) {
            Response<SpringJson<List<OneTimeReaderDTO>>> oneTimeReaders = Main.oneTimeReaderRepository.getOneTimeReadersByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId()).execute();
            processReaders(oneTimeReaders.body().getContent());
        }
    }

    private <T extends ReaderDTO> void processReaders(List<T> readers) {
        for(ReaderDTO readerDTO : readers) {
            if(readerDTO.getFirstName() == null) {
                break;
            }
            FullReader reader = new FullReader();
            setReader(reader, readerDTO);
            readerTable.getItems().add(reader);
        }
    }

    private void setReader(FullReader reader, ReaderDTO dto) {
        reader.setFirstName(dto.getFirstName());
        reader.setSecondName(dto.getSecondName());
        reader.setLastName(dto.getLastName());
        reader.setBirthday(dto.getBirthday());
        if(dto.getClass().equals(StudentDTO.class)) {
            reader.setStudyGroup(((StudentDTO)dto).getGroup());
            reader.setFaculty(((StudentDTO)dto).getGroup().getFaculty().toString());
        }
        else if(dto.getClass().equals(TeacherDTO.class)) {
            reader.setDegree(((TeacherDTO)dto).getDegree().toString());
            reader.setDepartment(((TeacherDTO)dto).getDepartment().toString());
            reader.setGrade(((TeacherDTO)dto).getGrade().toString());
            reader.setFaculty(((TeacherDTO)dto).getDepartment().getFaculty().toString());
        }
    }

    @Deprecated
    private void fillReaderTableByJSON() {
        JSONObject studentsJson = null;
        JSONObject teachersJson = null;
        JSONObject oneTimeReadersJson = null;
        try {
            studentsJson = readJsonFromUrl("http://localhost:8080/students");
            oneTimeReadersJson = readJsonFromUrl("http://localhost:8080/oneTimeReaders");
            teachersJson = readJsonFromUrl("http://localhost:8080/teachers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray studentJSONs = studentsJson.getJSONObject("_embedded").getJSONArray("students");
        JSONArray oneTimeReaderJSONs = oneTimeReadersJson.getJSONObject("_embedded").getJSONArray("oneTimeReaders");
        JSONArray teacherJSONs = teachersJson.getJSONObject("_embedded").getJSONArray("teachers");
        readerTable.getItems().clear();
        for(int i = 0; i < studentJSONs.length(); i++) {
            FullReader reader = new FullReader();
            JSONObject studentJson = studentJSONs.getJSONObject(i);
            setReader(reader, studentJson);
            /*try {
                reader.setStudyGroup(getForeignData(studentJson, "group").getInt("id"));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            readerTable.getItems().add(reader);
        }
        for(int i = 0; i < teacherJSONs.length(); i++) {
            FullReader reader = new FullReader();
            JSONObject teacherJson = teacherJSONs.getJSONObject(i);
            setReader(reader, teacherJson);
            try {
                reader.setDegree(getForeignData(teacherJson, "degree").getString("name"));
                reader.setDepartment(getForeignData(teacherJson, "department").getString("name"));
                reader.setFaculty(getForeignData(getForeignData(teacherJson,"department"), "faculty").getString("name"));
                reader.setGrade(getForeignData(teacherJson, "grade").getString("name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            readerTable.getItems().add(reader);
        }
        for(int i = 0; i < oneTimeReaderJSONs.length(); i++) {
            FullReader reader = new FullReader();
            JSONObject oneTimeReaderJson = oneTimeReaderJSONs.getJSONObject(i);
            setReader(reader, oneTimeReaderJson);
            readerTable.getItems().add(reader);
        }
    }

    private void setReader(FullReader reader, JSONObject json) {
        reader.setFirstName(json.getString("firstName"));
        reader.setSecondName(json.getString("secondName"));
        reader.setLastName(json.getString("lastName"));
        reader.setBirthday(json.getString("birthday"));
    }

    private static JSONObject getForeignData(JSONObject json, String data) throws IOException {
        return readJsonFromUrl(json.getJSONObject("_links").getJSONObject(data).getString("href"));
    }

    private static String readAll(java.io.Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
