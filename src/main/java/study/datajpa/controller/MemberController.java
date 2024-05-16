package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/{id}")
    public String findMember(@PathVariable(name = "id") long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/{id}/v2")
    public String findMemberV2(@PathVariable(name = "id") Member member) { //엔티티명을 바로 써준다.
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        Member member = new Member("m1", 10, null);
        memberRepository.save(member);
    }
}
