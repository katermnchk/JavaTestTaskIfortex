package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = """
            SELECT
                s.id AS id,
                s.device_type - 1 AS device_type,
                s.ended_at_utc AS ended_at_utc,
                s.started_at_utc AS started_at_utc,
                u.id AS user_id,
                u.first_name AS first_name,
                u.last_name AS last_name
            FROM sessions s
            JOIN users u ON s.user_id = u.id
            WHERE s.device_type = :#{#deviceType.code}
            ORDER BY s.started_at_utc
            LIMIT 1
            """, nativeQuery = true)
    Session getFirstDesktopSession(DeviceType deviceType);

    @Query(value = """
            SELECT
                s.id AS id,
                s.device_type - 1 AS device_type,
                s.ended_at_utc AS ended_at_utc,
                s.started_at_utc AS started_at_utc,
                u.id AS user_id,
                u.first_name AS first_name,
                u.last_name AS last_name
            FROM sessions s
            JOIN users u ON s.user_id = u.id
            WHERE u.deleted = false AND s.ended_at_utc < :endDate
            ORDER BY s.started_at_utc DESC
            """, nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(LocalDateTime endDate);
}