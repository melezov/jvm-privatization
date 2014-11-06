package test;

/**
 * This source will compile to
 *   2 public classes
 *   1 protected class
 *   1 package private class
 *   1 private class
 */
public class PublicClass {
    public    class InnerPublicClass {}
    protected class InnerProtectedClass {}
              class InnerPackagePrivateClass {}
    private   class InnerPrivateClass {}
}
