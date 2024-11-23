package by.bsuir.hotelwebapp.entity;

import org.hibernate.proxy.HibernateProxy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String username;

    @Column(length = 128, nullable = false) // TODO: Check length of hash
    private String passwordHash;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean isAdmin;

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

    public Boolean getIsAdmin() {
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
