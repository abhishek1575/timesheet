package timesheet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import timesheet.entity.User;
import java.util.Date;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetDto {
    private Long id;
    private String taskName;
    private Date startDate;
    private Date endDate;
    private Double effort;
    private String project;
    private String assignee;
    private String approver;
    private String status;
    private Long userId; // instead of full user object
}

