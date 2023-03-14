package hansung.cse.withSpace.responsedto.space;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.responsedto.space.page.PageDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberSpaceDto {

    private Long spaceId;
    private List<PageDto> pageList = new ArrayList<>();

//    public MemberSpaceDto(Member member) {
//        this.id = member.getMemberSpace().getId();
//        this.pageList = member.getMemberSpace().getPageList().stream()
//                .map(page -> new PageDto(page))
//                .collect(Collectors.toList());
//    }

    public MemberSpaceDto(Member member) {
        this.spaceId = member.getMemberSpace().getId();
        this.pageList = member.getMemberSpace().getPageList().stream()
                .filter(page -> page.getParentPage() == null)
                .map(page -> new PageDto(page))
                .collect(Collectors.toList());
    }
}
