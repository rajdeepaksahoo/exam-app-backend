package com.online.exam.controller.bulkupload;

import com.online.exam.dto.QuestionAndAnswerBulkUploadResponse;
import com.online.exam.model.UploadedFile;
import com.online.exam.service.QuestionAndAnswerBulkUploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class QuestionAndAnswerBulkUploadController {
    private final QuestionAndAnswerBulkUploadFileService questionAndAnswerBulkUploadFileService;
    @PostMapping(value = "/validate",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionAndAnswerBulkUploadResponse> questionAndAnswerBulkValidateFile(@RequestParam("file")MultipartFile file){
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse = questionAndAnswerBulkUploadFileService.uploadBulkQuestionAndAnswer(file, false,null);
        return new ResponseEntity<>(questionAndAnswerBulkUploadResponse,HttpStatus.OK);
    }
    @PostMapping(value = "/upload")
    public ResponseEntity<QuestionAndAnswerBulkUploadResponse> questionAndAnswerBulkUploadFile(@RequestBody QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest){
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadResponse = questionAndAnswerBulkUploadFileService.uploadBulkQuestionAndAnswer(null, true,questionAndAnswerBulkUploadRequest);
        return new ResponseEntity<>(questionAndAnswerBulkUploadResponse,HttpStatus.OK);
    }

    @PostMapping("/getUploadedFile")
    public ResponseEntity<byte[]> downloadUploadedFile(@RequestBody QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest){
        UploadedFile uploadedFile = questionAndAnswerBulkUploadFileService.getUploadedFile(questionAndAnswerBulkUploadRequest); // fetch from DB

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uploadedFile.getFileName())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(uploadedFile.getValidEntry());
    }

    @GetMapping("/getErrorFile/{errorFileUrl}")
    public ResponseEntity<byte[]> downloadErrorFile(@PathVariable("errorFileUrl") String errorFileUrl) {
        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest = new QuestionAndAnswerBulkUploadResponse();
        questionAndAnswerBulkUploadRequest.setOriginalFileUrl(errorFileUrl);
        UploadedFile uploadedFile = questionAndAnswerBulkUploadFileService.getUploadedFile(questionAndAnswerBulkUploadRequest); // fetch from DB

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uploadedFile.getFileName())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(uploadedFile.getInvalidEntry());
    }

//    @GetMapping("/getUploadedFile")
//    public ResponseEntity<byte[]> downloadUploadedFile1(){
//        QuestionAndAnswerBulkUploadResponse questionAndAnswerBulkUploadRequest  = new QuestionAndAnswerBulkUploadResponse();
//        questionAndAnswerBulkUploadRequest.setOriginalFileUrl(3+"");
//        UploadedFile uploadedFile = questionAndAnswerBulkUploadFileService.getUploadedFile(questionAndAnswerBulkUploadRequest); // fetch from DB
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + uploadedFile.getFileName())
//                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                .body(uploadedFile.getData());
//    }

//    @GetMapping("/getUploadedFileHistory")
//    public ResponseEntity<List<QuestionAndAnswerBulkUploadResponse>> downloadUploadedFile1(){
//
//    }
}


