package hu.me.domain;

import hu.me.validator.UniqueEmail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Credentials {

    @Column(unique=true, nullable = false, length = 45)
    @UniqueEmail
    private String loginName;
    @Column(nullable = false)
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
