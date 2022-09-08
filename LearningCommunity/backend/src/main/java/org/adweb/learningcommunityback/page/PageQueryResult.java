package org.adweb.learningcommunityback.page;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageQueryResult<T> {

    private int totalPage;
    private int totalElement;
    private List<T> data;

    private PageQueryResult(int totalPage, int totalElement, List<T> data) {
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.data = data;
    }

    public static <E> PageQueryResult<E> of(int totalPage, int totalElement, List<E> data) {
        return new PageQueryResult<>(totalPage, totalElement, data);
    }


}
