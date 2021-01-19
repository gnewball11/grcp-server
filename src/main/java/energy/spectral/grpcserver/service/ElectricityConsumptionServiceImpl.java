package energy.spectral.grpcserver.service;

import energy.spectral.grpcserver.dao.IElectricityConsumptionDAO;
import energy.spectral.grpcstub.ElectricityConsumptionRequest;
import energy.spectral.grpcstub.ElectricityConsumptionResponse;
import energy.spectral.grpcstub.ElectricityConsumptionServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@GrpcService
public class ElectricityConsumptionServiceImpl extends ElectricityConsumptionServiceGrpc.ElectricityConsumptionServiceImplBase{

    private IElectricityConsumptionDAO electricityConsumptionDAO;

    @Override
    public void getMeterUsage(ElectricityConsumptionRequest request, StreamObserver<ElectricityConsumptionResponse> responseObserver)  {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(request.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(request.getEndDate(), formatter);


        Map meterUsageReads = null;
        ElectricityConsumptionResponse response = null;
        try {
            meterUsageReads = electricityConsumptionDAO.readMeterUsage(startDate, endDate);
            response = ElectricityConsumptionResponse.newBuilder()
                    .putAllTimeSeries(meterUsageReads)
                    .build();
        } catch (IOException | URISyntaxException e) {
            response = ElectricityConsumptionResponse.newBuilder()
                    .setException("There was an error reading from the data source")
                    .setDescription(e.getMessage())
                    .build();
        }



        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Autowired
    public void setElectricityConsumptionDAO(IElectricityConsumptionDAO electricityConsumptionDAO) {
        this.electricityConsumptionDAO = electricityConsumptionDAO;
    }

}
