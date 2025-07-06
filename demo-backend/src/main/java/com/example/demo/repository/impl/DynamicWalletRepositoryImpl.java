package com.example.demo.repository.impl;

import com.example.demo.dto.wallet.WalletSearchRequest;
import com.example.demo.dto.PageRequestInfo;
import com.example.demo.entity.Wallet;
import com.example.demo.repository.DynamicWalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link DynamicWalletRepository} for searching wallets dynamically based on various criteria.
 */
@Repository
@Transactional
public class DynamicWalletRepositoryImpl implements DynamicWalletRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Searches for wallets based on the given {@link WalletSearchRequest} and pagination information.
     *
     * @param request        the search criteria for wallets
     * @return a paginated list of wallet entities matching the criteria
     */
    @Override
    public Page<Wallet> searchWallets(WalletSearchRequest request) {
        final PageRequestInfo pageRequestInfo = request.pageRequestInfo();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Wallet> query = criteriaBuilder.createQuery(Wallet.class);
        final Root<Wallet> from = query.from(Wallet.class);

        final List<Predicate> predicates = buildPredicates(criteriaBuilder, from, request);

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new)));
        }

        final List<Wallet> resultList = entityManager.createQuery(query)
                .setFirstResult( pageRequestInfo.page())
                .setMaxResults( pageRequestInfo.size()).getResultList();

        return new PageImpl<>(resultList, pageRequestInfo.getPageRequest(), getQueryCount(criteriaBuilder, request));
    }

    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<Wallet> from, WalletSearchRequest request) {
        List<Predicate> predicates = new ArrayList<>();
        Optional.ofNullable(request.name()).ifPresent(name -> predicates.add(criteriaBuilder.like(from.get("name"), name + "%")));
        Optional.ofNullable(request.customerId()).ifPresent(customerId -> predicates.add(criteriaBuilder.equal(from.get("customer").get("id"), customerId)));
        Optional.ofNullable(request.currency()).ifPresent(currency -> predicates.add(criteriaBuilder.equal(from.get("currency"), currency.name())));
        Optional.ofNullable(request.activeForShopping()).ifPresent(activeForShopping -> predicates.add(criteriaBuilder.equal(from.get("activeForShopping"), activeForShopping)));
        Optional.ofNullable(request.activeForWithdraw()).ifPresent(activeForWithdraw -> predicates.add(criteriaBuilder.equal(from.get("activeForWithdraw"), activeForWithdraw)));
        Optional.ofNullable(request.balanceLessThan()).ifPresent(balanceLowerThan -> predicates.add(criteriaBuilder.lessThan(from.get("balance"), balanceLowerThan)));
        Optional.ofNullable(request.balanceGreaterThan()).ifPresent(balanceGreaterThan -> predicates.add(criteriaBuilder.greaterThan(from.get("balance"), balanceGreaterThan)));
        return predicates;
    }


    private Long getQueryCount(CriteriaBuilder criteriaBuilder, WalletSearchRequest request) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Wallet> countRoot = countQuery.from(Wallet.class);

        List<Predicate> countPredicates = buildPredicates(criteriaBuilder, countRoot, request);

        countQuery.select(criteriaBuilder.count(countRoot));
        if (!countPredicates.isEmpty()) {
            countQuery.where(criteriaBuilder.and(countPredicates.toArray(Predicate[]::new)));
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}