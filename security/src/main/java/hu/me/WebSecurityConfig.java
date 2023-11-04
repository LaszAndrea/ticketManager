package hu.me;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.annotation.WebServlet;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/homepage").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/login?error").denyAll()
                .antMatchers("/addMovie").hasAuthority("ADMIN")
                .antMatchers("/addTime").hasAuthority("ADMIN")
                .antMatchers("/changeMovie").hasAuthority("ADMIN")
                .antMatchers("/changeTime").hasAuthority("ADMIN")
                .antMatchers("/update-details").hasAuthority("ADMIN")
                .antMatchers("/upload").hasAuthority("ADMIN")
                .antMatchers("/uploadMovie").hasAuthority("ADMIN")
                .antMatchers("/delete-sight").hasAuthority("ADMIN")
                .antMatchers("/user-home-page").hasAuthority("USER")
                .antMatchers("/time-details").hasAuthority("USER")
                .antMatchers("/change-user-email").hasAuthority("USER")
                .antMatchers("/change-user-name").hasAuthority("USER")
                .antMatchers("/change-user-phone").hasAuthority("USER")
                .antMatchers("/console/**").permitAll()
                .and()
                .formLogin()
                .defaultSuccessUrl("/homepage")
                .loginPage("/login");
                //.anyRequest().authenticated()
                //.and()
                //.formLogin()
                //.loginPage("/login");

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

