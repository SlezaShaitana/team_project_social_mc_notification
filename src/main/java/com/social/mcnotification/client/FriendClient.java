package com.social.mcnotification.client;

import com.social.mcnotification.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "mc-friend", url = "${friend.service.url}" + "/api/v1/friends") // ?
public interface FriendClient {
    @PutMapping("/{id}/approve")
    public ResponseEntity<FriendShortDto> confirmFriendRequest(@PathVariable("id") String id);

    @PutMapping("/unblock/{id}")
    public ResponseEntity<FriendShortDto> unblockFriend(@PathVariable("id") String id);

    @PutMapping("/block/{id}")
    public ResponseEntity<FriendShortDto> blockFriend(@PathVariable("id") String id);

    @PostMapping("/{id}/request")
    public ResponseEntity<FriendShortDto> createFriendRequest(@PathVariable("id") String id);

    @PostMapping("/subscribe/{id}")
    public ResponseEntity<FriendShortDto> subscribeToFriend(@PathVariable("id") String id);

    @GetMapping
    public ResponseEntity<PageFriendShortDto> getFriendList(FriendSearchDto searchDto, Pageable page);

    @GetMapping("/{id}")
    public ResponseEntity<FriendShortDto> getFriendshipNote(@PathVariable("id") UUID uuid);

    @DeleteMapping("/{id}")
    public void deleteFriend(@PathVariable("id") UUID uuid);

    @GetMapping("/{status}")
    public ResponseEntity<List<UUID>> getFriendsIdList(StatusCode status);

    @GetMapping("/recommendations")
    public ResponseEntity<List<FriendShortDto>> getRecommendations(FriendSearchDto searchDto);

    @GetMapping("/friendId")
    public ResponseEntity<List<UUID>> getAllFriendsIdList();

    @GetMapping("/friendId/{id}")
    public ResponseEntity<List<UUID>> getFriendsIdListByUserId(@PathVariable("id") UUID uuid);

    @GetMapping("/count")
    public ResponseEntity<Integer> getFriendRequestCount();

    @GetMapping("/check")
    public ResponseEntity<List<StatusCode>> getStatuses(List<UUID> ids);

    @GetMapping("/blockFriendId")
    public ResponseEntity<List<UUID>> getFriendsWhoBlockedUser();

}
