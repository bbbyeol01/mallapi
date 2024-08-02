package com.zerock.mallapi.contoller;

import com.zerock.mallapi.dto.PageRequestDTO;
import com.zerock.mallapi.dto.PageResponseDTO;
import com.zerock.mallapi.dto.ProductDTO;
import com.zerock.mallapi.service.ProductService;
import com.zerock.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    // 파일 등록
    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {
        log.info("register : {}", productDTO);

        List<MultipartFile> files = productDTO.getFiles();

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        Long pno = productService.register(productDTO);

        log.info("uploadFileNames : {}", uploadFileNames);
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
////            throw new RuntimeException(e);
//            e.printStackTrace();
//        }

        return Map.of("RESULT", pno);
    }

    // 파일 불러오기
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }


    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("pageRequestDTO : {}", pageRequestDTO);

        return productService.getList(pageRequestDTO);

    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDTO productDTO) {

        // productDTO 수정하려는 정보가 들어있는 DTO
        // oldProductDTO 기존(데이터베이스에 있던) DTO

        productDTO.setPno(pno);
        ProductDTO oldProductDTO = productService.get(pno);

        // product 는 이미지 파일 이름 목록을 가지고 있음
        // 1. 새로 업로드 해야 하는 파일을 fileUtil 로 저장함 -> 반환값: 파일 이름 목록
        // 2. 수정 되지 않은 파일(유지된 파일) 이름 목록과, 새로 저장된 이름 목록이 새 목록이 됨.

        // 기존 파일 이름(DB에 있는 파일 이름)
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        // 새로 업로드 해야 하는 파일
        List<MultipartFile> files = productDTO.getFiles();

        // 새로 업로드 된 파일 이름 목록
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // productDTO(새로 입력 받은 수정된 정보)는 새로운 파일 이름과 유지된 기존 파일 이름을 가지고 있음
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            // 파일 이름 목록(List)에 새 파일 이름 목록을 넣음
            // addAll: List 끼리 더할 때 중복 되지 않는 요소만 추가함
            uploadFileNames.addAll(currentUploadFileNames);
        }

        // 이름, 정보, 가격, 파일 이름 목록 수정
        productService.modify(productDTO);


        if (oldFileNames != null && !oldFileNames.isEmpty()) {

            // 지워야 하는 파일 이름 목록(기존 DB에 있던 파일 이름 목록)
            // 기존 파일 이름 목록에서 새로운 파일 이름 목록에 없는 이름만 남김
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> !uploadFileNames.contains(fileName)).toList();

            fileUtil.deleteFiles(removeFiles);
        }

        // {"RESULT" : "SUCCESS"} JSON 형태로 결과 반환
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        // 지워야 할 파일 이름 목록
        List<String> uploadFileNames = productService.get(pno).getUploadFileNames();

        // product DB 에서 상품 삭제(Soft delete -> delFlag = true)
        productService.remove(pno);

        // 파일 전부 삭제
        fileUtil.deleteFiles(uploadFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}
