package com.example.demo.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 스프링 시큐리티 설정 클래스
 * */
@Configuration //스프링의 설정 클래스임을 명시
@EnableWebSecurity //스프링 시큐리티 설정 클래스임을 명시
public class SecurityConfig {

    // 패스워드 인코더를 컨테이너에 빈으로 등록하는 메소드
    // 메소드에 @Bean을 붙이면, 반환값인 인코더 객체가 스프링 컨테이너에 등록됨
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자 인증을 처리하는 메소드
    // 메소드에 @Bean을 붙이면, 반환값인 인증 필터 객체가 스프링 컨테이너에 등록됨
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 보안규칙 (메뉴별 접근제한 설정)
        http.authorizeHttpRequests()
                .requestMatchers("/register").permitAll() //회원가입은 아무나 접근 가능
                .requestMatchers("/assets/*", "/css/*", "/js/*").permitAll() //리소스는 아무나 접근 가능
                .requestMatchers("/").authenticated() //메인화면은 로그인한 사용자이면 접근 가능
                .requestMatchers("/board/*").hasAnyRole("ADMIN", "USER") //게시물 관리는 관리자 또는 사용자이면 접근 가능
                .requestMatchers("/comment/*").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/member/*").hasRole("ADMIN"); //회원 관리는 관리자이면 접근 가능


        http.formLogin(); //시큐리티가 제공하는 기본 로그인페이지 사용하기
        http.csrf().disable(); //csrf는 get을 제외하여 상태값을 위조(변경)할 수있는 post,put,delete 메소드를 막음
        http.logout(); // 로그아웃 처리

        //커스텀 로그인 페이지 설정
        http.formLogin()
                .loginPage("/customlogin") // 로그인 화면 주소
                .loginProcessingUrl("/login") // 로그인 처리 주소
                .successHandler(
                        new AuthenticationSuccessHandler() {
							@Override
							public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
								response.sendRedirect("/");
							}
                        }
                )
                .permitAll(); // 접근 권한

        return http.build();
    }

}
