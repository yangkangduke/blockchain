package com.seeds.uc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> {
    private int currentPage;
    private int pageSize;
    private int totalPage;
    private List<T> data;
}
