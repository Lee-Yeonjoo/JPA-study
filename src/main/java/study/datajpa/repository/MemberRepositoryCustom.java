package study.datajpa.repository;

import study.datajpa.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    public List<Member> findMemberCustom();
}
