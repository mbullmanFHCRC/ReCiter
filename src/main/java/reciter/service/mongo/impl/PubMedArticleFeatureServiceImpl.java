package reciter.service.mongo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reciter.database.mongo.model.PubMedArticleFeature;
import reciter.database.mongo.repository.PubMedArticleFeatureRepository;
import reciter.service.mongo.PubMedArticleFeatureService;

@Service("pubMedArticleFeatureService")
public class PubMedArticleFeatureServiceImpl implements PubMedArticleFeatureService {

	@Autowired
	private PubMedArticleFeatureRepository pubMedArticleFeatureRepository;
	
	@Override
	public void save(PubMedArticleFeature pubMedArticleFeature) {
		pubMedArticleFeatureRepository.save(pubMedArticleFeature);
	}

}
