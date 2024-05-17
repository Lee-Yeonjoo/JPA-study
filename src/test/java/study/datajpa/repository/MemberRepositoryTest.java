package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;
import study.datajpa.domain.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        memberRepository.save(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember = memberRepository.findById(member1.getId()).get();
        assertThat(findMember).isEqualTo(member1);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(1);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryTest() {
        //given
        Member m1 = new Member("m1", 10, null);
        Member m2 = new Member("m2", 10, null);
        Member m3 = new Member("m3", 15, null);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        //when
        List<Member> members = memberRepository.findUser("m1", 10);

        //then
        assertThat(members.get(0).getUsername()).isEqualTo("m1");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void valueAndDto() {
        //given
        Team t1 = new Team("t1");
        Team t2 = new Team("t2");
        teamRepository.save(t1);
        teamRepository.save(t2);

        Member m1 = new Member("m1", 10, t1);
        Member m2 = new Member("m2", 10, t1);
        Member m3 = new Member("m3", 15, t2);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        //when
        List<String> name = memberRepository.findUsername();
        for (String s : name) {
            System.out.println("s = " + s);
        }

        List<MemberDto> dtos = memberRepository.findMemberDto();
        for (MemberDto dto : dtos) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void collectionParam() {
        //given
        Team t1 = new Team("t1");
        Team t2 = new Team("t2");
        teamRepository.save(t1);
        teamRepository.save(t2);

        Member m1 = new Member("m1", 10, t1);
        Member m2 = new Member("m2", 10, t1);
        Member m3 = new Member("m3", 15, t2);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        //when
        List<String> usernames = new ArrayList<>();
        usernames.add("m1");
        usernames.add("m2");
        List<Member> find = memberRepository.findByUsernames(usernames);

        //then
        for (Member member : find) {
            System.out.println("member = " + member);
        }
        assertThat(find.size()).isEqualTo(2);
    }

    @Test
    public void paging() {
        //given
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 10);
        Member m3 = new Member("m3", 15);
        Member m4 = new Member("m4", 10);
        Member m5 = new Member("m5", 10);
        Member m6 = new Member("m6", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "username")); //username으로 내림차순 정렬
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest); //반환타입이 Page면 totalCount쿼리도 같이 나간다.\
        Page<MemberDto> pageDto = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        for (Member member : page) {
            System.out.println("member = " + member);
        }
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.hasNext()).isTrue();

        for (MemberDto memberDto : pageDto) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void bulkUpdate() throws Exception{
        //given
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 15);
        Member m3 = new Member("m3", 19);
        Member m4 = new Member("m4", 20);
        Member m5 = new Member("m5", 24);
        Member m6 = new Member("m6", 28);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);
        memberRepository.save(m6);

        //when
        int count = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear(); //벌크연산 후에 영속성 컨텍스트를 날려야 한다. -> 다시 업데이트된 db의 내용을 조회하도록 or @Modifying의 clearAutomatically = true 옵션 설정.

        //then
        assertThat(count).isEqualTo(3);

        Optional<Member> findMember = memberRepository.findById(m4.getId());
        System.out.println("findMember = " + findMember);
    }

    @Test
    public void entityGraph() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 20, teamB);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll(); //페치조인됨

        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
    public void queryHint() {
        //given
        memberRepository.save(new Member("m1",10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("m1");
        member.setUsername("m2");

        em.flush(); //readOnly라서 업데이트 쿼리가 나가지 않는다.
    }

    @Test
    public void repositoryCustom() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 20, teamB);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberCustom();

        //then
        assertThat(members.size()).isEqualTo(2);
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void spec() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 20, teamB);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //then
        for (Member member : result) {
            System.out.println("member = " + member);
        }
        assertThat(result.size()).isEqualTo(1);
    }
}