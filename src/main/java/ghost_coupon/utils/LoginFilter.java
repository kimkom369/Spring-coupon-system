package ghost_coupon.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ghost_coupon.repositories.TokenRepository;

/* class provides a filter when logging in
 * 
 * @WebFilter: used to declare a filter in a web application.
 * */


@Component
@WebFilter("/*")
public class LoginFilter implements Filter {

    private final TokenRepository tokenRepository;

    @Autowired
    public LoginFilter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURL().toString();
        if (!path.contains("login")) {
            if (!path.contains("registration")) {
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if ("auth".equals(c.getName())) {
                            if (!tokenRepository.findByToken(c.getValue()).isPresent()) {
                                res.sendRedirect("http://localhost:4200");
                                return;
                            }
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}