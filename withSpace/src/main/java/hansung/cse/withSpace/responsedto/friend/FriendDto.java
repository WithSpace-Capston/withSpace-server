package hansung.cse.withSpace.responsedto.friend;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import lombok.Data;

@Data
public class FriendDto {

    private Long id;
    private String name;

    private String email;

    private Boolean status;

    public FriendDto(Member friend) {
        id = friend.getId();
        name = friend.getMemberName();
        email = friend.getEmail();
        status = friend.getStatus();
    }

    public FriendDto(FriendShip friendShip) { // MemberDto에서 사용
        Member friend = friendShip.getFriend();
        id = friend.getId();
        name = friend.getMemberName();
        email = friend.getEmail();
    }
}
