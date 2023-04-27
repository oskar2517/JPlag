package de.jplag.options;

import java.util.Objects;

public class SubmissionNamePair {
    private final String s1, s2;

    public SubmissionNamePair(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s1, s2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SubmissionNamePair)) {
            return false;
        }
        SubmissionNamePair other = (SubmissionNamePair) obj;
        return Objects.equals(s1, other.s1) && Objects.equals(s2, other.s2);
    }
}