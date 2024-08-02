package com.zerock.mallapi.repository;

import com.zerock.mallapi.domain.Member;
import com.zerock.mallapi.domain.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 등록")
    public void testInsertMember() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@naver.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("nickname" + i)
                    .build();

            member.addRole(MemberRole.USER);

            if (i >= 5) {
                member.addRole(MemberRole.ADMIN);
            }

            if (i >= 8) {
                member.addRole(MemberRole.MANAGER);

            }

           //when
            memberRepository.save(member);
        }



        //then

    }

    @Test
    @DisplayName("회원 조회")
    public void testRead() throws Exception {
        //given
        String email = "user9@naver.com";

        //when
        Member withRoles = memberRepository.getWithRoles(email);

        //then
        log.info("member : {}", withRoles);

    }
}
