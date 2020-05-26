package gmo.library.app.Repositories;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ArrayResponse<T> {
    List<T> students;
}
