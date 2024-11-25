package by.bsuir.hotelwebapp.entity;

import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", length = 320, nullable = false)
    private String passwordHash;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    public User() {
    }

    public User(Long id, String username, String passwordHash, String email, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isAdmin() {  // TODO: Check for possible errors (method isn't named as getIsAdmin)
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }

        Class<?> oEffectiveClass = object instanceof HibernateProxy 
                ? ((HibernateProxy) object).getHibernateLazyInitializer()
                        .getPersistentClass() 
                : object.getClass();                                             
        Class<?> thisEffectiveClass = this instanceof HibernateProxy 
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass() 
                : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) { 
            return false;
        }

        User user = (User) object;

        return this.getId() != null && this.getId() == user.getId();
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy 
                ? ((HibernateProxy) this).getHibernateLazyInitializer()
                        .getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }
}
