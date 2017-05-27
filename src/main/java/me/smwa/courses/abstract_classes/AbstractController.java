package me.smwa.courses.abstract_classes;

import me.smwa.courses.vb;

/**
 * Created by Michael on 2017-02-19.
 * All page controllers must be based on one of the children of this class.
 */
abstract public class AbstractController {
    protected vb getViewBuilder()
    {
        return vb.getInstance();
    }
    protected vb v() {
        return getViewBuilder();
    }
}
