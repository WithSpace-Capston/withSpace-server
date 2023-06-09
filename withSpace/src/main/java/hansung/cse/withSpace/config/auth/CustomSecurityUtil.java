//package hansung.cse.withSpace.config.auth;
//
//
//import hansung.cse.withSpace.domain.Member;
//import hansung.cse.withSpace.domain.MemberTeam;
//import hansung.cse.withSpace.domain.Team;
//import hansung.cse.withSpace.domain.chat.Room;
//import hansung.cse.withSpace.domain.space.Page;
//import hansung.cse.withSpace.domain.space.Space;
//import hansung.cse.withSpace.domain.space.schedule.Category;
//import hansung.cse.withSpace.domain.space.schedule.Schedule;
//import hansung.cse.withSpace.domain.space.schedule.ToDo;
//import hansung.cse.withSpace.service.*;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//
///**
// *
// *  현재 인증된 사용자의 정보를 가져오고, 이 정보를 사용하여 권한 검사를 수행하는 클래스
// */
//@Component
//@RequiredArgsConstructor
//public class CustomSecurityUtil{
//
//    /**
//     * 권한 제어 전에 해당 객체가 먼저 존재하는건지 확인하기
//     */
//
//    private final TeamService teamService;
//    private final MemberService memberService;
//    private final SpaceService spaceService;
//    private final PageService pageService;
//    //private final BlockService blockService;
//    private final ScheduleService scheduleService;
//    private final CategoryService categoryService;
//    private final ToDoService toDoService;
//    private final RoomService roomService;
//
////    private final JwtTokenUtil tokenUtil;
////
////    @Value("${jwt.secret}")
////    private String secret;
////
////    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
////        String token = JwtTokenExtractor.extract(request);
////        if (token != null && tokenUtil.validateToken(token)) {
////            Claims claims = Jwts.parserBuilder()
////                    .setSigningKey(tokenUtil.getSigningKey())
////                    .build()
////                    .parseClaimsJws(token)
////                    .getBody();
////
////            String uuid = claims.get("UUID", String.class);
////            return uuid;
////        }
////        return null;
////    }
////
//
//
//    /**
//     * 현재 인증된 사용자의 정보를 가져오는 메소드
//     * 스프링 시큐리티 기반인데 JWT를 통해 인증하는 쪽으로 바꿔서 사용 안 할 듯
//     */
//    public Authentication getAuthentication() {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }
//
//
//
//    public boolean isMemberOwner(Long memberId) { //memberId를 가지고 본인소유인지 파악
//
//
//
//        memberService.findOne(memberId); //회원 먼저 확인
//
//        Authentication authentication = getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//
//
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long currentUserId = Long.valueOf(userDetails.getId());
//            return currentUserId.equals(memberId); // 현재 사용자의 ID와 요청한 ID가 일치하는지 확인
//        }
//        return false; // 인증 정보가 없는 경우 false 반환
//    }
//
//    public boolean isTeamHost(Long teamId) { //팀장인지 확인
//
//        teamService.findOne(teamId); //팀이 있는지 먼저 확인
//
//        Authentication authentication = getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//
//
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long currentUserId = Long.valueOf(userDetails.getId()); //현재 로그인한 유저의 id
//
//            Team team = teamService.findOne(teamId);
//
//            return currentUserId.equals(team.getHost());
//        }
//        return false; // 인증 정보가 없는 경우 false 반환
//    }
//
//    public boolean isMemberInTeam(Long teamId) { //팀에 가입되었는지 확인
//
//        teamService.findOne(teamId); //팀이 있는지 먼저 확인
//
//        Authentication authentication = getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long currentUserId = Long.valueOf(userDetails.getId()); //현재 로그인한 유저의 id
//
//            Member member = memberService.findOne(currentUserId);
//
//            boolean isMemberOfTeam = member.getMemberTeams().stream()
//                    .map(MemberTeam::getTeam)
//                    .anyMatch(teams -> teams.getId().equals(teamId));
//
//            return isMemberOfTeam; //가입되어있는지 확인
//        }
//        return false; // 인증 정보가 없는 경우 false 반환
//    }
//
//    public boolean isSpaceOwner(Long spaceId) { //본인 스페이스인지 확인
//
//        spaceService.findOne(spaceId); //먼저 스페이스가 있는지 확인해줌
//
//        Authentication authentication = getAuthentication();
//
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long currentUserId = Long.valueOf(userDetails.getId()); //현재 로그인한 유저의 id
//
//            Member member = memberService.findOne(currentUserId);
//
//            boolean memberSpaceOwner = member.getMemberSpace().getId().equals(spaceId); //본인의 스페이스인지 확인
//
//            boolean teamSpaceOwner = member.getMemberTeams().stream()// 가입된 팀의 스페이스인지 확인
//                    .map(MemberTeam::getTeam)
//                    .filter(Objects::nonNull)
//                    .map(Team::getTeamSpace)
//                    .filter(Objects::nonNull)
//                    .anyMatch(teamSpace -> teamSpace.getId().equals(spaceId));
//
//            return (memberSpaceOwner || teamSpaceOwner); //가입되어있는지 확인
//        }
//        return false; // 인증 정보가 없는 경우 false 반환
//    }
//
//    public boolean isPageOwner(Long pageId) {
//        //페이지의 스페이스가 null일수도 있음
//        Page page = pageService.findOne(pageId);
//        Optional<Space> optionalSpace = Optional.ofNullable(page.getSpace());
//        Long spaceId = null;
//        if (optionalSpace.isPresent()) {
//            spaceId = pageService.findOne(pageId).getSpace().getId();
//        }
//        else{ //페이지를 쓰레기통에 넣은경우
//            spaceId = page.getTrashCan().getSpace().getId();
//        }
//
//        return isSpaceOwner(spaceId);
//    }
//
//    public boolean isBlockOwner(Long blockId) {
//        //Long spaceId = blockService.findOne(blockId).getPage().getSpace().getId();
//        //return isSpaceOwner(spaceId);
//        return false;
//    }
//
//    public boolean isScheduleOwner(Long scheduleId) {
//        Schedule schedule = scheduleService.findSchedule(scheduleId);
//        Long spaceId = schedule.getSpace().getId();
//        return isSpaceOwner(spaceId);
//    }
//
//    public boolean isCategoryOwner(Long categoryId) {
//        Category category = categoryService.findCategory(categoryId);
//        return isScheduleOwner(category.getSchedule().getId());
//    }
//
//    public boolean isToDoOwner(Long toDoId) {
//        ToDo toDo = toDoService.findToDo(toDoId);
//        return isCategoryOwner(toDo.getCategory().getId());
//    }
//
//    public boolean isRoomOwner(Long roomId) { //채팅방 접근권 확인
//        Room room = roomService.findOne(roomId);
//        return isSpaceOwner(room.getSpace().getId());
//    }
//}
