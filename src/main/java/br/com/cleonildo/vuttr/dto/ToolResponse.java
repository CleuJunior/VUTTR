package br.com.cleonildo.vuttr.dto;

import br.com.cleonildo.vuttr.entities.Tool;

import java.util.Set;

public record ToolResponse(
        Integer id,
        String title,
        String link,
        String description,
        Set<String> tags

) {
    public ToolResponse(Tool tool) {
        this(tool.getId(), tool.getTitle(), tool.getLink(), tool.getDescription(), tool.getTags());
    }
}

