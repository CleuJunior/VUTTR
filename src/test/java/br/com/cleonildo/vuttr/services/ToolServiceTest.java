package br.com.cleonildo.vuttr.services;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.entities.Tool;
import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import br.com.cleonildo.vuttr.repositories.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static br.com.cleonildo.vuttr.factory.ToolFactory.buildTool;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolRequest;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolRequestForUpdate;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolResponse;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolResponseUpdate;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolUpdate;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
class ToolServiceTest {

    @Mock
    private ToolRepository repository;
    @InjectMocks
    private ToolService service;
    private Tool tool;
    private ToolResponse toolResponse;

    @BeforeEach
    void setup() {
        this.tool = buildTool();
        this.toolResponse = buildToolResponse();
    }

    @Test
    @DisplayName("Return the list of all tools when calling getAllTools")
    void shouldReturnListTools_WhenCallingGetAllTools() {
        // Given
        given(repository.findAll()).willReturn(singletonList(tool));

        // Given
        var actual = this.service.getAllTools();

        // Then
        assertThat(actual.isEmpty(), is(false));
        assertThat(actual.size(), is(1));
        assertThat(actual.contains(toolResponse), is(true));

        verify(this.repository).findAll();
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @DisplayName("Return an empty too list when there's no tool")
    void shouldReturnEmptyToolList_WhenCallingGetAllTools_WithEmptyList() {
        // Given
        given(repository.findAll()).willReturn(emptyList());

        // Given
        var actual = this.service.getAllTools();

        // Then
        assertThat(actual.isEmpty(), is(true));
        assertThat(actual.contains(toolResponse), is(false));

        verify(this.repository).findAll();
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @DisplayName("Return the list of all tools with given tag when calling getAllToolsByTag")
    void shouldReturnListTools_WhenCallingGetAllToolsByTag_WithExistingTag() {
        // Given
        var tag = "organization";
        given(repository.findByTagsContaining(tag)).willReturn(singletonList(tool));

        // Given
        var actual = this.service.getAllToolsByTag(tag);

        // Then
        assertThat(actual.isEmpty(), is(false));
        assertThat(actual.size(), is(1));
        assertThat(actual.contains(toolResponse), is(true));

        verify(this.repository).findByTagsContaining(tag);
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @DisplayName("Return an empty list of tools with given a non existing tag when calling getAllToolsByTag")
    void shouldReturnEmptyListTools_WhenCallingGetAllToolsByTag_WithNonExistingTag() {
        // Given
        var tag = "organization";
        given(repository.findByTagsContaining(tag)).willReturn(emptyList());

        // Given
        var actual = this.service.getAllToolsByTag(tag);

        // Then
        assertThat(actual.isEmpty(), is(true));
        assertThat(actual.contains(toolResponse), is(false));

        verify(this.repository).findByTagsContaining(tag);
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    @DisplayName("Should get a tool by ID")
    void shouldGetTool_WhenCallingGetToolById_WhenIdExist() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.of(tool));

        var actual = this.service.getToolById(1);

        // Then
        assertNotNull(actual);
        assertThat(actual, is(toolResponse));

        verify(repository).findById(anyInt());
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should throw NotFoundException when ID does not exist")
    void shouldThrowNotFoundException_WhenIdDoesNotExist() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.empty());
        var expectedMessage = "Tool not found!";

        // Then
        assertThatThrownBy(() -> service.getToolById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("Given valid tool request, it should save the tool")
    void shouldSaveTool_SuccessfullyWhenCallingSaveTool() {
        // Given
        given(repository.save(any(Tool.class))).willReturn(tool);

        // When
        var request = buildToolRequest();
        var actual = this.service.saveTool(request);

        // Then
        assertNotNull(actual);
        assertThat(actual, is(toolResponse));

        verify(repository).save(any(Tool.class));
        verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Should update tool successfully when Id exist")
    void shouldUpdateTool_SuccessfullyWhenCallingUpdateTool_WhenIdExist() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.of(tool));
        given(repository.save(any(Tool.class))).willReturn(buildToolUpdate());

        // When
        var request = buildToolRequestForUpdate();
        var resposeUpdate = buildToolResponseUpdate();
        var actual = this.service.updateTool(anyInt(), request);

        // Then
        assertNotNull(actual);
        assertThat(actual, is(resposeUpdate));

        verify(repository).findById(anyInt());
        verify(repository).save(any(Tool.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should throw NotFoundException when ID does not exist when calling updateTool")
    void shouldThrowNotFoundException_WhenCallingUpdateTool_WhenIdDoesNotExist() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.empty());
        var expectedMessage = "Tool not found!";

        var request = any(ToolRequest.class);

        // Then
        assertThatThrownBy(() -> service.updateTool(1, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("Should delete a tool successsfully when calling deleteToolById when Id exist")
    void shouldDeleteToolSuccessfully_WhenCalliDeleteToolById_WhenIdExist() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.of(tool));
        willDoNothing().given(repository).delete(any(Tool.class));

        this.service.deleteToolById(1);

        // Then
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).delete(tool);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("Should throw NotFoundException when ID does not exist when calling deleteToolById")
    void shouldThrowNotFoundException_WhenIdDoesNotExist_WhenCallingDeleteToolById() {
        // Given
        given(repository.findById(anyInt())).willReturn(Optional.empty());
        var expectedMessage = "Tool not found!";

        // Then
        assertThatThrownBy(() -> service.deleteToolById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(expectedMessage);
    }

}