package com.zerock.mallapi.security;

import com.zerock.mallapi.domain.Member;
import com.zerock.mallapi.dto.MemberDTO;
import com.zerock.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  CustomUserDetailsService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("--------------------loadUserByUsername---------------------");

        Member member = memberRepository.getWithRoles(username);

        if (member == null) {
            throw new UsernameNotFoundException("Not Found");
        }

        List<String> memberRoleList = member.getMemberRoleList().stream().map(Enum::name).toList();
        MemberDTO memberDTO = new MemberDTO(member.getEmail(), member.getPw(), member.getNickname(), member.isSocial(), memberRoleList);

        return memberDTO;
    }
}
