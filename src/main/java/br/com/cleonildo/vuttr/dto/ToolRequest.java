package br.com.cleonildo.vuttr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ToolRequest(
        @NotBlank(message = "Title can't be blank")
        @NotEmpty(message = "Title can't be empty")
        String title,
        @NotBlank(message = "Link can't be blank")
        @NotEmpty(message = "Link can't be empty")
        String link,
        @NotBlank(message = "Description can't be blank")
        @NotEmpty(message = "Description can't be empty")
        String description,
        @NotNull(message = "Tag can't be null")
        Set<String> tags

) { }

