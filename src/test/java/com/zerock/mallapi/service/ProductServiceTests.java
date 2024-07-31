package com.zerock.mallapi.service;

import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Slf4j
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("상품 목록")
    public void testList() throws Exception {
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build(); // 1, 10

        //when
        PageResponseDTO<ProductDTO> list = productService.getList(pageRequestDTO);

        //then
        for (ProductDTO productDTO : list.getDtoList()) {
            log.info("productDTO : {}", productDTO);
        }

    }
}
