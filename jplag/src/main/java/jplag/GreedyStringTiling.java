package jplag;

import java.util.List;

/**
 * This class implements the Greedy String Tiling algorithm as introduced by Michael Wise. However, it is very specific
 * to the classes {@link Structure}, {@link Token}, as well as {@link Matches} and {@link Match}.
 * @see <a href=
 * "https://www.researchgate.net/publication/262763983_String_Similarity_via_Greedy_String_Tiling_and_Running_Karp-Rabin_Matching">
 * String Similarity via Greedy String Tiling and Running Karp−Rabin Matching </a>
 */
public class GreedyStringTiling implements TokenConstants {

    private Matches matches = new Matches();
    private JPlag program;

    public GreedyStringTiling(JPlag program) {
        this.program = program;
    }

    /**
     * Creating hashes in linear time. The hash-code will be written in every Token for the next <hash_length> token
     * (includes the Token itself).
     * @param structure contains the tokens.
     * @param hashLength is the hash length (condition: 1 < hashLength < 26)
     * @param makeTable determines if a simple hash table is created in the structure.
     */
    public void createHashes(Structure structure, int hashLength, boolean makeTable) {
        // Here the upper boundary of the hash length is set.
        // It is determined by the number of bits of the 'int' data type and the number of tokens.
        if (hashLength < 1) {
            hashLength = 1;
        }
        hashLength = (hashLength < 26 ? hashLength : 25);

        if (structure.size() < hashLength) {
            return;
        }

        int modulo = ((1 << 6) - 1);   // Modulo 64!

        int loops = structure.size() - hashLength;
        structure.table = (makeTable ? new Table(3 * loops) : null);
        int hash = 0;
        int i;
        int hashedLength = 0;
        for (i = 0; i < hashLength; i++) {
            hash = (2 * hash) + (structure.tokens[i].type & modulo);
            hashedLength++;
            if (structure.tokens[i].marked) {
                hashedLength = 0;
            }
        }
        int factor = (hashLength != 1 ? (2 << (hashLength - 2)) : 1);

        if (makeTable) {
            for (i = 0; i < loops; i++) {
                if (hashedLength >= hashLength) {
                    structure.tokens[i].hash = hash;
                    structure.table.add(hash, i);   // add into hashtable
                } else {
                    structure.tokens[i].hash = -1;
                }
                hash -= factor * (structure.tokens[i].type & modulo);
                hash = (2 * hash) + (structure.tokens[i + hashLength].type & modulo);
                if (structure.tokens[i + hashLength].marked) {
                    hashedLength = 0;
                } else {
                    hashedLength++;
                }
            }
        } else {
            for (i = 0; i < loops; i++) {
                structure.tokens[i].hash = (hashedLength >= hashLength) ? hash : -1;
                hash -= factor * (structure.tokens[i].type & modulo);
                hash = (2 * hash) + (structure.tokens[i + hashLength].type & modulo);
                if (structure.tokens[i + hashLength].marked) {
                    hashedLength = 0;
                } else {
                    hashedLength++;
                }
            }
        }
        structure.hash_length = hashLength;
    }

    public final JPlagComparison compare(Submission first, Submission second) {
        Submission smallerSubmission, largerSubmission;
        if (first.tokenList.size() > second.tokenList.size()) {
            smallerSubmission = second;
            largerSubmission = first;
        } else {
            smallerSubmission = first;
            largerSubmission = second;
        }
        // if hashtable exists in first but not in second structure: flip around!
        if (largerSubmission.tokenList.table == null && smallerSubmission.tokenList.table != null) {
            Submission tmp = smallerSubmission;
            smallerSubmission = largerSubmission;
            largerSubmission = tmp;
        }

        return compare(smallerSubmission, largerSubmission, this.program.getOptions().getMinTokenMatch());
    }
 
    /**
     * first parameter should contain the smaller sequence!!!
     */
    private final JPlagComparison compare(Submission submissionA, Submission submissionB, int minimalTokenMatch) {
        Structure structA = submissionA.tokenList;
        Structure structB = submissionB.tokenList;

        // FILE_END used as pivot

        // init
        Token[] A = structA.tokens;
        Token[] B = structB.tokens;
        int lengthA = structA.size() - 1;  // minus pivots!
        int lengthB = structB.size() - 1;  // minus pivots!
        JPlagComparison comparison = new JPlagComparison(submissionA, submissionB);

        if (lengthA < minimalTokenMatch || lengthB < minimalTokenMatch) {
            return comparison;
        }

        // Initialize
        if (!program.getOptions().hasBaseCode()) {
            for (int i = 0; i <= lengthA; i++) {
                A[i].marked = A[i].type == FILE_END || A[i].type == SEPARATOR_TOKEN;
            }

            for (int i = 0; i <= lengthB; i++) {
                B[i].marked = B[i].type == FILE_END || B[i].type == SEPARATOR_TOKEN;
            }
        } else {
            for (int i = 0; i <= lengthA; i++) {
                A[i].marked = A[i].type == FILE_END || A[i].type == SEPARATOR_TOKEN || A[i].basecode;
            }

            for (int i = 0; i <= lengthB; i++) {
                B[i].marked = B[i].type == FILE_END || B[i].type == SEPARATOR_TOKEN || B[i].basecode;
            }
        }

        // start:
        if (structA.hash_length != minimalTokenMatch) {
            createHashes(structA, minimalTokenMatch, false);
        }
        if (structB.hash_length != minimalTokenMatch || structB.table == null) {
            createHashes(structB, minimalTokenMatch, true);
        }

        int maxmatch;
        List<Integer> elementsB;

        do {
            maxmatch = minimalTokenMatch;
            matches.clear();
            for (int x = 0; x <= lengthA - maxmatch; x++) {
                if (A[x].marked || A[x].hash == -1 || (elementsB = structB.table.get(A[x].hash)) == null) {
                    continue;
                }
                inner: for (Integer y : elementsB) {
                    if (B[y].marked || maxmatch > lengthB - y) {
                        continue;
                    }

                    int j, hx, hy;
                    for (j = maxmatch - 1; j >= 0; j--) { // begins comparison from behind
                        if (A[hx = x + j].type != B[hy = y + j].type || A[hx].marked || B[hy].marked) {
                            continue inner;
                        }
                    }

                    // expand match
                    j = maxmatch;
                    while (A[hx = x + j].type == B[hy = y + j].type && !A[hx].marked && !B[hy].marked) {
                        j++;
                    }

                    if (j > maxmatch) {  // new biggest match? -> delete current smaller
                        matches.clear();
                        maxmatch = j;
                    }
                    matches.addMatch(x, y, j);  // add match
                }
            }
            for (int i = matches.size() - 1; i >= 0; i--) {
                int x = matches.matches[i].startA;  // begining of sequence A
                int y = matches.matches[i].startB;  // begining of sequence B
                comparison.addMatch(x, y, matches.matches[i].length);
                // in order that "Match" will be newly build (because reusing)
                for (int j = matches.matches[i].length; j > 0; j--) {
                    A[x++].marked = B[y++].marked = true;   // mark all Token!
                }
            }

        } while (maxmatch != minimalTokenMatch);

        return comparison;
    }

