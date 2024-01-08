package br.com.cleonildo.vuttr.controllers;

import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.dto.UserRequest;
import br.com.cleonildo.vuttr.dto.UserResponse;
import br.com.cleonildo.vuttr.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RestController
@RequestMapping(value = "/users", produces = "application/json")
@Tag(name = "users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Search data from user by id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") Integer id) {
        var body = this.service.geUsersById(id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Return the full list of user", method = "GET")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Search completed successfully")})
    @GetMapping
    public ResponseEntity<List<UserResponse>> getListUsers() {
        var body = this.service.getListUsers();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Save new User", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New user saved"),
            @ApiResponse(responseCode = "400", description = "Bad request from user")
    })
    @PostMapping
    public ResponseEntity<URI> saveUser(@RequestBody @Valid UserRequest request) {
        var response = this.service.saveUser(request);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update an existing user", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request from user")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponse> updatePatient(@PathVariable Integer id,
                                                      @RequestBody @Valid UserRequest request) {

        var body = this.service.updateUser(id, request);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Delete an existing user", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        this.service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
