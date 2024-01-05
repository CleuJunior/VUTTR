package br.com.cleonildo.vuttr.factory;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.entities.Tool;

import java.util.Set;

public class ToolFactory {

    private static final Integer TOOL_ID = 1;
    private static final String TOOL_TITLE = "Notion";
    private static final String TOOL_LINK =  "https://notion.so";
    private static final String TOOL_DESCRIPTION =  "All in one tool to organize teams and ideas.";
    private static final Set<String> TOOL_SET_TAGS = Set.of("organization", "planning", "collaboration", "writing", "calendar");
    private static final String TOOL_TITLE_UPDATE = "Notion";
    private static final String TOOL_LINK_UPDATE =  "https://notion.so";
    private static final String TOOL_DESCRIPTION_UPDATE =  "All in one tool to organize teams and ideas.";
    private static final Set<String> TOOL_SET_TAGS_UPDATE = Set.of("api", "json", "schema", "node", "github");

    private ToolFactory() {
    }

    public static Tool buildTool() {
        return new Tool(TOOL_ID, TOOL_TITLE, TOOL_LINK, TOOL_DESCRIPTION, TOOL_SET_TAGS);
    }

    public static Tool buildToolUpdate() {
        return new Tool(TOOL_ID, TOOL_TITLE_UPDATE, TOOL_LINK_UPDATE, TOOL_DESCRIPTION_UPDATE, TOOL_SET_TAGS_UPDATE);
    }

    public static ToolResponse buildToolResponse() {
        return new ToolResponse(TOOL_ID, TOOL_TITLE, TOOL_LINK, TOOL_DESCRIPTION, TOOL_SET_TAGS);
    }

    public static ToolResponse buildToolResponseUpdate() {
        return new ToolResponse(TOOL_ID, TOOL_TITLE_UPDATE, TOOL_LINK_UPDATE, TOOL_DESCRIPTION_UPDATE, TOOL_SET_TAGS_UPDATE);
    }

    public static ToolRequest buildToolRequest() {
        return new ToolRequest( TOOL_TITLE, TOOL_LINK, TOOL_DESCRIPTION, TOOL_SET_TAGS);
    }

    public static ToolRequest buildToolRequestForUpdate() {
        return new ToolRequest(TOOL_TITLE_UPDATE, TOOL_LINK_UPDATE, TOOL_DESCRIPTION_UPDATE, TOOL_SET_TAGS_UPDATE);
    }
}
