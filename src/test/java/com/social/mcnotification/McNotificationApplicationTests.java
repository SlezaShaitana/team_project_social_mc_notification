//package com.social.mcnotification;
//
//
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
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
//class McNotificationApplicationTests {
//
//
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
//	private TestRestTemplate template = new TestRestTemplate();
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
//		NotificationEntity notification = new NotificationEntity();
//		notification.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
//		notification.setId(UUID.fromString("1"));
//		notification.setAuthorId(UUID.fromString("5"));
//		notification.setReceiverId(UUID.fromString("10"));
//		notification.setContent("Hello");
//		notification.setIsReaded(false);
//		notification.setServiceName(MicroServiceName.ACCOUNT);
//		String date = LocalDateTime.now().toString();
//		notification.setSentTime(Timestamp.valueOf(date));
//		notificationEntities.add(notification);
//
//		NotificationEntity notification2 = new NotificationEntity();
//		notification2.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
//		notification2.setId(UUID.fromString("1"));
//		notification2.setAuthorId(UUID.fromString("5"));
//		notification2.setReceiverId(UUID.fromString("10"));
//		notification2.setIsReaded(false);
//		notificationEntities.add(notification2);
//
//		notificationRepository.saveAll(notificationEntities);
//
//		NotificationSettingEntity notificationSettingEntity = new NotificationSettingEntity();
//		notificationSettingEntity.setId(UUID.fromString("5"));
//		notificationSettingEntity.setEnableLike(true);
//		notificationSettingEntity.setEnableSendEmailMessage(false);
//		notificationSettingEntity.setEnablePost(true);
//		notificationSettingEntity.setEnableFriendBirthday(false);
//		notificationSettingEntity.setEnableMessage(false);
//		notificationSettingEntity.setEnablePostComment(true);
//		notificationSettingEntities.add(notificationSettingEntity);
//
//		NotificationSettingEntity notificationSetting2 = new NotificationSettingEntity();
//		notificationSetting2.setId(UUID.fromString("1172"));
//		notificationSetting2.setEnableLike(true);
//		notificationSetting2.setEnableSendEmailMessage(false);
//		notificationSetting2.setEnablePostComment(true);
//		notificationSettingEntities.add(notificationSetting2);
//
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
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testGetNotificationSettings() {
//		ResponseEntity<NotificationSettingEntity> response = template.getRestTemplate()
//				.getForEntity("http://localhost:" + port + "/api/v1/notifications/settings", NotificationSettingEntity.class);
//		assertTrue(response.getStatusCode().is2xxSuccessful()); //status code 200?
//		assertEquals(1, response.getBody().getId());
//	}
//
//	@Test
//	@DisplayName("Http-ответ со статусом 'ok'")
//	public void testUpdateNotificationSettings() {
//		NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(UUID.fromString("5"));
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
