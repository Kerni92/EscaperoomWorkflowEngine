const React = require('react');
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

import {GameChooser} from "../gamechooser/gamechooser";

export class NewGameDialog extends React.Component {

    constructor(props) {
        super(props);
        this.handleClickOpen = this.handleClickOpen.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.state = {
            open: false
        };
    }

    handleClickOpen(e) {
        this.setState({"open": true});
    };


    handleClose(e) {
        this.setState({"open": false});
    };

    handleSubmit(e) {


    }

    render() {
        const {open} = this.state;
        return (
            <div>
                <Button variant="outlined" color="primary" onClick={this.handleClickOpen}>
                    Open form dialog
                </Button>
                <Dialog open={open} onClose={this.handleClose} aria-labelledby="form-dialog-title">
                    <DialogTitle id="form-dialog-title">Neues Spiel Starten</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            Bitte wählen Sie einen Onlineescaperoom den Sie Spielen möchten und geben Sie die
                            Spielernamen ein:
                        </DialogContentText>
                        <GameChooser/>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="name1"
                            label="Spieler 1"
                            type="text"
                            fullWidth
                        />
                        <TextField
                            autoFocus
                            margin="dense"
                            id="name2"
                            label="Spieler 2"
                            type="text"
                            fullWidth
                        />
                        <TextField
                            autoFocus
                            margin="dense"
                            id="name3"
                            label="Spieler 2"
                            type="text"
                            fullWidth
                        />
                        <TextField
                            autoFocus
                            margin="dense"
                            id="name4"
                            label="Spieler 3"
                            type="text"
                            fullWidth
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            Cancel
                        </Button>
                        <Button onClick={this.handleClose} color="primary">
                            Subscribe
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}