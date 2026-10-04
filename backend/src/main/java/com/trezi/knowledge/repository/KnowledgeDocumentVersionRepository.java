package com.trezi.knowledge.repository;

import com.trezi.knowledge.entity.KnowledgeDocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KnowledgeDocumentVersionRepository extends JpaRepository<KnowledgeDocumentVersion, UUID> {
}
