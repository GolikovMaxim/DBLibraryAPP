package gmo.library.app;

import gmo.library.app.Controllers.Controller;
import gmo.library.app.Repositories.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EnableFeignClients
public class Main extends Application {
    public static final String SERVER_URL = "http://localhost:8080/";

    public static StudentRepository studentRepository;
    public static TeacherRepository teacherRepository;
    public static OneTimeReaderRepository oneTimeReaderRepository;

    public static ReadingRoomRepository readingRoomRepository;
    public static TicketRepository ticketRepository;

    public static DegreeRepository degreeRepository;
    public static DepartmentRepository departmentRepository;
    public static FacultyRepository facultyRepository;
    public static GradeRepository gradeRepository;

    public static StudyGroupRepository studyGroupRepository;

    public static IssueRepository issueRepository;
    public static FileCabinetRepository fileCabinetRepository;

    public static BookTakeRepository bookTakeRepository;
    public static OffenceRepository offenceRepository;
    public static PenaltyRepository penaltyRepository;

    private Retrofit retrofit;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void error(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorText);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
    }

    @SneakyThrows
    @Override
    public void start(Stage primaryStage) {
        retrofit = new Retrofit.Builder().baseUrl(SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();

        studentRepository = retrofit.create(StudentRepository.class);
        teacherRepository = retrofit.create(TeacherRepository.class);
        oneTimeReaderRepository = retrofit.create(OneTimeReaderRepository.class);

        readingRoomRepository = retrofit.create(ReadingRoomRepository.class);
        ticketRepository = retrofit.create(TicketRepository.class);

        degreeRepository = retrofit.create(DegreeRepository.class);
        departmentRepository = retrofit.create(DepartmentRepository.class);
        facultyRepository = retrofit.create(FacultyRepository.class);
        gradeRepository = retrofit.create(GradeRepository.class);

        studyGroupRepository = retrofit.create(StudyGroupRepository.class);

        issueRepository = retrofit.create(IssueRepository.class);
        fileCabinetRepository = retrofit.create(FileCabinetRepository.class);

        bookTakeRepository = retrofit.create(BookTakeRepository.class);
        offenceRepository = retrofit.create(OffenceRepository.class);
        penaltyRepository = retrofit.create(PenaltyRepository.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((Controller)fxmlLoader.getController()).init();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library App");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
