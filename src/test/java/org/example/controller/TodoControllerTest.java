package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TodoService todoService;

    private TodoEntity excepted;

    //각 test 메소드 실행전에 excepted의 값을 초기화
    @BeforeEach
    void setup() {
        this.excepted = new TodoEntity();
        this.excepted.setId(123L);
        this.excepted.setTitle("Test Title");
        this.excepted.setOrder(0L);
        this.excepted.setCompleted(false);
    }


    @Test
    void create() throws Exception{
        when(this.todoService.add(any(TodoRequest.class)))
                .then((i) ->{ // 받은 리퀘스트를 기반으로 TodoEntity를 생성
                    TodoRequest request = i.getArgument(0, TodoRequest.class);
                    //Title만 request의 값을 반환
                    return new TodoEntity(this.excepted.getId(),
                                            request.getTitle(),
                                            this.excepted.getOrder(),
                                            this.excepted.getCompleted());
                });
        TodoRequest request = new TodoRequest();
        request.setTitle("ANY TITLE");

        //request body에 넣기 위해 ObjectMapper를 이용
        ObjectMapper mapper = new ObjectMapper();
        //request를 String 형태로
        String content = mapper.writeValueAsString(request);

        this.mvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ANY TITLE"));

        //테스트 통과됨
    }

}