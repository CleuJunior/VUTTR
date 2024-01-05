package br.com.cleonildo.vuttr.controllers;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.services.ToolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolRequest;
import static br.com.cleonildo.vuttr.factory.ToolFactory.buildToolResponse;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ToolControllerTest {
    @Mock
    private ToolService service;
    @InjectMocks
    private ToolController controller;
    private MockMvc mockMvc;
    private final static String BASE_URL = "/tools";
    private final static String URL_ID = "/tools/{id}";
    private final static int ID = 1;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ToolResponse response = buildToolResponse();
    private final ToolRequest request = buildToolRequest();

    @BeforeEach
    void setupAttributes() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).alwaysDo(print()).build();
    }

    @Test
    @DisplayName("Should get list of tools and return status code OK")
    void shouldGetListOfTools_AndReturnStatusCodeOK() throws Exception {
        // Given
        given(service.getAllTools()).willReturn(singletonList(response));

        // Then
        this.mockMvc
                .perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].title").value(response.title()))
                .andExpect(jsonPath("$[0].link").value(response.link()))
                .andExpect(jsonPath("$[0].description").value(response.description()))
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags", containsInAnyOrder(response.tags().toArray())));

        // Verify
        verify(service).getAllTools();
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should get tool by ID and return status code OK")
    void shouldGetToolById_AndReturnStatusCodeOK() throws Exception {
        // Given
        given(service.getToolById(anyInt())).willReturn(response);

        // Then
        this.mockMvc
                .perform(get(URL_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.link").value(response.link()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", containsInAnyOrder(response.tags().toArray())));


        // Verify
        verify(this.service).getToolById(anyInt());
        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("Should get list of tools by tag and return status code OK")
    void shouldGetListToolsByTag_AndReturnStatusCodeOK() throws Exception {
        // Given
        String tag = "organization";
        given(service.getAllToolsByTag(tag)).willReturn(singletonList(response));

        // Then
        this.mockMvc
                .perform(get(BASE_URL).param("tag", tag)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].title").value(response.title()))
                .andExpect(jsonPath("$[0].link").value(response.link()))
                .andExpect(jsonPath("$[0].description").value(response.description()))
                .andExpect(jsonPath("$[0].tags").isArray())
                .andExpect(jsonPath("$[0].tags", containsInAnyOrder(response.tags().toArray())));

        // Verify
        verify(this.service).getAllToolsByTag(tag);
        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("Should save tool and return status code CREATED")
    void shouldSaveTool_AndReturnStatusCodeCreated() throws Exception {
        // Given
        given(service.saveTool(request)).willReturn(response);

        // Then
        this.mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();


        // Verify
        verify(this.service).saveTool(request);
        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("Should update tool and return status code OK")
    void shouldUpdateTool_AndReturnStatusCodeOK() throws Exception {
        // Given
        given(service.updateTool(anyInt(), any(ToolRequest.class))).willReturn(response);

        // Then
        this.mockMvc
                .perform(put(URL_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.link").value(response.link()))
                .andExpect(jsonPath("$.description").value(response.description()))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", containsInAnyOrder(response.tags().toArray())));


        // Verify
        verify(this.service).updateTool(anyInt(), any(ToolRequest.class));
        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("Should delete tool and return status code NO_CONTENT")
    void shouldDeleteTool_AndReturnStatusCodeNoContent() throws Exception {
        // Given
        willDoNothing().given(service).deleteToolById(ID);

        // Then
        this.mockMvc
                .perform(delete(URL_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify
        verify(this.service).deleteToolById(ID);
        verifyNoMoreInteractions(this.service);
    }

}