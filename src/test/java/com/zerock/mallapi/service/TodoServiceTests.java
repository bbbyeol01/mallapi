package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Todo;
import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.TodoDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
public class TodoServiceTests {

    @Autowired
    private TodoService todoService;
    
    @Test
    @DisplayName("todo 생성")
    public void testRegister() throws Exception {
        //given
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2024,8,1))
                .build();
        
        //when
        Long tno = todoService.register(todoDTO);
        
        //then
        Assertions.assertThat(tno).isEqualTo(202L);
    
    }
    
    @Test
    @DisplayName("todo 조회")
    public void testGet() throws Exception {
        //given
        Long tno = 202L;
        
        //when
        TodoDTO todoDTO = todoService.get(tno);
        TodoDTO resultDTO = todoService.get(202L);

        //then
        Assertions.assertThat(todoDTO).isEqualTo(resultDTO);
    
    }

    @Test
    @DisplayName("todo 수정")
    public void testModify() throws Exception {
        //given
        Long tno = 202L;

        TodoDTO todoDTO = TodoDTO.builder()
                .tno(tno)
                .title("수정된 제목")
                .complete(true)
                .build();
        //when
        todoService.modify(todoDTO);

        TodoDTO getTodoDTO = todoService.get(tno);

        //then
        Assertions.assertThat(getTodoDTO.getTitle()).isEqualTo("수정된 제목");

    }
    
    @Test
    public void testDelete() throws Exception {
        //given
        Long tno = 202L;

        //when
        todoService.remove(tno);

        //then

    }

    @Test
    @DisplayName("todo 목록(페이징 처리) 조회")
    public void testList() throws Exception {
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(2)
                .size(10)
                .build();

        //when
        PageResponseDTO<TodoDTO> list = todoService.list(pageRequestDTO);

        //then
        log.info("page {} ", list);

    }
}
