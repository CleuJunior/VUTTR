package br.com.cleonildo.vuttr.services;

import br.com.cleonildo.vuttr.dto.ToolRequest;
import br.com.cleonildo.vuttr.dto.ToolResponse;
import br.com.cleonildo.vuttr.entities.Tool;
import br.com.cleonildo.vuttr.handler.excpetion.NotFoundException;
import br.com.cleonildo.vuttr.repositories.ToolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static br.com.cleonildo.vuttr.handler.constants.ExcpetionMessageConstants.TOOL_NOT_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_DELETED;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_FOUND_BY_TAG;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_ID_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_ID_NOT_FOUND;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_LIST;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_LIST_EMPTY;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_SAVED;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_TAG_NOT_FOUD;
import static br.com.cleonildo.vuttr.log.LogConstants.TOOL_UPDATE;

@Service
@Transactional
public class ToolService {
    private static final Logger LOG = LoggerFactory.getLogger(ToolService.class);
    private final ToolRepository repository;

    public ToolService(ToolRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ToolResponse> getAllTools() {
        var response = this.repository
                .findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(ToolResponse::new)
                .toList();

        if (response.isEmpty()) {
            LOG.warn(TOOL_LIST_EMPTY);
            return response;
        }

        LOG.info(TOOL_LIST);
        return response;
    }

    @Transactional(readOnly = true)
    public List<ToolResponse> getAllToolsByTag(String tag) {
        var response = this.repository
                .findByTagsContaining(tag)
                .stream()
                .map(ToolResponse::new)
                .toList();

        if (response.isEmpty()) {
            LOG.warn(TOOL_TAG_NOT_FOUD, tag);
            return response;
        }

        LOG.info(TOOL_FOUND_BY_TAG, tag);
        return response;
    }

    @Transactional(readOnly = true)
    public ToolResponse getToolById(Integer id) {
        var response = this.repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn(TOOL_ID_NOT_FOUND, id);
                    return new NotFoundException(TOOL_NOT_FOUND);
                });

        LOG.info(TOOL_ID_FOUND, id);
        return new ToolResponse(response);
    }

    public ToolResponse saveTool(ToolRequest request) {
        var response = this.repository.save(this.toolFromRequest(request));

        LOG.info(TOOL_SAVED);
        return new ToolResponse(response);
    }

    private Tool toolFromRequest(ToolRequest request) {
        return new Tool(request.title(), request.link(), request.description(), request.tags());
    }

    @Transactional(noRollbackFor = NotFoundException.class)
    public ToolResponse updateTool(Integer id, ToolRequest request) {
        var response = this.repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn(TOOL_ID_NOT_FOUND, id);
                    return new NotFoundException(TOOL_NOT_FOUND);
                });

        response.setTitle(request.title());
        response.setLink(request.link());
        response.setDescription(request.description());
        response.setTags(request.tags());

        LOG.info(TOOL_UPDATE);
        return new ToolResponse(this.repository.save(response));
    }

    @Transactional(noRollbackFor = NotFoundException.class)
    public void deleteToolById(Integer id) {
        var response = this.repository.findById(id)
                .orElseThrow(() -> {
                    LOG.warn(TOOL_ID_NOT_FOUND, id);
                    return new NotFoundException(TOOL_NOT_FOUND);
                });

        this.repository.delete(response);
        LOG.info(TOOL_DELETED);
    }

}
