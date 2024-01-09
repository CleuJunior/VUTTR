package br.com.cleonildo.vuttr.controllers;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.services.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Controller
@RestController
@RequestMapping(value = "/tools", produces = "application/json")
@Tag(name = "tools")
@SecuritySchemes
public class ToolController {

    private final ToolService service;

    public ToolController(ToolService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Search data from a tool by id",
            method = "GET",
            security = {@SecurityRequirement(name = "bearer-jwt")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ToolResponse> getToolById(@PathVariable(value = "id") Integer id) {
        var body = this.service.getToolById(id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Search the full list of tools or search by tag", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Search completed successfully")})
    @GetMapping
    public ResponseEntity<List<ToolResponse>> getListTools() {
        var body = this.service.getAllTools();
        return ResponseEntity.ok(body);
    }

    @Parameters(@Parameter(name = "tag", description = "Name of the tag to be searched"))
    @GetMapping(params = "tag")
    public ResponseEntity<List<ToolResponse>> getListToolsByTag(@RequestParam(value = "tag", required = false) String tag) {
        var body = this.service.getAllToolsByTag(tag);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Save new tool", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New tool saved"),
            @ApiResponse(responseCode = "400", description = "Bad request from user")
    })
    @PostMapping
    public ResponseEntity<ToolResponse> saveTool(@RequestBody @Valid ToolRequest request) {
        var body = this.service.saveTool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(summary = "Update an existing tool", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tool updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request from user")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ToolResponse> updatePatient(@PathVariable Integer id,
                                                      @RequestBody @Valid ToolRequest request) {

        var body = this.service.updateTool(id, request);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Delete an existing tool", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tool deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        this.service.deleteToolById(id);
        return ResponseEntity.noContent().build();
    }

}
