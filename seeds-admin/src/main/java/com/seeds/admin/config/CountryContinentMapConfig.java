package com.seeds.admin.config;

import cn.hutool.json.JSONUtil;
import com.seeds.admin.dto.CountryContinent;
import com.seeds.admin.dto.CountryContinentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 国家和所在大洲映射关系
 *
 * @author: hewei
 * @date 2022/12/21
 */
@Configuration
@Slf4j
public class CountryContinentMapConfig {

    @Bean
    public CountryContinentService initCountryContinentMap() throws IOException {
        CountryContinentService countryContinentMap = new CountryContinentService();
        Map<String, String> map;
        File file = ResourceUtils.getFile("classpath:country.json");
        String json = FileUtils.readFileToString(file, "UTF-8");
        List<CountryContinent> countryContinents = JSONUtil.parseArray(json).toList(CountryContinent.class);
        map = countryContinents.stream().collect(Collectors.toMap(CountryContinent::getCountryCname, CountryContinent::getContinentName));
        countryContinentMap.setCountryContinentMap(map);
        return countryContinentMap;
    }

}
