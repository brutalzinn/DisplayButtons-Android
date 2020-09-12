package net.robertocpaes.displaybuttons.networking.compat;

/**
 * Created by NickAc on 26/12/2017.
 * This project is licensed with the MIT license.
 * Please see the project root to find the LICENSE file.
 * <p>
 * Class created to assist with data types compatibility
 */
public class GuidCompact {
    private String originalForm;

    public GuidCompact(String originalForm) {
        this.originalForm = originalForm;
    }

    @Override
    public String toString() {
        return originalForm;
    }
}
