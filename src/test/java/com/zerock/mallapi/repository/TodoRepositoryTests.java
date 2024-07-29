package com.zerock.mallapi.repository;

import com.zerock.mallapi.domain.Todo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;
    
    
    
    @Test
    @DisplayName("추가")
    @Transactional
    @Rollback
    public void insertFail() throws Exception {
        //given
        Todo todo = Todo.builder()
                .title("new Todo!")
                .writer("user")
                .dueDate(LocalDate.of(2024, 6, 11))
                .build();

        //when
        Todo saved = todoRepository.save(todo);
        Long tno = saved.getTno();

        Optional<Todo> byId = todoRepository.findById(tno);
        Todo todo1 = byId.orElseThrow();

        //then
        Assertions.assertThat(todo1).isNotNull();
    
    }


   @Test
   @DisplayName("조회 실패")
   @Transactional(readOnly = true)
   public void readFail() throws Exception {
       //given
       Long tno1 = 101L;
       Long tno2 = 102L;

       //when
       Optional<Todo> byId1 = todoRepository.findById(tno1);
       Optional<Todo> byId2 = todoRepository.findById(tno2);

       Todo todo1 = byId1.orElseThrow();
       Todo todo2 = byId2.orElseThrow();

       //then
        Assertions.assertThat(todo2).isNotEqualTo(todo1);

   }


   @DisplayName("조회")
   @Test
   @Transactional(readOnly = true)
   public void read() throws Exception {
       //given
       Long tno1 = 101L;
       Long tno2 = 101L;

       //when
       Optional<Todo> byId1 = todoRepository.findById(tno1);
       Optional<Todo> byId2 = todoRepository.findById(tno2);

       Todo todo1 = byId1.orElseThrow();
       Todo todo2 = byId2.orElseThrow();

       //then
       Assertions.assertThat(todo2).isEqualTo(todo1);

   }

    @Test
    @DisplayName("제목 수정 실패")
    @Transactional
    @Rollback
    public void updateTitleFail() throws Exception {
        //given
        Long tno = 101L;
        String title = "Change Title";

        //when
        Optional<Todo> byId1 = todoRepository.findById(tno);
        Todo todo1 = byId1.orElseThrow();

        String beforeTitle = todo1.getTitle();

        todo1.changeTitle(title);
        todoRepository.save(todo1);

        Optional<Todo> byId2 = todoRepository.findById(tno);
        Todo todo2 = byId2.orElseThrow();

        //then
        Assertions.assertThat(todo2.getTitle()).isNotEqualTo(beforeTitle);

    }
   
   @Test
   @DisplayName("제목 수정")
   @Transactional
   @Rollback
   public void updateTitle() throws Exception {
       //given
       Long tno = 101L;
       String title = "Change Title";
       
       //when
       Optional<Todo> byId1 = todoRepository.findById(tno);
       Todo todo1 = byId1.orElseThrow();

       todo1.changeTitle(title);
       todoRepository.save(todo1);

       Optional<Todo> byId2 = todoRepository.findById(tno);
       Todo todo2 = byId2.orElseThrow();

       //then
       Assertions.assertThat(todo2.getTitle()).isEqualTo(title);
   
   }

   @Test
   @DisplayName("완료 여부 수정 실패")
   @Transactional
   @Rollback
   public void updateCompleteFail() throws Exception {
       //given
       Long tno = 101L;
       boolean complete = true;

       //when
       Optional<Todo> byId1 = todoRepository.findById(tno);
       Todo todo1 = byId1.orElseThrow();

       boolean beforeComplete = todo1.isComplete();

       todo1.changeComplete(complete);
       todoRepository.save(todo1);

       Optional<Todo> byId2 = todoRepository.findById(tno);
       Todo todo2 = byId2.orElseThrow();

       //then
       Assertions.assertThat(todo2.isComplete()).isNotEqualTo(beforeComplete);

   }

    @Test
    @DisplayName("완료 여부 수정")
    @Transactional
    @Rollback
    public void updateComplete() throws Exception {
        //given
        Long tno = 101L;
        boolean complete = true;

        //when
        Optional<Todo> byId1 = todoRepository.findById(tno);
        Todo todo1 = byId1.orElseThrow();

        todo1.changeComplete(complete);
        todoRepository.save(todo1);

        Optional<Todo> byId2 = todoRepository.findById(tno);
        Todo todo2 = byId2.orElseThrow();

        //then
        Assertions.assertThat(todo2.isComplete()).isEqualTo(complete);

    }


    @Test
    @DisplayName("기간 수정 실패")
    @Transactional
    @Rollback
    public void updateDueDateFail() throws Exception {
        //given
        Long tno = 101L;
        LocalDate dueDate = LocalDate.of(2024, 6, 11);

        //when
        Optional<Todo> byId1 = todoRepository.findById(tno);
        Todo todo1 = byId1.orElseThrow();

        LocalDate beforeDueDate = todo1.getDueDate();

        todo1.changeDueDate(dueDate);
        todoRepository.save(todo1);

        Optional<Todo> byId2 = todoRepository.findById(tno);
        Todo todo2 = byId2.orElseThrow();

        //then
        Assertions.assertThat(todo2.getDueDate()).isNotEqualTo(beforeDueDate);

    }

    @Test
    @DisplayName("기간 수정")
    @Transactional
    @Rollback
    public void updateDueDate() throws Exception {
        //given
        Long tno = 101L;
        LocalDate dueDate = LocalDate.of(2024, 6, 11);

        //when
        Optional<Todo> byId1 = todoRepository.findById(tno);
        Todo todo1 = byId1.orElseThrow();

        todo1.changeDueDate(dueDate);
        todoRepository.save(todo1);

        Optional<Todo> byId2 = todoRepository.findById(tno);
        Todo todo2 = byId2.orElseThrow();

        //then
        Assertions.assertThat(todo2.getDueDate()).isEqualTo(dueDate);

    }




}
