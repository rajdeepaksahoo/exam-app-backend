package com.online.exam.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.exam.dto.ExcelQuestionAndAnswerDto;
import com.online.exam.dto.QuestionAndAnswerBulkUploadResponse;
import com.online.exam.exception.QAppException;
import com.online.exam.model.*;
import com.online.exam.repository.QuestionRepository;
import com.online.exam.repository.UploadedFileRepository;
import com.online.exam.repository.UserModelRepository;
import com.online.exam.service.QuestionAndAnswerBulkUploadFileService;
import com.online.exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionAndAnswerBulkUploadFileServiceImpl implements QuestionAndAnswerBulkUploadFileService {

    private final QuestionService questionService;
    private final UserModelRepository userModelRepository;
    private final UploadedFileRepository uploadedFileRepository;
    private final QuestionRepository questionRepository;
    @Override
    public QuestionAndAnswerBulkUploadResponse uploadBulkQuestionAndAnswer(MultipartFile multipartFile, boolean saveData,QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest) {
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse = new QuestionAndAnswerBulkUploadResponse();
        InputStream inputStream = null;
        UploadedFile uploadedFileDetails=null;
        //      Validate Uploaded File
        if(!saveData) {
            try {
                inputStream=multipartFile.getInputStream();
            } catch (IOException e) {
                throw new QAppException(e.getMessage());
            }
            validateExcel(multipartFile);
            try {
                questionAndAnswerBulkUploadResponse.setUploadedFileName(multipartFile.getOriginalFilename());
                uploadedFileDetails = new UploadedFile();
                uploadedFileDetails.setFileName(multipartFile.getOriginalFilename());
                uploadedFileDetails.setFileType(multipartFile.getContentType());
                uploadedFileDetails.setValidEntry(multipartFile.getBytes());
                uploadedFileDetails.setCreatedOn(new Date());
                uploadedFileDetails.setModifiedOn(new Date());
                String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                Optional<UserModel> userModel = userModelRepository.findByUsername(userName);
                uploadedFileDetails.setCreatedBy(userModel.isPresent()?userModel.get().getUserId():-1);
                uploadedFileDetails.setModifiedBy(userModel.isPresent()?userModel.get().getUserId():-1);
                UploadedFile savedFile = uploadedFileRepository.save(uploadedFileDetails);
                questionAndAnswerBulkUploadResponse.setOriginalFileUrl(savedFile.getId().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            //explicitly setting id to 3
//            questionAndAnswerBulkUploadRequest.setOriginalFileUrl(String.valueOf(3));
            String originalFileUrl = questionAndAnswerBulkUploadRequest.getOriginalFileUrl();
            Optional<UploadedFile> uploadedFile = uploadedFileRepository.findById(Long.parseLong(originalFileUrl));
            if(uploadedFile.isPresent()){
                inputStream = new ByteArrayInputStream(uploadedFile.get().getValidEntry());
                getExcelQuestionAndAnswerDto(inputStream,saveData,uploadedFile.get().getId());
            }else {
                throw new QAppException("Unable To Find File. Please Upload File Again.");
            }
        }

        ExcelQuestionAndAnswerDto excelQuestionAndAnswerDto = getExcelQuestionAndAnswerDto(inputStream,saveData,uploadedFileDetails!=null?uploadedFileDetails.getId():null);
        QuestionAndAnswerBulkUploadResponse bulkUploadResponse = excelQuestionAndAnswerDto.getQuestionAndAnswerBulkUploadResponse();
        if(bulkUploadResponse!=null){
            questionAndAnswerBulkUploadResponse.setNumberOfValidEntries(bulkUploadResponse.getNumberOfValidEntries());
            questionAndAnswerBulkUploadResponse.setNumberOfInvalidEntries(bulkUploadResponse.getNumberOfInvalidEntries());
            questionAndAnswerBulkUploadResponse.setErrorFileUrl(bulkUploadResponse.getErrorFileUrl());
        }
        return questionAndAnswerBulkUploadResponse;
    }

    @Override
    public UploadedFile getUploadedFile(QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest) {
        if(questionAndAnswerBulkUploadRequest==null || questionAndAnswerBulkUploadRequest.getOriginalFileUrl()==null){
            throw new QAppException("Invalid Uploaded File Url.");
        }
        Optional<UploadedFile> uploadedFile = uploadedFileRepository.findById(Long.parseLong(questionAndAnswerBulkUploadRequest.getOriginalFileUrl()));
        return uploadedFile.isPresent()?uploadedFile.get():null;
    }

    private void validateExcel(MultipartFile file){
        if(file==null){
            throw new QAppException("Invalid File Format. Please Upload A Valid Excel File");
        }
        List<String> validFileFormats = List.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-excel");
        if(! validFileFormats.contains(file.getContentType())){
            throw new QAppException("Invalid File Format. Please Upload A Valid Excel File");
        }
        List<String> headers = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row:sheet){
                int i = 0;
                while ((row.getCell(i))!=null){
                    headers.add(row.getCell(i).getStringCellValue());
                    i++;
                }
                break;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                log.info("Uploaded File Headers Are {}:: ",objectMapper.writeValueAsString(headers));
            }catch (Exception e){

            }
            if(headers.size()==0){
                throw new QAppException("Invalid Excel File");
            }
        } catch (IOException e) {
            throw new QAppException(e.getMessage());
        }

    }

//    Get Question And Answer List From Uploaded File
    private ExcelQuestionAndAnswerDto getExcelQuestionAndAnswerDto(InputStream multipartFile, boolean saveData, Long uploadedFileId) {
        ExcelQuestionAndAnswerDto excelQuestionAndAnswerDto = new ExcelQuestionAndAnswerDto();
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse= new QuestionAndAnswerBulkUploadResponse();
        List<Questions> questionsList = new ArrayList<>();
        try {
//            Created WorkBook
            Workbook workbook = new XSSFWorkbook(multipartFile);
//            Get Seet From WorkBook
            Sheet sheet = workbook.getSheetAt(0);
//            Iterate the sheet and get all rows
            boolean isFirstRow = true;
            long validEntries=0l;
            Long invalidEntries=0l;
            Workbook wb = new XSSFWorkbook();
            // Create red cell style
            CellStyle redCellStyle = wb.createCellStyle();
            redCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            redCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Sheet wbSheet = wb.createSheet("error_file_details");
            int rowNo=0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Row row : sheet){
                boolean isValidRow = true;
                String question=null;
                String domain=null;
                String option1=null;
                String option2=null;
                String option3=null;
                String option4=null;
                String answer=null;
                String errorReason=null;
                Cell errorCell;
                Cell cell;
                Row wbSheetRow = null;
                if (!isFirstRow){
                    cell = row.getCell(0);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Question.";
                    }else {
                        question=cell.getStringCellValue();
                    }

                    cell = row.getCell(1);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Domain.";
                    }else{
                        domain=cell.getStringCellValue();
                    }

                    cell = row.getCell(2);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Option 1.";
                    }else {
                        option1=cell.getStringCellValue();
                    }

                    cell = row.getCell(3);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Option 2.";
                    }else {
                        option2=cell.getStringCellValue();
                    }


                    cell = row.getCell(4);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Option 3.";
                    }else {
                        option3=cell.getStringCellValue();
                    }

                    cell = row.getCell(5);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Option 4.";
                    }else {
                        option4=cell.getStringCellValue();
                    }


                    cell = row.getCell(6);
                    if(cell==null || cell.getStringCellValue()==null || cell.getStringCellValue().equals("")){
                        isValidRow = false;
                        errorReason = "Invalid Answer.";
                    }else {
                        answer=cell.getStringCellValue();
                    }

                    if (isValidRow) {
                        validEntries++;
                    }else {
                        invalidEntries++;
                        wbSheetRow = wbSheet.createRow(invalidEntries.intValue());
                        errorCell = wbSheetRow.createCell(0);
                        errorCell.setCellValue( question);
                        if(question==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(1);
                        errorCell.setCellValue( domain);
                        if(domain==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(2);
                        errorCell.setCellValue( option1);
                        if(option1==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(3);
                        errorCell.setCellValue( option2);
                        if(option2==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(4);
                        errorCell.setCellValue( option3);
                        if(option3==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(5);
                        errorCell.setCellValue( option4);
                        if(option4==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(6);
                        errorCell.setCellValue( answer);
                        if(answer==null){
                            errorCell.setCellStyle(redCellStyle);
                        }
                        errorCell = wbSheetRow.createCell(7);
                        errorCell.setCellValue(errorReason);
                    }
                    if (isValidRow){

                        Questions questions = new Questions();
                        questions.setQuestion(question);
                        questions.setQuestionStream(domain);
                        Options options = new Options();
                        options.setOption1(option1);
                        options.setOption2(option2);
                        options.setOption3(option3);
                        options.setOption4(option4);
                        questions.setOptions(options);
                        Answer ans = new Answer();
                        ans.setAnswer(answer);
                        questions.setAnswer(ans);
                        ans.setQuestion(questions);
                        options.setQuestion(questions);
                        questionsList.add(questions);
                    }
                }
                if (isFirstRow) {
                    Font boldFont = wb.createFont();
                    boldFont.setBold(true);

                    // Create a cell style and set the bold font
                    CellStyle boldStyle = wb.createCellStyle();
                    boldStyle.setFont(boldFont);

                    wbSheetRow = wbSheet.createRow(rowNo);
                    errorCell = wbSheetRow.createCell(0);
                    errorCell.setCellValue(row.getCell(0).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(1);
                    errorCell.setCellValue(row.getCell(1).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(2);
                    errorCell.setCellValue(row.getCell(2).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(3);
                    errorCell.setCellValue(row.getCell(3).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(4);
                    errorCell.setCellValue(row.getCell(4).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(5);
                    errorCell.setCellValue(row.getCell(5).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(6);
                    errorCell.setCellValue(row.getCell(6).getStringCellValue());
                    errorCell.setCellStyle(boldStyle);
                    errorCell = wbSheetRow.createCell(7);
                    errorCell.setCellValue("Remarks");
                    errorCell.setCellStyle(boldStyle);
                    isFirstRow = false;
                }
                rowNo++;
            }
            QuestionAndAnswerBulkUploadResponse checkDuplicateEntries = checkDuplicateEntries(questionsList, wbSheet, invalidEntries.intValue(), validEntries, invalidEntries,redCellStyle);
            invalidEntries=checkDuplicateEntries.getNumberOfInvalidEntries();
            if(invalidEntries>0){
                wb.write(baos);
//                uploadedFileId=3l;
                Optional<UploadedFile> uploadedFileOptional = uploadedFileRepository.findById(uploadedFileId);
                if(uploadedFileOptional.isPresent()){
                    UploadedFile uploadedFile = uploadedFileOptional.get();
                    uploadedFile.setCreatedOn(new Date());
                    uploadedFile.setModifiedOn(new Date());
                    questionAndAnswerBulkUploadResponse.setErrorFileUrl(uploadedFileId+"");
                    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                    Optional<UserModel> userModel = userModelRepository.findByUsername(userName);
                    uploadedFile.setInvalidEntry(baos.toByteArray());
                    uploadedFile.setCreatedBy(userModel.isPresent()?userModel.get().getUserId():-1);
                    uploadedFile.setModifiedBy(userModel.isPresent()?userModel.get().getUserId():-1);
                    UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);
                    questionAndAnswerBulkUploadResponse.setOriginalFileUrl(savedFile.getId().toString());
                }

            }
            questionAndAnswerBulkUploadResponse.setNumberOfValidEntries((long)questionsList.size());
            questionAndAnswerBulkUploadResponse.setNumberOfInvalidEntries(invalidEntries);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
                excelQuestionAndAnswerDto.setQuestionsList(questionsList);
                excelQuestionAndAnswerDto.setQuestionAndAnswerBulkUploadResponse(questionAndAnswerBulkUploadResponse);
                if(saveData)
                    questionService.addAllQuestion(questionsList);
        }
        return excelQuestionAndAnswerDto;
    }

    private QuestionAndAnswerBulkUploadResponse checkDuplicateEntries(List<Questions> questionsList, Sheet sheet, int rowNo, long validEntries, Long invalidEntries,CellStyle redCellStyle) {
        List<String> listOfQuestions = questionsList.stream().map(Questions::getQuestion).map(String::toLowerCase).collect(Collectors.toList());
        Map<String, Questions> questionsMap = questionsList.stream().collect(Collectors.toMap(q->q.getQuestion().toLowerCase(), Function.identity(),(i,j)->j));
        List<Questions> questionsAvailableInDb = questionRepository.findByQuestionIn(listOfQuestions);
        Map<String, Questions> questionsMapFromDb = questionsAvailableInDb.stream().collect(Collectors.toMap(q->q.getQuestion().toLowerCase().trim(), Function.identity(),(i,j)->j));
        List<Integer> removeList = new ArrayList<>();
        Set<String> checkForFirstQuestion = new HashSet<>();
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse = new QuestionAndAnswerBulkUploadResponse();

        Iterator<Questions> iterator = questionsList.iterator();

        while (iterator.hasNext()) {
            Questions question = iterator.next();
            String q = question.getQuestion().toLowerCase().trim();

            if (questionsMapFromDb.containsKey(q)) {
                rowNo++;
                addInvalidRowToSheet(sheet, question, rowNo,"Duplicate Entry. Already Exists In Record.",redCellStyle);
                invalidEntries++;
                iterator.remove(); // safely remove from list
            } else if (questionsMap.containsKey(q) && !checkForFirstQuestion.add(q)) {
                rowNo++;
                addInvalidRowToSheet(sheet, question, rowNo,"Duplicate Entry. You Have Added Duplicate Records In The Given File.",redCellStyle);
                invalidEntries++;
                iterator.remove(); // safely remove from list
            }
        }
//        if(removeList.size()>0) {
//            questionsList.removeAll(removeList);
//        }

        questionAndAnswerBulkUploadResponse.setNumberOfInvalidEntries(invalidEntries);

        return questionAndAnswerBulkUploadResponse;
    }

    private void addInvalidRowToSheet(Sheet sheet,Questions question, int rowNo,String remark,CellStyle redCellStyle){
        String domain = question.getQuestionStream();
        Answer answer = question.getAnswer();
        Options options = question.getOptions();
        Row row = sheet.createRow(rowNo);
        Cell errorCell = row.createCell(rowNo);
        sheet.setColumnWidth(0, 15000); // ~58 characters
        errorCell = row.createCell(0);
        errorCell.setCellValue(question.getQuestion());
        errorCell = row.createCell(1);
        errorCell.setCellValue(domain);

        errorCell = row.createCell(2);
        errorCell.setCellValue(options.getOption1());

        errorCell = row.createCell(3);
        errorCell.setCellValue(options.getOption2());

        errorCell = row.createCell(4);
        errorCell.setCellValue(options.getOption3());

        errorCell = row.createCell(5);
        errorCell.setCellValue(options.getOption4());

        errorCell = row.createCell(6);
        errorCell.setCellValue(answer.getAnswer());

        errorCell = row.createCell(7);
        errorCell.setCellValue(remark);
        sheet.setColumnWidth(7, 15000);
        errorCell.setCellStyle(redCellStyle);
    }
}
