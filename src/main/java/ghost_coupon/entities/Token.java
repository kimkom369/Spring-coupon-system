package ghost_coupon.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import ghost_coupon.enums.ClientType;



@Entity
public class Token implements Serializable, Comparable<Token> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expDate;

    @Column(nullable = false)
    private String token;

    public Token() {
    }

    public Token(long userId , ClientType clientType,
                Date expDate , String token) {
        this.userId = userId;
        this.clientType = clientType;
        this.expDate = expDate;
        this.token = token;
    }

    public Token(ClientType clientType , Date expDate,
                String token) {
        this.clientType = clientType;
        this.expDate = expDate;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int compareTo(Token o) {
        return Long.compare(this.id, o.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", userId=" + userId +
                ", clientType=" + clientType +
                ", expDate=" + expDate +
                ", token='" + token + '\'' +
                '}';
    }
}
