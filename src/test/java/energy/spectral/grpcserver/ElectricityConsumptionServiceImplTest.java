package energy.spectral.grpcserver;

import energy.spectral.grpcserver.service.ElectricityConsumptionServiceImpl;
import energy.spectral.grpcstub.ElectricityConsumptionRequest;
import energy.spectral.grpcstub.ElectricityConsumptionResponse;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ElectricityConsumptionServiceImplTest {

	@Autowired
	private ElectricityConsumptionServiceImpl electricityConsumptionService;

	@Test
	void getAllReadsForOneDay() throws Exception {

		ElectricityConsumptionRequest request = ElectricityConsumptionRequest.newBuilder()
				.setStartDate("2019-01-02 00:00:00")
				.setEndDate("2019-01-02 23:59:59")
				.build();
		StreamRecorder<ElectricityConsumptionResponse> responseObserver = StreamRecorder.create();
		electricityConsumptionService.getMeterUsage(request, responseObserver);
		if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
			fail("The call did not terminate in time");
		}
		assertNull(responseObserver.getError());
		List<ElectricityConsumptionResponse> results = responseObserver.getValues();
		assertEquals(1, results.size());
		ElectricityConsumptionResponse response = results.get(0);
		assertEquals(96, response.getTimeSeriesMap().size());

	}

}
