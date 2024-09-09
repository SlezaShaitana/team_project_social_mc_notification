package com.social.mcnotification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mcnotification.PostgresTestContainerInitializer;
import com.social.mcnotification.TestSecurityConfig;
import com.social.mcnotification.dto.Count;
import com.social.mcnotification.dto.EventNotificationDto;
import com.social.mcnotification.dto.NotificationCountDto;
import com.social.mcnotification.dto.response.PageNotificationsDto;
import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.services.NotificationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.social.mcnotification.PostgresTestContainerInitializer.postgres;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Import(TestSecurityConfig.class)
public class ApiControllerTest extends PostgresTestContainerInitializer {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationSettingRepository notificationSettingRepository;
    @LocalServerPort
    private Integer port;
    @Autowired
    private final TestRestTemplate template = new TestRestTemplate();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private NotificationService notificationService;

    private final UUID id = UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d7d");
    private final UUID id2 = UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c");

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void fillingData() {
        List<NotificationEntity> notificationEntities = new ArrayList<>();

        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
        notification.setId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d"));
        notification.setAuthorId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d"));
        notification.setReceiverId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d7d"));
        notification.setContent("Hello");
        notification.setIsReaded(false);
        notification.setServiceName(MicroServiceName.ACCOUNT);
        notification.setSentTime(Timestamp.valueOf(java.time.LocalDateTime.now()));
        notificationEntities.add(notification);

        NotificationEntity notification2 = new NotificationEntity();
        notification2.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
        notification2.setId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d7d"));
        notification2.setSentTime(Timestamp.valueOf(LocalDateTime.now()));
        notification2.setAuthorId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d7d"));
        notification2.setReceiverId(UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c"));
        notification2.setIsReaded(false);
        notificationEntities.add(notification2);
        notificationRepository.saveAll(notificationEntities);

        NotificationSettingEntity notificationSettingEntity = new NotificationSettingEntity();
        notificationSettingEntity.setId(UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c"));
        notificationSettingEntity.setUserId(UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c"));
        notificationSettingEntity.setEnableLike(true);
        notificationSettingEntity.setEnableSendEmailMessage(false);
        notificationSettingEntity.setEnablePost(true);
        notificationSettingEntity.setEnableFriendBirthday(false);
        notificationSettingEntity.setEnableMessage(false);
        notificationSettingEntity.setEnablePostComment(true);
        notificationSettingRepository.save(notificationSettingEntity);
    }

    @AfterEach
    public void clearDataBase() {
        notificationRepository.deleteAll();
        notificationSettingRepository.deleteAll();
    }

    @Test
    //	@WithMockUser
    @DisplayName("Http-ответ со статусом 'ok'")
    public void testGetNotificationSettings() {
        ResponseEntity<NotificationSettingEntity> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", NotificationSettingEntity.class);
        assertTrue(response.getStatusCode().is2xxSuccessful()); //status code 200?
        assertEquals(id2, response.getBody().getUserId());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Http-ответ со статусом 'ok'")
    public void testUpdateNotificationSettings() throws Exception {
        mockMvc.perform(get("/api/v1/notifications/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id2.toString()));
    }

    @Test
    @DisplayName("Http-ответ со статусом 'ok'")
    public void testMarkAllEventsAsRead() throws Exception {
        mockMvc.perform(put("/api/v1/notifications/readed"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Http-ответ со статусом 'ok'")
    public void testCreateNotificationSettings() throws Exception {
        ResponseEntity<Boolean> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", Boolean.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void getNotifications() throws Exception {
        ResponseEntity<PageNotificationsDto> response = template.getRestTemplate()
                .getForEntity("http://localhost:" + port + "/api/v1/notifications/notifications", PageNotificationsDto.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    @DisplayName("Http-ответ со статусом 'ok'")
    public void createNotification() throws Exception {
        EventNotificationDto eventNotificationDto = new EventNotificationDto(
                UUID.fromString("aa69a222-5452-41f8-86e9-0d7631621d6d"),
                UUID.fromString("aa69a222-5452-41f8-86e9-0d7631621d6d"),
                NotificationType.MESSAGE,
                "null");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(eventNotificationDto);

        mockMvc.perform(post("/add", eventNotificationDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void getEventsCount() throws Exception {
        Count count = new Count(1);
        NotificationCountDto notificationCountDto = new NotificationCountDto(LocalDateTime.now(), count);

        when(notificationService.getEventsCount()).thenReturn(notificationCountDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(notificationCountDto);

        mockMvc.perform(get("/api/v1/notifications/count"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(json));

    }
}
