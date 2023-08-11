package hu.me.model;

import hu.me.domain.Time;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimeListModel {

    List<TimeModel> timeModel;

}
