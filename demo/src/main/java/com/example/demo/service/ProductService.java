package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.parameter.ProductQueryParameter;
import com.example.demo.repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 業務邏輯層
 */
@Service
public class ProductService {
    @Autowired
    private MockProductDAO productDAO;

    /**
     * 新增
     * @param request
     * @return
     */
    public Product createProduct(Product request){
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
        if (isIdDuplicated){
            throw new UnprocessableEntityException("The id of product is duplicated!");
        }
        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    /**
     * 查詢單筆產品
     * @param id
     * @return
     */
    public Product getProduct(String id){
        return productDAO.find(id).orElseThrow(()->new NotFoundException("Can't find product!"));
    }

    /**
     * 修改產品
     * @param id
     * @param request
     * @return
     */
    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    /**
     * 刪除產品
     * @param id
     */
    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    /**
     * 查詢所有產品
     * @param param
     * @return
     */
    public List<Product> getProducts(ProductQueryParameter param) {
        return productDAO.find(param);
    }
}
