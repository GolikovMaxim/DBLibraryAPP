package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDTO<ID extends Serializable> implements Serializable {
    private ID id;
}
