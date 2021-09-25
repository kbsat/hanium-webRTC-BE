package hanium.videoMeeting.DTO.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class PageResult<T> extends Result{
    private long totalCount;
    private long nowPage;
    private List<T> data;
}
