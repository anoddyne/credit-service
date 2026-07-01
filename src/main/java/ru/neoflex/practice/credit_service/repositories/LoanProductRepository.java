package ru.neoflex.practice.credit_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.practice.credit_service.models.LoanProduct;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, UUID> {
    List<LoanProduct> findByIsActive(boolean isActive);
}
