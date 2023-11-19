package ru.javaschool.documentviewer.documents.service;

import ma.glasnost.orika.MapperFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javaschool.documentviewer.configuration.OrikaConfiguration;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.exception.DocumentNotFoundException;
import ru.javaschool.documentviewer.documents.store.Document;
import ru.javaschool.documentviewer.documents.store.DocumentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private KafkaSender kafkaSender;

    @Spy
    private final MapperFacade mapper = new OrikaConfiguration().documentMapper();

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    public void testSaveWithStatusNew() {
        DocumentDto documentDto = new DocumentDto();
        when(documentRepository.save(any(Document.class)))
                .then(invocation -> invocation.getArgument(0, Document.class));

        DocumentDto saved = documentService.save(documentDto);
        assertNotNull(saved);
        assertEquals("NEW", saved.getStatus().getCode());
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void testUpdateAndSendMessage() {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(1L);
        documentDto.setDescription("test");

        when(documentRepository.existsById(documentDto.getId())).thenReturn(true);
        when(documentRepository.save(any(Document.class)))
                .then(invocation -> invocation.getArgument(0, Document.class));
        doNothing().when(kafkaSender).sendMessage(any(DocumentDto.class));

        DocumentDto updated = documentService.update(documentDto);

        assertNotNull(updated);
        assertEquals("test", updated.getDescription());
        verify(documentRepository, times(1)).save(any(Document.class));
        verify(kafkaSender, times(1)).sendMessage(any(DocumentDto.class));
    }

    @Test
    void testUpdateShouldThrowExceptionWhenDocumentNotExists() {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setDescription("test");

        when(documentRepository.existsById(documentDto.getId())).thenReturn(false);

        assertThrows(DocumentNotFoundException.class, () -> documentService.update(documentDto));
    }


    @Test
    void testGetWhenNotExists() {
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DocumentNotFoundException.class, () -> documentService.get(id));
    }
}
