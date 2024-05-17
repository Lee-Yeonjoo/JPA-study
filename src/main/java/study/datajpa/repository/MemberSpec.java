package study.datajpa.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

public class MemberSpec { //검색 조건을 명세서에 정의한다.

    public static Specification<Member> teamName(final String teamName) {
        return (Specification<Member>) (root, query, builder) -> {
            if(StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER);
            return builder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (Specification<Member>) (root, query, builder) ->
                builder.equal(root.get("username"), username);
    }
}
