package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * 스프링 시큐리티 설정 클래스
 * */

//스프링의 설정 클래스임을 명시
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
			// 패스워드 인코더를 컨테이너에 빈으로 등록하는 메소드
			@Bean
			public PasswordEncoder passwordEncoder() {
				return new BCryptPasswordEncoder();
			}
			@Bean
			public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
				
				http.authorizeHttpRequests()
				
				.requestMatchers("/register").permitAll() //회원가입은 아무나 접근 가능
				.requestMatchers("/assets/*","/css/*","/js/*") //리소스는 아무나 접근 가능
				.permitAll()
				.requestMatchers("/").authenticated() //메인화면은 로그인한 사용자이면 접근 가능
				.requestMatchers("/board/*").hasAnyRole("ADMIN","USER") //게시물 관리는 관리자 또는 사용자 
				.requestMatchers("/comment/*").hasAnyRole("ADMIN","USER")
				.requestMatchers("/member/*").hasRole("ADMIN");//회원 관리는 관리자이면 접근 가능
				
				http.formLogin();
				http.logout();
				
				// csrf 설정 해제
				http.csrf().disable();	
				
				return http.build();
				
			}
			
}
