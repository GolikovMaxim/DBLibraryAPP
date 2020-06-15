package gmo.library.app.Controllers;

import gmo.library.app.DTO.*;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import gmo.library.app.Utilities.FullReader;
import gmo.library.app.Utilities.Sort;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Controller {
    //
    //читатели
    //
    @FXML private Button readerSearchButton;
    @FXML private Button readerCreateButton;
    //данные для поиска
    @FXML private ComboBox<PointOfIssueDTO> readerPointOfIssueBox;
    @FXML private ComboBox<DepartmentDTO> readerDepartmentBox;
    @FXML private ComboBox<FacultyDTO> readerFacultyBox;
    @FXML private ComboBox<StudyGroupDTO> readerGroupBox;
    @FXML private TextField readerFullNameField;
    //таблица
    @FXML private TableView<FullReader> readerTable;
    @FXML private TableColumn<FullReader, String> readerColumnName;
    @FXML private TableColumn<FullReader, String> readerColumnBirthday;
    @FXML private TableColumn<FullReader, String> readerColumnGroup;
    @FXML private TableColumn<FullReader, String> readerColumnFaculty;
    @FXML private TableColumn<FullReader, String> readerColumnDepartment;
    @FXML private TableColumn<FullReader, String> readerColumnDegree;
    @FXML private TableColumn<FullReader, String> readerColumnGrade;
    //страницы
    @FXML private TextField readerPageNumberField;
    @FXML private Label readerTotalPagesLabel;
    @FXML private ChoiceBox<Integer> readerPageSizeBox;
    @FXML private ChoiceBox<Sort> readerSortBox;
    @FXML private ChoiceBox<Sort> readerSortOrderBox;
    //
    //издания
    //
    @FXML private TextField issueBookNameField;
    @FXML private ComboBox<FileCabinetDTO> issueFileCabinetBox;
    @FXML private Button issueSearchButton;
    @FXML private Button issueCreateButton;
    //таблица
    @FXML private TableView<IssueDTO> issueTable;
    @FXML private TableColumn<IssueDTO, String> issueColumnName;
    @FXML private TableColumn<IssueDTO, String> issueColumnFileCabinet;
    @FXML private TableColumn<IssueDTO, String> issueColumnPointOfIssue;
    @FXML private TableColumn<IssueDTO, String> issueColumnReceiptDate;
    @FXML private TableColumn<IssueDTO, String> issueColumnBookCount;
    @FXML private TableColumn<IssueDTO, String> issueColumnBooksInStock;
    //страницы
    @FXML private TextField issuePageNumberField;
    @FXML private Label issueTotalPagesLabel;
    @FXML private ChoiceBox<Sort> issueSortBox;
    @FXML private ChoiceBox<Sort> issueSortOrderBox;
    @FXML private ChoiceBox<Integer> issuePageSizeBox;
    //
    //взятые книги
    //
    @FXML private TextField bookTakeReaderNameField;
    @FXML private TextField bookTakeBookNameField;
    @FXML private DatePicker bookTakeTakeDatePicker;
    @FXML private ComboBox<PointOfIssueDTO> bookTakePointOfIssueBox;
    @FXML private Button bookTakeSearchButton;
    //таблица
    @FXML private TableView<BookTakeDTO> bookTakeTable;
    @FXML private TableColumn<BookTakeDTO, String> bookTakeColumnReaderName;
    @FXML private TableColumn<BookTakeDTO, String> bookTakeColumnBookName;
    @FXML private TableColumn<BookTakeDTO, String> bookTakeColumnPointOfIssue;
    @FXML private TableColumn<BookTakeDTO, String> bookTakeColumnTakeDate;
    @FXML private TableColumn<BookTakeDTO, String> bookTakeColumnReturnDate;
    //страницы
    @FXML private TextField bookTakePageNumberField;
    @FXML private Label bookTakeTotalPagesLabel;
    @FXML private ChoiceBox<Sort> bookTakeSortBox;
    @FXML private ChoiceBox<Sort> bookTakeSortOrderBox;
    @FXML private ChoiceBox<Integer> bookTakePageSizeBox;
    //
    //нарушения
    //
    @FXML private TextField offenceReaderNameField;
    @FXML private TextField offenceBookNameField;
    @FXML private DatePicker offenceAccrualDatePicker;
    @FXML private DatePicker offenceEndDatePicker;
    @FXML private ComboBox<PointOfIssueDTO> offencePointOfIssueBox;
    @FXML private Button offenceSearchButton;
    //таблица
    @FXML private TableView<OffenceDTO> offenceTable;
    @FXML private TableColumn<OffenceDTO, String> offenceColumnReaderName;
    @FXML private TableColumn<OffenceDTO, String> offenceColumnBookName;
    @FXML private TableColumn<OffenceDTO, String> offenceColumnPointOfIssue;
    @FXML private TableColumn<OffenceDTO, String> offenceColumnAccrualDate;
    @FXML private TableColumn<OffenceDTO, String> offenceColumnEndDate;
    //страницы
    @FXML private TextField offencePageNumberField;
    @FXML private Label offenceTotalPagesLabel;
    @FXML private ChoiceBox<Sort> offenceSortBox;
    @FXML private ChoiceBox<Sort> offenceSortOrderBox;
    @FXML private ChoiceBox<Integer> offencePageSizeBox;
    //
    //штрафы
    //
    @FXML private TextField penaltyReaderNameField;
    @FXML private TextField penaltyBookNameField;
    @FXML private DatePicker penaltyAccrualDatePicker;
    @FXML private DatePicker penaltyPayDatePicker;
    @FXML private ComboBox<PointOfIssueDTO> penaltyPointOfIssueBox;
    @FXML private TextField penaltyCostMoreField;
    @FXML private TextField penaltyCostLessField;
    @FXML private Button penaltySearchButton;
    //таблица
    @FXML private TableView<PenaltyDTO> penaltyTable;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnReaderName;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnBookName;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnPointOfIssue;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnCost;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnAccrualDate;
    @FXML private TableColumn<PenaltyDTO, String> penaltyColumnPayDate;
    //страницы
    @FXML private TextField penaltyPageNumberField;
    @FXML private Label penaltyTotalPagesLabel;
    @FXML private ChoiceBox<Sort> penaltySortBox;
    @FXML private ChoiceBox<Sort> penaltySortOrderBox;
    @FXML private ChoiceBox<Integer> penaltyPageSizeBox;

    private int totalPages = 1;

    public Controller() {

    }

    public void init() {
        readerColumnName.setCellValueFactory(cellData -> new SimpleStringProperty("" +
                cellData.getValue().getLastName() + " " + cellData.getValue().getFirstName() + " " + cellData.getValue().getSecondName()));
        readerColumnBirthday.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthday()));
        readerColumnGroup.setCellValueFactory(cellData -> new SimpleStringProperty("" +
                (cellData.getValue().getStudyGroup() == null ? "" : cellData.getValue().getStudyGroup())));
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
            showReaderWindow(null);
        });

        readerTable.setContextMenu(setContextMenu(() -> {
            showReaderWindow(readerTable.getSelectionModel().getSelectedItem());
        }));
        MenuItem menuItem = new MenuItem("Добавить взятие книги");
        menuItem.setOnAction(event -> {
            BookTakeCreateController.showBookTakeCreateWindow(readerTable.getSelectionModel().getSelectedItem().toReader(), null);
        });
        readerTable.getContextMenu().getItems().add(menuItem);

        initPagingAndSorting(readerPageSizeBox, readerPageNumberField, readerTotalPagesLabel, readerSortOrderBox, totalPages);
        readerSortBox.getItems().addAll(Sort.EMPTY, ReaderDTO.SORT_BY_LASTNAME, ReaderDTO.SORT_BY_FIRSTNAME, ReaderDTO.SORT_BY_SECONDNAME);
        readerSortBox.getSelectionModel().select(0);

        IssueController issueController = new IssueController(this);
        BookTakeController bookTakeController = new BookTakeController(this);
        OffenceController offenceController = new OffenceController(this);
        PenaltyController penaltyController = new PenaltyController(this);
    }

    private void showReaderWindow(FullReader reader) {
        try {
            Stage readerCreate = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("readerCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            readerCreate.setTitle("Добавление читателя");
            ((ReaderCreateController)fxmlLoader.getController()).init(readerCreate, reader, this);

            readerCreate.setScene(scene);
            readerCreate.setResizable(false);
            readerCreate.initModality(Modality.APPLICATION_MODAL);
            readerCreate.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initPagingAndSorting(ChoiceBox<Integer> pageSizeBox, TextField pageNumberField, Label totalPagesLabel, ChoiceBox<Sort> sortOrderBox, int totalPages) {
        pageSizeBox.getItems().addAll(30, 90, 300);
        pageSizeBox.getSelectionModel().select(0);
        pageNumberField.setText("" + 1);
        totalPagesLabel.setText("/ " + totalPages);
        sortOrderBox.getItems().addAll(Sort.DESCENDING, Sort.ASCENDING);
        sortOrderBox.getSelectionModel().select(0);
    }

    public static ContextMenu setContextMenu(Runnable runnable) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem updateReader = new MenuItem("Редактировать");
        updateReader.setOnAction(event -> runnable.run());
        contextMenu.getItems().add(updateReader);
        return contextMenu;
    }

    public void updateSearchInfo() throws IOException {
        updatePointOfIssues(readerPointOfIssueBox, true);
        updateDepartments(readerDepartmentBox, true);
        updateFaculties(readerFacultyBox, true);

        Response<SpringJson<List<StudyGroupDTO>>> studyGroups = Main.studyGroupRepository.getAllStudyGroups().execute();
        readerGroupBox.getItems().clear();
        readerGroupBox.getItems().add(null);
        readerGroupBox.getSelectionModel().selectFirst();
        readerGroupBox.getItems().addAll(studyGroups.body().getContent());
    }

    public static void updateDepartments(ComboBox<DepartmentDTO> departmentBox, boolean addNull) throws IOException {
        Response<SpringJson<List<DepartmentDTO>>> departments = Main.departmentRepository.getAllDepartments().execute();
        update(departmentBox, departments.body().getContent(), addNull);
    }

    public static void updateFaculties(ComboBox<FacultyDTO> facultyBox, boolean addNull) throws IOException {
        Response<SpringJson<List<FacultyDTO>>> faculties = Main.facultyRepository.getAllFaculties().execute();
        update(facultyBox, faculties.body().getContent(), addNull);
    }

    public static void updateFileCabinets(ComboBox<FileCabinetDTO> fileCabinetBox, boolean addNull) throws IOException {
        Response<SpringJson<List<FileCabinetDTO>>> fileCabinets = Main.fileCabinetRepository.getAllFileCabinets().execute();
        update(fileCabinetBox, fileCabinets.body().getContent(), addNull);
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

    public static List<String> getReadersName(TextField nameField) {
        String[] nameArray = nameField.getText().split(" ");
        List<String> nameList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            try {
                nameList.add(nameArray[i]);
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                nameList.add("");
            }
        }
        return nameList;
    }

    public static int getPageNumber(String pageNumberText, int totalPages) {
        int pageNumber;
        pageNumber = Integer.parseInt(pageNumberText);
        if(pageNumber < 1 || pageNumber > totalPages) {
            throw new NumberFormatException();
        }
        return pageNumber;
    }

    public void fillReaderTableByRetrofit() throws IOException {
        StudyGroupDTO studyGroupDTO = readerGroupBox.getSelectionModel().getSelectedItem();
        PointOfIssueDTO pointOfIssueDTO = readerPointOfIssueBox.getSelectionModel().getSelectedItem();
        DepartmentDTO departmentDTO = readerDepartmentBox.getSelectionModel().getSelectedItem();
        FacultyDTO facultyDTO = readerFacultyBox.getSelectionModel().getSelectedItem();
        List<String> nameList = getReadersName(readerFullNameField);

        int pageNumber;
        try {
            pageNumber = getPageNumber(readerPageNumberField.getText(), totalPages);
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        int typeCount = (departmentDTO == null ? 1 : 0) + (studyGroupDTO == null ? 1 : 0) +
                ((studyGroupDTO == null && departmentDTO == null && facultyDTO == null) ? 1 : 0);

        readerTable.getItems().clear();
        readerTotalPagesLabel.setText("");

        totalPages = 1;

        if(departmentDTO == null) {
            Response<SpringJson<List<StudentDTO>>> students = Main.studentRepository.getStudentsByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), studyGroupDTO == null ? 0 : studyGroupDTO.getId(),
                    pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId(),
                    readerPageSizeBox.getValue() / typeCount, pageNumber - 1,
                    readerSortBox.getValue().getValue() + "," + readerSortOrderBox.getValue().getValue()).execute();
            processSpringJSON(students);
        }
        if(studyGroupDTO == null) {
            Response<SpringJson<List<TeacherDTO>>> teachers = Main.teacherRepository.getTeachersByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), departmentDTO == null ? 0 : departmentDTO.getId(),
                    pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), facultyDTO == null ? 0 : facultyDTO.getId(),
                    readerPageSizeBox.getValue() / typeCount, pageNumber - 1,
                    readerSortBox.getValue().getValue() + "," + readerSortOrderBox.getValue().getValue()).execute();
            processSpringJSON(teachers);
        }
        if(studyGroupDTO == null && departmentDTO == null && facultyDTO == null) {
            Response<SpringJson<List<OneTimeReaderDTO>>> oneTimeReaders = Main.oneTimeReaderRepository.getOneTimeReadersByParams(
                    nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(),
                    readerPageSizeBox.getValue() / typeCount, pageNumber - 1,
                    readerSortBox.getValue().getValue() + "," + readerSortOrderBox.getValue().getValue()).execute();
            processSpringJSON(oneTimeReaders);
        }

        readerTotalPagesLabel.setText("/ " + totalPages);
    }

    private <T extends ReaderDTO> void processSpringJSON(Response<SpringJson<List<T>>> readers) {
        if(readers.body().getContent().get(0).getId() != null) {
            processReaders(readers.body().getContent());
        }
        if(readers.body().getPage().getTotalPages() > totalPages) {
            totalPages = readers.body().getPage().getTotalPages();
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
        reader.setId(dto.getId());
        if(dto.getClass().equals(StudentDTO.class)) {
            reader.setStudyGroup(((StudentDTO)dto).getGroup());
            reader.setFaculty(((StudentDTO)dto).getGroup().getFaculty().toString());
            reader.setPointOfIssue(((StudentDTO) dto).getPointOfIssue());
        }
        else if(dto.getClass().equals(TeacherDTO.class)) {
            reader.setDegree(((TeacherDTO)dto).getDegree().toString());
            reader.setDepartment(((TeacherDTO)dto).getDepartment().toString());
            reader.setGrade(((TeacherDTO)dto).getGrade().toString());
            reader.setFaculty(((TeacherDTO)dto).getDepartment().getFaculty().toString());
            reader.setPointOfIssue(((TeacherDTO) dto).getPointOfIssue());
        }
        else if(dto.getClass().equals(OneTimeReaderDTO.class)) {
            reader.setPointOfIssue(((OneTimeReaderDTO)dto).getReadingRoom());
            reader.setTakeDate(((OneTimeReaderDTO) dto).getTakeDate());
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

    @Deprecated
    private static JSONObject getForeignData(JSONObject json, String data) throws IOException {
        return readJsonFromUrl(json.getJSONObject("_links").getJSONObject(data).getString("href"));
    }

    @Deprecated
    private static String readAll(java.io.Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Deprecated
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
