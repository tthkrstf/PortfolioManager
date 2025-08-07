package com.restapi.restapi.mapper;

import com.restapi.restapi.dto.CompanyNewsDTO;
import com.restapi.restapi.dto.external.CompanyNewsRaw;
import com.restapi.restapi.dto.external.finnhub.FinnhubCompanyNewsRaw;
import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.dto.QuoteDTO;
import com.restapi.restapi.dto.external.QuoteRaw;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FinnhubMapper {
    public QuoteDTO mapQuote(QuoteRaw raw, String symbol) {
        if (!(raw instanceof FinnhubQuoteRaw)) {
            throw new IllegalArgumentException("Expected FinnhubQuoteRaw");
        }
        FinnhubQuoteRaw finnHubRaw = (FinnhubQuoteRaw) raw;
        return new QuoteDTO(symbol, finnHubRaw.getC(), finnHubRaw.getD(), finnHubRaw.getDp(),
                finnHubRaw.getH(), finnHubRaw.getL(), finnHubRaw.getO(), finnHubRaw.getPc());
    }

    public List<CompanyNewsDTO> mapNews(List<? extends CompanyNewsRaw> companyNewsRaws, String symbol) {
        if (!companyNewsRaws.isEmpty() && !(companyNewsRaws.get(0) instanceof FinnhubCompanyNewsRaw)) {
            throw new IllegalArgumentException("Expected FinnhubCompanyNewsRaw elements");
        }

        return companyNewsRaws.stream()
                .map(raw -> {
                    FinnhubCompanyNewsRaw finnRaw = (FinnhubCompanyNewsRaw) raw;

                    return new CompanyNewsDTO(
                            finnRaw.getCategory(),
                            formatDate(finnRaw.getDatetime()),
                            finnRaw.getHeadline(),
                            finnRaw.getImage(),
                            finnRaw.getRelated(),
                            finnRaw.getSource(),
                            finnRaw.getSummary(),
                            finnRaw.getUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    private Date formatDate(long date){
        LocalDate datetime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.of("UTC"))
                .toLocalDate();

        return Date.from(datetime.atStartOfDay(ZoneId.of("UTC")).toInstant());
    }
}
