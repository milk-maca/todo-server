package org.example.controller;


import lombok.AllArgsConstructor;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.model.TodoResponse;

import org.example.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin //CORS 이슈를 막기위해
@RestController
@AllArgsConstructor
@RequestMapping("/") // base url
public class TodoController {
    private final TodoService todoService;

    @PostMapping // post방식으로 Response를 응답으로 내려주는 create
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest todoRequest) {
        System.out.println("CREATE");
        //타이틀이 없으면 정상적인 요청이아님
        if (ObjectUtils.isEmpty(todoRequest.getTitle()))
            return ResponseEntity.badRequest().build();
        // 나머지는 default 값
        if (ObjectUtils.isEmpty(todoRequest.getOrder()))
            todoRequest.setOrder(0L);
        if (ObjectUtils.isEmpty(todoRequest.getCompleted()))
            todoRequest.setCompleted(false);

        //Service에 add하고 result 받음
        TodoEntity result = this.todoService.add(todoRequest);
        //result를 TodoResponse에 랩핑해서 내려줌

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping("{id}")                         //경로로 받을 id를 쓰기위해
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id){
        System.out.println("READ ONE");
        TodoEntity result = this.todoService.searchById(id);

        return ResponseEntity.ok(new TodoResponse(result));

    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll(){
        System.out.println("READ ALL");
        List<TodoEntity> list = this.todoService.searchAll();
        List<TodoResponse> todoResponse = list.stream().map(TodoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(todoResponse);

    }

    @PatchMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest todoRequest){
        System.out.println("UPDATE");
        TodoEntity result = this.todoService.updateById(id,todoRequest);

        return ResponseEntity.ok(new TodoResponse(result));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id){
        System.out.println("DELETE ONE");
        this.todoService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(){
        System.out.println("DELETE ALL");
        this.todoService.deleteAll();

        return ResponseEntity.ok().build();
    }
}
