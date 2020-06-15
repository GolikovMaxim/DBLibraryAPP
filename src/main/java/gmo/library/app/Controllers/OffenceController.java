package gmo.library.app.Controllers;

import gmo.library.app.DTO.BookTakeDTO;
import gmo.library.app.DTO.OffenceDTO;
import gmo.library.app.DTO.PointOfIssueDTO;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import gmo.library.app.Utilities.Sort;
import javafx.beans.property.SimpleStringProperty;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class OffenceController {
    private Controller controller;

    private int totalPages = 1;

    public OffenceController(Controller controller) {
        this.controller = controller;

        initTable();
        controller.getOffenceSearchButton().setOnAction(event -> {
            try {
                fillOffenceTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            Controller.updatePointOfIssues(controller.getOffencePointOfIssueBox(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.getOffenceSortBox().getItems().addAll(Sort.EMPTY, OffenceDTO.SORT_BY_ACCRUALDATE, OffenceDTO.SORT_BY_ENDDATE);
        controller.getOffenceSortBox().getSelectionModel().select(0);
        Controller.initPagingAndSorting(controller.getOffencePageSizeBox(), controller.getOffencePageNumberField(),
                controller.getOffenceTotalPagesLabel(), controller.getOffenceSortOrderBox(), totalPages);

    }

    private void initTable() {
        controller.getOffenceColumnReaderName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookTake().getReader().getFullName()));
        controller.getOffenceColumnBookName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookTake().getIssue().getBookName()));
        controller.getOffenceColumnPointOfIssue().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookTake().getPointOfIssue().toString()));
        controller.getOffenceColumnAccrualDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAccrualDate()));
        controller.getOffenceColumnEndDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndDate()));

        controller.getOffenceTable().setContextMenu(Controller.setContextMenu(() -> {
            OffenceCreateController.showOffenceCreateWindow(null, controller.getOffenceTable().getSelectionModel().getSelectedItem());
        }));
    }

    public void fillOffenceTableByRetrofit() throws IOException {
        List<String> nameList = Controller.getReadersName(controller.getOffenceReaderNameField());
        LocalDate accrualDate = controller.getOffenceAccrualDatePicker().getValue();
        LocalDate endDate = controller.getOffenceEndDatePicker().getValue();
        PointOfIssueDTO pointOfIssueDTO = controller.getOffencePointOfIssueBox().getSelectionModel().getSelectedItem();
        String bookName = controller.getOffenceBookNameField().getText();

        int pageNumber;
        try {
            pageNumber = Controller.getPageNumber(controller.getOffencePageNumberField().getText(), totalPages);
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        controller.getOffenceTable().getItems().clear();
        controller.getOffenceTotalPagesLabel().setText("");

        totalPages = 1;

        Response<SpringJson<List<OffenceDTO>>> offences = Main.offenceRepository.getOffencesByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), bookName,
                accrualDate == null ? "" : accrualDate.toString(), endDate == null ? "" : endDate.toString(),
                controller.getOffencePageSizeBox().getValue(), pageNumber - 1, controller.getOffenceSortBox().getValue().getValue() +
                        "," + controller.getOffenceSortOrderBox().getValue().getValue()).execute();
        controller.getOffenceTable().getItems().addAll(offences.body().getContent());
        if(offences.body().getPage().getTotalPages() > totalPages) {
            totalPages = offences.body().getPage().getTotalPages();
        }

        controller.getOffenceTotalPagesLabel().setText("/ " + totalPages);
    }
}
