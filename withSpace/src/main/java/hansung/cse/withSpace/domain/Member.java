package hansung.cse.withSpace.domain;


import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.space.MemberSpace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {   //회원

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberSpace memberSpace;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<FriendShip> friendRequester = new ArrayList<>(); //친구 신청한 사람

    @OneToMany(mappedBy = "friend", cascade = CascadeType.REMOVE)
    private List<FriendShip> friendReceiver = new ArrayList<>();//친구 신청 받은 사람

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<MemberRoom> memberRooms = new ArrayList<>(); // 일대일 개인채팅방

    // 일대일 개인채팅방
//    @OneToMany(mappedBy = "member1")
//    private List<Room> roomList1 = new ArrayList<>(); //참여자1
//    @OneToMany(mappedBy = "member2")
//    private List<Room> roomList2 = new ArrayList<>(); //참여자2

    @Column(unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String memberName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    //연관관계 편의 메소드//
//    public void makeMemberSpace(MemberSpace memberSpace) {
//        this.memberSpace = memberSpace;
//        memberSpace.setMember(this);
//    }

    public Member(UUID uuid, String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.uuid = uuid;

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        this.status = false;

        //스페이스 생성
        MemberSpace memberSpace = new MemberSpace(this);
        this.memberSpace = memberSpace;


    }

    public void setMemberSpace() {

    }

    public void update(String email, String password, String memberName) {
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (memberName != null) {
            this.memberName = memberName;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void changeUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}