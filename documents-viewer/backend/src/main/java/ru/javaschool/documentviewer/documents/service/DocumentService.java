package ru.javaschool.documentviewer.documents.service;

import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.controller.dto.Status;
import ru.javaschool.documentviewer.documents.exception.DocumentNotFoundException;
import ru.javaschool.documentviewer.documents.exception.SendMessageException;

import java.util.List;
import java.util.Set;

/**
 * Сервис по работе с документами
 */
public interface DocumentService {
    /**
     * Сохранить документ
     * @param documentDto документ
     * @return сохраненный документ
     */
    DocumentDto save(DocumentDto documentDto);

    /**
     * Удалить документ
     * @param ids идентификаторы документов
     */
    void deleteAll(Set<Long> ids);

    /**
     * Удалить документ по ид
     * @param id идентификатор документа
     */
    void delete(Long id);

    /**
     * Обновить документ и отправить на обработку
     * @param documentDto документ
     * @return обновленный документ
     * @throws DocumentNotFoundException документ не найден
     * @throws SendMessageException не удалось отправить документ на обработку
     */
    DocumentDto update(DocumentDto documentDto);

    /**
     * Обновить документ после обработки
     * @param id id документа
     * @param status статус
     * @return обновленный документ
     * @throws DocumentNotFoundException документ не найден
     */
    DocumentDto updateAfterProcessing(Long id, Status status);

    /**
     * Получить все документы
     * @return список документов
     */
    List<DocumentDto> findAll();

    /**
     * Получить документ по номеру
     * @param id идентификатор
     * @return документ
     * @throws DocumentNotFoundException документ не найден
     */
    DocumentDto get(Long id);
}
