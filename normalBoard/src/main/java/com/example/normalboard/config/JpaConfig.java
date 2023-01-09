package com.example.normalboard.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager entityManager;

    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("impati"); // TODO : 스프링 시큐리티 인증 기능 붙일 때 수정
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

}
