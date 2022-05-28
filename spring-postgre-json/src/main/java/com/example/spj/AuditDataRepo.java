/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.spj;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AnuragSingh
 */
@Repository
public interface AuditDataRepo extends JpaRepository<AuditData, Long> {

    @Query(value = "SELECT a.* FROM author a "
            + "WHERE CAST(a.book ->> 'price' AS INTEGER) = ?1",
            nativeQuery = true)
    public AuditData findByBookPriceNativeQueryCast(int price);

    @Query(value = "SELECT ad.* FROM audit_data ad "
            + "WHERE ad.table_data ->> 'isbn' = ?1",
            nativeQuery = true)
    public AuditData findByBookIsbnNativeQuery(String isbn);
}
