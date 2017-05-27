package me.smwa.ViewBuilder;

import me.smwa.courses.entities.User;

public abstract class ViewBuilder
{
    abstract public String page(String title, String contains);

    protected String template(String title, String contains)
    {
        return "<!doctype html>\n"
                +"<html lang=\"en\">\n"
                +"<head><meta charset=\"utf-8\"><title>" + title + "</title>\n"
                +"<link rel=\"stylesheet\" href=\"styles.css\">\n"
                +"<link rel=\"stylesheet\" href=\"bootstrap.min.css\">\n"
                +"<link rel=\"stylesheet\" href=\"mdb.min.css\">\n"
                +"<!--[if lt IE 9]>"
                +"<script src=\"html5shiv.js\"></script>"
                +"<![endif]-->\n"
                +"</head>\n"
                +"<body class=\"grey lighten-3\">\n<div class='container-fluid'>\n"
                +contains
                +"\n</div>\n<script src=\"index.js\"></script>\n<script src=\"jquery.min.js\"></script>\n</body>\n</html>";
    }

    public String row(String contains, String id, String cls)
    {
        return this.tag("div", id, "row " + cls, contains); // <div id='?' class='row ?'>?</div>
    }

    public String navbar(User user) {
        String links = "";
        if (user != null) {
            links += "<li class=\"nav-item\"><a class=\"nav-link\" href=\"courses\">Courses</a></li>";
            if (user.permission_edit_recipients) {
                links += "<li class=\"nav-item\"><a class=\"nav-link\" href=\"receivers\">Recipients</a></li>";
            }
            if (user.permission_edit_users) {
                links += "<li class=\"nav-item\"><a class=\"nav-link\" href=\"users\">Users</a></li>";
            }
            if (user.permission_process_report) {
                links += "<li class=\"nav-item\"><a class=\"nav-link\" href=\"report1\">Report</a></li>";
            }
        }
        return "<nav class=\"navbar navbar-dark orange\">\n" +
                "  <div class=\"container-fluid\">\n" +
                "    <div class=\"navbar-header\">\n" +
                "      <a class=\"navbar-brand\" href=\"menu\">P-Check</a>\n" +
                "    </div>\n" +
                "\n" +
                "    <div>\n" +
                "      <ul class=\"nav navbar-nav\">\n" +
                links +

                "      </ul>\n" +
                "    </div><!-- /.navbar-collapse -->\n" +
                "  </div><!-- /.container-fluid -->\n" +
                "</nav>";
    }

    public String navbarButton(String url, String text)
    {
        return "<li class=\"nav-item\"><a class=\"nav-link\" href=\""+url+"\">"+text+" </a></li>";
    }

    public String row(String contains)
    {
        return this.row(contains, "", "");
    }

    public String column(int width, String contains, String id, String cls, int offset)
    {
        cls = "col-md-" + Integer.toString(width) + " " + cls;
        if (offset > 0) {
            cls = "col-md-offset-" + Integer.toString(offset) + " " + cls;
        }
        return this.tag("div", id, cls, contains);
    }

    public String column(String contains)
    {
        return this.column(10, contains, "", "", 1);
    }

    public String header(String contains, String id, String cls)
    {
        return this.tag("h1", id, cls, contains);
    }

    public String header(String contains)
    {
        return this.header(contains, "", "");
    }

    public String subheader(String contains, String id, String cls)
    {
        return this.tag("h3", id, cls, contains);
    }

    public String subheader(String contains)
    {
        return this.subheader(contains, "", "");
    }

    public String list(String contains) {
        return this.tag("div", "", "list-group", contains);
    }

    public String listItem(String contains) {
        return this.tag("div", "", "list-group-item", contains);
    }

    public String listItem(String contains, String context) {
        String c = "";
        if (!context.equals("")) {
            c = "list-group-item-" + context;
        }
        return this.tag("div", "", "list-group-item "+c, contains);
    }

    public String link(String url, String text) {
        return this.tag("a", "", "", text, "href='"+url+"'");
    }

    public String linkRight(String url, String text) {
        return this.tag("a", "", "pull-right", text, "href='"+url+"'");
    }

    public String redirect(String url) {
        return page("login", link(url, "Click here to continue"))
                + "<script> location.href = '"+url+"'; </script>";
    }

