package com.gatway.gatwayfilter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.gatway.feignclient.ClientApi;
import com.gatway.feignclient.UserInfo;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

      // @Autowired
       // private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    @Lazy
    ClientApi client;


	public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
//                    //REST call to AUTH service
//                    template.getForObject("http://IDENTITY-SERVICE//validate?token" + authHeader, String.class);
                  //  jwtUtil.validateToken(authHeader);
                	//match token from user info db 
                	
                	List<UserInfo> token = client.getAllUsers();
                	
                	 for (UserInfo obj : token) {
                         if (authHeader.equals(obj.getToken())) { // Using equals() instead of ==
                             System.out.println("Token matched, User: " + obj.getUsername());
                         } else {
                             System.out.println("Token not matched");
                         }
                     }
                
                	

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}