package com.theironyard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Erik on 6/22/16.
 */

public interface PurchaseRepository extends PagingAndSortingRepository<Purchase, Integer> {
    Page <Purchase> findByCategory(Pageable pageable, String category);
    Page <Purchase> findAll(Pageable pageable);

}
