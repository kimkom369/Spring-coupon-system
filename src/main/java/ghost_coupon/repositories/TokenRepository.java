package ghost_coupon.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ghost_coupon.entities.Token;
import ghost_coupon.enums.ClientType;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    //----------- find by token ----------\\
    Optional<Token> findByToken(String token);
    
    //--------------------- find clienType & token  ----------------------------\\
    Optional<Token> findByClientTypeAndToken(ClientType clientType, String token);
    
    //------------------ delete all by clientType & userID ----------------\\
    @Modifying
    @Transactional
    void deleteAllByClientTypeAndUserId(ClientType clientType, long userId);
    
    //------------------ delete expired tokens ----------------\\
    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expDate < CURRENT_DATE")
    void deleteExpiredTokens();
}
