package com.foodygo.configuration;

import com.foodygo.enums.EnumTokenType;
import com.foodygo.exception.AuthenticationException;
import io.jsonwebtoken.lang.Strings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JWTToken jwtToken;
    @Autowired private CustomUserDetailService customUserDetailService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final List<String> NON_USER = List.of(
            "/swagger-ui/**",
            "/v3/**",
            "/swagger-resources/**",
            "/public/**"
    );

    public String getToken(HttpServletRequest request) {
        String s = request.getHeader("Authorization");
        if (s != null && s.startsWith("Bearer ") && StringUtils.hasText(s)) {
            return s.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if(isAuthentication(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
            String bearerToken = getToken(request);
            if(Strings.hasText(bearerToken) && jwtToken.validate(bearerToken, EnumTokenType.TOKEN)) {
                String email = jwtToken.getEmailFromJwt(bearerToken, EnumTokenType.TOKEN);
                CustomUserDetail customUserDetail = (CustomUserDetail) customUserDetailService.loadUserByUsername(email);
                if(customUserDetail != null) {
                    if(customUserDetail.getAccessToken() != null && customUserDetail.getAccessToken().equals(bearerToken)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } else {
                        throw new AuthenticationException("You don't have permission");
                    }
                } else {
                    throw new AuthenticationException("You don't have permission");
                }
            }
        } catch (Exception e) {
            log.error("Fail on set user authentication:{}", e.toString());
            resolver.resolveException(request, response, null, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAuthentication(String uri){
        AntPathMatcher pathcMatcher = new AntPathMatcher();
        return NON_USER.stream().anyMatch(pattern -> pathcMatcher.match(pattern,uri));
    }

}
