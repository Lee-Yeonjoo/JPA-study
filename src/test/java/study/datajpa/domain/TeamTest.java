package study.datajpa.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.TeamRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class TeamTest {

    @PersistenceContext
    EntityManager em;
    @Autowired TeamRepository teamRepository;

    @Test
    public void auditing() throws Exception{
        //given
        Team team = new Team("t1");
        teamRepository.save(team);

        Thread.sleep(100);
        team.setName("t2");

        em.flush();
        em.clear();

        //when
        Team findTeam = teamRepository.findById(team.getId()).get();

        //then
        System.out.println("createdAt = "+ findTeam.getCreatedAt());
        System.out.println("lastModifiedAt = "+ findTeam.getLastModifiedAt());
        System.out.println("createdBy = "+ findTeam.getCreatedBy());
        System.out.println("lastModifiedBy = "+ findTeam.getLastModifiedBy());
    }
}