package hu.me.transformer;


import hu.me.domain.Sights;
import hu.me.model.SightModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SightTransformer {

    public List<SightModel> transformSightListToSightModelList(List<Sights> sightsList) {
        List<SightModel> sightModels = new ArrayList<>();
        for (Sights sight : sightsList) {
            sightModels.add(transformSightToSightModel(sight));
        }
        return sightModels;
    }

    public SightModel transformSightToSightModel(Sights sight) {
        SightModel sightModel = new SightModel();
        if(sight != null) {
            sightModel.setName(sight.getName());
            sightModel.setDescription(sight.getDescription());
            sightModel.setAddress(sight.getAddress());
            sightModel.setCategory(sight.getCategory());
            sightModel.setReviewList(sight.getReviews());
            if (sight.getId() != null)
                sightModel.setId(sight.getId());
        }
        return sightModel;
    }

    public Sights transformSightModelToSight(SightModel sightModel) {
        Sights sight = new Sights();
        if(sightModel.getId()!=null)
            sight.setId(sightModel.getId());
        sight.setName(sightModel.getName());
        sight.setDescription(sightModel.getDescription());
        sight.setAddress(sightModel.getAddress());
        sight.setCategory(sightModel.getCategory());
        return sight;
    }


}
