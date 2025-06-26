package timesheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import timesheet.dto.SheetDto;
import timesheet.entity.Sheet;
import timesheet.repository.SheetRepository;
import timesheet.service.SheetService;

import java.util.List;

@RestController
@RequestMapping("/sheet")
public class SheetController {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private SheetRepository sheetRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addTimeSheet(@RequestBody Sheet sheet){
        try{
            sheetRepository.save(sheet);
            return ResponseEntity.ok("TimeSheet added Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error");
        }
    }

    @GetMapping("/getAllSheet")
    public ResponseEntity<?> getAllTimeSheetByUserId(Long id){
        try {
            List<SheetDto> sheetDtos=sheetService.getListOfSheetById(id);
            if (sheetDtos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Record Found");
            } else {
                return ResponseEntity.ok(sheetDtos);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error");
        }
    }

}
