package nl.brianvermeer.workshop.coffee.repository;

import nl.brianvermeer.workshop.coffee.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@Repository
public class SearchRepository {

    @Autowired
    EntityManager em;

    @Autowired
    DataSource dataSource;

    public List<Product> searchProduct(String input) {
        String sql = "Select * from Product where lower(description) like CONCAT('%', ?1, '%') OR lower(product_name) like CONCAT('%', ?2, '%')";
        Query query = em.createNativeQuery(sql, Product.class);
        query.setParameter(1, input.toLowerCase(Locale.ROOT));
        query.setParameter(2, input.toLowerCase(Locale.ROOT));
        return (List<Product>) query.getResultList();
    }

}
