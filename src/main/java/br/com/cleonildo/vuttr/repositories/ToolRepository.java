package br.com.cleonildo.vuttr.repositories;

import br.com.cleonildo.vuttr.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {

    List<Tool> findByTagsContaining(String tag);
}