package hansung.cse.withSpace.domain.chat;

import hansung.cse.withSpace.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room")
    private Room room;

    private Long senderId; //메세지를 보낸 멤버의 id
    private String senderName;

    private String content; //메세지 내용

    private LocalDateTime sendTime; //보낸 시간

    public Message(Member member, Room room, String content) {
        this.room = room;
        this.senderId = member.getId();
        this.senderName = member.getMemberName();
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }

}
