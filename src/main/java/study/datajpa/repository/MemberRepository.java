package study.datajpa.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.domain.Member;
import study.datajpa.domain.MemberDto;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

    @Modifying(clearAutomatically = true) //이걸 써줘야 update쿼리가 나간다. 해당 옵션을 true로 해야 벌크연산 후 영속성 컨텍스트가 초기화됨.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    public int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    public List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
        //페치조인 적용
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
        //JPA구현체에게 readOnly 힌트 제공
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findByUsername(String name); //select할 때 다른 데서의 접근을 막음
}
