package com.himanshu.spring.security.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationToken {
    private static final int EXPIRATION_TIME = 10; // 10 minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationTokenId;
    private String token;
    private Date expirationTime;

    @OneToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN")
    )
    private User user;

    public VerificationToken(User user, String token){
        this.user = user;
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token){
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken() {
    }

    private Date calculateExpirationDate(int expirationTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }

    public Long getVerificationTokenId() {
        return this.verificationTokenId;
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpirationTime() {
        return this.expirationTime;
    }

    public User getUser() {
        return this.user;
    }

    public void setVerificationTokenId(Long verificationTokenId) {
        this.verificationTokenId = verificationTokenId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof VerificationToken)) return false;
        final VerificationToken other = (VerificationToken) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$verificationTokenId = this.getVerificationTokenId();
        final Object other$verificationTokenId = other.getVerificationTokenId();
        if (this$verificationTokenId == null ? other$verificationTokenId != null : !this$verificationTokenId.equals(other$verificationTokenId))
            return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        final Object this$expirationTime = this.getExpirationTime();
        final Object other$expirationTime = other.getExpirationTime();
        if (this$expirationTime == null ? other$expirationTime != null : !this$expirationTime.equals(other$expirationTime))
            return false;
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof VerificationToken;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $verificationTokenId = this.getVerificationTokenId();
        result = result * PRIME + ($verificationTokenId == null ? 43 : $verificationTokenId.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        final Object $expirationTime = this.getExpirationTime();
        result = result * PRIME + ($expirationTime == null ? 43 : $expirationTime.hashCode());
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        return result;
    }

    public String toString() {
        return "VerificationToken(verificationTokenId=" + this.getVerificationTokenId() + ", token=" + this.getToken() + ", expirationTime=" + this.getExpirationTime() + ", user=" + this.getUser() + ")";
    }
}
