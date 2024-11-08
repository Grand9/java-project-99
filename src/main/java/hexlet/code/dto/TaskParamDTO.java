package hexlet.code.dto;

import lombok.Data;

@Data
public class TaskParamDTO {
    private Long assigneeId;
    private String titleCont;
    private String status;
    private Long labelId;
}
