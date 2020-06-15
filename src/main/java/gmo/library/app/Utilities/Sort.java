package gmo.library.app.Utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Sort {
    public static final Sort EMPTY = new Sort("Сортировка", "");
    public static final Sort DESCENDING = new Sort("По убыванию", "desc");
    public static final Sort ASCENDING = new Sort("По возрастанию", "asc");

    private String text;
    private String value;

    @Override
    public String toString() {
        return text;
    }
}
