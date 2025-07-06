package com.example.demo.repository;

import com.example.demo.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Wallet entities.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, DynamicWalletRepository {
    Page<Wallet> findWalletByCustomer_Id(Long customerId, Pageable pageable);

    boolean existsByCustomer_IdOrCustomer_UserId(Long custmerId, Long userId);
}
