package org.juitar.web.rest.resources;

/**
 * @author sha1n
 * Date: 4/29/14
 */
class TestJson {
    public String timestamp = String.valueOf(System.currentTimeMillis());
    public String message;

    public TestJson() {
    }

    public TestJson(String message) {
        this.message = message;
    }
}
