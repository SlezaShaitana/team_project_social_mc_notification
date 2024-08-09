package com.social.mcnotification.client;

import com.social.mcnotification.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "mc-friend", url = "${friend.service.url}" + "/api/v1/friends") // ?
public interface FriendClient {

//    @PostMapping("/subscribe/{id}")
//    public ResponseEntity<FriendShortDto> subscribeToFriend(@PathVariable("id") String id);
//
//    @GetMapping
//    public ResponseEntity<PageFriendShortDto> getFriendList(FriendSearchDto searchDto, Pageable page);
//
//    @GetMapping("/{id}")
//    public ResponseEntity<FriendShortDto> getFriendshipNote(@PathVariable("id") UUID uuid);
//
//
//    @GetMapping("/{status}")
//    public ResponseEntity<List<UUID>> getFriendsIdList(@PathVariable("status") StatusCode status);

//
//    @GetMapping("/friendId")
//    public ResponseEntity<List<UUID>> getAllFriendsIdList();

    @GetMapping("/friendId/{id}")
    public ResponseEntity<List<UUID>> getFriendsIdListByUserId(@PathVariable("id") UUID uuid);

//    @GetMapping("/blockFriendId")
//    public ResponseEntity<List<UUID>> getFriendsWhoBlockedUser();

}
