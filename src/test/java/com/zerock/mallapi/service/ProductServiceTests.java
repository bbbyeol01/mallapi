package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Product;
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

import java.util.List;
import java.util.UUID;

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

    @Test
    @DisplayName("상품 등록")
    public void register() throws Exception {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("새로운 상품")
                .price(10000)
                .pdesc("상품 설명")
                .build();

        //when
        productDTO.setUploadFileNames(List.of(UUID.randomUUID() + "_" + "TEST1.jpg", UUID.randomUUID() + "_" + "TEST2.jpg"));
        productService.register(productDTO);

        //then

    }

    @Test
    @DisplayName("상품 조회")
    public void testRead() throws Exception {
        //given
        Long pno = 12L;

        //when
        ProductDTO productDTO = productService.get(pno);

        //then
        log.info("productDTO : {}", productDTO);
        log.info("productDTO.getUploadFileNames : {}", productDTO.getUploadFileNames());

    }
}
