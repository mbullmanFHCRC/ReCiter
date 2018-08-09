/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package reciter.xml.retriever.pubmed;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import reciter.model.identity.Identity;
import reciter.pubmed.retriever.PubMedQuery;
import reciter.xml.retriever.pubmed.PubMedQueryType.PubMedQueryBuilder;

/**
 * There are no differences between initial query and the strict query.
 */
@Component("emailRetrievalStrategy")
public class EmailRetrievalStrategy extends AbstractRetrievalStrategy {

	private static final String retrievalStrategyName = "EmailRetrievalStrategy";

	@Override
	public String getRetrievalStrategyName() {
		return retrievalStrategyName;
	}

	/**
	 * Concatenate email strings with " or ".
	 */
	private String constructEmailQuery(Identity identity) {
		if (identity.getEmails() != null && !identity.getEmails().isEmpty()) {

			// Below is code from Apache's StringUtils class, modified to remove null checks.
			Iterator<String> iterator = identity.getEmails().iterator();

			final String first = iterator.next().replace(',', '.');
			if (!iterator.hasNext()) {
				return first;
			}

			// two or more elements
			final StringBuilder buf = new StringBuilder(30); // 30 is approx length of 2 email strings.
			if (first != null) {
				buf.append(first);
			}

			while (iterator.hasNext()) {
				buf.append(" OR ");
				final String obj = iterator.next();

				// data cleaning: sometimes emails would have ',' instead of '.'
				// i.e. (ayr2001@med.cornell,edu)
				// replace ',' with '.'
				buf.append(obj.replace(',', '.'));
			}
			return buf.toString();
		} else {
			return null;
		}
	}

	@Override
	protected List<PubMedQueryType> buildQuery(Identity identity) {
		List<PubMedQueryType> pubMedQueries = new ArrayList<PubMedQueryType>();

		PubMedQueryBuilder pubMedQueryBuilder = new PubMedQueryBuilder(constructEmailQuery(identity));
		PubMedQuery emailQuery = pubMedQueryBuilder.build();

		PubMedQueryType pubMedQueryType = new PubMedQueryType();
		pubMedQueryType.setLenientQuery(new PubMedQueryResult(emailQuery));
		pubMedQueryType.setStrictQuery(new PubMedQueryResult(emailQuery));
		pubMedQueryType.setLenientCountQuery(new PubMedQueryResult(emailQuery));
		pubMedQueryType.setStrictCountQuery(new PubMedQueryResult(emailQuery));
		pubMedQueries.add(pubMedQueryType);

		return pubMedQueries;
	}

	@Override
	protected List<PubMedQueryType> buildQuery(Identity identity, Date startDate, Date endDate) {
		List<PubMedQueryType> pubMedQueries = new ArrayList<PubMedQueryType>();

		PubMedQueryBuilder pubMedQueryBuilder = new PubMedQueryBuilder(constructEmailQuery(identity))
				.dateRange(true, startDate, endDate);
		PubMedQuery emailQuery = pubMedQueryBuilder.build();
		
		PubMedQueryBuilder pubMedQueryBuilderCount = new PubMedQueryBuilder(constructEmailQuery(identity));
		PubMedQuery emailQueryCount = pubMedQueryBuilderCount.build();

		PubMedQueryType pubMedQueryType = new PubMedQueryType();
		pubMedQueryType.setLenientQuery(new PubMedQueryResult(emailQuery));
		pubMedQueryType.setStrictQuery(new PubMedQueryResult(emailQuery));
		pubMedQueryType.setLenientCountQuery(new PubMedQueryResult(emailQueryCount));
		pubMedQueryType.setStrictCountQuery(new PubMedQueryResult(emailQueryCount));
		pubMedQueries.add(pubMedQueryType);

		return pubMedQueries;
	}
	
	@Override
	public RetrievalResult retrievePubMedArticles(List<Long> pmids) throws IOException {
		throw new UnsupportedOperationException("Does not support retrieval by pmids.");
	}
}
