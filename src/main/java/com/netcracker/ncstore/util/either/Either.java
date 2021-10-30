package com.netcracker.ncstore.util.either;

/**
 * Class that contains one of two objects at the same time
 * @param <L> - Left object type
 * @param <R> - Right object type
 */
public final class Either<L, R> {
    private L left;
    private R right;

    /**
     * Default constructor
     */
    public Either() {
        this.left = null;
        this.right = null;
    }

    /**
     * Checks if left value exists
     * @return - true if exists, false otherwise
     */
    public boolean isLeft() {
        return left != null;
    }

    /**
     * Checks if right value exists
     * @return - true if exists, false otherwise
     */
    public boolean isRight() {
        return right != null;
    }

    /**
     * Sets left value AND resets right value
     * @param leftValue - not null
     * @throws NullEitherException - if setting value is null
     */
    public void setLeft(final L leftValue) throws NullEitherException {
        if (leftValue != null) {
            this.left = leftValue;
            this.right = null;
        } else {
            throw new NullEitherException("Left value can't be null!");
        }
    }

    /**
     * Sets right value AND resets left value
     * @param rightValue - not null
     * @throws NullEitherException - if setting value is null
     */
    public void setRight(final R rightValue) throws NullEitherException {
        if (rightValue != null) {
            this.left = null;
            this.right = rightValue;
        } else {
            throw new NullEitherException("Right value can't be null!");
        }
    }

    /**
     * Gets left value if not null
     * @return - left value
     * @throws NullEitherException - if left value is null
     */
    public L getLeft() throws NullEitherException {
        if (left != null) {
            return left;
        } else {
            throw new NullEitherException("Left value is null!");
        }
    }

    /**
     * Gets right value if not null
     * @return - right value
     * @throws NullEitherException - if right value is null
     */
    public R getRight() throws NullEitherException {
        if (right != null) {
            return right;
        } else {
            throw new NullEitherException("Right value is null!");
        }
    }
}