    public String listItemLink(String url, String text, String context) {
        String c = "";
        if (!context.equals("")) {
            c = "list-group-item-" + context;
        }
        return this.tag("a", "", "list-group-item "+c, text, "href='"+url+"'");
    }

    public String horizontalLine() {
        return "<hr>";
    }

    public String listItemLink(String url, String text) {
        return listItemLink(url, text, "");
    }

    public String paragraph(String contains, String id, String cls)
    {
        return this.tag("p", id, cls, contains);
    }

    public String paragraph(String contains)
    {
        return this.paragraph(contains, "", "");
    }

    public String form(String contains)
    {
        return this.tag("form", "", "", contains, "method=\"post\" enctype='multipart/form-data'");
    }

    public String input(String name, String value, String label)
    {
        return "<div class=\"form-group\"><label for=\""+name+"\">"+label+"</label>"+this.tag("input", name, "form-control", "", "value=\""+value+"\" type=\"text\" placeholder=\""+label+"\" name=\""+name+"\" autofocus=\"autofocus\"")+"</div>";
    }

    public String inputfile(String name, String label)
    {
        return "<div class=\"form-group \"><label for=\""+name+"\">"+label+"</label>"+this.tag("input", name, "form-control style=\"border:0;\"", "", "type=\"file\" name=\""+name+"\" autofocus=\"autofocus\"")+"</div>";
    }

    public String textarea(String name, String value, String label)
    {
        return "<div class=\"form-group\"><label for=\""+name+"\">"+label+"</label>"+"<textarea name=\""+name+"\"  style=\"height: 150px\" class=\"form-control md-textarea\" placeholder=\""+label+"\">"+value+"</textarea></div>";
    }

    public String password(String name, String value, String label)
    {
        return "<div class=\"form-group\"><label for=\""+name+"\">"+label+"</label>"+this.tag("input", name, "form-control", "", "type=\"password\" value=\""+value+"\" placeholder=\""+label+"\" name=\""+name+"\" autofocus=\"autofocus\"")+"</div>";
    }

    public String inputHidden(String name, String value)
    {
        return this.tag("input", name, "form-control", "", "type=\"hidden\" value=\""+value+"\" name=\""+name+"\"");
    }

    public String cancelButton(String url, String value) {
        if (url == null) {
            return this.tag("button", "cancel", "btn btn-secondary", value, "onclick=\"window.history.back();\"");
        }
        return this.tag("a", "", "btn btn-secondary", value, "href=\""+url+"\"");
    }

    public String saveButton()
    {
        return this.tag("button", "save", "btn btn-primary", "Save", "type=\"submit\"");
    }

    public String submitButton(String buttonText)
    {
        return this.tag("button", "save", "btn btn-primary", buttonText, "type=\"submit\"");
    }

    public String radioButton(String label, String name, String value) {
        return "<fieldset class=\"form-group\">\n" +
                "    <input name=\""+name+"\" type=\"radio\" id=\""+name+"\" value=\"" + value + "\">\n" +
                "    <label for=\""+name+"\">"+label+"</label>\n" +
                "</fieldset>";
    }

    public String radioButtonSelected(String label, String name, String value) {
        return "<fieldset class=\"form-group\">\n" +
                "    <input name=\""+name+"\" type=\"radio\" id=\""+name+"\" value=\""+value+"\" checked=\"checked\">\n" +
                "    <label for=\""+name+"\">"+label+"</label>\n" +
                "</fieldset>";
    }

    public String pre(String contains) {
        return pre(contains, "");
    }

    public String pre(String contains, String classes)
    {
        return tag("pre", "", classes, contains);
    }

    private String tag(String type, String id, String cls, String contains, String attributes)
    {
        if (!id.equals("")) {
            id = "id='"+id+"'";
        }
        return "<" + type + " " + id + " class='" + cls + "' " + attributes + ">\n" + contains + "\n</" + type + ">";
    }

    private String tag(String type, String id, String cls, String contains)
    {
        return this.tag(type, id, cls, contains, "");
    }

    public String checkBox(String name, boolean ischecked, String label)
    {
        String isCheckedText = "";
        if (ischecked){
            isCheckedText = "Checked";

    }
        return this.tag("div", "", "checkbox",
                this.tag("label", "", "",
                    this.tag("input", name, "", "", "type=\"checkbox\" value=\"1\" name=\""+name+"\" "+isCheckedText)
                    + label
                )
        ) ;
    }
}
