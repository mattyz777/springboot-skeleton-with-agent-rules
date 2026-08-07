package com.matt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingRequest<T> {
    private Integer currentPage;
    private Integer pageSize;
    private T requestBody;
}
