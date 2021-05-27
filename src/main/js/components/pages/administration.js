import React from "react";
import Button from "@material-ui/core/Button";
import {createMuiTheme, withStyles} from '@material-ui/core/styles';

require("react")

const theme = createMuiTheme();


const useStyles = {
    root: {
        '& > *': {
            margin: theme.spacing(1),
        },
    },
    input: {
        display: 'none',
    },
};

class Administration extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            displayMessage: false,
            message: "",
            isError: false
        }
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(e) {
        e.preventDefault();
        console.log("Submit");
    }

    render() {
        const {classes} = this.props;
        return (
            <div className="administration">
                <h1>Willkommen auf der Administrationsseite</h1>

                <div className={classes.root}>
                    <form className={classes.form} id="form" onSubmit={this.handleSubmit}>
                        <input
                            className={classes.input}
                            id="contained-button-file"
                            multiple
                            type="file"
                        />
                        <label htmlFor="contained-button-file">
                            <Button variant="contained" color="primary" component="span" type="submit" form="form">
                                Upload
                            </Button>
                        </label>
                    </form>
                </div>
            </div>
        )
    }
}

export default withStyles(useStyles)(Administration);