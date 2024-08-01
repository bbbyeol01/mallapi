package com.zerock.mallapi.service;

import com.zerock.mallapi.domain.Product;
import com.zerock.mallapi.domain.ProductImage;
import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import com.zerock.mallapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        log.info("getList.................");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> objects = productRepository.selectList(pageable);

        List<ProductDTO> dtoList = objects.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .name(product.getName())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;

        }).collect(Collectors.toList());

        long totalCount = objects.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        Product save = productRepository.save(product);

        return save.getPno();
    }

    @Override
    public ProductDTO get(Long pno) {

        Product product = productRepository.selectOne(pno).orElseThrow();

        return entityToDto(product);
    }

    @Override
    public void modify(ProductDTO productDTO) {
        Optional<Product> byId = productRepository.findById(productDTO.getPno());
        Product product = byId.orElseThrow();

        // 이름, 정보, 가격 변경
        product.changeName(productDTO.getName());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());

        // 업로드 파일 이름 리스트 변경
        product.clearList();

        // 수정될 파일 이름 목록
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            // 이름 하나씩 저장함
            uploadFileNames.forEach(product::addImageString);
        }

        productRepository.save(product);

    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }

    private ProductDTO entityToDto(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .price(product.getPrice())
                .pdesc(product.getPdesc())
                .name(product.getName())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(ProductImage::getFileName).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;

    }

    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .pdesc(productDTO.getPdesc())
                .build();

        // 이미지
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames == null) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);

        return product;
    }
}
