package com.matt.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PagingResponse<T> {

    /**
     * current page index
     */
    private Integer current;

    /**
     * record number per page
     */
    private Integer size;

    /**
     * total matching records
     */
    private Long total;

    /**
     * total pages
     */
    private Long pages;

    /**
     * records
     */
    private List<T> records;
}
