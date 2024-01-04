package br.com.cleonildo.vuttr.handler.excpetion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ExcpetionResponse(
        @JsonProperty("status_code")
        Integer status,
        @JsonProperty("status_message")
        String statusErrorMessagem,
        @JsonProperty("excpetion_message")
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:SS")
        LocalDateTime timestamp
) { }
