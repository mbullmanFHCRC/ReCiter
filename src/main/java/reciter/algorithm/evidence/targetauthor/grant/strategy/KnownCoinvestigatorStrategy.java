package reciter.algorithm.evidence.targetauthor.grant.strategy;

import java.util.List;

import reciter.algorithm.evidence.targetauthor.AbstractTargetAuthorStrategy;
import reciter.engine.Feature;
import reciter.model.article.ReCiterArticle;
import reciter.model.article.ReCiterAuthor;
import reciter.model.identity.AuthorName;
import reciter.model.identity.Identity;
import reciter.model.identity.KnownRelationship;

public class KnownCoinvestigatorStrategy extends AbstractTargetAuthorStrategy {

	@Override
	public double executeStrategy(ReCiterArticle reCiterArticle, Identity identity) {
		double score = 0;
		List<KnownRelationship> relationships = identity.getKnownRelationships();

		if (relationships != null) {
			for (ReCiterAuthor author : reCiterArticle.getArticleCoAuthors().getAuthors()) {
				// do not match target author's name
				if (!author.getAuthorName().firstInitialLastNameMatch(identity.getPrimaryName())) {
					for (KnownRelationship authorName : relationships) {
						if (authorName.getName().isFullNameMatch(author.getAuthorName())) {
							reCiterArticle.getKnownRelationships().add(author);
							reCiterArticle.setClusterInfo(reCiterArticle.getClusterInfo() + "[known co-investigator match: " + 
									authorName + "] ");
							score += 1;
						}
					}
				}
			}
			reCiterArticle.setKnownCoinvestigatorScore(score);
		}
		return score;
	}

	@Override
	public double executeStrategy(List<ReCiterArticle> reCiterArticles, Identity identity) {
		double sum = 0;
		for (ReCiterArticle reCiterArticle : reCiterArticles) {
			sum += executeStrategy(reCiterArticle, identity);
		}
		return sum;
	}

	@Override
	public void populateFeature(ReCiterArticle reCiterArticle, Identity identity, Feature feature) {
		int score = 0;
		List<KnownRelationship> relationships = identity.getKnownRelationships();

		if (relationships != null) {
			for (ReCiterAuthor author : reCiterArticle.getArticleCoAuthors().getAuthors()) {
				// do not match target author's name
				if (!author.getAuthorName().firstInitialLastNameMatch(identity.getPrimaryName())) {
					for (KnownRelationship authorName : relationships) {
						if (authorName.getName().isFullNameMatch(author.getAuthorName())) {
							score += 1;
						}
					}
				}
			}
			reCiterArticle.setKnownCoinvestigatorScore(score);
		}
		feature.setNumKnownCoinvestigators(score);
	}

}
