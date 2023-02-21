package com.seeds.admin.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@Slf4j
public class CsvUtils {

    public static <T> List<T> getCsvData(MultipartFile file, Class<T> clazz) {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(file.getInputStream(), "UTF-8");
            // UTF-8为解析时用的编码格式，csv文件也需要定义为UTF-8编码格式
        } catch (IOException e) {
            log.error("读取csv文件失败！ ---> " + e.getMessage());
        }
        HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(clazz);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(in)
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .withMappingStrategy(mappingStrategy)
                .build();
        return csvToBean.parse();

    }
}
