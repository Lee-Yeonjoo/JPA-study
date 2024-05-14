package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age); //메소드 이름을 분석해 쿼리 생성

    @Query("select m from Member m where m.username = :username and m.age = :age")
    public List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
