package gmo.library.app.Controllers;

import gmo.library.app.DTO.FileCabinetDTO;
import gmo.library.app.DTO.IssueDTO;
import gmo.library.app.Utilities.Sort;
import gmo.library.app.Main;
import gmo.library.app.Repositories.SpringJson;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class IssueController {
    private Controller controller;

    private int totalPages = 1;

    public IssueController(Controller controller) {
        this.controller = controller;

        initTable();
        controller.getIssueSearchButton().setOnAction(event -> {
            try {
                fillIssueTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            Controller.updateFileCabinets(controller.getIssueFileCabinetBox(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        controller.getIssueSortBox().getItems().addAll(Sort.EMPTY, IssueDTO.SORT_BY_BOOKNAME, IssueDTO.SORT_BY_BOOKCOUNT,
                IssueDTO.SORT_BY_RECEIPTDATE, IssueDTO.SORT_BY_BOOKSINSTOCK);
        controller.getIssueSortBox().getSelectionModel().select(0);
        Controller.initPagingAndSorting(controller.getIssuePageSizeBox(), controller.getIssuePageNumberField(),
                controller.getIssueTotalPagesLabel(), controller.getIssueSortOrderBox(), totalPages);

        controller.getIssueCreateButton().setOnAction(event -> {
            showIssueWindow(null);
        });
    }

    public void showIssueWindow(IssueDTO issue) {
        try {
            Stage issueCreate = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("issueCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            issueCreate.setTitle("Добавление издания");
            ((IssueCreateController)fxmlLoader.getController()).init(issueCreate, this, issue);

            issueCreate.setScene(scene);
            issueCreate.setResizable(false);
            issueCreate.initModality(Modality.APPLICATION_MODAL);
            issueCreate.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTable() {
        controller.getIssueColumnName().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookName()));
        controller.getIssueColumnFileCabinet().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileCabinet().toString()));
        controller.getIssueColumnPointOfIssue().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileCabinet().getPointOfIssue().toString()));
        controller.getIssueColumnReceiptDate().setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReceiptDate()));
        controller.getIssueColumnBookCount().setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getBookCount()));
        controller.getIssueColumnBooksInStock().setCellValueFactory(cellData ->
                new SimpleStringProperty("" + cellData.getValue().getBooksInStock()));

        controller.getIssueTable().setContextMenu(Controller.setContextMenu(() -> {
            showIssueWindow(controller.getIssueTable().getSelectionModel().getSelectedItem());
        }));
    }

    public void fillIssueTableByRetrofit() throws IOException {
        String bookName = controller.getIssueBookNameField().getText();
        FileCabinetDTO fileCabinetDTO = controller.getIssueFileCabinetBox().getSelectionModel().getSelectedItem();

        int pageNumber;
        try {
            pageNumber = Controller.getPageNumber(controller.getIssuePageNumberField().getText(), totalPages);
        }
        catch (NumberFormatException nfe) {
            Main.error("Введите верный номер страницы.\n" +
                    "Номер должен быть от единицы до количества страниц.");
            return;
        }

        controller.getIssueTable().getItems().clear();
        controller.getIssueTotalPagesLabel().setText("");

        totalPages = 1;

        Response<SpringJson<List<IssueDTO>>> issues = Main.issueRepository.getIssueByBookName(bookName,
                fileCabinetDTO == null ? 0 : fileCabinetDTO.getId(), controller.getIssuePageSizeBox().getValue(), pageNumber - 1,
                controller.getIssueSortBox().getValue().getValue() + "," + controller.getIssueSortOrderBox().getValue().getValue()).execute();
        if(issues.body().getContent().get(0).getId() != null) {
            controller.getIssueTable().getItems().addAll(issues.body().getContent());
        }
        if(issues.body().getPage().getTotalPages() > totalPages) {
            totalPages = issues.body().getPage().getTotalPages();
        }

        controller.getIssueTotalPagesLabel().setText("/ " + totalPages);
    }
}
