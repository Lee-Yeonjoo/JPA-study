package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;
import study.datajpa.domain.Team;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

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
}