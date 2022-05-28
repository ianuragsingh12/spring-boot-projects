package com.example.spj;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AnuragSingh
 */
@Service
public class AuditService {

    @Autowired
    private AuditDataRepo auditDataRepo;

    @Autowired
    private BookRepo br;

    private final ObjectMapper om;

    {
        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
    }

    public void loadData() {
        Book book = new Book();
        book.setIsbn("123-abc");
        book.setTitle("this is test");
        book.setPrice(1000);

        br.save(book);
        System.out.println("loaded initial data.");
    }

    public void copyData() {

        Book book = br.findByIsbn("123-abc");
        System.out.println("found in original table: " + book);

        AuditData ad = new AuditData();
        ad.setTableName("book");
        ad.setTableData(book);
        ad.setDeletedDate(ZonedDateTime.now());

        auditDataRepo.save(ad);
        System.out.println("saved in audit_data");

        br.delete(book);
        System.out.println("deleted from original table");
    }

    public void restoreData() throws JsonProcessingException {
        AuditData ad = auditDataRepo.findByBookIsbnNativeQuery("123-abc");
        System.out.println("found in audit_data: " + ad);

        String json = om.writeValueAsString(ad.getTableData());
        Book book = om.readValue(json, Book.class);
        br.save(book);
        System.out.println("restored in original table");

        auditDataRepo.delete(ad);
        System.out.println("deleted from audit_data");
    }
}
