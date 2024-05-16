package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;
import study.datajpa.domain.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/{id}")
    public String findMember(@PathVariable(name = "id") long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/{id}/v2")
    public String findMemberV2(@PathVariable(name = "id") Member member) { //엔티티명을 바로 써준다.
        return member.getUsername(); //트랜잭션 밖이므로 단순조회만 가능!
    }

    @GetMapping("/paging")
    public Page<Member> list(@PageableDefault(size=4, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/paging/dto")
    public Page<MemberDto> listDto(@PageableDefault(size = 4, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
    }

    @PostConstruct
    public void init() {
        Team team1 = new Team("t1");
        Team team2 = new Team("t2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member = new Member("m1", 10, team1);
        Member member2 = new Member("m2", 10, team1);
        Member member3 = new Member("m3", 10, team2);
        Member member4 = new Member("m4", 10, team1);
        Member member5 = new Member("m5", 10, team2);
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
    }
}
