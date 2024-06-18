package study.datajpa;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class DataJpaApplication {

	private final MemberRepository memberRepository;

	public static void main(String[] args) {SpringApplication.run(DataJpaApplication.class, args);}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString()); //id 랜덤생성. -> 실무에선 세션정보나 스프링 시큐리티 로그인 정보의 id
	}
}
