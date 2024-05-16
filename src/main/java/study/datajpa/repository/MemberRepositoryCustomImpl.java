package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import study.datajpa.domain.Member;

import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    @Autowired
    EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
