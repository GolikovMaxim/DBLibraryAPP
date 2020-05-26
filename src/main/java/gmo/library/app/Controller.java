package gmo.library.app;

import com.google.gson.JsonElement;
import feign.Client;
import feign.Feign;
import feign.codec.Encoder;
import gmo.library.app.DTO.*;
import gmo.library.app.Repositories.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.time.LocalDate;
import java.util.List;

public class Controller {
    @FXML private Button readerSearchButton;
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

    public Controller() {
        retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/").addConverterFactory(GsonConverterFactory.create()).build();
        studentRepository = retrofit.create(StudentRepository.class);
        teacherRepository = retrofit.create(TeacherRepository.class);
        oneTimeReaderRepository = retrofit.create(OneTimeReaderRepository.class);

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
    }

    private void fillReaderTableByRetrofit() throws IOException {
        Response<SpringJson<List<StudentDTO>>> students = studentRepository.getAllStudents().execute();
        Response<SpringJson<List<TeacherDTO>>> teachers = teacherRepository.getAllTeachers().execute();
        Response<SpringJson<List<OneTimeReaderDTO>>> oneTimeReaders = oneTimeReaderRepository.getAllOneTimeReaders().execute();

        readerTable.getItems().clear();
        for(StudentDTO student : students.body().getContent()) {
            FullReader reader = new FullReader();
            setReader(reader, student);
            readerTable.getItems().add(reader);
        }
        for(TeacherDTO teacher : teachers.body().getContent()) {
            FullReader reader = new FullReader();
            setReader(reader, teacher);
            readerTable.getItems().add(reader);
        }
        for(OneTimeReaderDTO oneTimeReader : oneTimeReaders.body().getContent()) {
            FullReader reader = new FullReader();
            setReader(reader, oneTimeReader);
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
