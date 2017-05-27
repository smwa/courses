package me.smwa.courses;

import me.smwa.ViewBuilder.ViewBuilder;

public class vb extends ViewBuilder
{
    private static vb viewBuilder = new vb();
    public static vb getInstance() {
        return viewBuilder;
    }

    public String page(String title, String contains)
    {
        return super.template(title,
            form(
                row(
                    contains
                )
            )
        );
    }
}
