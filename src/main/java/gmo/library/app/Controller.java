package gmo.library.app;

import gmo.library.app.DTO.*;
import gmo.library.app.Repositories.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Controller {
    @FXML private Button readerSearchButton;
    //данные для поиска
    @FXML private ComboBox<PointOfIssueDTO> pointOfIssueBox;
    @FXML private ComboBox<DepartmentDTO> departmentBox;
    @FXML private ComboBox<FacultyDTO> facultyBox;
    @FXML private ComboBox<StudyGroupDTO> groupBox;
    @FXML private TextField fullNameField;
    @FXML private DatePicker birthdayPicker;
    //таблица
    @FXML private TableView<FullReader> readerTable;
    @FXML private TableColumn<FullReader, String> readerColumnName;
    @FXML private TableColumn<FullReader, String> readerColumnBirthday;
    @FXML private TableColumn<FullReader, String> readerColumnGroup;
    @FXML private TableColumn<FullReader, String> readerColumnFaculty;
    @FXML private TableColumn<FullReader, String> readerColumnDepartment;
    @FXML private TableColumn<FullReader, String> readerColumnDegree;
    @FXML private TableColumn<FullReader, String> readerColumnGrade;

    private Retrofit retrofit;

    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private OneTimeReaderRepository oneTimeReaderRepository;

    private ReadingRoomRepository readingRoomRepository;
    private TicketRepository ticketRepository;

    private DepartmentRepository departmentRepository;

    private FacultyRepository facultyRepository;

    private StudyGroupRepository studyGroupRepository;

    public Controller() {
        retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/").addConverterFactory(GsonConverterFactory.create()).build();

        studentRepository = retrofit.create(StudentRepository.class);
        teacherRepository = retrofit.create(TeacherRepository.class);
        oneTimeReaderRepository = retrofit.create(OneTimeReaderRepository.class);

        readingRoomRepository = retrofit.create(ReadingRoomRepository.class);
        ticketRepository = retrofit.create(TicketRepository.class);

        departmentRepository = retrofit.create(DepartmentRepository.class);

        facultyRepository = retrofit.create(FacultyRepository.class);

        studyGroupRepository = retrofit.create(StudyGroupRepository.class);

        //studentRepository = Feign.builder().target(StudentRepository.class, "http://localhost:8080/");
    }

    public void init() {
        readerColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName() + " " + cellData.getValue().getSecondName()));
        readerColumnBirthday.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthday()));
        readerColumnGroup.setCellValueFactory(cellData -> new SimpleStringProperty("" + (cellData.getValue().getStudyGroup() == 0 ? "" : cellData.getValue().getStudyGroup())));
        readerColumnFaculty.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFaculty()));
        readerColumnDepartment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        readerColumnDegree.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDegree()));
        readerColumnGrade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
        readerSearchButton.setOnAction(event -> {
            //fillReaderTableByJSON();
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
    }

    private void updateSearchInfo() throws IOException {
        Response<SpringJson<List<TicketDTO>>> tickets = ticketRepository.getAllTickets().execute();
        Response<SpringJson<List<ReadingRoomDTO>>> readingRooms = readingRoomRepository.getAllReadingRooms().execute();
        pointOfIssueBox.getItems().clear();
        pointOfIssueBox.getItems().add(null);
        pointOfIssueBox.getSelectionModel().selectFirst();
        pointOfIssueBox.getItems().addAll(tickets.body().getContent());
        pointOfIssueBox.getItems().addAll(readingRooms.body().getContent());

        Response<SpringJson<List<DepartmentDTO>>> departments = departmentRepository.getAllDepartments().execute();
        departmentBox.getItems().clear();
        departmentBox.getItems().add(null);
        departmentBox.getSelectionModel().selectFirst();
        departmentBox.getItems().addAll(departments.body().getContent());

        Response<SpringJson<List<FacultyDTO>>> faculties = facultyRepository.getAllFaculties().execute();
        facultyBox.getItems().clear();
        facultyBox.getItems().add(null);
        facultyBox.getSelectionModel().selectFirst();
        facultyBox.getItems().addAll(faculties.body().getContent());

        Response<SpringJson<List<StudyGroupDTO>>> studyGroups = studyGroupRepository.getAllStudyGroups().execute();
        groupBox.getItems().clear();
        groupBox.getItems().add(null);
        groupBox.getSelectionModel().selectFirst();
        groupBox.getItems().addAll(studyGroups.body().getContent());
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
        Response<SpringJson<List<StudentDTO>>> students = studentRepository.getStudentsByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), studyGroupDTO == null ? 0 : studyGroupDTO.getId(),
                pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId()).execute();
        Response<SpringJson<List<TeacherDTO>>> teachers = teacherRepository.getTeachersByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), departmentDTO == null ? 0 : departmentDTO.getId(),
                pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId()).execute();
        Response<SpringJson<List<OneTimeReaderDTO>>> oneTimeReaders = oneTimeReaderRepository.getOneTimeReadersByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId()).execute();

        readerTable.getItems().clear();
        System.out.println(teachers.toString());
        processReaders(students.body().getContent());
        processReaders(teachers.body().getContent());
        processReaders(oneTimeReaders.body().getContent());
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
            reader.setStudyGroup(((StudentDTO)dto).getGroup().getId());
            reader.setFaculty(((StudentDTO)dto).getGroup().getFaculty().toString());
        }
        else if(dto.getClass().equals(TeacherDTO.class)) {
            reader.setDegree(((TeacherDTO)dto).getDegree().toString());
            reader.setDepartment(((TeacherDTO)dto).getDepartment().toString());
            reader.setGrade(((TeacherDTO)dto).getGrade().toString());
            reader.setFaculty(((TeacherDTO)dto).getDepartment().getFaculty().toString());
        }
    }

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
            try {
                reader.setStudyGroup(getForeignData(studentJson, "group").getInt("id"));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
