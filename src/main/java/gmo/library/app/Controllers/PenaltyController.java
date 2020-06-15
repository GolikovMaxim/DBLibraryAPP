package gmo.library.app.Controllers;

import gmo.library.app.DTO.PenaltyDTO;
import gmo.library.app.DTO.PointOfIssueDTO;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import gmo.library.app.Utilities.Sort;
import javafx.beans.property.SimpleStringProperty;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PenaltyController {
    private Controller controller;

    private int totalPages = 1;

    public PenaltyController(Controller controller) {
        this.controller = controller;

        initTable();
        controller.getPenaltySearchButton().setOnAction(event -> {
            try {
                fillPenaltyTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            Controller.updatePointOfIssues(controller.getPenaltyPointOfIssueBox(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.getPenaltySortBox().getItems().addAll(Sort.EMPTY, PenaltyDTO.SORT_BY_COST, PenaltyDTO.SORT_BY_ACCRUALDATE, PenaltyDTO.SORT_BY_PAYDATE);
        controller.getPenaltySortBox().getSelectionModel().select(0);
        Controller.initPagingAndSorting(controller.getPenaltyPageSizeBox(), controller.getPenaltyPageNumberField(),
                controller.getPenaltyTotalPagesLabel(), controller.getPenaltySortOrderBox(), totalPages);
    }

    private void initTable() {
        controller.getPenaltyColumnReaderName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOffence().getBookTake().getReader().getFullName()));
        controller.getPenaltyColumnBookName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOffence().getBookTake().getIssue().getBookName()));
        controller.getPenaltyColumnPointOfIssue().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOffence().getBookTake().getPointOfIssue().toString()));
        controller.getPenaltyColumnCost().setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getCost()));
        controller.getPenaltyColumnAccrualDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAccrualDate()));
        controller.getPenaltyColumnPayDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPayDate() == null ? "-" : cellData.getValue().getPayDate()));

        controller.getPenaltyTable().setContextMenu(Controller.setContextMenu(() -> {
            PenaltyCreateController.showPenaltyCreateWindow(null, controller.getPenaltyTable().getSelectionModel().getSelectedItem());
        }));
    }

    public void fillPenaltyTableByRetrofit() throws IOException {
        List<String> nameList = Controller.getReadersName(controller.getPenaltyReaderNameField());
        LocalDate accrualDate = controller.getPenaltyAccrualDatePicker().getValue();
        LocalDate payDate = controller.getPenaltyPayDatePicker().getValue();
        PointOfIssueDTO pointOfIssueDTO = controller.getPenaltyPointOfIssueBox().getSelectionModel().getSelectedItem();
        String bookName = controller.getPenaltyBookNameField().getText();

        int costMore = 0, costLess = 0;
        try {
            if(!controller.getPenaltyCostMoreField().getText().equals("")) {
                costMore = Integer.parseInt(controller.getPenaltyCostMoreField().getText());
            }
            if(!controller.getPenaltyCostLessField().getText().equals("")) {
                costLess = Integer.parseInt(controller.getPenaltyCostLessField().getText());
            }
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верные границы размера штрафа.");
            return;
        }

        int pageNumber;
        try {
            pageNumber = Controller.getPageNumber(controller.getPenaltyPageNumberField().getText(), totalPages);
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        controller.getPenaltyTable().getItems().clear();
        controller.getPenaltyTotalPagesLabel().setText("");

        totalPages = 1;

        Response<SpringJson<List<PenaltyDTO>>> penalties = Main.penaltyRepository.getPenaltiesByParams(
                nameList.get(0), nameList.get(1), nameList.get(2), pointOfIssueDTO == null ? 0 : pointOfIssueDTO.getId(), bookName,
                accrualDate == null ? "" : accrualDate.toString(), payDate == null ? "" : payDate.toString(), costMore, costLess,
                controller.getPenaltyPageSizeBox().getValue(), pageNumber - 1, controller.getPenaltySortBox().getValue().getValue() +
                        "," + controller.getPenaltySortOrderBox().getValue().getValue()).execute();
        if(penalties.body().getContent().get(0).getId() != null) {
            controller.getPenaltyTable().getItems().addAll(penalties.body().getContent());
        }
        if(penalties.body().getPage().getTotalPages() > totalPages) {
            totalPages = penalties.body().getPage().getTotalPages();
        }

        controller.getOffenceTotalPagesLabel().setText("/ " + totalPages);
    }
}
