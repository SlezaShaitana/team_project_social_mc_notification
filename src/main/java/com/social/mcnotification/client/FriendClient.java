package com.social.mcnotification.client;

import com.social.mcnotification.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@FeignClient(name = "mc-friend", url = "${friend.service.url}" + "/api/v1/friends") //
@FeignClient(name = "mc-friend", url = "http://79.174.80.200:8090/api/v1/friends", configuration = FeignClientConfiguration.class)
public interface FriendClient {

//    @GetMapping
//    public ResponseEntity<PageFriendShortDto> getFriendList(FriendSearchDto searchDto, Pageable page);

//    @GetMapping("/friendId")
//    public ResponseEntity<List<UUID>> getAllFriendsIdList();

    @GetMapping("/friendId/{id}")
    public ResponseEntity<List<UUID>> getFriendsIdListByUserId(@PathVariable("id") UUID uuid);

    @GetMapping("/blockFriendId")
    public ResponseEntity<List<UUID>> getFriendsWhoBlockedUser();

}
