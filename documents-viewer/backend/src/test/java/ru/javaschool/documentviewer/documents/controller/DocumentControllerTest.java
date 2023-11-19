package ru.javaschool.documentviewer.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.javaschool.documentviewer.configuration.JacksonConfiguration;
import ru.javaschool.documentviewer.documents.controller.dto.DocumentDto;
import ru.javaschool.documentviewer.documents.controller.dto.IdsDto;
import ru.javaschool.documentviewer.documents.service.DocumentServiceImpl;

import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {
    private static final String BASE_PATH = "/documents";

    private final ObjectMapper mapper = new JacksonConfiguration().objectMapper();
    private MockMvc mockMvc;
    @MockBean
    private DocumentServiceImpl service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testSave() throws Exception {
        var organization = randomAlphabetic(100);

        when(service.save(any())).thenReturn(any());

        var cityDto = new DocumentDto();
        cityDto.setId(5L);
        cityDto.setOrganization(organization);
        mockMvc.perform(postAction(BASE_PATH, cityDto)).andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).save(cityDto);
    }

    @Test
    public void testGet() throws Exception {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(1L);
        documentDto.setDescription("test");

        when(service.findAll()).thenReturn(List.of(documentDto));
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testSend() throws Exception {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(1L);
        documentDto.setDescription("test");

	when(service.get(any())).thenReturn(documentDto);
        when(service.update(any())).then(invocation -> invocation.getArgument(0, DocumentDto.class));
        mockMvc.perform(postAction(BASE_PATH + "/send", documentDto))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {

        doNothing().when(service).delete(any());
        mockMvc.perform(delete(BASE_PATH + "/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAll() throws Exception {

        doNothing().when(service).deleteAll(any());
        IdsDto idsDto = new IdsDto();
        idsDto.setIds(Set.of(1L, 2L));
        mockMvc.perform(deleteAction(BASE_PATH, idsDto))
                .andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder postAction(String uri, Object dto) throws JsonProcessingException {
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder deleteAction(String uri, Object dto) throws JsonProcessingException {
        return delete(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }
}
