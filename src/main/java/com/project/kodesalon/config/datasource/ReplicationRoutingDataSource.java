package com.project.kodesalon.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private final String masterKey;
    private final String slaveKey;

    public ReplicationRoutingDataSource(final String masterKey, final String slaveKey) {
        this.masterKey = masterKey;
        this.slaveKey = slaveKey;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isCurrentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isCurrentTransactionReadOnly) {
            log.info("Connection Slave");
            return slaveKey;
        }

        log.info("Connection Master");
        return masterKey;
    }
}
