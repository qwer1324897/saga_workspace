package com.ch.sagaproduct.controller;

import com.ch.sagaproduct.util.ExcelParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ExcelParser excelParser;

    // 엑셀 파일로 상품 등록 요청 처리
    @PostMapping("/excel")
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) throws Exception {  // 프론트에서 파라미터명을 file 로 해놨기 때문에 file 로 해야함.

        /*ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
         엑셀 저장 처리(엑셀 안에 이미지가 있기 때문에, 엑셀을 서버의 저장소에 먼저 저장해 놓아야 함.
        ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
        Path uploadDir = Path.of("C:/tmp/saga-product/upload"); // 그냥 String 보다 경로 처리등에 있어서 편함.
        // 윈도우만 쓰는 \ 역슬래쉬 쓰지 말고 맥, 리눅스 다 통합되는 슬래쉬 쓰자~ 어차피 경로가 C드라이브라 의미없긴 한데 그래도~
        Path savePath = uploadDir.resolve(file.getOriginalFilename()); // 저장할 장소 + 저장될 파일명 조합

        try {
            file.transferTo(savePath.toFile());  // 이 시점에 메모리에 존재하던 엑셀파일정보가, 하드디스크에 저장되는 순간

        /*ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
         엑셀 분석 및 이미지 저장 호출
        ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/
            excelParser.parseAndSaveImages(savePath.toString());  // Path 를 경로 문자열로 변환(풀경로)


            return ResponseEntity.ok(Map.of(
                    "msg", "ok"
            ));
        } finally {
            log.debug("삭제결과: {}", Files.deleteIfExists(savePath));
        }
    }


}
