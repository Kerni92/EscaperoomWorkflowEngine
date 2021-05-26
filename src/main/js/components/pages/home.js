import {NewGameDialog} from "../dialog/newgamedialog";
import {Link} from "react-router-dom";

const React = require('react');

export class Home extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="home">
                <p>Willkommen auf der Homeseite</p>
                <NewGameDialog/>
                <Link to="/admin">Adminseite</Link>
            </div>
        );
    }
}


