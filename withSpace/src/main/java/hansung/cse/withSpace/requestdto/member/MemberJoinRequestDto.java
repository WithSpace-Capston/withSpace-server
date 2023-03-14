package hansung.cse.withSpace.requestdto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequestDto {

    private String memberName;
    private String email;
    private String password;

}
