package com.seeds.account.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 *
 * @author milo
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMap<T> implements Serializable {
    private static final long serialVersionUID = -1L;

    List<T> list;
    Map<String, T> map;

    public static <T> ListMap<T> init(List<T> list, Map<String, T> map) {
        return ListMap.<T>builder().list(list).map(map).build();
    }
}
