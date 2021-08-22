package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.imports.WorkflowImportResult;
import de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation.UserService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.definition.imports.EscaperoomGameImportService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;

public abstract class UploadWorkflowDialog extends AbstractDialog<String> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private UserService userService;

    @SpringBean
    private EscaperoomGameImportService escaperoomGameImportService;

    private String username = null;
    private String password = null;
    private String errorMessage = "";


    private Form<?> form;
    private WebMarkupContainer feedback;
    private FileUploadField fileUploadField;

    public UploadWorkflowDialog(String id, IModel<String> title) {
        super(id, title);


        this.form = new Form<>("dialogForm");
        this.form.setMultiPart(true);

        this.feedback = new WebMarkupContainer("feedbackPanel");
        feedback.add(new Label("messageLabel", new PropertyModel<>(this, "errorMessage")));
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        feedback.setVisible(false);
        form.add(feedback);

        form.add(new Label("description", new ResourceModel("dialog.uploadworkflow.description", "dialog.uploadworkflow.description")));
        fileUploadField = new FileUploadField("fileUpload");
        form.add(fileUploadField);
        form.add(new TextField<String>("username", new PropertyModel<>(this, "username")));
        form.add(new PasswordTextField("password", new PropertyModel<>(this, "password")));

        form.add(new AjaxSubmitLink("uploadWorkflow") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                errorMessage = "";
                Injector.get().inject(UploadWorkflowDialog.this);
                if (username == null || username.isEmpty()) {
                    errorMessage = "Es wurde kein Benutzername angegeben.";
                }
                if (password == null || password.isEmpty()) {
                    errorMessage = "Es wurde kein Passwort angegeben";
                }
                if (!userService.checkAuthorisation(username, password)) {
                    errorMessage = "Für die Kombination aus Benutzername und Password konnte kein gültiger Benutzer gefunden werden!";
                }
                if (fileUploadField.getFileUpload() == null) {
                    errorMessage = "Es wurde keine Datei ausgewählt!";
                }
                if (!errorMessage.isEmpty()) {
                    feedback.setVisible(true);
                    target.add(feedback);
                    return;
                }

                try {
                    File file = File.createTempFile("upload", ".json");
                    fileUploadField.getFileUpload().writeTo(file);

                    final WorkflowImportResult result = escaperoomGameImportService.createEscaperoomgameFromFile(file);
                    if (result.isSuccess()) {
                        errorMessage = "Der neue Escaperoom wurde erfolgreich angelegt.";
                    }
                    else {
                        errorMessage = String.join(" , ", result.getErrorMessages());
                    }
                }
                catch (Exception e) {
                    errorMessage = "Es ist ein unerwarteter Fehler aufgetreten: " + e.getMessage();
                }
                feedback.setVisible(true);
                target.add(feedback);
            }
        });

        form.add(new AjaxLink<Void>("closeDialog") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                UploadWorkflowDialog.this.closeDialog(target);
            }

        });
        form.add(new AjaxLink<Void>("closeDialogX") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                UploadWorkflowDialog.this.closeDialog(target);
            }

        });
        this.add(form);
    }

}
