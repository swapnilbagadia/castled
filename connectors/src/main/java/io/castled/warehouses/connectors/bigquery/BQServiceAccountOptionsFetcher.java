package io.castled.warehouses.connectors.bigquery;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.castled.commons.models.ServiceAccountDetails;
import io.castled.forms.dtos.FormFieldOption;
import io.castled.utils.JsonUtils;
import io.castled.warehouses.WarehouseConfig;
import io.castled.warehouses.connectors.bigquery.daos.ServiceAccountDetailsDAO;
import io.castled.warehouses.optionsfetchers.WarehouseOptionsFetcher;
import org.jdbi.v3.core.Jdbi;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Singleton
public class BQServiceAccountOptionsFetcher implements WarehouseOptionsFetcher {

    private final ServiceAccountDetailsDAO serviceAccountDetailsDAO;

    @Inject
    public BQServiceAccountOptionsFetcher(Jdbi jdbi) {
        this.serviceAccountDetailsDAO = jdbi.onDemand(ServiceAccountDetailsDAO.class);
    }

    @Override
    public List<FormFieldOption> getFieldOptions(WarehouseConfig warehouseConfig) {
        BigQueryWarehouseConfig bigQueryWarehouseConfig = (BigQueryWarehouseConfig) warehouseConfig;
        ServiceAccountDetails serviceAccountDetails = bigQueryWarehouseConfig.getServiceAccountDetails();
        serviceAccountDetailsDAO.createServiceAccountDetails(1L, serviceAccountDetails.getClientEmail(),
                Base64.getEncoder().encodeToString(JsonUtils.objectToByteArray(serviceAccountDetails)));
        return Collections.singletonList(new FormFieldOption(bigQueryWarehouseConfig.getServiceAccountDetails().getClientEmail(),
                bigQueryWarehouseConfig.getServiceAccountDetails().getClientEmail()));
    }
}