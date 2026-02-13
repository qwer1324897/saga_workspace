package com.ch.sagaproduct.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// 호스트 안에 저장된 엑셀을 분석하여, 이미지가 발견되면 호스트의 지정된 디렉토리에 복사
// 이미지가 아닌 일반 텍스트 정보는 추출하여 (Mysql 에 넣는 건데, 지금은 db 연동 안 하고 생략)

@Slf4j
@Component
public class ExcelParser {

    // 엑셀의 위치를 넘겨받아, 해당 엑셀을 분석하여야 함. 따라서 매개변수로 엑셀의 풀 경로를 받아야 함.
    public void parseAndSaveImages(String excelPath) throws Exception {
        // 파일이 존재하는지 체크
        Path excelFile = Path.of(excelPath);
        if(!Files.exists(excelFile) || Files.isDirectory(excelFile)) { // 파일이 존재하지 않거나, 혹은 존재하더라도 파일 형태가 아닌 디렉토리라면 에러 던지기
            throw new IllegalArgumentException("엑셀 파일이 없거나, 경로가 잘못되었는데요?" + excelFile.toAbsolutePath());
        }

        // 엑셀 파일이 들어있는 부모 디렉토리 경로 뽑아내기. (동적으로 하위에 원하는 디렉토리를 생성하기 위해)
        Path uploadDir = excelFile.getParent();

        // 엑셀 파일을 열어서 분석 시작.
        // try with resource 문. catch 와 DB, 스트림(IO) 등에서 필수적으로 처리하는 finally 를 메서드로 처리하여 코드를 줄여쓰는 방법.
        try(FileInputStream fileInputStream = new FileInputStream(excelFile.toFile());
            // 97~2003 구 엑셀 .xls  << HSSWorkbook 으로 접근하고, 2003 이후 .xlsx 의 경우 XSSFWorkbook 으로 접근

            // 엑셀 파일 열기.
            XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(fileInputStream)
        ){
            // 엑셀의 가장 첫 번째 시트(0번째) 를 선택
            XSSFSheet sheet = wb.getSheetAt(0);

            // POI 는 이미지를 도형의 일부로 생각함. 즉, 도형(Shape)이 모든 그려지는 요소의 최상위 개념.
            XSSFDrawing drawing = sheet.getDrawingPatriarch();

            // 만약 시트에 그림이 하나도 없으면 메세지 남기기
            if(drawing == null) {
                log.debug("엑셀 시트에 이미지가 하나도 없는데요?");
                return;
            }

            // 시트에 있는 모든 Shape 을 하나씩 꺼내서 검사하자.
            for(XSSFShape shape :drawing.getShapes()) {
                if(shape instanceof XSSFPicture picture) {  // 이미지형 이라면, 인스턴스를 받아줌
                    // 그림의 위치를 찾고, 그 그림이 속한 행들의 텍스트도 추출.
                    XSSFClientAnchor anchor = (XSSFClientAnchor) picture.getAnchor();

                    // 발견된 이미지가 몇 번째 행에 존재하는지도 알아보자
                    int rowIndex = anchor.getRow1();
                    XSSFRow row = sheet.getRow(rowIndex); // 이미지의 행을 시트에 물어봐서 얻어옴.
                    String categoryId = getCellValue(row.getCell(0));   // 엑셀은 셀 안에 숫자를 넣어도 전부 String 으로 처리 가능.
                    String productName = getCellValue(row.getCell(2));  // 상품명
                    String brand = getCellValue(row.getCell(3));
                    String price = getCellValue(row.getCell(4));

                    log.debug("subcategory ID: {}, product_name: {}, brand: {}, price: {}", categoryId,productName,brand,price);

                    // 이미지를 꺼내서, subcategory_id 의 숫자값을 조합한 디렉토리를 생성하여 이미지명 또한 img...n 형태로 저장
                    // ex) product2/img_현재시간.jpg
                    String dirName = "product" + categoryId;    // product2, product3

                    // 저장할 디렉토리의 상위 디렉토리(엑셀이 있던 디렉토리)의 경로를 가져오기
                    Path productDir = uploadDir.resolve(dirName);
                    Files.createDirectories(productDir);

                    // 이미지명 + 현재시간 + .기존확장자
                    XSSFPictureData pictureData = picture.getPictureData();   // 이미지 정보를 얻기
                    String extension = pictureData.suggestFileExtension();  // 이미지 파일의 확장자 얻기 extension(확장)

                    // 파일명 생성. Path 객체에는 위의 productDir(경로) 까지 포함되어 있음.
                    String imgName = "img" + System.currentTimeMillis() + "." + extension;
                    Path out = productDir.resolve(imgName);

                    // 파일 생성. (이미 같은 이미지가 존재할 경우엔 덮어씀) 실제 하드디스크에 이미지 생성.
                    Files.write(out,pictureData.getData(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                    // (경로+파일명 Path객체, byteData, 생성옵션, 옵션)

                    log.debug("엑셀에서 추출하여 저장한 이미지는 {}", imgName);
                }
            }

        }
    }

    // 엑셀의 각 칸에 들어있는 데이터 꺼내기.
    //  POI 는 각 셀에 들어있는 ㄷ이터의 종류별로 타입을 구분하는데, 데이터를 접근 할때는 반드시
    // 타입에 맞는 메서드를 호출해야 하므로, 조건문을 좀 더 깔끔하게 처리하기 위해, 아래의 메서드를 정의.
    private String getCellValue(XSSFCell cell) {
        if(cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();    //셀이 들어있는 데이터가 문자열일 경우 getStringCellValue()
            case NUMERIC -> String.valueOf((long)cell.getNumericCellValue());   // 숫자인 경우
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }


}
