package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;
    @Autowired TeamJpaRepository teamJpaRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember = memberJpaRepository.findById(member1.getId()).get();
        assertThat(findMember).isEqualTo(member1);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(1);
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
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        memberJpaRepository.save(m6);

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> page = memberJpaRepository.findByPaging(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        for (Member member : page) {
            System.out.println("member = " + member);
        }
        assertThat(page.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate() throws Exception{
        //given
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 15);
        Member m3 = new Member("m3", 19);
        Member m4 = new Member("m4", 20);
        Member m5 = new Member("m5", 21);
        Member m6 = new Member("m6", 28);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);
        memberJpaRepository.save(m6);

        //when
        int count = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void findLazy() {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        Member m1 = new Member("m1", 10, teamA);
        Member m2 = new Member("m2", 20, teamB);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberJpaRepository.findAll();

        //then
        for (Member member : members) {
            member.getTeam().getName();
        }

    }
}