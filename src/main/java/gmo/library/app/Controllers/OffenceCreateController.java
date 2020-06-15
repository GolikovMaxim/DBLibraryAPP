package gmo.library.app.Controllers;

import gmo.library.app.DTO.BookTakeDTO;
import gmo.library.app.DTO.OffenceDTO;
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

public class OffenceCreateController {
    @FXML private Label titleLabel;
    @FXML private TextField readerNameField;
    @FXML private TextField bookNameField;
    @FXML private DatePicker accrualDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button submitButton;

    public static void showOffenceCreateWindow(BookTakeDTO bookTakeDTO, OffenceDTO offenceDTO) {
        try {
            Stage bookTakeCreate = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(BookTakeCreateController.class.getClassLoader().getResource("offenceCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            bookTakeCreate.setTitle("Добавление нарушения");
            ((OffenceCreateController)fxmlLoader.getController()).init(bookTakeCreate, bookTakeDTO, offenceDTO);

            bookTakeCreate.setScene(scene);
            bookTakeCreate.setResizable(false);
            bookTakeCreate.initModality(Modality.APPLICATION_MODAL);
            bookTakeCreate.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Stage stage, BookTakeDTO bookTakeDTO, OffenceDTO offenceDTO) throws IOException {
        updateInfo(bookTakeDTO, offenceDTO);
        if(offenceDTO != null) {
            stage.setTitle("Редактирование нарушения");
        }
        submitButton.setOnAction(event -> {
            if(accrualDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                Main.error("Заполните все поля.");
                return;
            }
            if(accrualDatePicker.getValue().isAfter(endDatePicker.getValue())) {
                Main.error("Дата окончания должна быть после даты нарушения.");
                return;
            }

            OffenceDTO offence = new OffenceDTO();
            offence.setAccrualDate(accrualDatePicker.getValue().toString());
            offence.setEndDate(endDatePicker.getValue().toString());
            Response<OffenceDTO> offenceDTOResponse;
            try {
                if(offenceDTO == null) {
                    offence.setBookTake(bookTakeDTO);
                    offenceDTOResponse = Main.offenceRepository.createOffence(new OffenceDTO.OffenceHATEOAS(offence)).execute();
                }
                else {
                    offence.setBookTake(offenceDTO.getBookTake());
                    offenceDTOResponse = Main.offenceRepository.updateOffence(offenceDTO.getId().toString(), new OffenceDTO.OffenceHATEOAS(offence)).execute();
                }
                if(!offenceDTOResponse.isSuccessful()) {
                    Main.error("Произошла ошибка.\n" + offenceDTOResponse.errorBody().string());
                    return;
                }
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateInfo(BookTakeDTO bookTakeDTO, OffenceDTO offenceDTO) {
        if(offenceDTO != null) {
            titleLabel.setText("Редактирование нарушения");
            submitButton.setText("Изменить");
            readerNameField.setText(offenceDTO.getBookTake().getReader().getFullName());
            bookNameField.setText(offenceDTO.getBookTake().getIssue().getBookName());
            accrualDatePicker.setValue(LocalDate.parse(offenceDTO.getAccrualDate()));
            endDatePicker.setValue(LocalDate.parse(offenceDTO.getEndDate()));
        }
        if(bookTakeDTO != null) {
            readerNameField.setText(bookTakeDTO.getReader().getFullName());
            bookNameField.setText(bookTakeDTO.getIssue().getBookName());
            accrualDatePicker.setValue(LocalDate.now());
            endDatePicker.setValue(LocalDate.now().plusMonths(2));
        }
    }
}
