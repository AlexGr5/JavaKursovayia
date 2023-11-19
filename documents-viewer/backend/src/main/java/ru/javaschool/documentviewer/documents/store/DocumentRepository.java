package ru.javaschool.documentviewer.documents.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    void deleteByIdIn(Collection<Long> ids);
}
