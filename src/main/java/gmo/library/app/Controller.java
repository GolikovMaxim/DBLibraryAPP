package gmo.library.app;

import gmo.library.app.DTO.Reader;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Date;

public class Controller {
    @FXML private Button readerSearchButton;
    @FXML private TableView<Reader> readerTable;
    @FXML private TableColumn<Reader, String> readerColumnName;
    @FXML private TableColumn<Reader, LocalDate> readerColumnBirthday;
    @FXML private TableColumn<Reader, String> readerColumnGroup;
    @FXML private TableColumn<Reader, String> readerColumnFaculty;
    @FXML private TableColumn<Reader, String> readerColumnDepartment;
    @FXML private TableColumn<Reader, String> readerColumnDegree;
    @FXML private TableColumn<Reader, String> readerColumnGrade;

    public Controller() {

    }

    public void init() {
        readerColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName() + " " + cellData.getValue().getSecondName()));
        readerColumnBirthday.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBirthday()));
        readerColumnGroup.setCellValueFactory(cellData -> new SimpleStringProperty("" + (cellData.getValue().getStudyGroup() == 0 ? "" : cellData.getValue().getStudyGroup())));
        readerColumnFaculty.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFaculty()));
        readerColumnDepartment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));
        readerColumnDegree.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDegree()));
        readerColumnGrade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
        readerSearchButton.setOnAction(event -> {
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
                Reader reader = new Reader();
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
                Reader reader = new Reader();
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
                Reader reader = new Reader();
                JSONObject oneTimeReaderJson = oneTimeReaderJSONs.getJSONObject(i);
                setReader(reader, oneTimeReaderJson);
                readerTable.getItems().add(reader);
            }
        });
    }

    private void setReader(Reader reader, JSONObject json) {
        reader.setFirstName(json.getString("firstName"));
        reader.setSecondName(json.getString("secondName"));
        reader.setLastName(json.getString("lastName"));
        reader.setBirthday(LocalDate.parse(json.getString("birthday")));
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
