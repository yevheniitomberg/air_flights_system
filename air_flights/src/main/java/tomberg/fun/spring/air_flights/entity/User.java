package tomberg.fun.spring.air_flights.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Pattern(regexp = "^(.+)@(.+)$", message = "It is not an email address!")
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must contains minimum 8 characters")
    @Column(name = "password")
    private String password;

    @Column
    private String confirmLink;

    @Column
    private boolean fullyRegistered;

    @Transient
    private  String commitPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isFullyRegistered();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommitPassword() {
        return commitPassword;
    }

    public void setCommitPassword(String commitPassword) {
        this.commitPassword = commitPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getConfirmLink() {
        return confirmLink;
    }

    public void setConfirmLink(String confirmLink) {
        this.confirmLink = confirmLink;
    }

    public boolean isFullyRegistered() {
        return fullyRegistered;
    }

    public void setFullyRegistered(boolean fullyRegistered) {
        this.fullyRegistered = fullyRegistered;
    }
}
