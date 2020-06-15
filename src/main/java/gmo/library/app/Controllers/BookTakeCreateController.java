package gmo.library.app.Controllers;

import gmo.library.app.DTO.*;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import gmo.library.app.Utilities.Sort;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BookTakeCreateController {
    @FXML private Label bookTakeLabel;
    @FXML private TextField bookNameField;
    @FXML private ComboBox<FileCabinetDTO> fileCabinetBox;
    @FXML private Button issueSearchButton;
    //таблица
    @FXML private TableView<IssueDTO> issueTable;
    @FXML private TableColumn<IssueDTO, String> bookNameColumn;
    @FXML private TableColumn<IssueDTO, String> fileCabinetColumn;
    @FXML private TableColumn<IssueDTO, String> pointOfIssueColumn;
    @FXML private TableColumn<IssueDTO, String> bookCountColumn;
    @FXML private TableColumn<IssueDTO, String> booksInStockColumn;
    //страницы
    @FXML private TextField pageNumberField;
    @FXML private Label totalPagesLabel;
    @FXML private ChoiceBox<Sort> sortBox;
    @FXML private ChoiceBox<Sort> sortOrderBox;
    @FXML private ChoiceBox<Integer> pageSizeBox;
    //данные
    @FXML private TextField readerNameField;
    @FXML private DatePicker takeDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private ComboBox<PointOfIssueDTO> pointOfIssueBox;
    @FXML private Button submitButton;

    private int totalPages = 1;

    public static void showBookTakeCreateWindow(ReaderDTO readerDTO, BookTakeDTO bookTakeDTO) {
        try {
            Stage bookTakeCreate = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(BookTakeCreateController.class.getClassLoader().getResource("bookTakeCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            bookTakeCreate.setTitle("Добавление данных о взятии книги");
            ((BookTakeCreateController)fxmlLoader.getController()).init(bookTakeCreate, readerDTO, bookTakeDTO);

            bookTakeCreate.setScene(scene);
            bookTakeCreate.setResizable(false);
            bookTakeCreate.initModality(Modality.APPLICATION_MODAL);
            bookTakeCreate.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Stage stage, ReaderDTO readerDTO, BookTakeDTO bookTakeDTO) throws IOException {
        initTable();
        if(bookTakeDTO != null) {
            stage.setTitle("Изменение данных о взятии книги");
        }
        issueSearchButton.setOnAction(event -> {
            try {
                fillTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sortBox.getItems().addAll(Sort.EMPTY, IssueDTO.SORT_BY_BOOKNAME, IssueDTO.SORT_BY_BOOKCOUNT,
                IssueDTO.SORT_BY_RECEIPTDATE, IssueDTO.SORT_BY_BOOKSINSTOCK);
        sortBox.getSelectionModel().select(0);
        Controller.initPagingAndSorting(pageSizeBox, pageNumberField, totalPagesLabel, sortOrderBox, totalPages);

        updateInfo(readerDTO, bookTakeDTO);

        submitButton.setOnAction(event -> {
            if(issueTable.getSelectionModel().getSelectedItem() == null || takeDatePicker.getValue() == null ||
                    pointOfIssueBox.getSelectionModel().getSelectedItem() == null) {
                Main.error("Заполните все поля.");
                return;
            }

            BookTakeDTO bookTake = new BookTakeDTO();
            bookTake.setIssue(issueTable.getSelectionModel().getSelectedItem());
            bookTake.setPointOfIssue(pointOfIssueBox.getSelectionModel().getSelectedItem());
            bookTake.setTakeDate(takeDatePicker.getValue().toString());
            bookTake.setReturnDate(returnDatePicker.getValue() == null ? "" : returnDatePicker.getValue().toString());
            Response<BookTakeDTO> bookTakeDTOResponse;
            try {
                if(bookTakeDTO == null) {
                    bookTake.setReader(readerDTO);
                    bookTakeDTOResponse = Main.bookTakeRepository.createBookTake(new BookTakeDTO.BookTakeHATEOAS(bookTake)).execute();
                }
                else {
                    bookTake.setReader(bookTakeDTO.getReader());
                    bookTakeDTOResponse = Main.bookTakeRepository.updateBookTake(bookTakeDTO.getId().toString(),
                            new BookTakeDTO.BookTakeHATEOAS(bookTake)).execute();
                }
                if(!bookTakeDTOResponse.isSuccessful()) {
                    Main.error("Произошла ошибка.\n" + bookTakeDTOResponse.errorBody().string());
                    return;
                }
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initTable() {
        bookNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookName()));
        fileCabinetColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileCabinet().toString()));
        pointOfIssueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileCabinet().getPointOfIssue().toString()));
        bookCountColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getBookCount()));
        booksInStockColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getBooksInStock()));
    }

    public void updateInfo(ReaderDTO readerDTO, BookTakeDTO bookTakeDTO) throws IOException {
        if(readerDTO != null) {
            readerNameField.setText(readerDTO.getFullName());
        }
        Controller.updatePointOfIssues(pointOfIssueBox, true);
        Controller.updateFileCabinets(fileCabinetBox, true);
        if(bookTakeDTO != null) {
            bookTakeLabel.setText("Изменить данные о взятии книги");
            submitButton.setText("Изменить");
            readerNameField.setText(bookTakeDTO.getReader().getFullName());
            takeDatePicker.setValue(LocalDate.parse(bookTakeDTO.getTakeDate()));
            if(bookTakeDTO.getReturnDate() != null) {
                returnDatePicker.setValue(LocalDate.parse(bookTakeDTO.getReturnDate()));
            }
            pointOfIssueBox.getSelectionModel().select(bookTakeDTO.getPointOfIssue());
            bookNameField.setText(bookTakeDTO.getIssue().getBookName());
            fileCabinetBox.getSelectionModel().select(bookTakeDTO.getIssue().getFileCabinet());
            fillTableByRetrofit();
            issueTable.getSelectionModel().select(0);
        }
    }

    public void fillTableByRetrofit() throws IOException {
        String bookName = bookNameField.getText();
        FileCabinetDTO fileCabinetDTO = fileCabinetBox.getSelectionModel().getSelectedItem();

        int pageNumber;
        try {
            pageNumber = Integer.parseInt(pageNumberField.getText());
            if(pageNumber < 1 || pageNumber > totalPages) {
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        issueTable.getItems().clear();
        totalPagesLabel.setText("");

        totalPages = 1;

        Response<SpringJson<List<IssueDTO>>> issues = Main.issueRepository.getIssueByBookName(bookName,
                fileCabinetDTO == null ? 0 : fileCabinetDTO.getId(), pageSizeBox.getValue(), pageNumber - 1,
                sortBox.getValue().getValue() + "," + sortOrderBox.getValue().getValue()).execute();
        issueTable.getItems().addAll(issues.body().getContent());
        if(issues.body().getPage().getTotalPages() > totalPages) {
            totalPages = issues.body().getPage().getTotalPages();
        }

       totalPagesLabel.setText("/ " + totalPages);
    }
}
