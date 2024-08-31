//package com.social.mcnotification;
//
//
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.social.mcnotification.controller.ApiController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.social.mcnotification.dto.*;
//import com.social.mcnotification.enums.MicroServiceName;
//import com.social.mcnotification.enums.NotificationType;
//import com.social.mcnotification.model.NotificationEntity;
//import com.social.mcnotification.model.NotificationSettingEntity;
//import com.social.mcnotification.repository.NotificationRepository;
//import com.social.mcnotification.repository.NotificationSettingRepository;
//import org.joda.time.LocalDateTime;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.data.domain.Page;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.web.bind.annotation.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.get;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@WebMvcTest(ApiController.class)
//class McNotificationApplicationTests {
//
//
//	@LocalServerPort
//	private Integer port;
//
//	@Autowired
//	private NotificationRepository notificationRepository;
//
//	@Autowired
//	private NotificationSettingRepository notificationSettingRepository;
//
//	private final TestRestTemplate template = new TestRestTemplate();
//
//	private final UUID id = UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d");
//
//	public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
//
//	@BeforeAll
//	public static void beforeAll() {
//		postgres.start();
//	}
//
//	@AfterAll
//	public static void afterAll() {
//		postgres.stop();
//	}
//
//
//	@DynamicPropertySource
//	public static void configureProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", postgres::getJdbcUrl);
//		registry.add("spring.datasource.username", postgres::getUsername);
//		registry.add("spring.datasorce.password", postgres::getPassword);
//	}
//
//	@BeforeEach
//	public void fillingData() {
//		List<NotificationEntity> notificationEntities = new ArrayList<>();
//		List<NotificationSettingEntity> notificationSettingEntities = new ArrayList<>();
//
//        for (int i = 1; i < 4; i++) {
//			NotificationEntity notification = new NotificationEntity();
//			notification.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
//
//			if (i <= 2) {
//				notification.setReceiverId(id);
//			}
//
//			notification.setAuthorId(UUID.randomUUID());
//			notification.setReceiverId(UUID.randomUUID());
//			notification.setContent("У друга день рождение! ");
//			notification.setIsReaded(false);
//			notification.setServiceName(MicroServiceName.ACCOUNT);
//			String date = LocalDateTime.now().toString();
//			notification.setSentTime(Timestamp.valueOf(date));
//			notificationEntities.add(notification);
//			notificationRepository.saveAll(notificationEntities);
//        }
//
//		NotificationSettingEntity notificationSettingEntity = new NotificationSettingEntity();
//		notificationSettingEntity.setUserId(id);
//		notificationSettingEntity.setId(UUID.randomUUID());
//		notificationSettingEntity.setEnableLike(true);
//		notificationSettingEntity.setEnableSendEmailMessage(false);
//		notificationSettingEntity.setEnablePost(true);
//		notificationSettingEntity.setEnableFriendBirthday(false);
//		notificationSettingEntity.setEnableMessage(false);
//		notificationSettingEntity.setEnablePostComment(true);
//		notificationSettingEntities.add(notificationSettingEntity);
//		notificationSettingRepository.saveAll(notificationSettingEntities);
//
//	}
//
//	@AfterEach
//	public void clearDataBase() {
//		notificationRepository.deleteAll();
//		notificationSettingRepository.deleteAll();
//	}
//
//
//
//	@Test
//	void contextLoads() {
//	}
//
//
//
//
//	@Test
//	@WithMockUser
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testGetNotificationSettings() {
//
//		ResponseEntity<NotificationSettingEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", NotificationSettingEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful()); //status code 200?
////		assertEquals(1, response.getBody().getUserId());
//	}
//
//	@Test
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testUpdateNotificationSettings() {
//		NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findByUserId(UUID.fromString("5"));
//		notificationSettingEntity.setEnablePost(false);
//		notificationSettingRepository.save(notificationSettingEntity);
//
//		ResponseEntity<NotificationSettingEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", NotificationSettingEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//		assertEquals(false, response.getBody().isEnablePost());
//
//	}
//
//	@Test
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testMarkAllEventsAsRead() {
//
//		ResponseEntity<NotificationEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/readed", NotificationEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//
//		List<NotificationEntity> notificationEntities = notificationRepository.findAll();
//		notificationEntities.forEach(notificationEntity -> notificationEntity.setIsReaded(true));
//		notificationEntities.forEach(notification -> assertEquals(true, notification.getIsReaded()));
//	}
//
//
//	@Test
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testCreateNotificationSettings() throws Exception {
//		ResponseEntity<NotificationEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", NotificationEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//	}
//
//
//	@Test
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void createNotification() {
//		ResponseEntity<NotificationEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/add", NotificationEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//	}
//
//	@Test
//	public void getNotifications() throws Exception {
//		ResponseEntity<NotificationEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/notifications", NotificationEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//	}
//
//	@Test
//	public void getEventsCount() throws Exception {
//		ResponseEntity<NotificationEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/count", NotificationEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful());
//
//	}
//
//
//
//}
