package ru.javaschool.documentviewer.documents.store;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "document")
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Вид документа
     */
    @Column
    private String type;

    /**
     * Организация
     */
    @Column
    private String organization;

    /**
     * Описание
     */
    @Column
    private String description;

    /**
     * Пациент
     */
    @Column
    private String patient;

    /**
     * Дата документа
     */
    @Column
    private Date date;

    /**
     * Статус
     */
    @Column(name = "status_code")
    private String statusCode;

    /**
     * Статус для отображения в интерфейсе
     */
    @Column(name = "status_name")
    private String statusName;
}
