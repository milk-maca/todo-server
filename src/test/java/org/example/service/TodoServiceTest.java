package org.example.service;

import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

//Mock객체 사용을위해 추가
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    // Mock 사용하는 이유 외부 시스템 의존하지않고 자체 유닛 테스트 수행
    // 실제 DB에 영향을 주지 않고 테스트
    @Mock //직접적으로 Mock 객체를 사용할 todoRepository 선언
    private TodoRepository todoRepository;

    @InjectMocks // todoRepository를 주입받아서 사용
    private TodoService todoService;


    @Test
    void add() { //todoRepository가 save를 호출해서 TodoEntity 값을 받으면 받은 Entity 값을 반환
        when(this.todoRepository.save(any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");

        TodoEntity actual = this.todoService.add(expected);

        //동일한지 비교
        assertEquals(expected.getTitle(), actual.getTitle());

        //종료 코드 0(으)로 완료된 프로세스
    }

    @Test
    void searchById() {
        TodoEntity testEntity = new TodoEntity();
        testEntity.setId(100000L);
        testEntity.setTitle("Test Title 100000");
        testEntity.setOrder(0L);
        testEntity.setCompleted(false);

        Optional<TodoEntity> optional = Optional.of(testEntity);

        // findById()는 옵셔널을 반환함 어떤값이던 값이 주어졌을때 옵셔널 리턴
        given(this.todoRepository.findById(anyLong())).willReturn(optional);

        TodoEntity actual = this.todoService.searchById(100L);
        TodoEntity expected = optional.get();

        //동일한지 비교
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());

        //종료 코드 0(으)로 완료된 프로세스

    }

    @Test   //값이 없는값 조회시
    public void searchByIdFailed(){
        given(this.todoRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,() ->{
            this.todoService.searchById(100L);
        });
    }
}