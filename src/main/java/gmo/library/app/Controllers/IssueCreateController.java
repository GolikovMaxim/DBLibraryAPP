package gmo.library.app.Controllers;

import gmo.library.app.DTO.FileCabinetDTO;
import gmo.library.app.DTO.IssueDTO;
import gmo.library.app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;

public class IssueCreateController {
    @FXML private Label issueLabel;
    @FXML private TextField bookNameField;
    @FXML private TextField bookCountField;
    @FXML private DatePicker receiptDatePicker;
    @FXML private ComboBox<FileCabinetDTO> fileCabinetBox;
    @FXML private Button submitButton;

    public void init(Stage stage, IssueController issueController, IssueDTO issue) throws IOException {
        updateInfo(issue);
        if(issue != null) {
            stage.setTitle("Редактирование издания");
        }
        submitButton.setOnAction(event -> {
            if(bookNameField.getText().equals("") || bookCountField.getText().equals("") ||
                    receiptDatePicker.getValue() == null || fileCabinetBox.getValue() == null) {
                Main.error("Заполните все поля.");
                return;
            }
            int bookCount;
            try {
                bookCount = Integer.parseInt(bookCountField.getText());
            }
            catch (NumberFormatException nfe) {
                Main.error("Укажите действительное количество книг.");
                return;
            }

            IssueDTO issueDTO = new IssueDTO();
            issueDTO.setBookName(bookNameField.getText());
            issueDTO.setBookCount(bookCount);
            issueDTO.setReceiptDate(receiptDatePicker.getValue().toString());
            issueDTO.setFileCabinet(fileCabinetBox.getSelectionModel().getSelectedItem());
            Response<IssueDTO> issueDTOResponse;
            try {
                if(issue == null) {
                    issueDTOResponse = Main.issueRepository.createIssue(new IssueDTO.IssueHATEOAS(issueDTO)).execute();
                }
                else {
                    issueDTOResponse = Main.issueRepository.updateIssue(issue.getId(), new IssueDTO.IssueHATEOAS(issueDTO)).execute();
                }
                if(!issueDTOResponse.isSuccessful()) {
                    Main.error("Произошла ошибка.\n" + issueDTOResponse.errorBody().string());
                    return;
                }
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                issueController.fillIssueTableByRetrofit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateInfo(IssueDTO issueDTO) throws IOException {
        Controller.updateFileCabinets(fileCabinetBox, true);
        if(issueDTO != null) {
            issueLabel.setText("Редактирование издания");
            submitButton.setText("Изменить");
            bookNameField.setText(issueDTO.getBookName());
            bookCountField.setText("" + issueDTO.getBookCount());
            receiptDatePicker.setValue(LocalDate.parse(issueDTO.getReceiptDate()));
            fileCabinetBox.getSelectionModel().select(issueDTO.getFileCabinet());
        }
    }
}
