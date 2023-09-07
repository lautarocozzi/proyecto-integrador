package com.mrInstruments.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.utils.UtilsDtoMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.mrInstruments.backend.security.Constants.*;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private AuthenticationManager authenticationManager;

	private ObjectMapper objectMapper = new ObjectMapper();

	public JWTAuthenticationFilter(String url,AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(url));
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			User credentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							credentials.getEmail(),
							credentials.getPassword(),
							new ArrayList<>()
					)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		User user = ((User) auth.getPrincipal());

		String token = Jwts.builder()
				.claim("id",user.getId().toString())
				.claim("fullname",user.getNombre() + " " + user.getApellido())
				.claim("user_role",user.getUserRol())
				.setSubject(user.getEmail())
				.setIssuedAt(new Date()).setIssuer(ISSUER_INFO)
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
		response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);
		response.setContentType("application/json");  // Set content type of the response
		response.setCharacterEncoding("UTF-8"); // set the encoding
//		response.getWriter().write(objectMapper.writeValueAsString(UtilsDtoMapper.userToUserDto(user)));  // Write response body.
		response.getWriter().write(objectMapper.writeValueAsString(token));
	}
}
