package br.com.cleonildo.vuttr.repositories;

import br.com.cleonildo.vuttr.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Integer> {
}