    public final JPlagBaseCodeComparison compareWithBaseCode(Submission first, Submission second) {
        Submission smallerSubmission, largerSubmission;
        if (first.tokenList.size() > second.tokenList.size()) {
            smallerSubmission = second;
            largerSubmission = first;
        } else {
            smallerSubmission = second;
            largerSubmission = first;
        }
        // if hashtable exists in first but not in second structure: flip around!
        if (largerSubmission.tokenList.table == null && smallerSubmission.tokenList.table != null) {
            Submission tmp = smallerSubmission;
            smallerSubmission = largerSubmission;
            largerSubmission = tmp;
        }

        return compareWithBaseCode(smallerSubmission, largerSubmission, this.program.getOptions().getMinTokenMatch());
    }

    private JPlagBaseCodeComparison compareWithBaseCode(Submission subA, Submission subB, int minimalTokenMatch) {
        Structure structA = subA.tokenList;
        Structure structB = subB.tokenList;

        // FILE_END used as pivot

        // init
        Token[] A = structA.tokens;
        Token[] B = structB.tokens;
        int lengthA = structA.size() - 1;  // minus pivots!
        int lengthB = structB.size() - 1;  // minus pivots!
        JPlagBaseCodeComparison baseCodeComparison = new JPlagBaseCodeComparison(subA, subB);

        if (lengthA < minimalTokenMatch || lengthB < minimalTokenMatch) {
            return baseCodeComparison;
        }

        // Initialize
        for (int i = 0; i <= lengthA; i++) {
            A[i].marked = A[i].type == FILE_END || A[i].type == SEPARATOR_TOKEN;
        }

        for (int i = 0; i <= lengthB; i++) {
            B[i].marked = B[i].type == FILE_END || B[i].type == SEPARATOR_TOKEN;
        }

        // start:
        if (structA.hash_length != minimalTokenMatch) {
            createHashes(structA, minimalTokenMatch, true);
        }
        if (structB.hash_length != minimalTokenMatch || structB.table == null) {
            createHashes(structB, minimalTokenMatch, true);
        }

        int maxmatch;
        List<Integer> elementsB;

        do {
            maxmatch = minimalTokenMatch;
            matches.clear();
            for (int x = 0; x <= lengthA - maxmatch; x++) {
                if (A[x].marked || A[x].hash == -1 || (elementsB = structB.table.get(A[x].hash)) == null) {
                    continue;
                }
                inner: for (Integer y : elementsB) {
                    if (B[y].marked || maxmatch > lengthB - y) {
                        continue;
                    }

                    int j, hx, hy;
                    for (j = maxmatch - 1; j >= 0; j--) { // begins comparison from behind
                        if (A[hx = x + j].type != B[hy = y + j].type || A[hx].marked || B[hy].marked) {
                            continue inner;
                        }
                    }
                    // expand match
                    j = maxmatch;
                    while (A[hx = x + j].type == B[hy = y + j].type && !A[hx].marked && !B[hy].marked) {
                        j++;
                    }

                    if (j != maxmatch) {  // new biggest match? -> delete current smaller
                        matches.clear();
                        maxmatch = j;
                    }
                    matches.addMatch(x, y, j);  // add match
                }
            }
            for (int i = matches.size() - 1; i >= 0; i--) {
                int x = matches.matches[i].startA;  // beginning in sequence A
                int y = matches.matches[i].startB;  // beginning in sequence B
                baseCodeComparison.addMatch(x, y, matches.matches[i].length);
                // in order that "Match" will be newly build (because reusing)
                for (int j = matches.matches[i].length; j > 0; j--) {
                    A[x].marked = B[y].marked = true;   // mark all Token!
                    A[x].basecode = B[y].basecode = true;
                    x++;
                    y++;
                }
            }
        } while (maxmatch != minimalTokenMatch);

        return baseCodeComparison;
    }

    public void resetBaseSubmission(Submission submission) {
        Token[] tokens = submission.tokenList.tokens;
        for (int i = 0; i < submission.tokenList.size() - 1; i++) {
            tokens[i].basecode = false;
        }
    }
}
