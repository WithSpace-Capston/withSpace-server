package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.MemberTeam;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.chat.RoomNotFoundException;
import hansung.cse.withSpace.exception.friend.NotFriendException;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.repository.FriendShipRepository;
import hansung.cse.withSpace.repository.member.MemberRepository;
import hansung.cse.withSpace.repository.RoomRepository;
import hansung.cse.withSpace.responsedto.chat.GetRoomResponseDto;
import hansung.cse.withSpace.responsedto.chat.MemberRoomResponseDto;
import hansung.cse.withSpace.responsedto.chat.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberService memberService;
    private final FriendShipRepository friendShipRepository;


    public Room findOne(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    public List<MemberRoomResponseDto> findMembersRoom(Long memberId) {
        List<Room> personalChatRooms = roomRepository.findByMember1IdOrMember2Id(memberId, memberId);

        Member member = memberService.findOne(memberId);
        List<MemberTeam> memberTeams = member.getMemberTeams();
        List<Room> teamChatRooms = memberTeams.stream()
                .map(MemberTeam::getTeam)
                .flatMap(team -> team.getTeamSpace().getRoomList().stream())
                .collect(Collectors.toList());


        List<Room> allChatRooms = new ArrayList<>();
        allChatRooms.addAll(personalChatRooms);
        allChatRooms.addAll(teamChatRooms);

        List<MemberRoomResponseDto> memberRoomDtos = allChatRooms.stream()
                .map(MemberRoomResponseDto::new)
                .collect(Collectors.toList());

        return memberRoomDtos;
    }

    @Transactional
    public void removeRoom(Long roomId) {
        Room room = findOne(roomId);
        roomRepository.delete(room);
    }


//    public void isFriend(Long memberId, Long friendId) {
//        //서로 친구관계인지 확인
//        FriendShip friendShip1 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendId, FriendStatus.ACCEPTED);
//        FriendShip friendShip2 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(friendId, memberId, FriendStatus.ACCEPTED);
//
//        boolean isFriend = (friendShip1 != null && friendShip2 != null);
//
//        if(!isFriend){
//            throw new NotFriendException("두 회원이 서로 친구가 아닙니다.");
//        }
//
//    }

    @Transactional
    public Long makePersonalChattingRoom(Member member, Member friend) { // 개인 채팅방 생성
        String title = member.getMemberName() + "님과 " + friend.getMemberName() + "님의 개인 채팅방";
        Room room = new Room(title, member, friend);
        roomRepository.save(room);
        //roomRepository.findByIdWithMembers(room.getId());
        return room.getId();
    }
    @Transactional
    public Long makeTeamChattingRoom(Space space, String roomName) { // 팀 채팅방 생성
        Room room = new Room(roomName, space);
        roomRepository.save(room);
        return room.getId();
    }

    @Transactional
    public void removeFriendRoom(Long memberId, Long friendId) {
        Room room = roomRepository.findByMember1IdAndMember2Id(memberId, friendId);
        Room room2 = roomRepository.findByMember2IdAndMember1Id(memberId, friendId);
        if (room != null) {
            roomRepository.delete(room);
        } else if(room2 != null){
            roomRepository.delete(room2);
        }
    }


//    @Transactional
//    public void makeFriendChattingRoom(Long myRoomId, Long friendRoomId) { //서로 친구 채팅방 연결
//        Room myRoom = roomRepository.findById(myRoomId)
//                .orElseThrow(() -> new RoomNotFoundException("내 채팅방을 찾을 수 없습니다."));
//        Room friendRoom = roomRepository.findById(friendRoomId)
//                .orElseThrow(() -> new RoomNotFoundException("친구의 채팅방을 찾을 수 없습니다."));
//        myRoom.setFriendRoomId(friendRoomId);
//        friendRoom.setFriendRoomId(myRoomId);
//    }


}
