package cunstom.tag;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/TagHandler.java to edit this template
 */
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.JspFragment;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author milo9
 */
public class CustomAttribute extends SimpleTagSupport {

    private Date value;

    public void setValue(Date value) {
        this.value = value;
    }

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
// https://chatgpt.com/c/672a10e1-38c4-8006-93da-722ca40826c5
        try {
            // TODO: insert code to write html before writing the body content.
            // e.g.:
            //
            // out.println("<strong>" + attribute_1 + "</strong>");
            // out.println("    <blockquote>");

            if (value != null) {
                SimpleDateFormat vietnameeseFormat = new SimpleDateFormat("'ngay:'  dd 'thang:' MM 'nam:' yyyy");
                String formatted = vietnameeseFormat.format(value);
                out.print(formatted);
            }

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in CustomAttribute tag", ex);
        }
    }

}
