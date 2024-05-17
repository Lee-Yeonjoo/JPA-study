package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class QueryByExampleTest {

    @Autowired
    EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void basic() {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        memberRepository.save(new Member("m1", 0, teamA));
        memberRepository.save(new Member("m2", 0, teamA));
        em.flush();

        //when
        //Probe 생성 -> example 객체 역할
        Team team = new Team("teamA");
        Member member = new Member("m1", 10, team);

        //ExampleMatcher 생성, age는 무시
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age"); //age는 무시하고, example과 같은 것을 찾는다.

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        //then
        for (Member member1 : result) {
            System.out.println("member1 = " + member1);
        }
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
