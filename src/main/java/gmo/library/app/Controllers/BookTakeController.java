package gmo.library.app.Controllers;

import gmo.library.app.DTO.BookTakeDTO;
import gmo.library.app.DTO.PointOfIssueDTO;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import gmo.library.app.Utilities.Sort;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BookTakeController {
    private Controller controller;

    private int totalPages = 1;

    public BookTakeController(Controller controller) {
        this.controller = controller;

        initTable();
        controller.getBookTakeSearchButton().setOnAction(event -> {
            try {
                fillBookTakeTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            Controller.updatePointOfIssues(controller.getBookTakePointOfIssueBox(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.getBookTakeSortBox().getItems().addAll(Sort.EMPTY, BookTakeDTO.SORT_BY_TAKEDATE, BookTakeDTO.SORT_BY_RETURNDATE);
        controller.getBookTakeSortBox().getSelectionModel().select(0);
        Controller.initPagingAndSorting(controller.getBookTakePageSizeBox(), controller.getBookTakePageNumberField(),
                controller.getBookTakeTotalPagesLabel(), controller.getBookTakeSortOrderBox(), totalPages);

    }

    private void initTable() {
        controller.getBookTakeColumnReaderName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReader().getFullName()));
        controller.getBookTakeColumnBookName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIssue().getBookName()));
        controller.getBookTakeColumnPointOfIssue().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPointOfIssue().toString()));
        controller.getBookTakeColumnTakeDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTakeDate()));
        controller.getBookTakeColumnReturnDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReturnDate() == null ? "-" : cellData.getValue().getReturnDate()));

        controller.getBookTakeTable().setContextMenu(Controller.setContextMenu(() -> {
            BookTakeCreateController.showBookTakeCreateWindow(null, controller.getBookTakeTable().getSelectionModel().getSelectedItem());
        }));
        MenuItem menuItem = new MenuItem("Добавить нарушение");
        menuItem.setOnAction(event -> {
            OffenceCreateController.showOffenceCreateWindow(controller.getBookTakeTable().getSelectionModel().getSelectedItem(), null);
        });
        controller.getBookTakeTable().getContextMenu().getItems().add(menuItem);
    }

    public void fillBookTakeTableByRetrofit() throws IOException {
        List<String> nameList = Controller.getReadersName(controller.getBookTakeReaderNameField());
        LocalDate takeDate = controller.getBookTakeTakeDatePicker().getValue();
        PointOfIssueDTO pointOfIssueDTO = controller.getBookTakePointOfIssueBox().getSelectionModel().getSelectedItem();
        String bookName = controller.getBookTakeBookNameField().getText();

        int pageNumber;
        try {
            pageNumber = Controller.getPageNumber(controller.getBookTakePageNumberField().getText(), totalPages);
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        controller.getBookTakeTable().getItems().clear();
        controller.getBookTakeTotalPagesLabel().setText("");

        totalPages = 1;

        Response<SpringJson<List<BookTakeDTO>>> bookTakes = Main.bookTakeRepository.getBookTakesByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), bookName,
                takeDate == null ? "" : takeDate.toString(), controller.getBookTakePageSizeBox().getValue(), pageNumber - 1,
                controller.getBookTakeSortBox().getValue().getValue() + "," + controller.getBookTakeSortOrderBox().getValue().getValue()).execute();
        controller.getBookTakeTable().getItems().addAll(bookTakes.body().getContent());
        if(bookTakes.body().getPage().getTotalPages() > totalPages) {
            totalPages = bookTakes.body().getPage().getTotalPages();
        }

        controller.getBookTakeTotalPagesLabel().setText("/ " + totalPages);
    }
}
