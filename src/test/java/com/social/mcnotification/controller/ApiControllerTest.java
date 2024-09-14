package com.social.mcnotification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mcnotification.testContainer.PostgresTestContainerInitializer;
import com.social.mcnotification.security.TestSecurityConfig;
import com.social.mcnotification.dto.EventNotificationDto;
import com.social.mcnotification.dto.NotificationUpdateDto;
import com.social.mcnotification.dto.response.PageNotificationsDto;
import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.security.WithCustomUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-local.yaml")
@ActiveProfiles("local")
@AutoConfigureMockMvc(addFilters = false, webClientEnabled = false)

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
    private final UUID id = UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d7d");
    private final UUID id2 = UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c");
    @Autowired
    private WebApplicationContext context;

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3"));

    @DynamicPropertySource
    static void registryKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    public NotificationSettingEntity getNotificationSettingEntity() {
        NotificationSettingEntity notificationSettingEntity = new NotificationSettingEntity();
        notificationSettingEntity.setId(UUID.fromString("62f41404-c119-4eeb-b57e-b0c527cf1e6c"));
        notificationSettingEntity.setUserId(id2);
        notificationSettingEntity.setEnableLike(true);
        notificationSettingEntity.setEnableSendEmailMessage(false);
        notificationSettingEntity.setEnablePost(true);
        notificationSettingEntity.setEnableFriendBirthday(false);
        notificationSettingEntity.setEnableMessage(false);
        notificationSettingEntity.setEnablePostComment(true);
        return notificationSettingEntity;
    }

    @BeforeEach
    public void fillingData() {
        List<NotificationEntity> notificationEntities = new ArrayList<>();

        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
        notification.setId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d"));
        notification.setAuthorId(UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d"));
        notification.setReceiverId(id);
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
        notification2.setReceiverId(id2);
        notification2.setIsReaded(false);
        notificationEntities.add(notification2);
        notificationRepository.saveAll(notificationEntities);

        NotificationSettingEntity entity = getNotificationSettingEntity();
        notificationSettingRepository.save(entity);
    }

    @AfterEach
    public void clearDataBase() {
        notificationRepository.deleteAll();
        notificationSettingRepository.deleteAll();
    }

    @Test
    @DisplayName("Получение настроек")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void testGetNotificationSettings() throws Exception {
        mockMvc.perform(get("/api/v1/notifications/settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id2.toString()))
                .andExpect(jsonPath("$.enableLike").value(true))
                .andExpect(jsonPath("$.enableSendEmailMessage").value(false))
                .andExpect(jsonPath("$.enablePost").value(true))
                .andExpect(jsonPath("$.enableFriendBirthday").value(false))
                .andExpect(jsonPath("$.enableMessage").value(false))
                .andExpect(jsonPath("$.enablePostComment").value(true))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    @DisplayName("Обновление настроек")
    public void testUpdateNotificationSettings() throws Exception {
        NotificationUpdateDto updateDto = new NotificationUpdateDto(false, NotificationType.FRIEND_BIRTHDAY);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/v1/notifications/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        NotificationSettingEntity setting = notificationSettingRepository.findByUserId(id2);
        Assertions.assertFalse(setting.isEnableFriendBirthday());
    }

    @Test
    @DisplayName("Прочитать все")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void testMarkAllEventsAsRead() throws Exception {
        mockMvc.perform(put("/api/v1/notifications/readed"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        List<NotificationEntity> notification = notificationRepository.findByReceiverId(id2);
        assertTrue(notification.isEmpty());
    }

    @Test
    @DisplayName("Создание настроек")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void testCreateNotificationSettings() throws Exception {
        mockMvc.perform(post("/api/v1/notifications/settings{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Получение оповещений")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void getNotifications() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/notifications/page")
                        .header("Authorization", "Bearer token")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "sentTime,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String json = response.getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        PageNotificationsDto pageNotificationsDto = objectMapper.readValue(json, PageNotificationsDto.class);
        assertEquals(1, pageNotificationsDto.getContent().length);
    }

    @Test
    @DisplayName("Создание нотификации")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void createNotification() throws Exception {
        EventNotificationDto eventNotificationDto = new EventNotificationDto(
                UUID.fromString("aa69a222-5452-41f8-86e9-0d7631621d6d"),
                UUID.fromString("aa69a222-5452-41f8-86e9-0d7631621d6d"),
                NotificationType.MESSAGE,
                "null");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(eventNotificationDto);

        mockMvc.perform(post("/api/v1/notifications/add", eventNotificationDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение счетчика событий")
    @WithCustomUser(email = "testuser@example.com", roles = {"USER"}, userId = "62f41404-c119-4eeb-b57e-b0c527cf1e6c")
    public void getEventsCount() throws Exception {
        mockMvc.perform(get("/api/v1/notifications/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{\"data\": {\"count\": 1}}"));
//                .andExpect(content().json("{\"timestamp\": \"" + timestamp + "\", \"data\": {\"count\": 1}}"));
    }
}
