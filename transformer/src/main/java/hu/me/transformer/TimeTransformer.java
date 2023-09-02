package hu.me.transformer;

import hu.me.domain.Time;
import hu.me.model.TimeModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeTransformer {

    public List<TimeModel> transformTimeListToTimeModelList(List<Time> timeList) {
        List<TimeModel> timeModels = new ArrayList<>();
        for (Time time : timeList) {
            timeModels.add(transformTimeToTimeModel(time));
        }
        return timeModels;
    }

    public TimeModel transformTimeToTimeModel(Time time) {
        TimeModel timeModel = new TimeModel();
        if(time != null) {
            timeModel.setTime_date(time.getTime_date());
            timeModel.setMovie(time.getMovie());
            timeModel.setSeats(time.getSeats());
            timeModel.setUser(time.getUsersThisTime());
            if (time.getId() != null)
                timeModel.setId(time.getId());
        }
        return timeModel;
    }

    public Time transformTimeModelToTime(TimeModel timeModel) {
        Time time = new Time();
        if(timeModel.getId()!=null)
            time.setId(timeModel.getId());
        time.setTime_date(timeModel.getTime_date());
        time.setMovie(timeModel.getMovie());
        time.setSeats(timeModel.getSeats());
        time.setUsersThisTime(timeModel.getUser());
        return time;
    }

}
