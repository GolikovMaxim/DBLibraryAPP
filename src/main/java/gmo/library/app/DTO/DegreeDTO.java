package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DegreeDTO extends AbstractDTO<Long> {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}