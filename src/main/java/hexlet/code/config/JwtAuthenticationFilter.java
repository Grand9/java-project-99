package hexlet.code.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.PublicKey;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final PublicKey publicKey;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, PublicKey publicKey) {
        this.userDetailsService = userDetailsService;
        this.publicKey = publicKey;
    }

    /**
     * This method performs the filtering of requests to check for a valid JWT token.
     *
     * @param request  The HttpServletRequest object that contains the request made by the client.
     * @param response The HttpServletResponse object that contains the response to the client.
     * @param chain    The filter chain to continue the processing of the request.
     * @throws ServletException if an error occurs during the filter's operation.
     * @throws IOException      if an input or output error is detected when handling the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = Jwts.parser()
                        .setSigningKey(publicKey)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (SignatureException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
