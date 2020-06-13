package gmo.library.app.Repositories;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpringJson<T> {
    T content;
    Page page;

    @Getter @Setter
    public static class Page {
        private int size;
        private int totalPages;
        private int number;
    }
}
