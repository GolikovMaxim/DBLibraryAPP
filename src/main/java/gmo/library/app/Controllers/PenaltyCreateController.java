package gmo.library.app.Controllers;

import gmo.library.app.DTO.BookTakeDTO;
import gmo.library.app.DTO.OffenceDTO;
import gmo.library.app.DTO.PenaltyDTO;
import gmo.library.app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;

public class PenaltyCreateController {
    @FXML private Label titleLabel;
    @FXML private TextField readerNameField;
    @FXML private TextField bookNameField;
    @FXML private DatePicker accrualDatePicker;
    @FXML private DatePicker payDatePicker;
    @FXML private TextField costField;
    @FXML private Button submitButton;

    public static void showPenaltyCreateWindow(OffenceDTO offenceDTO, PenaltyDTO penaltyDTO) {
        try {
            Stage penaltyCreate = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(PenaltyCreateController.class.getClassLoader().getResource("penaltyCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            penaltyCreate.setTitle("Добавление штрафа");
            ((PenaltyCreateController)fxmlLoader.getController()).init(penaltyCreate, offenceDTO, penaltyDTO);

            penaltyCreate.setScene(scene);
            penaltyCreate.setResizable(false);
            penaltyCreate.initModality(Modality.APPLICATION_MODAL);
            penaltyCreate.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Stage stage, OffenceDTO offenceDTO, PenaltyDTO penaltyDTO) throws IOException {
        updateInfo(offenceDTO, penaltyDTO);
        if(penaltyDTO != null) {
            stage.setTitle("Редактирование штрафа");
        }
        submitButton.setOnAction(event -> {
            if(accrualDatePicker.getValue() == null || costField.getText().equals("")) {
                Main.error("Заполните все поля.");
                return;
            }
            if(payDatePicker.getValue() != null && accrualDatePicker.getValue().isAfter(payDatePicker.getValue())) {
                Main.error("Дата оплаты должна быть после даты назначения.");
                return;
            }
            int cost;
            try {
                cost = Integer.parseInt(costField.getText());
            }
            catch (NumberFormatException nfe) {
                Main.error("Введите верный размер штрафа.");
                return;
            }

            PenaltyDTO penalty = new PenaltyDTO();
            penalty.setAccrualDate(accrualDatePicker.getValue().toString());
            penalty.setCost(cost);
            penalty.setPayDate(payDatePicker.getValue() == null ? null : payDatePicker.getValue().toString());
            Response<PenaltyDTO> penaltyDTOResponse;
            try {
                if(penaltyDTO == null) {
                    penalty.setOffence(offenceDTO);
                    penaltyDTOResponse = Main.penaltyRepository.createPenalties(new PenaltyDTO.PenaltyHATEOAS(penalty)).execute();
                }
                else {
                    penalty.setOffence(penaltyDTO.getOffence());
                    penaltyDTOResponse = Main.penaltyRepository.updatePenalties(penaltyDTO.getId().toString(), new PenaltyDTO.PenaltyHATEOAS(penalty)).execute();
                }
                if(!penaltyDTOResponse.isSuccessful()) {
                    Main.error("Произошла ошибка.\n" + penaltyDTOResponse.errorBody().string());
                    return;
                }
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateInfo(OffenceDTO offenceDTO, PenaltyDTO penaltyDTO) {
        if(penaltyDTO != null) {
            titleLabel.setText("Редактирование штрафа");
            submitButton.setText("Изменить");
            readerNameField.setText(penaltyDTO.getOffence().getBookTake().getReader().getFullName());
            bookNameField.setText(penaltyDTO.getOffence().getBookTake().getIssue().getBookName());
            accrualDatePicker.setValue(LocalDate.parse(penaltyDTO.getAccrualDate()));
            if(penaltyDTO.getPayDate() != null) {
                payDatePicker.setValue(LocalDate.parse(penaltyDTO.getPayDate()));
            }
            costField.setText("" + penaltyDTO.getCost());
        }
        if(offenceDTO != null) {
            readerNameField.setText(offenceDTO.getBookTake().getReader().getFullName());
            bookNameField.setText(offenceDTO.getBookTake().getIssue().getBookName());
            accrualDatePicker.setValue(LocalDate.now());
        }
    }
}
