const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


import {BrowserRouter as Router, Route, Switch} from "react-router-dom";

import Administration from "./components/pages/administration"
import {Game} from "./components/pages/game"
import {Home} from "./components/pages/home"

class App extends React.Component {

    constructor(props) {
        super(props);
        // this.state = {employees: []};
    }

    componentDidMount() {
        // client({method: 'GET', path: '/api/employees'}).done(response => {
        //     this.setState({employees: response.entity._embedded.employees});
        // });
    }

    render() {

        return (
            <Router>
                <div className={"application"}>
                    <Switch>
                        <Route exact path="/">
                            <Home/>
                        </Route>
                        <Route path="/admin">
                            <Administration/>
                        </Route>
                        <Route path="/game">
                            <Game/>
                        </Route>
                    </Switch>

                </div>
            </Router>
        )
    }
}

class GameChooser extends React.Component {

    state = {
        games: []
    };

    constructor(props) {
        super(props);
    }

    async componentDidMount() {
        const response = await fetch("/api/ui/game/getAvailableWorkflows");
        const content = await response.json();
        this.setState({games: content});
    }

    render() {
        const {games} = this.state;
        return (
            <div className={"chooser"}>
                {games.map(game =>
                    <div key={game.id}>
                        <p>
                            <span>{game.id}</span>
                            <span>{game.name}</span>
                        </p>
                    </div>)}
            </div>
        );
    }

}

ReactDOM.render(
    <App/>,
    document.getElementById('react')
)