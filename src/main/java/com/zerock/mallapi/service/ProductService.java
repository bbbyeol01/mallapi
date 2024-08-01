package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Product;
import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);
}
