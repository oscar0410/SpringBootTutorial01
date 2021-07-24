package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.parameter.ProductQueryParameter;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 資料持久層
 */
@Repository
public class MockProductDAO {
    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct
    private void initDB() {
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
    }

    /**
     * 新增
     * @param product
     * @return
     */
    public Product insert(Product product){
        productDB.add(product);
        return product;
    }

    /**
     * 修改
     * @param id
     * @param product
     * @return
     */
    public Product replace(String id, Product product){
        Optional<Product> productOp = find(id);
        productOp.ifPresent(p->{
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });
        return product;
    }

    /**
     * 刪除
     * @param id
     */
    public void delete(String id){
        productDB.removeIf(product -> product.getId().equals(id));
    }

    /**
     * 查詢單筆
     * @param id
     * @return
     */
    public Optional<Product> find(String id){
        return productDB.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    /**
     * 查詢多筆
     * @param param
     * @return
     */
    public List<Product> find(ProductQueryParameter param) {
        String keyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();
        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);

        return productDB.stream()
                .filter(p -> p.getName().contains(keyword))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * 依造規則進行排序
     * @param orderBy
     * @param sortRule
     * @return
     */
    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return comparator;
        }

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }

        return sortRule.equalsIgnoreCase("desc")
                ? comparator.reversed()
                : comparator;
    }
}
