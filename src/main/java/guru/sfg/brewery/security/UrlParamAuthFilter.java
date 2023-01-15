package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UrlParamAuthFilter extends MyAbstractAuthFilter {

    public UrlParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("Api-Key");
    }

    @Override
    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("Api-Secret");
    }
}
