package energy.spectral.grpcserver.dao;

import energy.spectral.grpcserver.utils.CSVReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ElectricityConsumptionDAO implements IElectricityConsumptionDAO{


    private CSVReaderHelper csvReaderHelper;


    @Override
    public Map<String, String> readMeterUsage(LocalDateTime startDate, LocalDateTime endDate) throws IOException, URISyntaxException {

        Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource("csv/meterusage.csv").toURI()));
        List<String[]>  meterUsageReads = csvReaderHelper.readAll(reader);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Map<String,String> meterUsageReadsFiltered = meterUsageReads.stream()
                                                                    .filter(meterUsageRead -> (LocalDateTime.parse(meterUsageRead[0], formatter).isEqual(startDate) || LocalDateTime.parse(meterUsageRead[0], formatter).isAfter(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               startDate)) && LocalDateTime.parse(meterUsageRead[0], formatter).isBefore(endDate))
                                                                    .collect(Collectors.toMap(read -> read[0], read -> read[1]));


        return meterUsageReadsFiltered;
    }


    @Autowired
    public void setCsvReaderHelper(CSVReaderHelper csvReaderHelper) {
        this.csvReaderHelper = csvReaderHelper;
    }

}
