package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public abstract class ReaderDTO extends AbstractDTO<Long> {
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthday;
}
