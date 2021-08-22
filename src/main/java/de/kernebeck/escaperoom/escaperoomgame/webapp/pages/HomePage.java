package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import de.kernebeck.escaperoom.escaperoomgame.webapp.dialog.CreateGameDialog;
import de.kernebeck.escaperoom.escaperoomgame.webapp.dialog.JoinOrContinueGameDialog;
import de.kernebeck.escaperoom.escaperoomgame.webapp.dialog.UploadWorkflowDialog;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

public class HomePage extends WebPage {

    public HomePage() {
        super();
    }

    private WebMarkupContainer dialog;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final Form<Void> form = new Form<>("form");
        add(form);

        initDialog();


        form.add(new AjaxButton("newGameButton", new ResourceModel("homepage.button.createnewgame", "homepage.button.createnewgame")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                final CreateGameDialog createGameDialog = new CreateGameDialog("dialogContent", new ResourceModel("homepage.button.createnewgame", "homepage.button.createnewgame")) {
                    @Override
                    public void closeDialog(AjaxRequestTarget target) {
                        HomePage.this.hideDialog(target);
                    }

                    @Override
                    public void updateDialog(IPartialPageRequestHandler target) {
                        //nothing todo
                    }
                };
                createGameDialog.setOutputMarkupId(true);
                dialog.replace(createGameDialog);
                dialog.setVisible(true);
                target.add(dialog);
            }
        });

        form.add(new AjaxButton("joinOrContinueGame", new ResourceModel("homepage.button.joinorcontinuegame", "homepage.button.joinorcontinuegame")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                final JoinOrContinueGameDialog joinOrContinueGameDialog = new JoinOrContinueGameDialog("dialogContent", new ResourceModel("homepage.button.continueorjoingame", "homepage.button.continueorjoingame")) {
                    @Override
                    public void closeDialog(AjaxRequestTarget target) {
                        HomePage.this.hideDialog(target);
                    }

                    @Override
                    public void updateDialog(IPartialPageRequestHandler target) {
                        //nothing todo
                    }
                };
                joinOrContinueGameDialog.setOutputMarkupId(true);
                dialog.replace(joinOrContinueGameDialog);
                dialog.setVisible(true);
                target.add(dialog);
            }
        });

        form.add(new AjaxButton("uploadGame", new ResourceModel("homepage.button.uploadGame", "homepage.button.uploadGame")) {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                final UploadWorkflowDialog uploadWorkflowDialog = new UploadWorkflowDialog("dialogContent", new ResourceModel("homepage.button.uploadGame", "homepage.button.uploadGame")) {
                    @Override
                    public void closeDialog(AjaxRequestTarget target) {
                        HomePage.this.hideDialog(target);
                    }

                    @Override
                    public void updateDialog(IPartialPageRequestHandler target) {
                        //nothing todo
                    }
                };
                uploadWorkflowDialog.setOutputMarkupId(true);
                dialog.replace(uploadWorkflowDialog);
                dialog.setVisible(true);
                target.add(dialog);
            }
        });
    }

    private void initDialog() {
        dialog = new WebMarkupContainer("dialog");
        dialog.setVisible(false);
        dialog.setOutputMarkupId(true);
        dialog.setOutputMarkupPlaceholderTag(true);
        final WebMarkupContainer dialogContent = new WebMarkupContainer("dialogContent");
        dialogContent.setOutputMarkupId(true);
        dialog.add(dialogContent);
        add(dialog);
    }

    private void hideDialog(AjaxRequestTarget target) {
        dialog.replace(new WebMarkupContainer("dialogContent"));
        dialog.setVisible(false);
        target.add(dialog);
    }


}
