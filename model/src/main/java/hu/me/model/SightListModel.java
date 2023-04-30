package hu.me.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SightListModel {

    private List<SightModel> sightModel;

}
