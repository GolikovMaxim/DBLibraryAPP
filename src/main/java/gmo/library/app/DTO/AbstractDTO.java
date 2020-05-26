package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public abstract class AbstractDTO<ID extends Serializable> implements Serializable {
    private ID id;
}
