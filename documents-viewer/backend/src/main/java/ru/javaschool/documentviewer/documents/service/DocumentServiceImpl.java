package ru.javaschool.documentviewer.documents.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.controller.dto.Status;
import ru.javaschool.documentviewer.documents.exception.DocumentNotFoundException;
import ru.javaschool.documentviewer.documents.store.DocumentRepository;
import ru.javaschool.documentviewer.documents.store.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Primary
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final KafkaSender kafkaSender;
    private final MapperFacade mapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, KafkaSender kafkaSender, MapperFacade mapper) {
        this.documentRepository = documentRepository;
        this.kafkaSender = kafkaSender;
        this.mapper = mapper;
    }

    @Override
    public DocumentDto save(DocumentDto documentDto) {
        Document document = mapper.map(documentDto, Document.class);
        document.setId(null);
        document.setStatusCode("NEW");
        document.setStatusName("Новый");
        document.setDate(new Date());
        document = documentRepository.save(document);
        return mapper.map(document, DocumentDto.class);
    }

    @Override
    public void deleteAll(Set<Long> ids) {
        documentRepository.deleteByIdIn(ids);
    }

    @Override
    public void delete(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public DocumentDto update(DocumentDto documentDto) {
        if (!documentRepository.existsById(documentDto.getId())) {
            throw new DocumentNotFoundException("Document with id=" + documentDto.getId() + " not found");
        }
        Document document = mapper.map(documentDto, Document.class);
        kafkaSender.sendMessage(documentDto);
        document = documentRepository.save(document);
        return mapper.map(document, DocumentDto.class);
    }

    @Override
    public DocumentDto updateAfterProcessing(Long id, Status status) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with id=" + id + " not found"));
        document.setStatusCode(status.getCode());
        document.setStatusName(status.getName());
        document = documentRepository.save(document);
        return mapper.map(document, DocumentDto.class);
    }

    @Override
    public List<DocumentDto> findAll() {
        return mapper.mapAsList(documentRepository.findAll(), DocumentDto.class);
    }

    @Override
    public DocumentDto get(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with id=" + id + " not found"));
        return mapper.map(document, DocumentDto.class);
    }
}
