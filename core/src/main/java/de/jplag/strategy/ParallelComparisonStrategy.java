package de.jplag.strategy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import de.jplag.GreedyStringTiling;
import de.jplag.JPlagComparison;
import de.jplag.SubmissionSet;
import de.jplag.options.JPlagOptions;

/**
 * Strategy for the parallel comparison of submissions. Uses all available cores.
 * @author Timur Saglam
 */
public class ParallelComparisonStrategy extends AbstractComparisonStrategy {
    public ParallelComparisonStrategy(JPlagOptions options, GreedyStringTiling greedyStringTiling) {
        super(options, greedyStringTiling);
    }

    @Override
    protected void handleBaseCode(SubmissionSet submissionSet) {
        boolean withBaseCode = submissionSet.hasBaseCode();
        if (withBaseCode) {
            compareSubmissionsToBaseCode(submissionSet);
        }
    }

        List<SubmissionTuple> tuples = buildComparisonTuples(submissionSet.getSubmissions(), options);
        List<JPlagComparison> comparisons = tuples.stream().parallel().map(tuple -> compareSubmissions(tuple.left(), tuple.right()))
                .flatMap(Optional::stream).toList();

    @Override
    protected Optional<JPlagComparison> compareTuple(SubmissionTuple tuple) {
        return compareSubmissions(tuple.left(), tuple.right());
    }
}
