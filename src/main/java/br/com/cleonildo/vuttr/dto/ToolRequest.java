package br.com.cleonildo.vuttr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ToolRequest(
        @NotBlank(message = "Title can't be blank")
        @NotNull(message = "Title can't be null")
        @Size(min = 3, message = "Title must have at least 3 characters.")
        String title,
        @NotBlank(message = "Link can't be blank")
        @NotNull(message = "Link can't be null")
        @Size(min = 8, message = "Link must have at least 8 characters.")
        String link,
        @NotBlank(message = "Description can't be blank")
        @NotNull(message = "Description can't be null")
        @Size(min = 10, message = "Description must have at least 10 characters.")
        String description,
        @NotNull(message = "Tag list can't be null")
        Set<@NotBlank(message = "Tag can't be blank") @NotNull(message = "Tag can't null.")
        @Size(min = 3, message = "Tag must have at least 3 characters.") String> tags

) { }

