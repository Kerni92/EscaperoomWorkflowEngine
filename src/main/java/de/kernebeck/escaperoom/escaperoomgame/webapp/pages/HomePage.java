package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import com.googlecode.wicket.jquery.core.resource.StyleSheetPackageHeaderItem;
import de.kernebeck.escaperoom.escaperoomgame.webapp.dialog.CreateGameDialog;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

public class HomePage extends WebPage {

    public HomePage() {
        super();
    }

    private CreateGameDialog dialog;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<Void> form = new Form<>("form");
        add(form);

        dialog = new CreateGameDialog("dialog", new ResourceModel("homepage.dialog.title", "homepage.dialog.title"));
        form.add(new AjaxButton("newGameButton", new ResourceModel("homepage.button.createnewgame", "homepage.button.createnewgame")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                HomePage.this.dialog.open(target);
            }
        });
        dialog.setOutputMarkupId(true);
        add(dialog);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(new StyleSheetPackageHeaderItem(HomePage.class));
    }


}
