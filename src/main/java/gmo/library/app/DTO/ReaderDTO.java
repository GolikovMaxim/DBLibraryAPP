package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ReaderDTO extends AbstractDTO<Long> {
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthday;
}
