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

    @Getter @Setter
    @NoArgsConstructor
    public static abstract class AbstractHATEOAS<ID extends Serializable> implements Serializable {
        private ID id;

        public AbstractHATEOAS(AbstractDTO<ID> abstractDTO) {
            id = abstractDTO.id;
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
