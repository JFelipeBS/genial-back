package com.genial.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.genial.demo.DTO.ProductDto;
import com.genial.demo.entity.Storage;

import com.genial.demo.entity.Product;
import com.genial.demo.repositories.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private StorageService storageService;
  
    public ProductDto findByName(String Name){
        Product product = repository.findByName(Name);
        ProductDto dto = mapper.map(product, ProductDto.class);
        return dto;
    }

    public List<ProductDto> findAll(String Name){
        List<ProductDto> list = new ArrayList<ProductDto>();
        Storage storage = storageService.findByProduct(Name);
        List<Product> storageProduct = storage.getProductList();

        for (Product product : storageProduct) {
            ProductDto partial = mapper.map(product, ProductDto.class);
            list.add(partial);
        }

        return list;
    }

    @Transactional(propagation=Propagation.REQUIRED,readOnly=false)
    public ProductDto save(Product dto){
        Product product = new Product();
        product = mapper.map(dto,Product.class);
        return mapper.map(repository.save(product), ProductDto.class);
    }

    @Transactional(propagation=Propagation.REQUIRED,readOnly=false)
    public void delete (Product dto){
        repository.deleteByName(dto.getName());
    }
    

    @Transactional(propagation=Propagation.REQUIRED, readOnly=false)
    public ProductDto update(String name, ProductDto dto) {
        Product product = repository.findByName(name);
        if (product == null) {
          return null;
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setQuantidade(dto.getQuantidade());
        product.setSector(dto.getSector());
        product.setValue(dto.getValue());
    
        return mapper.map(repository.save(product), ProductDto.class);
    }

}
