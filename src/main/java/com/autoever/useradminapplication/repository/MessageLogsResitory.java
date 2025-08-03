package com.autoever.useradminapplication.repository;

import com.autoever.useradminapplication.domain.entity.MessageLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogsResitory extends JpaRepository<MessageLogs, Long> {
}
