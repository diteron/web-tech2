package by.bsuir.homelibrary.entity;

/**
 * Represents a user with login credentials, email, and admin status.
 * <p>
 * The {@code User} class provides methods to get and set a user's login,
 * hashed password, email address, and admin status.
 * </p>
 */
public class User {
    private String login;
    private String passwordHash;
    private String email;
    private boolean isAdmin = false;

    public User() {

    }

    public User(String login, String passwordHash, String email, boolean isAdmin) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
    
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        
        return result;
    }

    /**
     * Checks if this user is equal to another object.
     * <p>
     * Two users are considered equal if they have the same login name.
     * </p>
     *
     * @param obj the object to compare with this user
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
            
        User other = (User) obj;

        if (login == null && other.login != null)
            return false;
        else if (!this.login.equals(other.login))
            return false;

        return true;
    }
}
