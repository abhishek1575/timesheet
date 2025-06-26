package timesheet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timesheet.dto.SheetDto;
import timesheet.entity.Sheet;
import timesheet.repository.SheetRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SheetService {

    @Autowired
    private SheetRepository sheetRepository;

    @Transactional
    public List<SheetDto> getListOfSheetById(Long id){
        List<Sheet> sheetList = sheetRepository.getListOfSheetById(id);
        return sheetList.stream().map(this::mapToSheetDto)
                .collect(Collectors.toList());
    }

    private SheetDto mapToSheetDto(Sheet sheet) {
        return SheetDto.builder()
                .id(sheet.getId())
                .taskName(sheet.getTaskName())
                .startDate(sheet.getStartDate())
                .endDate(sheet.getEndDate())
                .effort(sheet.getEffort())
                .project(sheet.getProject())
                .assignee(sheet.getAssignee())
                .approver(sheet.getApprover())
                .status(sheet.getStatus())
                .userId(sheet.getUser().getId())
                .build();
    }



}
