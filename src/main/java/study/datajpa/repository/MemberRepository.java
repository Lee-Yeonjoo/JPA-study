package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //메소드 이름을 분석해 쿼리 생성

    @Query("select m from Member m where m.username = :username and m.age = :age")
    public List<Member> findUser(@Param("username") String username, @Param("age") int age); //파라미터 바인딩(이름 기준)

    @Query("select m.username from Member m")
    public List<String> findUsername();

    @Query("select new study.datajpa.domain.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    public List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :usernames")
    public List<Member> findByUsernames(@Param("usernames") List<String> usernames); //IN절에 쓰이는 컬렉션 파라미터도 바인딩할 수 있다.

    Page<Member> findPageByAge(int age, Pageable pageable);
}
