package br.com.cleonildo.vuttr.controllers;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.services.ToolService;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping(value = "/tools")
@Controller
public class ToolController {

    private final ToolService service;

    public ToolController(ToolService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ToolResponse> getToolById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.service.getToolById(id));
    }

    @GetMapping
    public ResponseEntity<List<ToolResponse>> getListTools() {
        return ResponseEntity.status(HttpStatus.OK).body(this.service.getAllTools());
    }

    @GetMapping(params = "tag")
    public ResponseEntity<List<ToolResponse>> getListToolsByTag(@RequestParam String tag) {
        return ResponseEntity.status(HttpStatus.OK).body(this.service.getAllToolsByTag(tag));
    }

    @PostMapping
    public ResponseEntity<ToolResponse> saveTool(@RequestBody @Valid ToolRequest request) {
        var response = this.service.saveTool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ToolResponse> updatePatient(@PathVariable Integer id,
                                                      @RequestBody @Valid ToolRequest request) {

        var response = this.service.updateTool(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        this.service.deleteToolById(id);
        return ResponseEntity.noContent().build();
    }

}
