package gl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Created by suman.das on 11/28/18.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter  {

    private ResourceServerTokenServices tokenServices;
    private JwtAccessTokenConverter accessTokenConverter;

    public ResourceServerConfig ( ResourceServerTokenServices tokenServices,
                                  JwtAccessTokenConverter accessTokenConverter ) {
        this.tokenServices = tokenServices;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();

        http.headers().frameOptions().disable();
    }

}
