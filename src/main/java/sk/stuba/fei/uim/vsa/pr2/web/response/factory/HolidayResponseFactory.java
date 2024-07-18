package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.entities.Holiday;
import sk.stuba.fei.uim.vsa.pr2.web.response.HolidayDto;

public class HolidayResponseFactory {

    public HolidayDto toDto(Holiday entity){
        return new HolidayDto(entity.getId(), entity.getNazov(), entity.getDatum());
    }

    public Holiday toEntity(HolidayDto dto){
        Holiday holiday = new Holiday(dto.getName(), dto.getDate());
        holiday.setId(dto.getId());
        return holiday;
    }
}
