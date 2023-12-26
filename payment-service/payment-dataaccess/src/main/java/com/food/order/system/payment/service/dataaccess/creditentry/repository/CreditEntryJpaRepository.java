package com.food.order.system.payment.service.dataaccess.creditentry.repository;

import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @Author mselvi
 * @Created 20.12.2023
 */

@Repository
public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {

    /*
    * Trancsation'ları serialize hale getirdik.Aynı anda sadece bir tanesi save and update işlemi yapabilir.
    * PaymentRequestHelper içinde bu method çağrılıyor.2 farklı thread aynı işlemi yaparken tutarsızlık oluşmasın diye
    * pessimistic lock yaptık.Pessimictic lock demek bir işlemi synchorized hale getirmek demek.Aynı anda sadece bir
    * thread işlem yapabilir.
    * */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
