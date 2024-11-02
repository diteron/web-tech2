package by.bsuir.homelibrary.entity;

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

    public static class Builder {
        private String login;
        private String passwordHash;
        private String email;
        private boolean isAdmin = false;

        public User build() {
            return new User(this.login, this.passwordHash, this.email, this.isAdmin);
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder isAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }
    }
}
