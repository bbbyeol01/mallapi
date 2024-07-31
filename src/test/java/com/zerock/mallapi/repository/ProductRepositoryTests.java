package com.zerock.mallapi.repository;

import com.zerock.mallapi.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품 등록")
    public void testInsert() throws Exception {
        //given
        for(int i = 0; i < 10; i++){
            Product product = Product.builder()
                    .name("상품" + i)
                    .price(100 * i)
                    .pdesc("상품설명 " + i)
                    .build();

        //when
            product.addImageString(UUID.randomUUID().toString() + "_" + "Image1.jpg");
            product.addImageString(UUID.randomUUID().toString() + "_" + "Image2.jpg");

        //then
            productRepository.save(product);
            log.info("---------------------");

        }
    }

    @Test
    @DisplayName("상품 읽기")
    @Transactional
    public void testRead() throws Exception {
        //given
        Long pno = 1L;

        //when
        Optional<Product> byId = productRepository.findById(pno);
        Product product = byId.orElseThrow();

        //then
        log.info("product : {}", product);
        log.info("----------------------");
        log.info("product.getImageList : {}", product.getImageList());

    }

    @Test
    @DisplayName("상품 읽기2")
    @Transactional
    public void testRead2() throws Exception {

        //given
        Long pno = 1L;

        //when
        Optional<Product> byId = productRepository.findById(pno);
        Product product = byId.orElseThrow();

        //then
        log.info("product : {}", product);
        log.info("---------------------");
        log.info("product.getImageList : {}", product.getImageList());

    }

    @Test
    @DisplayName("상품 삭제")
    @Transactional
    @Commit
    public void testDelete() throws Exception {
        //given
        Long pno = 2L;

        //when
        // 삭제
        productRepository.updateToDelete(pno, true);

        //then

    }

    @Test
    @DisplayName("상품 수정")
    public void testUpdate() throws Exception {

        //given
        Long pno = 10L;

        Product product = productRepository.selectOne(pno).orElseThrow();

        //when
        product.changeName("10번 상품");
        product.changePrice(10000);
        product.changeDesc("10번 상품 설명입니다.");

        product.clearList();

        product.addImageString(UUID.randomUUID().toString() + "NEWIMAGE1");
        product.addImageString(UUID.randomUUID().toString() + "NEWIMAGE2");
        product.addImageString(UUID.randomUUID().toString() + "NEWIMAGE3");

        productRepository.save(product);


        //then
        Product result = productRepository.selectOne(pno).orElseThrow();
        log.info("result : {}", result);

    }


    // 상품 목록 조회(이미지는 첫번째 등록된 하나씩만 포함되어야 함)
    @Test
    @DisplayName("상품 목록(페이징)")
    public void testList() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());


        //when
        Page<Object[]> objects = productRepository.selectList(pageable);

        //then
        objects.getContent().forEach(arr -> log.info(Arrays.toString(arr)));

    }
}
