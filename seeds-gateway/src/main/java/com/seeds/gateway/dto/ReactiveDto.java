package com.seeds.gateway.dto;

import com.seeds.common.dto.GenericDto;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class ReactiveDto {

    public static <T> Mono<GenericDto<T>> success(Mono<T> data) {
        return data.map(d -> GenericDto.success(d)).defaultIfEmpty(GenericDto.success(null));
    }

    public static <T> Mono<GenericDto<T>> failure(String message, int code) {
        return Mono.just(GenericDto.failure(message, code));
    }

}
