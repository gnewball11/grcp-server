package energy.spectral.grpcserver.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;

public interface IElectricityConsumptionDAO {

    Map<String, String> readMeterUsage(LocalDateTime startDate, LocalDateTime endDate) throws IOException, URISyntaxException;

}
